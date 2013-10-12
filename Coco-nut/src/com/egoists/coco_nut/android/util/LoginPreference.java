package com.egoists.coco_nut.android.util;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginPreference {
    private final String PREF_ID = "id";
    private final String PREF_PASSWD = "passwd";
    private final String PREF_NAME = "LOGIN";
    private final String PREF_UUID = "UUID";
    
    public String mId = null;
    public String mPasswd = null;
    public String mUuid = null;
    
    Context mContext;
    
    public LoginPreference(Context context) {
        mContext = context;
    }

    public void savePreference(String id, String passwd, String myUuid) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(PREF_ID, id);
        edit.putString(PREF_PASSWD, passwd);
        edit.putString(PREF_UUID, myUuid);
        edit.commit();
    }
    
    public void loadPreference() {
        // 로그인 정보 가져오기
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, 0);
        mId = pref.getString(PREF_ID, ""); // 이름
        mPasswd = pref.getString(PREF_PASSWD, ""); // 이름
        mUuid = pref.getString(PREF_UUID, "");  // UUID
    }
}
