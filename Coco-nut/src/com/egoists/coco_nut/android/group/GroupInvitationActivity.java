package com.egoists.coco_nut.android.group;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.group.adapter.UsersListAdapter;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.CoconutUrlEncoder;
import com.egoists.coco_nut.android.util.LoginPreference;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.query.BaasioQuery.ORDER_BY;

@EActivity(R.layout.activity_group_invitation)
public class GroupInvitationActivity extends Activity {
    @Extra("created_group_uuid")
    String mCreatedGroupUuid;
    
    @ViewById
    EditText edTxtSearchUser;
    
    private UsersListAdapter mListAdapter;
    private Context mContext;
    private LoginPreference mLoginPref;
    private ProgressDialog mDialog;
    
    @AfterViews
    void showUserList() {
        mContext = this;
        mLoginPref = new LoginPreference(mContext);
                
        AndLog.d("created group uuid : " + mCreatedGroupUuid);
        
        // 액션바
        final ActionBar actionBar = getActionBar();
        actionBar.setLogo(R.drawable.ic_coconut_white_logo);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("그룹 사용자 추가");
        
        // 사용자 검색 결과 리스트
        mListAdapter = new UsersListAdapter(this, mContext, new ArrayList<BaasioUser>());
        ListView list = (ListView)findViewById(R.id.list);  
        list.setAdapter(mListAdapter);
        
        // 검색창에서 바로 검색
        edTxtSearchUser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getMyFriends();
                    return true;
                }
                return false;
            }
        });
        
        getMyFriendsByBaasio();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_group_coconut, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToProjectSelectionActivity();
                return true;
            case R.id.menu_create_group:
                backToProjectSelectionActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // 전화번호인지 판단
    boolean isPhoneNumer(String query) {
        try {
            // 전화번호에 '-'가 있으면 없애고 판단
            String num = query.replaceAll("-", "");
            AndLog.d(num + " is phone number?");
            Long.parseLong(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    @Click(R.id.btnSearchUser)
    void getMyFriends() {
        // 사용자 검색시 나온 키보드 내려놓기
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edTxtSearchUser.getWindowToken(), 0);
        
        getMyFriendsByBaasio();
    }
    
    /**
     * 사용자 검색
     */
    void getMyFriendsByBaasio() {
        // 로그인 정보 가져오기
        mLoginPref.loadPreference();
        
        // 검색에서 무조건 본인은 제외
        String exceptMeQuery = " not username='" + mLoginPref.mId + "'";
        
        // 사용자 이름으로 검색인지, 폰번호로 검색인지 판단
        String userQuery = edTxtSearchUser.getText().toString();
        String additionalQuery = "";
        if (userQuery.length() != 0) {
            if (isPhoneNumer(userQuery)) {
                additionalQuery = " phone='" + userQuery.replaceAll("-", "") + "'";
            } else {
                additionalQuery = " name='" + userQuery + "'";
            }
        }

        String fullQuery = "select * where" + additionalQuery + exceptMeQuery;
        String rawQuery = CoconutUrlEncoder.encode(fullQuery);
        AndLog.d(rawQuery);
        
        mDialog = ProgressDialog.show(GroupInvitationActivity.this, "", "검색중", true);
        
        // 쿼리 전송
        BaasioQuery query = new BaasioQuery();
        query.setRawString("users?ql=" + rawQuery);
        query.setOrderBy(BaasioBaseEntity.PROPERTY_MODIFIED, ORDER_BY.DESCENDING);
        query.queryInBackground(new BaasioQueryCallback() { // 질의 요청

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list, BaasioQuery query, long timestamp) {
                AndLog.d("Succeed : get userlist");
                mDialog.dismiss();
                List<BaasioUser> users = BaasioBaseEntity.toType(entities, BaasioUser.class);
                
                refreshUserList(users);
            }

            @Override
            public void onException(BaasioException e) {
                mDialog.dismiss();
                AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                BaasioDialogFactory.createErrorDialog(mContext, e).show();
            }
        });
    }
    
    @UiThread
    void refreshUserList(List<BaasioUser> users) {
        mListAdapter.update(users);
    }
    
    // 폰의 back 버튼 누를 경우
    @Override
    public void onBackPressed() {
        backToProjectSelectionActivity();
    }
    
    /**
     * 사용자 추가
     * @param userUuid
     */
    public void addUserToGroup(UUID userUuid) {
        mDialog = ProgressDialog.show(GroupInvitationActivity.this, "", "추가중", true);
        
        BaasioUser user = new BaasioUser();
        user.setUuid(UUID.fromString(userUuid.toString()));         // 추가하려는 회원의 uuid   

        BaasioGroup entity = new BaasioGroup();
        entity.setUuid(UUID.fromString(mCreatedGroupUuid));         // Group의 uuid
        entity.addInBackground(
                user
                , new BaasioCallback<BaasioUser>() {

                    @Override
                    public void onException(BaasioException e) {
                        // 실패
                        mDialog.dismiss();
                        AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }

                    @Override
                    public void onResponse(BaasioUser response) {
                        mDialog.dismiss();
                        if (response != null) {
                            // 성공
                            String name = response.getName(); // ID(Username)
                            AndLog.d("Succeed : " + name + " is added");
                            Toast.makeText(mContext, name + " 이 그룹에 추가되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    
    /**
     * 사용자 삭제
     * @param userUuid
     */
    public void removeUserToGroup(UUID userUuid) {
        mDialog = ProgressDialog.show(GroupInvitationActivity.this, "", "삭제중", true);
        
        BaasioUser user = new BaasioUser();
        user.setUuid(UUID.fromString(userUuid.toString()));         // 추가하려는 회원의 uuid   

        BaasioGroup entity = new BaasioGroup();
        entity.setUuid(UUID.fromString(mCreatedGroupUuid));         // Group의 uuid
        entity.removeInBackground(
                user
                , new BaasioCallback<BaasioUser>() {

                    @Override
                    public void onException(BaasioException e) {
                        // 실패
                        mDialog.dismiss();
                        AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }

                    @Override
                    public void onResponse(BaasioUser response) {
                        mDialog.dismiss();
                        if (response != null) {
                            // 성공
                            String name = response.getName(); // ID(Username)
                            AndLog.d("Succeed : " + name + " is added");
                            Toast.makeText(mContext, name + " 이 그룹에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    
    void backToProjectSelectionActivity() {
        Intent intent = new Intent(getApplication(), 
                com.egoists.coco_nut.android.group.GroupSelectionActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        GroupInvitationActivity.this.finish();
    }
}
