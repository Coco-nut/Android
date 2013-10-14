package com.egoists.coco_nut.android.group;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.login.LoginActivity_;

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
          // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragment_kanban_setting);
        
        Preference logout = findPreference("logout");
        
        logout.setOnPreferenceClickListener(new OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference arg0){
                    SettingActivity.LoginPref.savePreference("", "");
                    Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent logout = new Intent(getActivity().getApplication(), com.egoists.coco_nut.android.login.LoginActivity_.class);
                    logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logout);
                    return false;
                }
        });
    }

}
