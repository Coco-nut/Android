package com.egoists.coco_nut.android.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.MyAndroidInfo;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.AnimationRes;
import com.kth.baasio.callback.BaasioSignInCallback;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;

@NoTitle
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {
	@ViewById
	LinearLayout layLoginLogo;
	@ViewById
	LinearLayout layLoginForm;
	@ViewById
	TextView txtLoginNotice;
	@ViewById
	EditText edTxtLoginId;
	@ViewById
	EditText edTxtLoginPassword;
	
	String mId;
	String mPasswd;
	
	@AnimationRes
	Animation fadeIn;
	
	private final String PREF_ID = "id";
	private final String PREF_PASSWD = "passwd";
	private final String PREF_NAME = "LOGIN";
	
	private Context mContext;
	
	@AfterViews
	void initLoginForm() {
	    AndLog.setLevel(AndLog.TRACE);
	    
	    mContext = this;
	    doPreLogin();
	}
	
	void saveLoginInfo(String id, String passwd) {
	    super.onPause();
	    SharedPreferences pref = getSharedPreferences(PREF_NAME, 0);
	    SharedPreferences.Editor edit = pref.edit();
	    edit.putString(PREF_ID, id);
	    edit.putString(PREF_PASSWD, passwd);
	    edit.commit();
	}
	
	@Background
	void doPreLogin() {
	    // 로그인 정보 가져오기
        SharedPreferences pref = getSharedPreferences(PREF_NAME, 0);
        String id = pref.getString(PREF_ID, ""); // 이름
        String passwd = pref.getString(PREF_PASSWD, ""); // 이름
        // TODO
//        String id = "";
//        String passwd = "";
        doPreLogInByBaasio(id, passwd);
	}
	
	@UiThread
	void displayLoginLayout() {
		// 메인 로고 위로 이동
	    txtLoginNotice.setVisibility(View.VISIBLE);
	    
		Animation moveUp = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, -0.25f);
		moveUp.setFillAfter(true);
		moveUp.setDuration(1000);
		layLoginLogo.startAnimation(moveUp);
		
		// 로그인 폼 보이기
		showLoginForm();
	}
	
	@UiThread(delay=1000)
	void showLoginForm() {
		fadeIn.setFillAfter(true);
		fadeIn.setDuration(2000);
		layLoginForm.startAnimation(fadeIn);
		edTxtLoginId.setText(MyAndroidInfo.getMyIdFromEmail(this));
	}
	
	@Click(R.id.txtSignup)
	void doSignUp() {
		moveToSignupActivity();
	}
	
	@Click(R.id.btnLogin) 
	void doLogIn() {
	    // 입력폼 null check
	    String loginId = edTxtLoginId.getText().toString();
	    String loginPw = edTxtLoginPassword.getText().toString();
	    if (loginId == null || loginPw == null || loginId.length() == 0 || loginPw.length() == 0) {
	        AndLog.e("Empty Id or Password");
	        BaasioDialogFactory.createErrorDialog(this, R.string.err_empty_login_form).show();
	        return;
	    }
	    
	    doLogInByBaasio(loginId, loginPw);
	}
	
	@Background
	void doPreLogInByBaasio(String userId, String passwd) {
	    BaasioUser user = null;
	    if (userId.length() != 0 || passwd.length() != 0) {
	        try {
	            user = BaasioUser.signIn(mContext, userId, passwd);
	        } catch (BaasioException e) {
	            AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
	            // DO NOTHING
	        }
	    }
	    
	    if (user == null) {
	        // 수동 로그인 이동
	        displayLoginLayout();
	    } else {
	        // 로그인 성공
            moveToProjectSelectionActivity();
	    }
	    
	}

	ProgressDialog mDialog;
	
	// BAAS.IO SDK를 이용한 로그인
    void doLogInByBaasio(String userId, String passwd) {
        mId = userId;
        mPasswd = passwd;
        mDialog = ProgressDialog.show(LoginActivity.this, "", "로그인 중", true);
        
        BaasioUser.signInInBackground(mContext, userId, passwd, new BaasioSignInCallback() {
            
            @Override
            public void onException(BaasioException e) {
                mDialog.dismiss();
                AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                if (e.getStatusCode() != null) {
                    if (e.getErrorCode() == 201) {
                        // username(ID) 또는 비밀번호 오류
                        BaasioDialogFactory.createErrorDialog(mContext, R.string.error_login_fail).show();
                        return;
                    }
                }
                // 기타 오류
                BaasioDialogFactory.createErrorDialog(mContext, e).show();
            }

            @Override
            public void onResponse(BaasioUser response) {
                mDialog.dismiss();
                if (response != null) {
                    // 로그인 성공
                    saveLoginInfo(mId, mPasswd);    // 로그인 정보 저장
                    moveToProjectSelectionActivity();
                }
            }
        });
    }
    
	void moveToSignupActivity() {
	    // 회원 가입 activity로 이동
        startActivity(new Intent(getApplication(), 
                com.egoists.coco_nut.android.login.SignupActivity_.class));
    }
	
	void moveToProjectSelectionActivity() {
		// 로딩이 끝난후 이동할 Activity
        startActivity(new Intent(getApplication(), 
        		com.egoists.coco_nut.android.project.ProjectSelectionActivity_.class)); 
        LoginActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
	}
}
