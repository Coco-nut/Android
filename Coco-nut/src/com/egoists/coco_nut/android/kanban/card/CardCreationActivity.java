package com.egoists.coco_nut.android.kanban.card;

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
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;

@EActivity(R.layout.activity_card_creation)
public class CardCreationActivity extends Activity {
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
    
    void createCard() {
        //
        String projectName = edTxtCreateProj.getText().toString();
        if (projectName == null || projectName.length() == 0) {
            //BaasioDialogFactory.createErrorDialog(mContext, "제목이 비었음").show();
            return;
        }
    }
    
    // 그룹 회원 추가 activity로 이동
    void moveToProjectInvitationActivity(final UUID groupUuid) {
        Intent i = new Intent(getApplication(), 
                com.egoists.coco_nut.android.project.ProjectInvitationActivity_.class);
        i.putExtra("created_group_uuid", groupUuid.toString());
        startActivity(i);
        
        CardCreationActivity.this.finish();
    }
}
