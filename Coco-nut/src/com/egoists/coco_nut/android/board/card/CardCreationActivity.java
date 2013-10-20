package com.egoists.coco_nut.android.board.card;

import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.adapter.CardLabelArrayAdapter;
import com.egoists.coco_nut.android.board.event.ReloadEvent;
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

import de.greenrobot.event.EventBus;

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
    @ViewById
    Spinner spinnerCardCreateCategory;
        
    public static final String RELATION_NAME          = "group_card";
    
    private int mCardRating = 0;    // 중요도 (기본 0)
    private int mCardLabel = -1;    // 카드 레이블
    
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
        
        // 카드 라벨 설정 스피너 생성
        String[] labels = getResources().getStringArray(R.array.selectedCardLabel);
        spinnerCardCreateCategory.setPrompt("카드 라벨을 고르세요");
        CardLabelArrayAdapter adSpin = new CardLabelArrayAdapter(mContext, android.R.layout.simple_spinner_item, labels); 
        spinnerCardCreateCategory.setAdapter(adSpin);
        spinnerCardCreateCategory.setSelection(adSpin.getCount());
        
        spinnerCardCreateCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mCardLabel = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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
        // BoadTabActivity가 카드를 다시 리로드
        EventBus.getDefault().post(new ReloadEvent());
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
        
        BaasioEntity entity = new BaasioEntity(Card.ENTITY);         // "card" entity
        entity.setProperty(Card.ENTITY_NAME_TITLE, title);           // 카드 제목 (필수)
        
        if (subTitle != null && subTitle.length() > 0) {
            // 부제목
            entity.setProperty(Card.ENTITY_NAME_SUBTITLE, subTitle);
        }
        
        entity.setProperty(Card.ENTITY_NAME_LABEL, mCardLabel);         // 카드 레이블
        entity.setProperty(Card.ENTITY_NAME_RATING, mCardRating);       // 중요도
        entity.setProperty(Card.ENTITY_NAME_STATE, Card.ENTITY_VALUE_STATE);   // 기본 상태 : todo
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
                                                DialogFactory
                                                    .createNoButton(CardCreationActivity.this,R.string.title_succeed, "할 일에 카드가 등록되었습니다")
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
                });
    }
}
