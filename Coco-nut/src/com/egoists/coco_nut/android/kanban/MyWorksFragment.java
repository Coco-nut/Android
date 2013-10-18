package com.egoists.coco_nut.android.kanban;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.kanban.card.CardView;
import com.egoists.coco_nut.android.kanban.card.KanbanData;
import com.googlecode.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_my_works)
public class MyWorksFragment extends Fragment {
	
	private KanbanData kanbanData;
	
	private ScrollView sv;
	private LinearLayout linear_layout;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		sv = (ScrollView) inflater.inflate(R.layout.fragment_to_do, container, false); 
		
		kanbanData = ((KanbanActivity)getActivity()).kanbanData;
		linear_layout = (LinearLayout) sv.findViewById(R.id.todo_cardlist);
		for (int i = 0; i < kanbanData.Doing.size(); i++)
		{
			if(kanbanData.Doing.get(i).ismine)
			{
				if (i != 0){
					LinearLayout space = new LinearLayout(getActivity());
					space.setMinimumHeight((int)(getResources().getDisplayMetrics().density * 8 + 0.5));
					linear_layout.addView(space);
				}
				CardView cardview = new CardView(getActivity(), kanbanData.Doing.get(i));
				linear_layout.addView(cardview);
			}
		}
		for (int i = 0; i < kanbanData.Do.size(); i++)
		{
			if(kanbanData.Do.get(i).ismine)
			{
				if (i != 0){
					LinearLayout space = new LinearLayout(getActivity());
					space.setMinimumHeight((int)(getResources().getDisplayMetrics().density * 8 + 0.5));
					linear_layout.addView(space);
				}
				CardView cardview = new CardView(getActivity(), kanbanData.Do.get(i));
				linear_layout.addView(cardview);
			}
		}
		for (int i = 0; i < kanbanData.Done.size(); i++)
		{
			if(kanbanData.Done.get(i).ismine)
				{
				if (i != 0){
					LinearLayout space = new LinearLayout(getActivity());
					space.setMinimumHeight((int)(getResources().getDisplayMetrics().density * 8 + 0.5));
					linear_layout.addView(space);
				}
				CardView cardview = new CardView(getActivity(), kanbanData.Done.get(i));
				linear_layout.addView(cardview);
			}
		}
		return sv;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.findItem(R.id.menu_add).setVisible(true);
	}
}
