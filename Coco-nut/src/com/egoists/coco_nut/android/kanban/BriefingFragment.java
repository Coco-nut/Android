package com.egoists.coco_nut.android.kanban;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BriefingFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_briefing, container, false);
		return rootView;
	}
}
