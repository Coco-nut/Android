package com.egoists.coco_nut.android.board.card;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.event.GroupUsersEvent;
import com.egoists.coco_nut.android.board.event.RequestGroupUsersEvent;
import com.egoists.coco_nut.android.board.event.UpdateDuetoDateEvent;
import com.egoists.coco_nut.android.board.event.UpdateDuetoTimeEvent;
import com.egoists.coco_nut.android.board.event.UpdateStartDateEvent;
import com.egoists.coco_nut.android.board.event.UpdateStartTimeEvent;
import com.egoists.coco_nut.android.board.event.UpdatedCardEvent;
import com.egoists.coco_nut.android.cache.ImageCache;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.DialogFactory;
import com.egoists.coco_nut.android.util.DuetoDatePickerFragment;
import com.egoists.coco_nut.android.util.DuetoTimePickerFragment;
import com.egoists.coco_nut.android.util.StartDatePickerFragment;
import com.egoists.coco_nut.android.util.StartTimePickerFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_card_detail_edit)
public class CardDetailEditActivity extends FragmentActivity implements android.widget.RadioGroup.OnCheckedChangeListener {
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
    @ViewById
    TextView txtStartTime;
    @ViewById
    TextView txtStartDate;
    @ViewById
    TextView txtDueToTime;
    @ViewById
    TextView txtDueToDate;
    
    @ViewById
    ScrollView viewGroupCardEdit;
   
    // 카드 상태 
    @ViewById
    RadioGroup radioGroupCardEditState;
    @ViewById
    RadioButton radioCardEditStateTodo;
    @ViewById
    RadioButton radioCardEditStateDoing;
    @ViewById
    RadioButton radioCardEditStateDone;
    
    // 유저 리스트의 하위 레이아웃
    @ViewById
    LinearLayout layoutRoot;
    @ViewById
    TextView textUserName;
    @ViewById
    TextView textUserPhone;
    
    private List<BaasioUser> mUsers;            // 그룹의 모든 사용자
    private ArrayList<Person> mParticipant;     // 이 카드의 참가자
    private boolean mIsJoined[];                // 그룹의 사용자가 참여 할 것인지를 저장하는 플래그 (인덱스 기반)
    
    private Calendar mStartCal = null;      // 시작시간
    private Calendar mDuetoCal = null;      // 종료시간
    
    private Context mContext;
    private LayoutInflater mInflater;
    
    private ImageFetcher mImageFetcher;
    private ProgressDialog mDialog;
    
