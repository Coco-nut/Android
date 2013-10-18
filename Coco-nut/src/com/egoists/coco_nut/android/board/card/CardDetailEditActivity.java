package com.egoists.coco_nut.android.board.card;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.adapter.CardLabelArrayAdapter;
import com.egoists.coco_nut.android.util.DatePickerFragment;
import com.egoists.coco_nut.android.util.TimePickerFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_card_detail_edit)
public class CardDetailEditActivity extends FragmentActivity {
    @Extra("card_detail")
    Card mCard;
    @ViewById
    EditText edTxtCardEditTitle;
    @ViewById
    EditText edTxtCardEditSubtitle;
    @ViewById
    EditText edTxtCardEditDescription;
    @ViewById
    Spinner spinnerCardEditCategory;
    @ViewById
    RatingBar ratingCardEdit;
    
    private Context mContext;
    
    
    private int mCardRating = 0;    // 중요도 (기본 0)
    private int mCardLabel;         // 카드 레이블
    
    @AfterViews
    void init() {
        mContext = this;
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("카드 수정");
        
        edTxtCardEditTitle.setText(mCard.title);
        edTxtCardEditSubtitle.setText(mCard.sub_title);
        edTxtCardEditDescription.setText(mCard.discription);
        
        // 별점 리스너~
        ratingCardEdit.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mCardRating = (int) rating;
            }
        });
        
        // 카드 라벨 설정 스피너 생성
        String[] labels = getResources().getStringArray(R.array.selectedCardLabel);
        spinnerCardEditCategory.setPrompt("카드 라벨을 고르세요");
        CardLabelArrayAdapter adSpin = new CardLabelArrayAdapter(mContext, android.R.layout.simple_spinner_item, labels); 
        spinnerCardEditCategory.setAdapter(adSpin);
        if (mCard.label == -1)
            spinnerCardEditCategory.setSelection(adSpin.getCount());
        else
            spinnerCardEditCategory.setSelection(mCard.label);
        
        spinnerCardEditCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
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
    
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}
