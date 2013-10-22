package com.egoists.coco_nut.android.board.card;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.adapter.ColoredCardLabel;
import com.egoists.coco_nut.android.board.event.ReloadEvent;
import com.egoists.coco_nut.android.board.event.UpdatedCardEvent;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.DateConverter;
import com.egoists.coco_nut.android.util.DialogFactory;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_card_detail)
public class CardDetailActivity extends Activity {
    @Extra("card_detail")
    Card mCard;
    
    @ViewById
    TextView txtCardDetailTitle;
    @ViewById
    TextView txtCardDetailSubTitle;
    @ViewById
    TextView txtCardDetailDueTo;
    @ViewById
    TextView txtCardDetailDescription;
    @ViewById
    TextView txtCardDetailLabel;
    @ViewById
    TextView txtCardDetailCommentCount;
    
    @ViewById
    LinearLayout layoutCardDetailParticipant;
    @ViewById
    ImageView imgCardDetailLabelFlag;
    @ViewById
    LinearLayout layoutCardDetailComments;
    @ViewById
    EditText edTxtCardDetailComment;
    
    @ViewById
    ScrollView scrollCardDetail;
    
    private Context mContext;
    private ImageFetcher mImageFetcher;
    private LayoutInflater mInflater;
    private ProgressDialog mDialog;
    
    // 만약 업데이트를 다시 했다면 메인으로 되돌아갈 때 리프레쉬를 해야함
    private boolean isUpdated = false;      
    
