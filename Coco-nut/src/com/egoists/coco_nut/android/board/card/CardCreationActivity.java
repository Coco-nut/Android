package com.egoists.coco_nut.android.board.card;

import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.DialogFactory;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.exception.BaasioException;

@EActivity(R.layout.activity_card_creation)
public class CardCreationActivity extends Activity {
    @Extra("group_uuid")
    String mExtraGroupUuid;
    @ViewById
    EditText edTextCreateCardTitle;
    @ViewById
    EditText edTextCreateCardSubTitle;
    @ViewById
    RatingBar ratingCreateCard;
        
    public static final String RELATION_NAME          = "group_card";
    public static final String ENTITY                 = "card";
    public static final String ENTITY_NAME_TITLE      = "title";      // 카드 제목
    public static final String ENTITY_NAME_SUBTITLE   = "subtitle";   // 카드 부제목
    public static final String ENTITY_NAME_RATING     = "rating";     // 중요도
    public static final String ENTITY_NAME_STATE      = "state";      // 카드 상태
    public static final String ENTITY_VAL_STATE_TODO       = "todo";       // 카드의 기본 상태는 todo 이다
    
    private int mCardRating = 0;    // 중요도 (기본 0)
    
    private Context mContext;
    private ProgressDialog mDialog;
    
    @AfterViews
    void init() {
        mContext = this;
        
        // 액션바
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // 별점 리스너~
        ratingCreateCard.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mCardRating = (int) rating;
            }
        });
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
    
    void backToBoardTabActivity() {
//        Intent intent = new Intent(getApplication(), 
//                com.egoists.coco_nut.android.board.BoardTabActivity_.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        this.finish();
    }
    
    // 카드 생성
    @Click({R.id.btnDoCreateCard})
    void createCard() {
        // 입력폼 체크
        String title = edTextCreateCardTitle.getText().toString();
        if (title.length() == 0) {
            BaasioDialogFactory.createErrorDialog(mContext, R.string.error_require_card_title).show();
            return;
        }
        String subTitle = edTextCreateCardSubTitle.getText().toString();
        AndLog.d("Try to create card : " + mCardRating);
        createCardByBassio(title, subTitle, "");
    }
    
    /**
     * 카드 생성
     * @param title
     * @param subTitle
     * @param category
     */
    void createCardByBassio(String title, String subTitle, String category) {
        mDialog = ProgressDialog.show(CardCreationActivity.this, "", "카드 생성 중", true);
        
        BaasioEntity entity = new BaasioEntity(ENTITY);         // "card" entity
        entity.setProperty(ENTITY_NAME_TITLE, title);           // 카드 제목 (필수)
        
        if (subTitle != null && subTitle.length() > 0) {
            // 부제목
            entity.setProperty(ENTITY_NAME_SUBTITLE, subTitle);
        }
        
        entity.setProperty(ENTITY_NAME_RATING, mCardRating);            // 중요도
        entity.setProperty(ENTITY_NAME_STATE, ENTITY_VAL_STATE_TODO);   // 상태
        
        entity.saveInBackground(new BaasioCallback<BaasioEntity>() {
                    @Override
                    public void onException(BaasioException e) {
                        mDialog.dismiss();
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }

                    @Override
                    public void onResponse(BaasioEntity response) {
                        // 카드 생성 성공시 현재의 그룹과 연결한다.
                        if (response != null) {
                            // 성공
                            BaasioGroup group = new BaasioGroup();
                            group.setUuid(UUID.fromString(mExtraGroupUuid));
                            group.connectInBackground(
                                    RELATION_NAME, 
                                    response
                                    , BaasioEntity.class
                                    , new BaasioCallback<BaasioEntity>() {
                                        @Override
                                        public void onException(BaasioException e) {
                                            mDialog.dismiss();
                                            BaasioDialogFactory.createErrorDialog(mContext, e).show();
                                        }
                                        @Override
                                        public void onResponse(BaasioEntity response) {
                                            mDialog.dismiss();
                                            if (response != null) {
                                                backToBoardTabActivity();
                                                DialogFactory
                                                    .createNoButton(CardCreationActivity.this,R.string.title_succeed, "")
                                                    .setPositiveButton(
                                                            R.string.create_card_succeed,
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    backToBoardTabActivity();
                                                                    }
                                                                })
                                                    .setCancelable(false)
                                                    .show();
//                                                BaasioDialogFactory.createFinishButtonDialog(
//                                                        CardCreationActivity.this, 
//                                                        R.string.title_succeed, 
//                                                        R.string.create_card_succeed).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
