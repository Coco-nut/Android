package com.egoists.coco_nut.android.board.card;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.adapter.ColoredCardLabel;
import com.egoists.coco_nut.android.board.event.ReloadEvent;
import com.egoists.coco_nut.android.board.event.UpdatedCardEvent;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.DateConverter;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_card_detail_all)
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
    LinearLayout layoutCardDetailParticipant;
    @ViewById
    ImageView imgCardDetailLabelFlag;
    
    private Context mContext;
    private ImageFetcher mImageFetcher;
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
        txtCardDetailDueTo.setText(DateConverter.getTimeString(mCard.startdate, mCard.enddate));
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
    }
    
    
    // 폰의 back 버튼 누를 경우
    @Override
    public void onBackPressed() {
        backToBoardTabActivity();
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
}