    @AfterViews
    void init() {
        mContext = this;
        EventBus.getDefault().register(this);
        mImageFetcher = new ImageFetcher(mContext);
        mImageFetcher.setImageCache(new ImageCache(mContext, "COCONUT"));
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        mParticipant = new ArrayList<Person>();
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setLogo(R.drawable.ic_coconut_white_logo);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("카드 수정");
        
        // 화면 터치시 키보드 내리기
        viewGroupCardEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    AndLog.d("Touch");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edTxtCardEditTitle.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(edTxtCardEditSubtitle.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(edTxtCardEditDescription.getWindowToken(), 0);
                }
                return false;
            }
        });
        
        // 라디오 버튼
        // 할 일일 경우 완료 체크박스 표시 불가.
        // 완료일 경우 전부 표시 불가
        if (mCard.status == 0) {    // todo
            radioCardEditStateTodo.setChecked(true);
            radioCardEditStateDone.setVisibility(View.GONE);
        } else if (mCard.status == 1) { // doing
            radioCardEditStateDoing.setChecked(true);
        } else if (mCard.status == 2) { // done
            radioCardEditStateDone.setChecked(true);
            radioCardEditStateTodo.setVisibility(View.GONE);
            radioCardEditStateDoing.setVisibility(View.GONE);
        }
        
        radioGroupCardEditState.setOnCheckedChangeListener(this);
        
        // 제목, 부제목, 상세 설명, 중요도
        edTxtCardEditTitle.setText(mCard.title);
        edTxtCardEditSubtitle.setText(mCard.sub_title);
        edTxtCardEditDescription.setText(mCard.discription);
        ratingCardEdit.setRating((float)mCard.importance);
        // 일정 출력
        if (mCard.startdate != null) {
            txtStartDate.setText(getDateString(mCard.startdate));
            txtStartTime.setText(getTimeString(mCard.startdate));
        }
        if (mCard.enddate != null) {
            txtDueToDate.setText(getDateString(mCard.enddate));
            txtDueToTime.setText(getTimeString(mCard.enddate));
        }
        
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
        ArrayAdapter<String> adSpin = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, labels); 
        spinnerCardEditCategory.setAdapter(adSpin);
        if (mCard.label == -1) {
            spinnerCardEditCategory.setSelection(adSpin.getCount()-1);    // 기본 = 마지막 라벨 : 기타
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
        // 완료일 경우 사용자를 표시하지 않는다.
        if (mCard.status != 2) { 
            EventBus.getDefault().post(new RequestGroupUsersEvent());
        }
    }
    
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    
    void backToCardDetailActivity() {
        // 종료
        EventBus.getDefault().post(new UpdatedCardEvent(mCard));
        this.finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // TODO 메뉴 바꿀것
        getMenuInflater().inflate(R.menu.edit_coconut, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.edit:
                doUpdateByBaasio();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) { // 라디오버튼
        switch (arg1) {
        case R.id.radioCardEditStateTodo:
            mCard.status = 0;
            break;
        case R.id.radioCardEditStateDoing:
            mCard.status = 1;
            break;
        case R.id.radioCardEditStateDone:
            mCard.status = 2;
            break;
        }
    }

    ///////////////////////////////////////////////////////
    //  날짜, 시간 관련
    ///////////////////////////////////////////////////////
    
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new StartDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new StartTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    
    public void showDueToDatePickerDialog(View v) {
        DialogFragment newFragment = new DuetoDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    
    public void showDueToTimePickerDialog(View v) {
        DialogFragment newFragment = new DuetoTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    
    private String getDateString(Calendar c) {
        if (c != null)
            return c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONDAY) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        return "";
    }
    
    private String getTimeString(Calendar c) {
        if (c != null)
            return c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return "";
    }
    
    ///////////////////////////////////////////////////////
    //  EventBus 관련 이벤트 처리부
    ///////////////////////////////////////////////////////
    boolean setStartDate = false;
    
    public void onEvent(UpdateStartTimeEvent event) {
        int hour = event.h;
        int minute = event.m;
        
        txtStartTime.setText(hour + ":" + minute);
        
        // 날짜가 선택되어 있지 않았다면 현재 날짜로 바꿔준다
        if (setStartDate == false) {
            mStartCal = Calendar.getInstance();
            txtStartDate.setText(getDateString(mStartCal));
            setStartDate = true;
        }
        mStartCal.set(Calendar.HOUR_OF_DAY, hour);
        mStartCal.set(Calendar.MINUTE, minute);        
    }

    public void onEvent(UpdateStartDateEvent event) {
        setStartDate = true;
        
        int year = event.y;
        int month = event.m;
        int day = event.d;
        
        txtStartDate.setText(year + "/" + (month+1) + "/" + day);
        mStartCal = Calendar.getInstance();
        mStartCal.set(Calendar.YEAR, year);
        mStartCal.set(Calendar.MONTH, month);
        mStartCal.set(Calendar.DAY_OF_MONTH, day);
    }
    
    boolean setDutoDate = false;
    
    public void onEvent(UpdateDuetoTimeEvent event) {
        int hour = event.h;
        int minute = event.m;
        
        txtDueToTime.setText(hour + ":" + minute);
        
        // 날짜가 선택되어 있지 않았다면 현재 날짜로 바꿔준다
        if (setDutoDate == false) {
            mDuetoCal = Calendar.getInstance();
            txtDueToDate.setText(getDateString(mDuetoCal));
            setDutoDate = true;
        }
        mDuetoCal.set(Calendar.HOUR_OF_DAY, hour);
        mDuetoCal.set(Calendar.MINUTE, minute);
    }

    public void onEvent(UpdateDuetoDateEvent event) {
        setDutoDate = true;
        
        int year = event.y;
        int month = event.m;
        int day = event.d;
        
        txtDueToDate.setText(year + "/" + (month+1) + "/" + day);
        mDuetoCal = Calendar.getInstance();
        mDuetoCal.set(Calendar.YEAR, year);
        mDuetoCal.set(Calendar.MONTH, month);
        mDuetoCal.set(Calendar.DAY_OF_MONTH, day);
    }
    
    // 사용자 정보를 획득한다
    public void onEvent(GroupUsersEvent event) {
        mUsers = event.users;
        AndLog.d("Users of this group : " + mUsers.size());
        
        // 사용자 참가여부 리스트 설정, 초기화
        int nGroupUsers = mUsers.size();
        mIsJoined = new boolean[nGroupUsers];
        
        // 그룹의 사용자를 출력한다
        for (int i=0; i<nGroupUsers; i++) {
            final BaasioUser user = mUsers.get(i);
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
            final CheckBox userJoin = (CheckBox)userItemRoot.findViewById(R.id.checkUserJoin);
            userJoin.setTag(""+i);  // 체크박스에 현재 사용자 index 저장
            // 이미 조인한 사용자는 체크박스를 활성화해둔다
            if (isInvolvedUserForThisCard(user.getUuid().toString())) {
                mIsJoined[i] = true;
                userJoin.setChecked(true);
                
            }
            userJoin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String tag = (String)userJoin.getTag();
                    if (isChecked) {
                        if (tag.length() != 0)
                            mIsJoined[Integer.parseInt(tag)] = true;
                        AndLog.d(tag + "th user is joined");
                    } else {
                        if (tag.length() != 0)
                            mIsJoined[Integer.parseInt(tag)] = false;
                        AndLog.d(tag + "th user resign");
                    }
                }
                
            });
            layGroupCardEditGroupUsers.addView(userItemRoot);
        }
    }
    
    boolean isInvolvedUserForThisCard(String uuid) {
        List<Person> involvedUsers = mCard.participants;
        for (Person involvedUser : involvedUsers) {
            AndLog.d("Is " + involvedUser.uuid + " involved?");
            if (involvedUser.uuid.equals(uuid))
                return true;
        }
        return false;
    }
    
    ///////////////////////////////////////////////////////
    //  BaasIO 관련 통신 처리부
    ///////////////////////////////////////////////////////
    
    void doUpdateByBaasio() {
        BaasioEntity entity = new BaasioEntity(Card.ENTITY);
        // UUID
        entity.setUuid(UUID.fromString(mCard.uuid));
        // 제목
        mCard.title = edTxtCardEditTitle.getText().toString();
        entity.setProperty(Card.ENTITY_NAME_TITLE, mCard.title);
        // 부제목
        mCard.sub_title = edTxtCardEditSubtitle.getText().toString();
        entity.setProperty(Card.ENTITY_NAME_SUBTITLE, mCard.sub_title);
        // 상태
        entity.setProperty(Card.ENTITY_NAME_STATE, mCard.status);
        if (mCard.status == 1)
            entity.setProperty(Card.ENTITY_NAME_DOING_DATE, Calendar.getInstance().getTimeInMillis());
        if (mCard.status == 2)
            entity.setProperty(Card.ENTITY_NAME_DONE_DATE, Calendar.getInstance().getTimeInMillis());
        // 상세내역
        mCard.discription = edTxtCardEditDescription.getText().toString();
        entity.setProperty(Card.ENTITY_NAME_DESCRIPTION, mCard.discription);
        // 중요도
        mCard.importance = mCard.importance;
        entity.setProperty(Card.ENTITY_NAME_RATING, (int)mCard.importance);
        // 라벨
        entity.setProperty(Card.ENTITY_NAME_LABEL, mCard.label);
        // 시작시간
        if (mStartCal != null) {
            mCard.startdate = mStartCal;
            entity.setProperty(Card.ENTITY_NAME_START_DATE, mCard.startdate.getTimeInMillis());
        }
        // 종료시간
        if (mDuetoCal != null) {
            mCard.enddate = mDuetoCal;
            entity.setProperty(Card.ENTITY_NAME_DUETO_DATE, mCard.enddate.getTimeInMillis());
        }
        
        // 카드 상태가 완료이면 사용자 업데이트 안함
        if (mCard.status != 2) {
            // 실제로 체크한 사용자만 추려내서 업데이트한다
            for (int i=0; i<mUsers.size(); i++) {
                if (mIsJoined[i]) {
                    BaasioUser user = mUsers.get(i);
                    mParticipant.add(new Person(user, 5 * (mUsers.size() - 1)));
                }
            }
            mCard.participants = mParticipant;
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.convertValue(mParticipant, JsonNode.class);
            entity.setProperty(Card.ENTITY_NAME_PARTY, jsonNode);
        }
        
        mDialog = ProgressDialog.show(CardDetailEditActivity.this, "", "카드 업데이트 중", true);
        entity.updateInBackground(
                new BaasioCallback<BaasioEntity>() {

                    @Override
                    public void onException(BaasioException e) {
                        // 실패
                        mDialog.dismiss();
                        if (e.getErrorCode() == 0) {
                            // 네트워크 오류
                            BaasioDialogFactory.createErrorDialog(mContext, R.string.error_network).show();
                            return;
                        }
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }

                    @Override
                    public void onResponse(BaasioEntity response) {
                        if (response != null) {
                            mDialog.dismiss();
                            // 성공
                            DialogFactory
                            .createNoButton(CardDetailEditActivity.this,R.string.title_succeed, "카드가 수정되었습니다")
                            .setPositiveButton(
                                    R.string.create_card_succeed,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            backToCardDetailActivity();
                                            }
                                        })
                            .setCancelable(false)
                            .show();
                            
                        }
                    }
                });
    }
}
