package com.egoists.coco_nut.android.kanban;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.kanban.briefing.BriefingFragment;
import com.egoists.coco_nut.android.kanban.card.Card;
import com.egoists.coco_nut.android.kanban.card.KanbanData;
import com.egoists.coco_nut.android.kanban.card.Person;
import com.egoists.coco_nut.android.kanban.card.Checkbox;
import com.egoists.coco_nut.android.kanban.card.Comment;
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
	public KanbanData kanbanData;

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
		// 서버에서 participantList를 받아옴. 지금은 더미 데이터 생성 부분
		getKanbanData();
	}
	
	private void getKanbanData()
	{
		//서버에서 카드/참여자 정보 불러옴. 현재 더미 데이터 입력
		kanbanData = new KanbanData();
		
		kanbanData.participants.put("준수", new Person("준수", getResources(), true));
		kanbanData.participants.put("윤후", new Person("윤후", getResources(), false));
		kanbanData.participants.put("지아", new Person("지아", getResources(), false));
		kanbanData.participants.put("누군지", new Person("누군지", getResources(), false));
		kanbanData.participants.put("몰라", new Person("몰라", getResources(), false));
		kanbanData.participants.put("사실", new Person("사실", getResources(), false));
		kanbanData.participants.put("누구든", new Person("누구든", getResources(), false));
		kanbanData.participants.put("별로", new Person("별로", getResources(), false));
		kanbanData.participants.put("상관", new Person("상관", getResources(), false));
		kanbanData.participants.put("없지", new Person("없지", getResources(), false));
		Calendar tempdate = Calendar.getInstance();
		tempdate.set(Calendar.MONTH, 8);
		tempdate.set(Calendar.DATE, 30);
		ArrayList<Comment> tempcomments = new ArrayList<Comment>();
		tempcomments.add(new Comment(kanbanData.participants.get("준수"), 
				"다들 월요일에 만나요~"));
		tempcomments.add(new Comment(kanbanData.participants.get("윤후"), 
				"답사 화이팅 ^^!"));
		tempcomments.add(new Comment(kanbanData.participants.get("지아"), 
				"네넵!"));
		
		ArrayList<Checkbox> tempcheckboxes = new ArrayList<Checkbox>();
		tempcheckboxes.add(new Checkbox("카메라 들고 오기", true));
		tempcheckboxes.add(new Checkbox("창덕궁 자료조사 파일 가져오기", false));
		
		ArrayList<Person> tempparticipants = new ArrayList<Person>();
		tempparticipants.add(kanbanData.participants.get("준수"));
		tempparticipants.add(kanbanData.participants.get("윤후"));
		tempparticipants.add(kanbanData.participants.get("지아"));
		tempparticipants.add(kanbanData.participants.get("몰라"));
		
		kanbanData.Do.add(new Card("고궁 답사", "창덕궁", 
				"창덕궁 답사 일정입니다.\n"
				+ "안국역 3번 출구에서 9시까지 만나요 :D\n\n"
				+ "윤후는 카메라 꼭 들고 와주고, \n"
				+ "지아는 창덕궁 자료 조사했던 파일 꼭 들고 와줘요!\n\n"
				+ "일반 관람 코스와 후원 관람 코스 두가지 코스를 다 돌아볼 거에요.\n\n"
				+ "/일반 관람 코스\n"
				+ "돈화문 - 궐내각사 - 금천교 - 인정문 - 희정당 - 내조전 - 낙선재\n\n"
				+ "/후원 관람 코스\n"
				+ "함양문 - 부용정 - 의두합 - 불로문 - 애련지권역 - 연경당\n"
				+ "- 존덕정권역 - 옥류천 - 돈화문"
				, 1, 3,
			null, null, null, tempdate, tempcomments, tempcheckboxes,
			tempparticipants, 0, true));
		kanbanData.Do.add(new Card("고궁 답사", "창덕궁", 
				"창덕궁 답사 일정입니다.\n"
				+ "안국역 3번 출구에서 9시까지 만나요 :D\n\n"
				+ "윤후는 카메라 꼭 들고 와주고, \n"
				+ "지아는 창덕궁 자료 조사했던 파일 꼭 들고 와줘요!\n\n"
				+ "일반 관람 코스와 후원 관람 코스 두가지 코스를 다 돌아볼 거에요.\n\n"
				+ "/일반 관람 코스\n"
				+ "돈화문 - 궐내각사 - 금천교 - 인정문 - 희정당 - 내조전 - 낙선재\n\n"
				+ "/후원 관람 코스\n"
				+ "함양문 - 부용정 - 의두합 - 불로문 - 애련지권역 - 연경당\n"
				+ "- 존덕정권역 - 옥류천 - 돈화문"
				, 2, 3,
			null, null, null, tempdate, tempcomments, tempcheckboxes,
			tempparticipants, 0, true));
		kanbanData.Do.add(new Card("고궁 답사", "창덕궁", 
				"창덕궁 답사 일정입니다.\n"
				+ "안국역 3번 출구에서 9시까지 만나요 :D\n\n"
				+ "윤후는 카메라 꼭 들고 와주고, \n"
				+ "지아는 창덕궁 자료 조사했던 파일 꼭 들고 와줘요!\n\n"
				+ "일반 관람 코스와 후원 관람 코스 두가지 코스를 다 돌아볼 거에요.\n\n"
				+ "/일반 관람 코스\n"
				+ "돈화문 - 궐내각사 - 금천교 - 인정문 - 희정당 - 내조전 - 낙선재\n\n"
				+ "/후원 관람 코스\n"
				+ "함양문 - 부용정 - 의두합 - 불로문 - 애련지권역 - 연경당\n"
				+ "- 존덕정권역 - 옥류천 - 돈화문"
				, 3, 3,
			null, null, null, tempdate, tempcomments, tempcheckboxes,
			tempparticipants, 0, true));
		kanbanData.Do.add(new Card("고궁 답사", "창덕궁", 
				"창덕궁 답사 일정입니다.\n"
				+ "안국역 3번 출구에서 9시까지 만나요 :D\n\n"
				+ "윤후는 카메라 꼭 들고 와주고, \n"
				+ "지아는 창덕궁 자료 조사했던 파일 꼭 들고 와줘요!\n\n"
				+ "일반 관람 코스와 후원 관람 코스 두가지 코스를 다 돌아볼 거에요.\n\n"
				+ "/일반 관람 코스\n"
				+ "돈화문 - 궐내각사 - 금천교 - 인정문 - 희정당 - 내조전 - 낙선재\n\n"
				+ "/후원 관람 코스\n"
				+ "함양문 - 부용정 - 의두합 - 불로문 - 애련지권역 - 연경당\n"
				+ "- 존덕정권역 - 옥류천 - 돈화문"
				, 4, 3,
			null, null, null, tempdate, tempcomments, tempcheckboxes,
			tempparticipants, 0, true));
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
