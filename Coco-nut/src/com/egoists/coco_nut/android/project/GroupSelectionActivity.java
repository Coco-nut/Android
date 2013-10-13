package com.egoists.coco_nut.android.project;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.kanban.KanbanSettingActivity;
import com.egoists.coco_nut.android.project.adapter.GroupSelectionPagerAdapter;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.LoginPreference;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.query.BaasioQuery.ORDER_BY;

@EActivity(R.layout.activity_group_selection)
public class GroupSelectionActivity extends FragmentActivity {
    private Context mContext;
    private ProgressDialog mDialog;
    
    GroupSelectionPagerAdapter mGroupSelectionPagerAdapter;
    BaasioGroup mDummyGroup;
	
	@ViewById(R.id.pager_project)
	ViewPager mViewPager;

	@AfterViews
	void initViewPager() {
	    // 실제 그룹이 아닌 놈.
	    // Adapter는 얘를 만나면 실제 그룹이 아니라 그룹 생성을 표시한다.
	    mDummyGroup = new BaasioGroup();
	    mDummyGroup.setTitle("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF");
	    
	    // 아답터 셋팅
	    mGroupSelectionPagerAdapter = new GroupSelectionPagerAdapter(
		        getSupportFragmentManager(), new ArrayList<BaasioGroup>());
		mViewPager.setAdapter(mGroupSelectionPagerAdapter);
		
		mContext = this;
		KanbanSettingActivity.LoginPref = new LoginPreference(mContext);
	}
	
	@Override
	protected void onResume() {
	    getMyGroupsByBaasio();
	    super.onResume();
	}
	
	@Click({R.id.btnCreateProject})
    void createNewProject() {
        Intent intent = new Intent(this, com.egoists.coco_nut.android.project.GroupCreationActivity_.class);
        startActivity(intent);
    }
	
	@Click({R.id.btn_notices})
	void goToKanban() {
		Intent intent = new Intent(this, com.egoists.coco_nut.android.kanban.KanbanActivity_.class);
		startActivity(intent);
	}
	
	@Click({R.id.btn_setting})
	void moveToSettingActivity() {
        startActivity(new Intent(getApplication(), 
                com.egoists.coco_nut.android.kanban.KanbanSettingActivity.class));
    }
	
	@UiThread
    void refreshGroupList(List<BaasioGroup> groups) {
	    mGroupSelectionPagerAdapter.update(groups);
    }
	
	void moveToLoginActivity() {
        // 로딩이 끝난후 이동할 Activity
        startActivity(new Intent(getApplication(), com.egoists.coco_nut.android.login.LoginActivity_.class)); 
        GroupSelectionActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
    }
	
	void getMyGroups() {
	    
	}
	
	void getMyGroupsByBaasio() {
    	// 로그인 정보 가져오기
	    KanbanSettingActivity.LoginPref.loadPreference();
        
        mDialog = ProgressDialog.show(GroupSelectionActivity.this, "", "내 그룹 가져오는 중", true);
        
	    // 쿼리 전송
        BaasioQuery query = new BaasioQuery();
        query.setRawString("users/" + KanbanSettingActivity.LoginPref.mUuid + "/groups");
        query.setOrderBy(BaasioBaseEntity.PROPERTY_MODIFIED, ORDER_BY.DESCENDING);
        query.queryInBackground(new BaasioQueryCallback() { // 질의 요청

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list, BaasioQuery query, long timestamp) {
                AndLog.d("Succeed : get my groups");
                mDialog.dismiss();
                List<BaasioGroup> groups = BaasioBaseEntity.toType(entities, BaasioGroup.class);
                groups.add(mDummyGroup);
                refreshGroupList(groups);
            }

            @Override
            public void onException(BaasioException e) {
                mDialog.dismiss();
                AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                BaasioDialogFactory.createErrorDialog(mContext, e).show();
            }
        });
	}
}
