package com.egoists.coco_nut.android.project;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.UniqueString;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.exception.BaasioException;

@EActivity(R.layout.activity_project_creation)
public class ProjectCreationActivity extends Activity {
    @ViewById
    EditText edTxtCreateProj;
    
    private Context mContext;
    
    @AfterViews
    void initForm() {
        mContext = this;
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.creation_project_coconut, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_create_project:
                createProject();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    void createProject() {
        // 프로젝트 이름 폼 체크
        String projectName = edTxtCreateProj.getText().toString();
        if (projectName == null || projectName.length() == 0) {
            BaasioDialogFactory.createErrorDialog(mContext, R.string.error_empty_project_name).show();
            return;
        }
        
        doCreateProjectByBaasio(projectName);
    }
    
    void moveToProjectInvitationActivity() {
        // 회원 가입 activity로 이동
        startActivity(new Intent(getApplication(), 
                com.egoists.coco_nut.android.project.ProjectInvitationActivity_.class));
    }

    void doCreateProjectByBaasio(String projectName) {
        String projectPath = UniqueString.generate();
        AndLog.d("Try to create project : " + projectName + ", " + projectPath);
        BaasioGroup group = new BaasioGroup();
        group.setTitle(projectName);    // 그룹 표시내용
        group.setPath(projectPath);     // 그룹 Unique한 Path 이름
        group.saveInBackground(
                new BaasioCallback<BaasioGroup>() {
                    @Override
                    public void onException(BaasioException e) {
                        AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }

                    @Override
                    public void onResponse(BaasioGroup response) {
                        if (response != null) {
                            // 성공
                            String path = response.getPath();           // Group path
                            AndLog.d("Succeed : " + path + " project created.");
//                            BaasioDialogFactory.createFinishButtonDialog(
//                                    ProjectCreationActivity.this, R.string.title_succeed, R.string.create_project_succeed).show();
                            moveToProjectInvitationActivity();
                        }
                    }
                });
    }
}
