package com.egoists.coco_nut.android.project;

import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.LoginPreference;
import com.egoists.coco_nut.android.util.UniqueString;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;

@EActivity(R.layout.activity_project_creation)
public class ProjectCreationActivity extends Activity {
    @ViewById
    EditText edTxtCreateProj;
    @ViewById
    ImageView imgSelectedGroupTemplete;
    
    private Context mContext;
    private LoginPreference mLoginPref;
    private ProgressDialog mDialog;
    private int mTemplete;
    
    @AfterViews
    void initForm() {
        mContext = this;
        mLoginPref = new LoginPreference(mContext);
        // UUID 정보 가져오기
        mLoginPref.loadPreference();
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        mTemplete = 0;  // 그룹 탬플릿 (default=0)
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
    
    @Click({R.id.btnCreateGroup})
    void createProject() {
        // 프로젝트 이름 폼 체크
        String projectName = edTxtCreateProj.getText().toString();
        if (projectName == null || projectName.length() == 0) {
            BaasioDialogFactory.createErrorDialog(mContext, R.string.error_empty_project_name).show();
            return;
        }
        
        doCreateProjectByBaasio(projectName);
    }

    void doCreateProjectByBaasio(String projectName) {
        String projectPath = UniqueString.generate();
        AndLog.d("Try to create project : " + projectName + ", " + projectPath);
        
        mDialog = ProgressDialog.show(ProjectCreationActivity.this, "", "그룹 생성 중", true);
        BaasioGroup group = new BaasioGroup();
        group.setTitle(projectName);    // 그룹 표시내용
        group.setPath(projectPath);     // 그룹 Unique한 Path 이름
        group.setProperty("templete", mTemplete);   // 그룹 탬플릿 넘버
        group.saveInBackground(
                new BaasioCallback<BaasioGroup>() {
                    @Override
                    public void onException(BaasioException e) {
                        mDialog.dismiss();
                        AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }

                    @Override
                    public void onResponse(BaasioGroup response) {
                        if (response != null) {
                            // 성공
                            String path = response.getPath();           // Group path
                            AndLog.d("Succeed : " + path + " project created.");
                            addMeIntoCreatedGroup(response.getUuid());
                        }
                    }
                });
    }
    
    // 그룹을 생성한 다음 무조건 본인을 추가한다
    void addMeIntoCreatedGroup(final UUID groupUuid) {
        String myUuid = mLoginPref.mUuid;
        
        BaasioUser user = new BaasioUser();
        user.setUuid(UUID.fromString(myUuid));         // 추가하려는 회원의 uuid   

        BaasioGroup entity = new BaasioGroup();
        entity.setUuid(groupUuid);                   // Group의 uuid
        entity.addInBackground(
                user
                , new BaasioCallback<BaasioUser>() {

                    @Override
                    public void onException(BaasioException e) {
                        // 실패
                        AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }

                    @Override
                    public void onResponse(BaasioUser response) {
                        mDialog.dismiss();
                        if (response != null) {
                            // 성공
                            String username = response.getUsername(); // ID(Username)
                            AndLog.d("Succeed : " + username + " is added");
                            moveToProjectInvitationActivity(groupUuid);
                        }
                    }
                });
    }
    
    // 그룹 회원 추가 activity로 이동
    void moveToProjectInvitationActivity(final UUID groupUuid) {
        Intent i = new Intent(getApplication(), 
                com.egoists.coco_nut.android.project.ProjectInvitationActivity_.class);
        i.putExtra("created_group_uuid", groupUuid.toString());
        startActivity(i);
        
        ProjectCreationActivity.this.finish();
    }
    
    // 그룹 탬플릿 선택
    @Click({
        R.id.imgGroupTemplete0, R.id.imgGroupTemplete1, R.id.imgGroupTemplete2, R.id.imgGroupTemplete3, R.id.imgGroupTemplete4, 
        R.id.imgGroupTemplete5, R.id.imgGroupTemplete6, R.id.imgGroupTemplete7, R.id.imgGroupTemplete8})
    void changeGroupTemplete(View clickedView) {
        // 그룹 탬플릿 저장
        int selectedGroupTemplete = Integer.parseInt((String)clickedView.getTag());
        AndLog.d("Chang group templete : " + selectedGroupTemplete);
        mTemplete = selectedGroupTemplete;
        
        // 선택 이미지로 교체
        imgSelectedGroupTemplete.setImageDrawable(((ImageView)clickedView).getDrawable());
    }
}
