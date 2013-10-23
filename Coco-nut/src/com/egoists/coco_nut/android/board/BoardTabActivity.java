package com.egoists.coco_nut.android.board;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.Card;
import com.egoists.coco_nut.android.board.card.Cards;
import com.egoists.coco_nut.android.board.event.AllCardsEvent;
import com.egoists.coco_nut.android.board.event.DoingCardsEvent;
import com.egoists.coco_nut.android.board.event.DoneCardsEvent;
import com.egoists.coco_nut.android.board.event.GroupUsersEvent;
import com.egoists.coco_nut.android.board.event.MyCardsEvent;
import com.egoists.coco_nut.android.board.event.ReloadEvent;
import com.egoists.coco_nut.android.board.event.RequestAllCardsEvent;
import com.egoists.coco_nut.android.board.event.RequestDoingCardsEvent;
import com.egoists.coco_nut.android.board.event.RequestDoneCardsEvent;
import com.egoists.coco_nut.android.board.event.RequestGroupUsersEvent;
import com.egoists.coco_nut.android.board.event.RequestMyCardsEvent;
import com.egoists.coco_nut.android.board.event.RequestTodoCardsEvent;
import com.egoists.coco_nut.android.board.event.TodoCardsEvent;
import com.egoists.coco_nut.android.kanban.briefing.BriefingFragment;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.query.BaasioQuery.ORDER_BY;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_kanban)
public class BoardTabActivity extends FragmentActivity implements TabListener {
    @Extra("group_name")
    String mExtraGroupName;
    @Extra("group_uuid")
    String mExtraGroupUuid;
    @ViewById(R.id.pager_kanban)
    ViewPager mViewPager;
    
    Activity mContext;
    private ProgressDialog mDialog;
    
    public List<Card> mCards;    // 그룹의 가져온 전체 카드
    public List<Card> mMyCards;  // 내 카드
    public List<Card> mTodoCards;    // 할일 카드
    public List<Card> mDoingCards;    // 하는중 카드
    public List<Card> mDoneCards;    // 완료 카드
    
    public List<BaasioUser> mUsers;
    
    private final String RELATION_NAME          = "group_card";
    public static final String ARG_GROUP_UUID   = "group_uuid";
    
    /**
     * TODO FragmentPagerAdapter를 받겠지만 한번에 메모리 로딩을 해야함. (5페이지 전부)
     * 메모리 부담이 크다고 판단되면 FragmentStatePagerAdapter로 바꾸기.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    
    @AfterViews
    void initViewPager() {
        EventBus.getDefault().register(this);
        mContext = this;
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setLogo(R.drawable.ic_coconut_white_logo);
//        Android BUG! 해결책은?
//        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        actionBar.setTitle(mExtraGroupName);
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
        
        mViewPager.setCurrentItem(2);   // 기본은 진행중
        
        mCards = new ArrayList<Card>();
        mMyCards = new ArrayList<Card>();
        mTodoCards = new ArrayList<Card>();
        mDoingCards = new ArrayList<Card>();
        mDoneCards = new ArrayList<Card>();
        
        // 그룹의 사용자 획득
        getGroupUsersByBaasio();
    }
    
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kanban_menu_coconut, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_add:
                Intent i = new Intent(this, com.egoists.coco_nut.android.board.card.CardCreationActivity_.class);
                i.putExtra(ARG_GROUP_UUID, mExtraGroupUuid);
                startActivity(i);
                return true;
//            case R.id.menu_notification:
//                //노티..
//                return true;
            case R.id.menu_setting:
                Intent intent = new Intent(this, com.egoists.coco_nut.android.group.GroupSettingActivity.class);
                intent.putExtra(ARG_GROUP_UUID, mExtraGroupUuid);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////////////////////////////
    //  EventBus 관련 이벤트 처리부
    ///////////////////////////////////////////////////////
    
    // 서버로부터 다시 카드를 받는다
    public void onEvent(ReloadEvent event) {
        getGroupCardsByBaasio();
    }
    
    // MyCards Fragment에 카드를 쏴준다
    public void onEvent(RequestMyCardsEvent event) {
        EventBus.getDefault().post(new MyCardsEvent(mMyCards));
    }
    
    // TodoFragment에 카드를 쏴준다
    public void onEvent(RequestTodoCardsEvent event) {
        EventBus.getDefault().post(new TodoCardsEvent(mTodoCards));
    }
    
    // DoingCards Fragment에 카드를 쏴준다
    public void onEvent(RequestDoingCardsEvent event) {
        EventBus.getDefault().post(new DoingCardsEvent(mDoingCards));
    }
    
    // DoneCards Fragment에 카드를 쏴준다
    public void onEvent(RequestDoneCardsEvent event) {
        EventBus.getDefault().post(new DoneCardsEvent(mDoneCards));
    }

    // Brifing Fragment에 카드를 쏴준다
    public void onEvent(RequestAllCardsEvent event) {
        EventBus.getDefault().post(new AllCardsEvent(mCards));
    }
    
    // 그룹의 사용자들 정보를 전송한다
    public void onEvent(RequestGroupUsersEvent event) {
        EventBus.getDefault().post(new GroupUsersEvent(mUsers));
    }
    
    /**
     * 카드 다운 완료 이벤트를 각 프레그먼트로 보낸다
     */
    void dispatchGroupCardsToFragments(List<BaasioEntity> bassioCards) {
        Cards cardManager = new Cards();
        mCards = cardManager.toCardArray(getResources(), bassioCards, mUsers);
        
        classifyAllCards();
        
        EventBus.getDefault().post(new MyCardsEvent(mMyCards));
        EventBus.getDefault().post(new TodoCardsEvent(mTodoCards));
        EventBus.getDefault().post(new DoingCardsEvent(mDoingCards));
        EventBus.getDefault().post(new DoneCardsEvent(mDoneCards));
    }
    
