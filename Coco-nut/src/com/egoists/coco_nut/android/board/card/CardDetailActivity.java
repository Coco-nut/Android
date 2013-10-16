package com.egoists.coco_nut.android.board.card;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;

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
    LinearLayout layoutCardDetailParticipant;
    
    private Context mContext;
    private ImageFetcher mImageFetcher;
    
    @AfterViews
    void initCard() {
//        EventBus.getDefault().registerSticky(this);
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        actionBar.setTitle("카드 상세");
        txtCardDetailTitle.setText(mCard.title);
        txtCardDetailSubTitle.setText(mCard.sub_title);
        
        mContext = this;
        mImageFetcher = new ImageFetcher(mContext);
        
        // 참가자 아이콘 추가
        ImageView pictureView;
        for (Person person : mCard.participants) {
            pictureView = Person.getImageView(mContext);
            String imageUrl = person.pictureUrl;
            if (imageUrl != null) {
                mImageFetcher.loadImage(imageUrl, pictureView, R.drawable.card_personphoto_default);
            } 
            layoutCardDetailParticipant.addView(pictureView);
        }
    }
    
    @Override
    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.only_back_coconut, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    
//    ///////////////////////////////////////////////////////
//    //  EventBus 관련 이벤트 처리부
//    ///////////////////////////////////////////////////////
//        
//     // 서버로부터 다시 카드를 받는다
//    public void onEventSticky(CardDetailEvent event) {
//        AndLog.d("Recieved card detail event");
//        txtCardDetailTitle.setText(event.card.title);
//        txtCardDetailSubTitle.setText(event.card.sub_title);
//    }
}
