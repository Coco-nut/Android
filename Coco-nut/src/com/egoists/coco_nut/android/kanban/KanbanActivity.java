package com.egoists.coco_nut.android.kanban;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.kanban.briefing.BriefingFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_kanban)
public class KanbanActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * TODO FragmentPagerAdapter를 받겠지만 한번에 메모리 로딩을 해야함. (5페이지 전부)
	 * 메모리 부담이 크다고 판단되면 FragmentStatePagerAdapter로 바꾸기.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	@ViewById(R.id.pager_kanban)
	ViewPager mViewPager;

	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // This is called when the Home (Up) button is pressed in the action bar.
	            // Create a simple intent that starts the hierarchical parent activity and
	            // use NavUtils in the Support Package to ensure proper handling of Up.
//	            Intent upIntent = new Intent(this, ProjectSelectionActivity_.class);
//	            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//	                // This activity is not part of the application's task, so create a new task
//	                // with a synthesized back stack.
//	                TaskStackBuilder.from(this)
//	                        // If there are ancestor activities, they should be added here.
//	                        .addNextIntent(upIntent)
//	                        .startActivities();
//	                finish();
//	            } else {
//	                // This activity is part of the application's task, so simply
//	                // navigate up to the hierarchical parent activity.
//	                NavUtils.navigateUpTo(this, upIntent);
//	            }
	        	finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}
	
	@AfterViews
	void initViewPager() {
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// actionbar에 tab 추가.
		int tabCount = mSectionsPagerAdapter.getCount();
		for (int i = 0; i < tabCount; i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// NOTE : AndroidAnnotation을 사용한 Fragments 는 끝에 '_' 처리.
			switch (position) {
			case 0:
				return new MyWorksFragment_();
			case 1:
				return new BriefingFragment();
			case 2:
				return new ToDoFragment_();
			case 3:
				return new InProgressFragment_();
			case 4:
			default:
				return new DoneFragment_();
			}
		}

		@Override
		public int getCount() {
			// Show 5 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_my_works);
			case 1:
				return getString(R.string.title_briefing);
			case 2:
				return getString(R.string.title_to_do);
			case 3:
				return getString(R.string.title_in_progress);
			case 4:
				return getString(R.string.title_done);
			}
			return null;
		}
	}

}
