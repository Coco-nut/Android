package com.egoists.coco_nut.android.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;

@NoTitle
@EActivity(R.layout.fragment_project_info)
public class LoginActivity extends Activity {

	boolean hasUserToken() {
		// 사용자 로그인 되어 있는지의 여부
		return true;
	}
	
	@AfterViews
	void doFinish() {
		if (hasUserToken()) {
			AndLog.i("User token is detected.");
			Handler hd = new Handler();
	        hd.postDelayed(new splashhandler() , 1000); // 1초 후에 hd Handler 실행
		}
		
		// 로그인 창을 그린다.
		
		// 로그인 작업을 수행
	}
	     
    private class splashhandler implements Runnable {
        public void run() {
        	// 로딩이 끝난후 이동할 Activity
            startActivity(new Intent(getApplication(), com.egoists.coco_nut.android.project.ProjectSelectionActivity_.class)); 
            LoginActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }

}
