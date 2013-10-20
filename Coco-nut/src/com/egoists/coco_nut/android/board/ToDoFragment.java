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
import com.egoists.coco_nut.android.board.card.Person;
import com.egoists.coco_nut.android.board.card.adapter.CardListAdapter;
import com.egoists.coco_nut.android.board.event.RequestTodoCardsEvent;
import com.egoists.coco_nut.android.board.event.TodoCardsEvent;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;

import de.greenrobot.event.EventBus;

@EFragment
public class ToDoFragment extends Fragment {
    private CardListAdapter mListAdapter;
    private ArrayList<Card> mTodoCards;
    Activity mContext;
    
    String mGroupUuid = null;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndLog.d("onCreateView");
        
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
        mTodoCards = new ArrayList<Card>();
        mListAdapter = new CardListAdapter(mContext, mTodoCards);
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
        EventBus.getDefault().post(new RequestTodoCardsEvent());
    }
    
    /**
     * EventBus로 부터 카드 획득
     * 리스트에 뿌려준다
     * @param event
     */
    public void onEvent(TodoCardsEvent event) {
        
        // TODO 테스트 할 것
//        mTodoCards = (ArrayList<Card>) event.cards;
//        for (Card c : mTodoCards) { // FOR Log
//            for (Person p : c.participants)
//            AndLog.d(p.name + " is involved");
//        }
        mTodoCards = (ArrayList<Card>) ((BoardTabActivity)mContext).mTodoCards;
      for (Card c : mTodoCards) { // FOR Log
      for (Person p : c.participants)
      AndLog.d(p.name + " is involved");
  }
        refreshCardList(mTodoCards);
    }
}
