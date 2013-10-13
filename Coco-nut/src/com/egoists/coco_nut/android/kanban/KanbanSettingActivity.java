package com.egoists.coco_nut.android.kanban;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.LoginPreference;


public class KanbanSettingActivity extends Activity {
	
    public static LoginPreference LoginPref;
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.only_back_coconut, menu);
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
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  
	  getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new KanbanSettingFragment()).commit();
	  
	  
	 }

	}
