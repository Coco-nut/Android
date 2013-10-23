package com.egoists.coco_nut.android.group;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.LoginPreference;

public class GroupSettingActivity extends Activity {
    public String mGroupUuid;
    
    public static LoginPreference LoginPref;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_back_coconut, menu);
        getActionBar().setLogo(R.drawable.ic_coconut_white_logo);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent i = getIntent();
        mGroupUuid = i.getStringExtra("group_uuid");
        
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("설정");
        
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                       new GroupSettingFragment()).commit();
    }
}
