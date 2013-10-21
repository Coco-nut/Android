package com.egoists.coco_nut.android.board.card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.event.ReloadEvent;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.DialogFactory;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.exception.BaasioException;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_card_vote)
public class CardVoteActivity extends Activity {
    @Extra("card_detail")
    Card mCard;
    @ViewById
    LinearLayout layCardVote;
    
    private Context mContext;
    private LayoutInflater mInflater;
    private ImageFetcher mImageFetcher;
    private ProgressDialog mDialog;
    private HashMap<String, Float> mVoterInfo;
    private String mMyUuid;
    
    @AfterViews
    void initFrom() {
        mContext = this;
        mImageFetcher = new ImageFetcher(mContext);
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        mMyUuid = Baas.io().getSignedInUser().getUuid().toString(); // 본인 UUID 획득
        
        mVoterInfo = new HashMap<String, Float>();
        
        // 평가 대상 사용자 출력
        int nParticipant = mCard.participants.size(); 
        for (int i=0; i<nParticipant; i++) {
            Person person = mCard.participants.get(i);
            LinearLayout userVoteRoot = (LinearLayout)mInflater.inflate(R.layout.list_card_vote, null);
            // 사용자 이름
            TextView txtName = (TextView)userVoteRoot.findViewById(R.id.txtVoteUserName);
            txtName.setText(person.name);
            // 프로필 사진
            ImageView pictureView = (ImageView)userVoteRoot.findViewById(R.id.imgVoteProfile);
            mImageFetcher.loadImage(person.picture, pictureView, R.drawable.card_personphoto_default);
            // 평점 레이트
            final RatingBar voteRate = (RatingBar)userVoteRoot.findViewById(R.id.ratingVote);
            voteRate.setTag(""+person.uuid);  // TAG 저장 : 대상자 uuid
            voteRate.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    String uuid = (String) voteRate.getTag();
                    AndLog.d("User " + uuid + "'s rating : " + rating);
                    mVoterInfo.put(uuid, rating);   // 평가 입력
                }
                
            });
            layCardVote.addView(userVoteRoot);
        }
    }
    
    void backToBoardTabActivity() {
            // BoadTabActivity가 카드를 다시 리로드
            EventBus.getDefault().post(new ReloadEvent());
        
        this.finish();
    }

    @Click(R.id.btnCardVote)
    void doVoteByBaasio() {
        BaasioEntity entity = new BaasioEntity(Card.ENTITY);
        
        // UUID
        entity.setUuid(UUID.fromString(mCard.uuid));
        
        // 평가 내역 업데이트
        ArrayList<Person> participants = new ArrayList<Person>();   // 평가 대상
        // 평가자
        ArrayList<Voter> voters = (mCard.voters == null) ? new ArrayList<Voter>() : mCard.voters;            
        for (Person person : mCard.participants) {
            float rate = mVoterInfo.get(person.uuid);
            if (rate > 0) {
                person.sumRate += (int)rate-5;
                voters.add(new Voter(mMyUuid));
            }
            participants.add(person);
        }
        mCard.voters = voters;
        mCard.participants = participants;
        
        // 평가 대상 재업데이트
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(mCard.participants, JsonNode.class);
        entity.setProperty(Card.ENTITY_NAME_PARTY, jsonNode);
        // 평가자 재업데이트
        JsonNode jsonVoterNode = mapper.convertValue(mCard.voters, JsonNode.class);
        entity.setProperty(Card.ENTITY_NAME_VOTERS, jsonVoterNode);
        
        mDialog = ProgressDialog.show(CardVoteActivity.this, "", "평가 내역 업데이트 중", true);
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
                            .createNoButton(CardVoteActivity.this,R.string.title_succeed, "평가가 완료되었습니다")
                            .setPositiveButton(
                                    R.string.create_card_succeed,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            backToBoardTabActivity();
                                            }
                                        })
                            .setCancelable(false)
                            .show();
                            
                        }
                    }
                });
    }
}
