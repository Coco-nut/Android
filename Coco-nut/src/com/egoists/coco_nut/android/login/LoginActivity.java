package com.egoists.coco_nut.android.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.AnimationRes;

@NoTitle
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {
	@ViewById
	ImageView imgMainLogo;
	
	@ViewById
	LinearLayout laySignUpForm;
	
	@ViewById
	TextView txtSignUpPhoneNum;
	
	@AnimationRes
	Animation fadeIn;
	
	@AfterViews
	void signUp() {
		holdMainLogo();
	}
	
	@UiThread(delay=1500)
	void holdMainLogo() {
		// 그냥 1.5초 대기
		checkUserToken();
	}
	
	@Background
	void checkUserToken() {
		if (hasUserToken()) {
			AndLog.i("User token is detected.");
			moveToProjectSelectionActivity();
		}
		displayLoginLayout();
	}
	
	@UiThread
	void displayLoginLayout() {
		// 메인 로고 위로 이동
		Animation moveUp = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, -0.25f);
		moveUp.setFillAfter(true);
		moveUp.setDuration(1000);
		imgMainLogo.startAnimation(moveUp);
		
		// 로그인 폼 보이기
		laySignUpForm.setVisibility(View.VISIBLE);
		txtSignUpPhoneNum.setText(getMyPhoneNumber());
	}
	
	boolean hasUserToken() {
		// TODO 사용자 로그인 되어 있는지의 여부
		return false;
	}
	
	String getMyPhoneNumber() {
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		return mgr.getLine1Number();
	}
	
	@Click(R.id.btnSignUp)
	void doSignUp() {
		// TODO 계정 등록으로 변경할 것
		moveToProjectSelectionActivity();
	}

	void moveToProjectSelectionActivity() {
		// 로딩이 끝난후 이동할 Activity
        startActivity(new Intent(getApplication(), com.egoists.coco_nut.android.project.ProjectSelectionActivity_.class)); 
        LoginActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
	}
}
