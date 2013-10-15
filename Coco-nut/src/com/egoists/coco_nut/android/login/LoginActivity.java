package com.egoists.coco_nut.android.login;

import org.codehaus.jackson.JsonNode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egoists.coco_nut.android.BaasioConfig;
import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.Person;
import com.egoists.coco_nut.android.group.SettingActivity;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.LoginPreference;
import com.egoists.coco_nut.android.util.MyAndroidInfo;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.AnimationRes;
import com.kth.baasio.callback.BaasioCallback;
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
	@AnimationRes
    Animation fadeIn;
	
	private String mId;
	private String mPasswd;
	private ProgressDialog mDialog;
	
	private Context mContext;
	
	@AfterViews
	void initLoginForm() {
	    AndLog.setLevel(AndLog.TRACE);
	    
	    mContext = this;
	    SettingActivity.LoginPref = new LoginPreference(mContext);
	    waitAndPreLogin();
	}
	
	@UiThread(delay=500)
	void waitAndPreLogin() {
	    // 그냥 0.5초 대기
	    doAutoLogin();
	}

	
	@Background
	void doAutoLogin() {
	    // 로그인 정보 가져오기
	    SettingActivity.LoginPref.loadPreference();
        String id = SettingActivity.LoginPref.mId;
        String passwd = SettingActivity.LoginPref.mPasswd;

        doAutoLogInByBaasio(id, passwd);
	}
	
	@Background
    void doAutoLogInByBaasio(String userId, String passwd) {
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
                    if (e.getErrorCode() == 210) {
                        // 회원 가입이 필요
                        BaasioDialogFactory.createErrorDialog(mContext, R.string.error_require_signup).show();
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
                    SettingActivity.LoginPref.savePreference(mId, mPasswd);    // 로그인 정보 저장
                	
                	// 사용자 폰번호가 없으면 업데이트한다
                	JsonNode node = response.getProperty(Person.ENTITY_NAME_PHONE);
                	if (node == null || node.toString().length() == 0) {
                	    doUserUpdateByBaasio(response);
                	    return;
                	}
                	moveToProjectSelectionActivity();
                }
            }
        });
    }
    
 // 사용자 정보 추가 (핸드폰 번호)
    void doUserUpdateByBaasio(final BaasioUser user) {
        AndLog.d("Update phone number");
        String phoneNum = MyAndroidInfo.getMyPhoneNumber(this).replace("+82", "0");
        
        mDialog = ProgressDialog.show(LoginActivity.this, "", "전화번호 업데이트 중", true);
        String picture = "https://blob.baas.io/" + BaasioConfig.BAASIO_ID + "/" + BaasioConfig.APPLICATION_ID + "/files/4fd1a584-35bc-11e3-85fc-06f4fe0000b5";
        user.setProperty(Person.ENTITY_NAME_PICTURE, picture);    //추가 정보
        user.setProperty("phone", phoneNum);    //추가 정보
        user.updateInBackground(mContext,
            new BaasioCallback<BaasioUser>() {
                @Override
                public void onException(BaasioException e) {
                    mDialog.dismiss();
                    BaasioDialogFactory.createErrorDialog(mContext, e).show();
                }

                @Override
                public void onResponse(BaasioUser response) {
                    if (response != null) {
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
	    Intent intent = new Intent(getApplication(), 
                com.egoists.coco_nut.android.group.GroupSelectionActivity_.class);
        startActivity(intent); 
        LoginActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
	}
}
