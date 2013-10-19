package com.egoists.coco_nut.android.kanban.briefing;

import java.util.ArrayList;

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
import com.egoists.coco_nut.android.board.event.MyCardsEvent;
import com.egoists.coco_nut.android.board.event.RequestMyCardsEvent;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.fragment_briefing)
public class BriefingFragment extends Fragment {
	
	LinearLayout briefing_layout;
	LinearLayout gantt_list;
	LinearLayout wip_list;
	LinearLayout parti_list;
	LinearLayout peer_list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		briefing_layout = (LinearLayout) inflater.inflate(R.layout.fragment_briefing, container, false); 
		gantt_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_ganttlist);
		GanttView gantview = new GanttView(getActivity());
		gantt_list.addView(gantview);
		
		wip_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_wiplist);
		WIPView wipview = new WIPView(getActivity());
		wip_list.addView(wipview);

		parti_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_partilist);
		ParticipationView partiview = new ParticipationView(getActivity());
		parti_list.addView(partiview);
		
		peer_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_peerlist);
		PeerView peerview = new PeerView(getActivity());
		peer_list.addView(peerview);
		
		return briefing_layout;
	}

}
