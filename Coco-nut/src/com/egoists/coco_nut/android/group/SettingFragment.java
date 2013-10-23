package com.egoists.coco_nut.android.group;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.DialogFactory;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;

public class SettingFragment extends PreferenceFragment implements OnPreferenceClickListener {
    Activity mContext;
    private ProgressDialog mDialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragment_kanban_setting);
        
        Preference profile = findPreference("profile");
        profile.setOnPreferenceClickListener(this);
        
        Preference password = findPreference("password");
        password.setOnPreferenceClickListener(this);
        Preference version = findPreference("version");
        version.setOnPreferenceClickListener(this);
        
        Preference logout = findPreference("logout");
        logout.setOnPreferenceClickListener(this);
        
    }
    
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("profile")) {
            // 프로필 설정
            Intent i = new Intent(getActivity().getApplication(), com.egoists.coco_nut.android.login.UserSettingActivity_.class);
            startActivity(i);
        } else if (preference.getKey().equals("password")) {
            // 비밀번호 변경
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_change_passwd,(ViewGroup) mContext.findViewById(R.id.layout_change_passwd));

            // 커스텀 다이얼로그
            AlertDialog.Builder aDialog = new AlertDialog.Builder(mContext);
            aDialog.setTitle("비밀번호 변경");
            aDialog.setView(layout);
            aDialog.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // 비번 입력
                    final String passwdOld = ((EditText)layout.findViewById(R.id.edTxtChangePasswordOld)).getText().toString();
                    final String passwdNew = ((EditText)layout.findViewById(R.id.edTxtChangePasswordNew)).getText().toString();
                    final String passwdConfirm = ((EditText)layout.findViewById(R.id.edTxtChangePasswordConfirm)).getText().toString();
                    AndLog.d(passwdOld + ", " + passwdNew + ", " + passwdConfirm);
                    
                    // 입력 폼 체크
                    if (passwdOld == null || passwdOld.length() <= 0 ||
                            passwdNew == null || passwdNew.length() <= 0 ||
                            passwdConfirm == null || passwdConfirm.length() <= 0 ) {
                        BaasioDialogFactory.createErrorDialog(mContext, R.string.error_empty_password).show();
                    }
                    // 새 비밀번호 확인
                    if (passwdNew.equals(passwdConfirm) == false) {
                        BaasioDialogFactory.createErrorDialog(mContext, R.string.error_not_matched_password).show();
                    }
                    // 입력 길이 체크
                    if (passwdNew.length() < 8) {
                        BaasioDialogFactory.createErrorDialog(mContext, R.string.error_not_proper_length_password).show();
                    }
                    
                    // 패쓰워드 변경
                    doChangePasswdByBaasio(passwdOld, passwdNew);
                }
            });
            aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog ad = aDialog.create();
            ad.show();
        } else if (preference.getKey().equals("version")) {
            // 버전 정보
            DialogFactory
                .createNoButton(mContext,R.string.team_name, "디자이너 : 2배 빠른 옥나라\n개발자 : 네모홀릭 이병준\n개발자 : 기가마킨 최영근\n\nteam.egoists@gmail.com")
                .setPositiveButton(
                        R.string.title_confirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                }
                            })
//                .setCancelable(false)
                .show();
            
        } else if(preference.getKey().equals("logout")) {
            // 로그 아웃
            DialogFactory
                .createNoButton(mContext, R.string.setting_logout, R.string.setting_logout_message)
                .setPositiveButton(
                        R.string.title_confirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 로그 아웃
                                SettingActivity.LoginPref.savePreference("", "");
                                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent logout = new Intent(getActivity().getApplication(), com.egoists.coco_nut.android.login.LoginActivity_.class);
                                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(logout);
                            }
                        })
                .setNegativeButton(
                        R.string.title_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .show();
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
    
    void doChangePasswdByBaasio(String passwdOld, String passwdNew) {
        mDialog = ProgressDialog.show(mContext, "", "비밀번호 변경 중...", true);
        BaasioUser.changePasswordInBackground(
                passwdOld, passwdNew,
                new BaasioCallback<Boolean>() {

                    @Override
                    public void onException(BaasioException e) {
                        // 실패
                        mDialog.dismiss();
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }

                    @Override
                    public void onResponse(Boolean response) {
                        mDialog.dismiss();
                        if (response == true) {
                            //성공
                            BaasioDialogFactory.createOneButtonDialog(mContext, R.string.change_passwd, R.string.change_passwd_message).show();
                        }
                    }
                });
    }
}
