package com.egoists.coco_nut.android.board;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.Card;
import com.egoists.coco_nut.android.board.card.adapter.CardListAdapter;
import com.egoists.coco_nut.android.board.event.MyCardsEvent;
import com.egoists.coco_nut.android.board.event.RequestMyCardsEvent;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;

import de.greenrobot.event.EventBus;

@EFragment
public class MyCardsFragment extends Fragment {
    private CardListAdapter mListAdapter;
    private ArrayList<Card> mMyCards;
    Activity mContext;
    
    String mGroupUuid = null;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndLog.d("onCreateView");
        // GROUP UUID 추출
        View view = inflater.inflate(R.layout.fragment_my_cards, container, false);
        
        ListView list = (ListView)view.findViewById(R.id.list);  
        list.setAdapter(mListAdapter);
        
        return view;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        
        AndLog.d("Register");
        EventBus.getDefault().register(this);
        
        // 카드 리스트
        mMyCards = new ArrayList<Card>();
        mListAdapter = new CardListAdapter(mContext, mMyCards);
    }

    @Override
    public void onDestroy() {
        AndLog.d("Unregister");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        AndLog.d("onResume");
        requestCards();
    }
        
    @UiThread
    void refreshCardList(List<Card> cards) {
        AndLog.d("Refresh " + cards.size() + " cards");
        mListAdapter.update(cards);
    }
    
    ///////////////////////////////////////////////////////
    //  EventBus 관련 이벤트 처리부
    ///////////////////////////////////////////////////////
    
    // BoadTabActivity로부터 카드를 요청한다
    void requestCards() {
        EventBus.getDefault().post(new RequestMyCardsEvent());
    }
    
    /**
     * EventBus로 부터 카드 획득
     * 리스트에 뿌려준다
     * @param event
     */
    public void onEvent(MyCardsEvent event) {
        AndLog.d("Get event");
        mMyCards = (ArrayList<Card>) event.myCards;
        refreshCardList(mMyCards);
    }
}
