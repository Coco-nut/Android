package com.egoists.coco_nut.android.project;

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
import com.egoists.coco_nut.android.util.LoginPreference;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

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
	    mLoginPref.savePreference("", "");
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
}
