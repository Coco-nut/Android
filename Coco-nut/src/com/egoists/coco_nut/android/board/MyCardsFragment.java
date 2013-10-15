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
import com.egoists.coco_nut.android.board.adapter.CardListAdapter;
import com.egoists.coco_nut.android.board.card.Card;
import com.egoists.coco_nut.android.board.event.MyCardsEvent;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;

import de.greenrobot.event.EventBus;

@EFragment
public class MyCardsFragment extends Fragment {
    private CardListAdapter mListAdapter;
    Activity mContext;
    
    String mGroupUuid = null;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // GROUP UUID 추출
        Bundle bundle = getArguments();
        if (bundle == null) {
            return null;
        }
        mGroupUuid = bundle.getString(BoardTabActivity.ARG_GROUP_UUID);
        AndLog.d("Group UUID : " + mGroupUuid);
        
        View view = inflater.inflate(R.layout.fragment_my_cards, container, false);
        
        // 카드 리스트
        mListAdapter = new CardListAdapter(mContext, new ArrayList<Card>());
        ListView list = (ListView)view.findViewById(R.id.list);  
        list.setAdapter(mListAdapter);
        
        return view;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
    
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
        
    @UiThread
    void refreshCardList(List<Card> cards) {
        mListAdapter.update(cards);
    }
    
    /**
     * EventBus로 부터 카드 획득
     * 리스트에 뿌려준다
     * @param event
     */
    public void onEvent(MyCardsEvent event) {
        refreshCardList(event.myCards);
    }
}
