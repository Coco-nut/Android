package com.egoists.coco_nut.android.kanban;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
	
	private RelativeLayout relative_layout;
	private LinearLayout linear_layout;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		relative_layout = (RelativeLayout) inflater.inflate(R.layout.fragment_to_do, container, false); 
		
		kanbanData = ((KanbanActivity)getActivity()).kanbanData;
		linear_layout = (LinearLayout) relative_layout.findViewById(R.id.todo_cardlist);
		for (int i = 0; i < kanbanData.Do.size(); i++)
		{
			AndLog.i("" + kanbanData.Do.size());
			CardView cardview = new CardView(getActivity(), kanbanData.Do.get(i));
			linear_layout.addView(cardview);
		}
		return relative_layout;
	}
}
