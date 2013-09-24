package com.egoists.coco_nut.android.project;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.R.id;
import com.egoists.coco_nut.android.R.layout;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.widget.TextView;

@EFragment(R.layout.fragment_project_info)
public class ProjectSelectionFragment extends Fragment {
	public static final String ARG_SECTION_NUMBER = "section_number";

	@ViewById(R.id.section_label)
	TextView dummyTextView;
	
	@AfterViews
	void calledAfterViewInjection() {
		dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
	}
}
