package com.egoists.coco_nut.android.project.adapter;

import java.util.List;

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
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.query.BaasioQuery.ORDER_BY;

@EFragment(R.layout.fragment_group_info)
public class GroupSelectionFragment extends Fragment {
	public static final String ARG_GROUP_NAME      = "group_name";
	public static final String ARG_GROUP_TEMPLETE  = "group_templete";
	public static final String ARG_GROUP_UUID      = "group_uuid";
	public static final String ARG_GROUP_MEMBERS   = "group_members";
	public final static int MAX_GROUP_TEMPLETE = 13;
	
	@ViewById
    ImageView imgGroupSelection;
	@ViewById
	TextView txtFragGroupName;
	@ViewById
	TextView txtFragGroupMembers;
	
	
    static int[] mImages = new int[MAX_GROUP_TEMPLETE];
	
	@AfterViews
	void calledAfterViewInjection() {
	    setTempleteResId();
	    
	    Bundle bundle = getArguments();
	    if (bundle == null) {
	        return;
	    }
	    
	    final String groupName = bundle.getString(ARG_GROUP_NAME);
	    
	    // 실제 그룹 정보가 아닌 그룹 생성 정보를 표시한다.
//	    if (groupUuid.equals("01485242-33b9-11e3-85fc-06f4fe0000b5")) {
	    if (groupName.equals("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF")) {
	        txtFragGroupName.setText("그룹 생성");
	        imgGroupSelection.setImageResource(mImages[MAX_GROUP_TEMPLETE-1]);
	        txtFragGroupMembers.setText("그룹을 새로 생성합니다");
	        // 이미지를 클릭하면 해당 그룹의 칸반으로 이동
	        imgGroupSelection.setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                    Intent intent = new Intent(getActivity(), 
	                            com.egoists.coco_nut.android.project.GroupCreationActivity_.class);
	                    startActivity(intent);
	                }
	        });
	        return;
	    }
	    
	    final String groupUuid = bundle.getString(ARG_GROUP_UUID);
	    final int groupTemplete = Integer.parseInt(bundle.getString(ARG_GROUP_TEMPLETE));
	    
	    
	    AndLog.d("Group:" + groupName + ", templete:" + groupTemplete);
	    
	    // 그룹의 유저 추출
	    getMyFriendsByBaasio(groupUuid);
	    
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
	
	void getMyFriendsByBaasio(String groupUuid) {
        // 쿼리 전송
        BaasioQuery query = new BaasioQuery();
        query.setRawString("groups/" + groupUuid + "/users");
        query.setOrderBy(BaasioBaseEntity.PROPERTY_MODIFIED, ORDER_BY.DESCENDING);
        query.queryInBackground(new BaasioQueryCallback() { // 질의 요청

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list, BaasioQuery query, long timestamp) {
                AndLog.d("Succeed : get userlist");
                List<BaasioUser> users = BaasioBaseEntity.toType(entities, BaasioUser.class);
                
                showGroupMembers(users);
            }

            @Override
            public void onException(BaasioException e) {
                AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
            }
        });
    }
	
	@UiThread
	void showGroupMembers(List<BaasioUser> users) {
	    String groupMembers = "";
	    
	    for (BaasioUser user : users) {
	        groupMembers += user.getUsername() + ", ";
	    }
	    groupMembers = groupMembers.substring(0, groupMembers.lastIndexOf(", "));
	    txtFragGroupMembers.setText(groupMembers);
	}
	
	void setTempleteResId() {
	    mImages[0] = R.drawable.ic_group_templete_0;
        mImages[1] = R.drawable.ic_group_templete_1;
        mImages[2] = R.drawable.ic_group_templete_2;
        mImages[3] = R.drawable.ic_group_templete_3;
        mImages[4] = R.drawable.ic_group_templete_4;
        mImages[5] = R.drawable.ic_group_templete_5;
        mImages[6] = R.drawable.ic_group_templete_6;
        mImages[7] = R.drawable.ic_group_templete_7;
        mImages[8] = R.drawable.ic_group_templete_8;
        mImages[9] = R.drawable.ic_group_templete_9;
        mImages[10] = R.drawable.ic_group_templete_10;
        mImages[11] = R.drawable.ic_group_templete_11;
        mImages[12] = R.drawable.ic_group_templete_create;
	}
}
