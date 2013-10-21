package com.egoists.coco_nut.android.board.card;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;

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
    
    @AfterViews
    void initFrom() {
        mContext = this;
        mImageFetcher = new ImageFetcher(mContext);
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        for (Person person : mCard.participants) {
            LinearLayout userVoteRoot = (LinearLayout)mInflater.inflate(R.layout.list_card_vote, null);
            // 사용자 이름
            TextView txtName = (TextView)userVoteRoot.findViewById(R.id.txtVoteUserName);
            txtName.setText(person.name);

            // 프로필 사진
            ImageView pictureView = (ImageView)userVoteRoot.findViewById(R.id.imgVoteProfile);
            mImageFetcher.loadImage(person.picture, pictureView, R.drawable.card_personphoto_default);
            
            
            layCardVote.addView(userVoteRoot);
        }
        
        
    }

}
