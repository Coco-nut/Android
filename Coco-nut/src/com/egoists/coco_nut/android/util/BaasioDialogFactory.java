package com.egoists.coco_nut.android.util;

import com.egoists.coco_nut.android.R;
import com.kth.baasio.exception.BaasioException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class BaasioDialogFactory extends DialogFactory {

    /** One Button 퀵 다이얼로그 반환 **/
    public static AlertDialog.Builder createErrorDialog(Context con, int messageRes) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(R.string.title_error)
        .setMessage(messageRes)
        .setPositiveButton(R.string.title_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false);
        return dlg;
    }
    
    /** One Button 퀵 다이얼로그 반환 **/
    public static AlertDialog.Builder createErrorDialog(Context con, BaasioException e) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(R.string.title_error)
        .setMessage(e.getErrorCode() + "\n" + e.getErrorDescription())
        .setPositiveButton(R.string.title_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false);
        return dlg;
    }

}