    void classifyAllCards() {
        mMyCards.clear();
        mTodoCards.clear();
        mDoingCards.clear();
        mDoneCards.clear();
        for (Card card : mCards) {
            AndLog.d("Classifying " + card.title + " card...");

            // 내가 속한 카드
            if (card.ismine) {
                mMyCards.add(card);
            }
            
            // 카드 상태 (할일, 하는중, 한일) 분배
            switch (card.status) {
            case 0:
                mTodoCards.add(card);
                break;
            case 1:
                mDoingCards.add(card);
                break;
            case 2:
                mDoneCards.add(card);
                break;
            default :
                break;
            }
        }
    }
    
    ///////////////////////////////////////////////////////
    //  BaasIO 관련 통신 처리부
    ///////////////////////////////////////////////////////
    
    /**
     * 해당 그룹의 사용자 추출
     * @param groupUuid
     */
    void getGroupUsersByBaasio() {
        mDialog = ProgressDialog.show(mContext, "", "카드 가져오는 중", true);
        AndLog.d("Getting group users ... ");
        // 쿼리 전송
        BaasioQuery query = new BaasioQuery();
        query.setRawString("groups/" + mExtraGroupUuid + "/users");
        query.setOrderBy(BaasioBaseEntity.PROPERTY_MODIFIED, ORDER_BY.DESCENDING);
        query.queryInBackground(new BaasioQueryCallback() { // 질의 요청

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list, BaasioQuery query, long timestamp) {
                mUsers = BaasioBaseEntity.toType(entities, BaasioUser.class);
                // 그룹의 모든 카드 획득
                getGroupCardsByBaasio();
            }

            @Override
            public void onException(BaasioException e) {
                mDialog.dismiss();
                AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
            }
        });
    }
    
    /**
     * 해당 그룹의 카드 리스트 추출
     * @param groupUuid
     */
    void getGroupCardsByBaasio() {
        BaasioGroup group = new BaasioGroup();
        group.setUuid(UUID.fromString(mExtraGroupUuid));
        BaasioQuery query = new BaasioQuery();
        query.setRelation(
                group               // 그룹
                , RELATION_NAME);   // 관계 카드
        query.setLimit(999);
        query.queryInBackground(new BaasioQueryCallback() {

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list, BaasioQuery query, long timestamp) {
                mDialog.dismiss();
                // 성공
                List<BaasioEntity> cards = BaasioBaseEntity.toType(entities, BaasioEntity.class);
                dispatchGroupCardsToFragments(cards);
            }

            @Override
            public void onException(BaasioException e) {
                mDialog.dismiss();
                if (e.getErrorCode() == 0) {
                    // 네트워크 오류
                    BaasioDialogFactory.createErrorDialog(mContext, R.string.error_network).show();
                    return;
                }
                BaasioDialogFactory.createErrorDialog(mContext, e).show();
            }
        });
    }
    
    ///////////////////////////////////////////////////////
    //  Tab 관리
    ///////////////////////////////////////////////////////

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Fragment m;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // NOTE : AndroidAnnotation을 사용한 Fragments 는 끝에 '_' 처리.
            switch (position) {
            case 0:
                AndLog.d("My Card Fragment");
                return new MyCardsFragment_();
            case 1:
                AndLog.d("ToDo Fragment");
                return new ToDoFragment_();
            case 2:
                AndLog.d("Doing Fragment");
                return new DoingFragment_();
            case 3:
                AndLog.d("Done Fragment");
                return new DoneFragment_();
            case 4:
            default:
                AndLog.d("Brieging Fragment");
                return new BriefingFragment();
                
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
                return getString(R.string.title_to_do);
            case 2:
                return getString(R.string.title_in_progress);
            case 3:
                return getString(R.string.title_done);
            case 4:
                return getString(R.string.title_briefing);
            }
            return null;
        }
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
}
