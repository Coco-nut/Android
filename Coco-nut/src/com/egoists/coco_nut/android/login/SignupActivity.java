package com.egoists.coco_nut.android.login;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.MyAndroidInfo;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioSignUpCallback;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;

@NoTitle
@EActivity(R.layout.activity_signup)
public class SignupActivity extends Activity {
    @ViewById
    EditText edTxtSignUpId;
    @ViewById
    EditText edTxtSignUpName;
    @ViewById
    EditText edTxtSignUpPassword;
    @ViewById
    EditText edTxtSignUpConfirmPassword;
    @ViewById
    TextView txtSignUpMessage;
    
    private Context mContext;
        
    @AfterViews
    void initSignupForm() {
        mContext = this;
        edTxtSignUpId.setText(MyAndroidInfo.getMyIdFromEmail(this));
    }
    
    @Click(R.id.btnSignUp)
    void doSignUp() {
        String userId = edTxtSignUpId.getText().toString();
        String passwd = edTxtSignUpPassword.getText().toString();
        String userName = edTxtSignUpName.getText().toString();
        
        if (userId == null || userId.length() == 0
                || passwd == null || passwd.length() == 0
                || userName == null || userName.length() == 0) {
            AndLog.e("Empty Id or Password");
            BaasioDialogFactory.createErrorDialog(this, R.string.err_empty_login_form).show();
            return;
        }
        
        // Passwd 체크
        if (false == passwd.equals(edTxtSignUpConfirmPassword.getText().toString())) {
            AndLog.e("password is not equal.");
            BaasioDialogFactory.createErrorDialog(mContext, R.string.error_equaled_password).show();
            return;
        }
        
        doSignUpByBaasio(userId, userName, passwd);
    }
    
    // BAAS.IO SDK를 이용한 회원가입
    void doSignUpByBaasio(String userId, String name, String passwd) {
        String email = MyAndroidInfo.getMyEmail(this);
        
        BaasioUser.signUpInBackground(userId, name, email, passwd, new BaasioSignUpCallback() {
            @Override
            public void onException(BaasioException e) {
                AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                if (e.getErrorCode() == 913) {
                    // 이미 가입된 사용자
                    BaasioDialogFactory.createErrorDialog(mContext, R.string.error_userid_already_exist).show();
                    return;
                }
                // 기타 오류
                BaasioDialogFactory.createErrorDialog(mContext, e).show();
            }
            
            @Override
            public void onResponse(BaasioUser response) {
                if (response != null) {
                    AndLog.d("Succeed : Sign Up");
                    doUserUpdateByBaasio(response);
                }
            }
        });
    }
    
    // 사용자 정보 추가 (핸드폰 번호)
    void doUserUpdateByBaasio(final BaasioUser user) {
        String phoneNum = MyAndroidInfo.getMyPhoneNumber(this).replace("+82", "0");
        
//        final BaasioUser user = Baas.io().getSignedInUser();
        user.setProperty("phone", phoneNum);    //추가 정보
        user.updateInBackground(mContext,
            new BaasioCallback<BaasioUser>() {
                @Override
                public void onException(BaasioException e) {
                    BaasioDialogFactory.createErrorDialog(mContext, e).show();
                }

                @Override
                public void onResponse(BaasioUser response) {
                    if (response != null) {
                        String signupSucceedMessage = mContext.getResources().getString(R.string.signup_succeed);
                        signupSucceedMessage = signupSucceedMessage.replace("@userid", user.getName());
                        BaasioDialogFactory.createFinishButtonDialog(SignupActivity.this, R.string.title_succeed, signupSucceedMessage).show();
                    }
                }
            });
    }
}
