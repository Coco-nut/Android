package com.egoists.coco_nut.android.board.card;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.adapter.CardLabelArrayAdapter;
import com.egoists.coco_nut.android.board.event.GroupUsersEvent;
import com.egoists.coco_nut.android.board.event.RequestGroupUsersEvent;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.DatePickerFragment;
import com.egoists.coco_nut.android.util.TimePickerFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.entity.user.BaasioUser;

import de.greenrobot.event.EventBus;

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
    @ViewById
    LinearLayout layGroupCardEditGroupUsers;
   
    // 유저 리스트의 하위 레이아웃
    @ViewById
    LinearLayout layoutRoot;
    @ViewById
    TextView textUserName;
    @ViewById
    TextView textUserPhone;
    
    private List<BaasioUser> mUsers;        // 그룹의 모든 사용자
    private ArrayList<Person> mParticipant;      // 이 카드의 참가자
    
    private Context mContext;
    private LayoutInflater mInflater;
    
    private ImageFetcher mImageFetcher;
    
    @AfterViews
    void init() {
        mContext = this;
        EventBus.getDefault().register(this);
        mImageFetcher = new ImageFetcher(mContext);
        
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mParticipant = new ArrayList<Person>();
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("카드 수정");
        
        edTxtCardEditTitle.setText(mCard.title);
        edTxtCardEditSubtitle.setText(mCard.sub_title);
        edTxtCardEditDescription.setText(mCard.discription);
        ratingCardEdit.setRating((float)mCard.importance);
        
        // 별점 리스너~
        ratingCardEdit.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mCard.importance = (int) rating;
            }
        });
        
        // 카드 라벨 설정 스피너 생성
        String[] labels = getResources().getStringArray(R.array.selectedCardLabel);
        spinnerCardEditCategory.setPrompt("카드 라벨을 고르세요");
        CardLabelArrayAdapter adSpin = new CardLabelArrayAdapter(mContext, android.R.layout.simple_spinner_item, labels); 
        spinnerCardEditCategory.setAdapter(adSpin);
        if (mCard.label == -1) {
            spinnerCardEditCategory.setSelection(adSpin.getCount());
        } else {
            spinnerCardEditCategory.setSelection(mCard.label);
        }
        spinnerCardEditCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mCard.label = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        
        // 그룹 사용자 획득
        EventBus.getDefault().post(new RequestGroupUsersEvent());
    }
    
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
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
    
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    
    // 사용자 정보를 획득한다
    public void onEvent(GroupUsersEvent event) {
        mUsers = event.users;
        AndLog.d("Users of this group : " + mUsers.size());
        
        // 그룹의 사용자를 출력한다
        for (final BaasioUser user : mUsers) {
            LinearLayout userItemRoot = (LinearLayout)mInflater.inflate(R.layout.listview_item_userlist, null);
            // 사용자 이름
            TextView txtName = (TextView)userItemRoot.findViewById(R.id.textUserName);
            txtName.setText(user.getName());
            // 전화번호
            TextView phone = (TextView)userItemRoot.findViewById(R.id.textUserPhone);
            phone.setText(user.getProperty(Person.ENTITY_NAME_PHONE).asText());
            // 프로필 사진
            ImageView pictureView = (ImageView)userItemRoot.findViewById(R.id.imageProfile);
            mImageFetcher.loadImage(user.getPicture(), pictureView, R.drawable.card_personphoto_default);
            // 사용자 참가 여부 체크박스
            CheckBox userJoin = (CheckBox)userItemRoot.findViewById(R.id.checkUserJoin);
            userJoin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mParticipant.add(new Person(user.getUuid().toString(), user.getName(), user.getPicture(), false));
                    } else {
//                        mParticipant.re
//                        
                    }
                    
                    AndLog.d(user.getName() + " join? : " + isChecked);
                }
                
            });
            layGroupCardEditGroupUsers.addView(userItemRoot);
        }
    }
}
