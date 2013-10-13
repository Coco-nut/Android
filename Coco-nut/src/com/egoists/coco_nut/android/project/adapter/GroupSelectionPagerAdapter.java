package com.egoists.coco_nut.android.project.adapter;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.egoists.coco_nut.android.util.AndLog;
import com.kth.baasio.entity.group.BaasioGroup;

public class GroupSelectionPagerAdapter extends FragmentPagerAdapter {
    List<BaasioGroup> mGroups;
    
    public GroupSelectionPagerAdapter(FragmentManager fm, List<BaasioGroup> groups) {
        super(fm);
        mGroups = groups;
    }

    @Override
    public Fragment getItem(int position) {
        BaasioGroup group = getGroup(position);
        
        Fragment fragment = new GroupSelectionFragment_();
        
        Bundle args = new Bundle();
        args.putString(GroupSelectionFragment_.ARG_GROUP_NAME, group.getTitle());
        
        // 마지막 item은 더미
        if (position + 1 != getCount()) {
            args.putString(GroupSelectionFragment_.ARG_GROUP_UUID, group.getUuid().toString());
            args.putString(GroupSelectionFragment_.ARG_GROUP_TEMPLETE, group.getProperty("templete").toString());
        }
        
        fragment.setArguments(args);
        return fragment;
    }

    public BaasioGroup getGroup(int position) {
        return mGroups.get(position);
    }

    @Override
    public int getCount() {
        return mGroups.size();
    }
    
    public void update(List<BaasioGroup> groups) {
        AndLog.d("UserList is updated");
        mGroups.clear();
        mGroups.addAll(groups);
        this.notifyDataSetChanged();
    }

}
