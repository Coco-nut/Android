package com.egoists.coco_nut.android.project;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.LoginPreference;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.query.BaasioQuery.ORDER_BY;

@EActivity(R.layout.activity_project_selection)
public class ProjectSelectionActivity extends FragmentActivity {
    private LoginPreference mLoginPref;
    private Context mContext;
    
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	@ViewById(R.id.pager_project)
	ViewPager mViewPager;

	@AfterViews
	void initViewPager() {
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		mContext = this;
		mLoginPref = new LoginPreference(mContext);
		getMyGroupsByBaasio();
	}
	
	@Click({R.id.btnCreateProject})
    void createNewProject() {
        Intent intent = new Intent(this, com.egoists.coco_nut.android.project.ProjectCreationActivity_.class);
        startActivity(intent);
    }
	
	@Click({R.id.btn_notices})
	void goToKanban() {
		Intent intent = new Intent(this, com.egoists.coco_nut.android.kanban.KanbanActivity_.class);
		startActivity(intent);
	}
	
	@Click({R.id.btn_setting})
	void delLoginPreference() {
	    mLoginPref.savePreference("", "", "");
	    Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
	    moveToLoginActivity();
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new ProjectSelectionFragment_();
			Bundle args = new Bundle();
			args.putInt(ProjectSelectionFragment_.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}
	}
	
	void moveToLoginActivity() {
        // 로딩이 끝난후 이동할 Activity
        startActivity(new Intent(getApplication(), com.egoists.coco_nut.android.login.LoginActivity_.class)); 
        ProjectSelectionActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
    }
	
	void getMyGroups() {
	    
	}
	
	void getMyGroupsByBaasio() {
    	// 로그인 정보 가져오기
        mLoginPref.loadPreference();
        
	    // 쿼리 전송
        BaasioQuery query = new BaasioQuery();
        query.setRawString("users/" + mLoginPref.mUuid + "/groups");
        query.setOrderBy(BaasioBaseEntity.PROPERTY_MODIFIED, ORDER_BY.DESCENDING);
        query.queryInBackground(new BaasioQueryCallback() { // 질의 요청

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list, BaasioQuery query, long timestamp) {
                AndLog.d("Succeed : get my groups");
                
                List<BaasioGroup> groups = BaasioBaseEntity.toType(entities, BaasioGroup.class);
                for (BaasioGroup group : groups) {
                    AndLog.d(group.getTitle());
                }
                
            }

            @Override
            public void onException(BaasioException e) {
                AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                BaasioDialogFactory.createErrorDialog(mContext, e).show();
            }
        });
	}
}
