package com.egoists.coco_nut.android.kanban;

import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.LoginPreference;
import com.egoists.coco_nut.android.util.UniqueString;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;


public class KanbanSettingFragment extends PreferenceFragment {


    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  // TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		  
		  // Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.fragment_kanban_setting);
		
		Preference logout = findPreference("logout");
		
		logout.setOnPreferenceClickListener(new OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference arg0){
					KanbanSettingActivity.LoginPref.savePreference("", "", "");
				    Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
				    Intent logout = new Intent(getActivity().getApplication(), com.egoists.coco_nut.android.login.LoginActivity_.class);
				    logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				    startActivity(logout);
				    return false;
				}
		});
		
	
	}

}
