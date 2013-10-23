package com.egoists.coco_nut.android.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.egoists.coco_nut.android.R;

public class GroupSettingFragment extends PreferenceFragment implements OnPreferenceClickListener {
    public static final String EXTRA_CREATED_GROUP_UUID = "created_group_uuid";
    
    Activity mContext;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragment_group_setting);
        
        Preference profile = findPreference("invite");
        profile.setOnPreferenceClickListener(this);
    }
    
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("invite")) {
            // 회원 초대
            Intent i = new Intent(mContext, 
                    com.egoists.coco_nut.android.group.GroupInvitationActivity_.class);
            i.putExtra(EXTRA_CREATED_GROUP_UUID, ((GroupSettingActivity)mContext).mGroupUuid);
            startActivity(i);
        }        
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
}
