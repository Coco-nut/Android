package com.egoists.coco_nut.android.group;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.group.adapter.GroupSelectionPagerAdapter;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.query.BaasioQuery.ORDER_BY;

@EActivity(R.layout.activity_group_selection)
public class GroupSelectionActivity extends FragmentActivity {
    public static final int ACTIVITY_RESULT_REFRESH = 1;
    public static final String DUMMY_GROUP_TITLE = "FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF";
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
	    mDummyGroup.setTitle(DUMMY_GROUP_TITLE);
	    
	    // 아답터 셋팅
	    mGroupSelectionPagerAdapter = new GroupSelectionPagerAdapter(
		        getSupportFragmentManager(), new ArrayList<BaasioGroup>());
		mViewPager.setAdapter(mGroupSelectionPagerAdapter);
		
		mContext = this;
		getMyGroupsByBaasio();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	}
	
	@Click({R.id.btnCreateProject})
    public void createNewGroup() {
        Intent intent = new Intent(this, com.egoists.coco_nut.android.group.GroupCreationActivity_.class);
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
                com.egoists.coco_nut.android.group.SettingActivity.class));
    }
	
	@UiThread
    void refreshGroupList(List<BaasioGroup> groups) {
	    mGroupSelectionPagerAdapter.update(groups);
    }
	
	void getMyGroupsByBaasio() {
    	mDialog = ProgressDialog.show(GroupSelectionActivity.this, "", "내 그룹 가져오는 중", true);
        final BaasioUser user = Baas.io().getSignedInUser();
	    // 쿼리 전송
        BaasioQuery query = new BaasioQuery();
        query.setRawString("users/" + user.getUuid().toString() + "/groups");
        query.setOrderBy(BaasioBaseEntity.PROPERTY_MODIFIED, ORDER_BY.ASCENDING);
        query.setLimit(30);
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
