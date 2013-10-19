package com.egoists.coco_nut.android.kanban.briefing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.event.AllCardsEvent;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.EFragment;


@EFragment(R.layout.fragment_briefing)
public class BriefingFragment extends Fragment {
	
	GanttView gantview;
	WIPView wipview;
	ParticipationView partiview;
	PeerView peerview;
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.findItem(R.id.menu_add).setVisible(false);
	}
    /**
     * EventBus로 부터 카드 획득
     * 리스트에 뿌려준다
     * @param event
     */
    public void onEvent(AllCardsEvent event) {
        AndLog.d("Get event");
        ganttview.refresh();
    }
}
