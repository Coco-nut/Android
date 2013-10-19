package com.egoists.coco_nut.android.kanban.briefing;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.Card;
import com.egoists.coco_nut.android.board.card.adapter.CardListAdapter;
import com.egoists.coco_nut.android.board.event.AllCardsEvent;
import com.egoists.coco_nut.android.board.event.MyCardsEvent;
import com.egoists.coco_nut.android.board.event.RequestAllCardsEvent;
import com.egoists.coco_nut.android.board.event.RequestDoneCardsEvent;
import com.egoists.coco_nut.android.board.event.RequestMyCardsEvent;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.fragment_briefing)
public class BriefingFragment extends Fragment {
	
	PeerView peerview;
	ParticipationView partiview;
	WIPView wipview;
	GanttView gantview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		
		LinearLayout briefing_layout = (LinearLayout) inflater.inflate(R.layout.fragment_briefing, container, false); 
		LinearLayout gantt_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_ganttlist);
		gantview = new GanttView(getActivity());
		gantt_list.addView(gantview);
		
		LinearLayout wip_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_wiplist);
		wipview = new WIPView(getActivity());
		wip_list.addView(wipview);

		LinearLayout parti_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_partilist);
		partiview = new ParticipationView(getActivity());
		parti_list.addView(partiview);
		
		LinearLayout peer_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_peerlist);
		peerview = new PeerView(getActivity());
		peer_list.addView(peerview);
		
		return briefing_layout;
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AndLog.d("Register");
        EventBus.getDefault().register(this);
        
    }
    @Override
    public void onResume() {
        super.onResume();
        AndLog.d("onResume");
        requestCards();
    }
    
    void requestCards() {
        EventBus.getDefault().post(new RequestAllCardsEvent());
    }
    @Override
    public void onDestroy() {
        AndLog.d("Unregister");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    /**
     * EventBus로 부터 카드 획득
     * 리스트에 뿌려준다
     * @param event
     */
    public void onEvent(AllCardsEvent event) {
        gantview.refresh();
        wipview.refresh();
        partiview.refresh();
        peerview.refresh();
    }

}
