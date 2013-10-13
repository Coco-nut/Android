package com.egoists.coco_nut.android.project;

import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

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
    
    private Context mContext;
    private LoginPreference mLoginPref;
    private ProgressDialog mDialog;
    
    @AfterViews
    void initForm() {
        mContext = this;
        mLoginPref = new LoginPreference(mContext);
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.creation_project_coconut, menu);
//        return true;
//    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
//            case R.id.menu_create_project:
//                createProject();
//                return true;
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
    
    void addMeIntoCreatedGroup(final UUID groupUuid) {
        // UUID 정보 가져오기
        mLoginPref.loadPreference();
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
}
