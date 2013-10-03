package com.egoists.coco_nut.android.kanban.briefing;

import com.egoists.coco_nut.android.R;
import com.googlecode.androidannotations.annotations.EFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


@EFragment(R.layout.fragment_briefing)
public class BriefingFragment extends Fragment {
	
	LinearLayout briefing_layout;
	LinearLayout gantt_list;
	LinearLayout wip_list;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		briefing_layout = (LinearLayout) inflater.inflate(R.layout.fragment_briefing, container, false); 
		gantt_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_ganttlist);
		GanttView gantview = new GanttView(getActivity());
		gantt_list.addView(gantview);
		
		wip_list = (LinearLayout) briefing_layout.findViewById(R.id.pager_kanban_briefing_wiplist);
		WIPView wipview = new WIPView(getActivity());
		wip_list.addView(wipview);
		
		return briefing_layout;
	}
	
}
