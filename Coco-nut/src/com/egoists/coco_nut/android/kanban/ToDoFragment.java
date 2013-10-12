package com.egoists.coco_nut.android.kanban;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.R.layout;
import com.egoists.coco_nut.android.kanban.briefing.GanttView;
import com.egoists.coco_nut.android.kanban.card.CardView;
import com.egoists.coco_nut.android.kanban.card.KanbanData;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_to_do)
public class ToDoFragment extends Fragment {
	
	private KanbanData kanbanData;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		RelativeLayout relative_layout = 
				(RelativeLayout) inflater.inflate(R.layout.fragment_to_do, container, false); 
		
		kanbanData = ((KanbanActivity)getActivity()).kanbanData;
		ScrollView sv = new ScrollView(getActivity());
		sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        ll.setOrientation(1);
        sv.addView(ll);
		for (int i = 0; i < kanbanData.Do.size(); i++)
		{
			AndLog.i("" + kanbanData.Do.size());
			CardView cardview = new CardView(getActivity(), kanbanData.Do.get(i));
			ll.addView(cardview);
		}
        relative_layout.addView(sv);
		return relative_layout;
	}
}
