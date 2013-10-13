package com.egoists.coco_nut.android.project.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_group_info)
public class GroupSelectionFragment extends Fragment {
	public static final String ARG_GROUP_NAME      = "group_name";
	public static final String ARG_GROUP_TEMPLETE  = "group_templete";
	public static final String ARG_GROUP_UUID      = "group_uuid";
	public static final String ARG_GROUP_MEMBERS   = "group_members";
	public final static int MAX_GROUP_TEMPLETE = 9;
	
	@ViewById
    ImageView imgGroupSelection;
	@ViewById
	TextView txtFragGroupName;
	
	
    static int[] mImages = new int[MAX_GROUP_TEMPLETE];
	
	@AfterViews
	void calledAfterViewInjection() {
	    setTempleteResId();
	    
	    Bundle bundle = getArguments();
	    if (bundle == null) {
	        return;
	    }
	    
	    final String groupName = bundle.getString(ARG_GROUP_NAME);
	    final int groupTemplete = Integer.parseInt(bundle.getString(ARG_GROUP_TEMPLETE));
	    final String groupUuid = bundle.getString(ARG_GROUP_UUID);
	    String groupMember;
	    
	    AndLog.d("Group:" + groupName + ", templete:" + groupTemplete);
	    
	    txtFragGroupName.setText(groupName);
	    imgGroupSelection.setImageResource(mImages[groupTemplete]);
	    // 이미지를 클릭하면 해당 그룹의 칸반으로 이동
	    imgGroupSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), 
                            com.egoists.coco_nut.android.kanban.KanbanActivity_.class);
                    intent.putExtra(ARG_GROUP_UUID, groupUuid);
                    startActivity(intent);
                }
	    });
	}
	
	void setTempleteResId() {
	    mImages[0] = R.drawable.ic_group_templete_1;
        mImages[1] = R.drawable.ic_group_templete_1;
        mImages[2] = R.drawable.ic_group_templete_2;
        mImages[3] = R.drawable.ic_group_templete_1;
        mImages[4] = R.drawable.ic_group_templete_1;
        mImages[5] = R.drawable.ic_group_templete_1;
        mImages[6] = R.drawable.ic_group_templete_1;
        mImages[7] = R.drawable.ic_group_templete_1;
        mImages[8] = R.drawable.ic_group_templete_1;
	}
}
