package com.egoists.coco_nut.android.util;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Patterns;

public class MyAndroidInfo {

    public static String getMyEmail(Activity activity) {
        String myEmailAddress = null;
        
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(activity.getApplicationContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                myEmailAddress = account.name;
                break;
            }
        }
        return myEmailAddress;
    }
    
    public static String getMyIdFromEmail(Activity activity) {
        String email = getMyEmail(activity);
        // 사용자 메일 주소에서 @을 뺀 ID를 default로 셋팅
        String words[] = email.split("@");
        return words[0];
    }
    
    public static String getMyPhoneNumber(Activity activity) {
        TelephonyManager mgr = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
        return mgr.getLine1Number();
    }
}