    @AfterViews
    void initCard() {
        EventBus.getDefault().register(this);
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("카드 상세");
        
        mContext = this;
        mImageFetcher = new ImageFetcher(mContext);
        
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        // 화면 터치시 키보드 내리기
        scrollCardDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    AndLog.d("Touch");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edTxtCardDetailComment.getWindowToken(), 0);
                }
                return false;
            }
        });
        
        // 카드 상세 화면 그리기
        drawCardDetail();
    }
    
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_coconut, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToBoardTabActivity();
                return true;
            case R.id.edit:
                // 카드 수정으로...
                Intent i = new Intent(mContext,
                        com.egoists.coco_nut.android.board.card.CardDetailEditActivity_.class);
                AndLog.d("Push card detail event");
                i.putExtra("card_detail", mCard);
                mContext.startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @UiThread
    void drawCardDetail() {
        // 깃발 그림
        // TODO poor implementation
        int importanceImgResources[] = {
                R.drawable.flag_card_label_0, R.drawable.flag_card_label_1, 
                R.drawable.flag_card_label_2, R.drawable.flag_card_label_3,
                R.drawable.flag_card_label_4, R.drawable.flag_card_label_5};
        
        txtCardDetailTitle.setText(mCard.title);
        txtCardDetailSubTitle.setText(mCard.sub_title);
        txtCardDetailDescription.setText(mCard.discription);
        
        // 카드 시작, 종료 시간
        String strStartCal = (mCard.startdate == null) 
                ? "" : DateConverter.getStringTime(mCard.startdate.getTimeInMillis());
        String strEndCal = (mCard.enddate == null) 
                ? "" : DateConverter.getStringTime(mCard.enddate.getTimeInMillis());
        txtCardDetailDueTo.setText(strStartCal + "\n ~ " + strEndCal);
        
        // 라벨 + 카테고리 추가
        String[] labels = getResources().getStringArray(R.array.selectedCardLabel);
        txtCardDetailLabel.setText(labels[mCard.label]);
        imgCardDetailLabelFlag.setImageResource(importanceImgResources[(int)mCard.importance]);
        imgCardDetailLabelFlag.setColorFilter(ColoredCardLabel.getColor(mCard.label));
        
        // 참가자 아이콘 추가
        layoutCardDetailParticipant.removeAllViews();
        ImageView pictureView;
        for (Person person : mCard.participants) {
            pictureView = Person.getImageView(mContext);
            String imageUrl = person.picture;
            if (imageUrl != null) {
                mImageFetcher.loadImage(imageUrl, pictureView, R.drawable.card_personphoto_default);
            } 
            layoutCardDetailParticipant.addView(pictureView);
        }
        
        // 댓글 추가
        drawComments();
    }
    
    @UiThread
    void drawComments() {
        layoutCardDetailComments.removeAllViews();
        edTxtCardDetailComment.setText("");
        // 댓글 출력
        if (mCard.comments != null) {
            AndLog.d(mCard.comments.size() + " comments in this card");
            txtCardDetailCommentCount.setText(""+mCard.comments.size());
            for (Comment comment : mCard.comments) {
                LinearLayout userItemRoot = (LinearLayout)mInflater.inflate(R.layout.list_card_comment, null);
                
                // 댓글 시간
                TextView txtName = (TextView)userItemRoot.findViewById(R.id.txtCommenterName);
                txtName.setText(DateConverter.getStringTime(comment.time));
                // 댓글
                TextView txtComment = (TextView)userItemRoot.findViewById(R.id.txtComment);
                txtComment.setText(comment.comment);
                // 댓글자 사진
                ImageView pictureView = (ImageView)userItemRoot.findViewById(R.id.imgCommenter);
                mImageFetcher.loadImage(comment.commenterPicture, pictureView, R.drawable.card_personphoto_default);
                
                // 레이아웃에 추가
                layoutCardDetailComments.addView(userItemRoot);
            }
        }
    }
    
    // 폰의 back 버튼 누를 경우
    @Override
    public void onBackPressed() {
        backToBoardTabActivity();
    }
    
    // 댓글 전송 버튼
    @Click(R.id.btnCardDetailSendComment)
    public void sendComment() {
        String commentToBeSend = edTxtCardDetailComment.getText().toString();
        sendCommentByBaasio(commentToBeSend);
    }
    
    void refresh() {
        drawComments();
        isUpdated = true;
    }
    
    ///////////////////////////////////////////////////////
    //  EventBus 관련 이벤트 처리부
    ///////////////////////////////////////////////////////
        
    // 카드 수정이 완료되면 메인 화면으로...
    public void onEvent(UpdatedCardEvent event) {
        mCard = event.card;
        isUpdated = true;
        drawCardDetail();
    }
    
    void backToBoardTabActivity() {
        if (isUpdated) {
            // BoadTabActivity가 카드를 다시 리로드
            EventBus.getDefault().post(new ReloadEvent());
        }
        
        this.finish();
    }
    
    ///////////////////////////////////////////////////////
    //  BaasIO 관련 통신 처리부
    ///////////////////////////////////////////////////////
    void sendCommentByBaasio(String comment) {
        BaasioUser me = Baas.io().getSignedInUser();
        
        BaasioEntity entity = new BaasioEntity(Card.ENTITY);
        // UUID
        entity.setUuid(UUID.fromString(mCard.uuid));
        
        if (mCard.comments == null)
            mCard.comments = new ArrayList<Comment>();
        mCard.comments.add(new Comment(me.getName(), me.getUuid().toString(), me.getPicture(), comment, Calendar.getInstance().getTimeInMillis()));
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(mCard.comments, JsonNode.class);
        entity.setProperty(Card.ENTITY_NAME_COMMENTS, jsonNode);
        
        mDialog = ProgressDialog.show(CardDetailActivity.this, "", "댓글 등록 중", true);
        entity.updateInBackground(
                new BaasioCallback<BaasioEntity>() {

                    @Override
                    public void onException(BaasioException e) {
                        // 실패
                        mDialog.dismiss();
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }

                    @Override
                    public void onResponse(BaasioEntity response) {
                        if (response != null) {
                            mDialog.dismiss();
                            // 성공
                            DialogFactory
                            .createNoButton(CardDetailActivity.this,R.string.title_succeed, "댓글이 등록 되었습니다")
                            .setPositiveButton(
                                    R.string.create_card_succeed,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            refresh();
                                            }
                                        })
                            .setCancelable(false)
                            .show();
                            
                        }
                    }
                });
    }
}
