package com.egoists.coco_nut.android.util;

import com.egoists.coco_nut.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogFactory {
    /** One Button 퀵 다이얼로그 반환 **/
    public static AlertDialog.Builder createOneButtonDialog(Context con, int titleRes, int messageRes) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(titleRes)
        .setMessage(messageRes)
        .setPositiveButton(R.string.title_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false);
        return dlg;
    }
    
    /** One Button 퀵 다이얼로그 반환 **/
    public static AlertDialog.Builder createOneButtonDialog(Context con, int titleRes, String messageStr) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(titleRes)
        .setMessage(messageStr)
        .setPositiveButton(R.string.title_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false);
        return dlg;
    }
      
    
    /** One Button 퀵 다이얼로그 반환 **/
    public static AlertDialog.Builder createFinishButtonDialog(final Activity con,  int titleRes, int messageRes) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(titleRes)
        .setMessage(messageRes)
        .setPositiveButton(R.string.title_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                con.finish();
            }
        }).setCancelable(false);
        return dlg;
    }
    
    /** One Button 퀵 다이얼로그 반환 **/
    public static AlertDialog.Builder createFinishButtonDialog(final Activity con,  int titleRes, String messageStr) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(titleRes)
        .setMessage(messageStr)
        .setPositiveButton(R.string.title_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                con.finish();
            }
        }).setCancelable(false);
        return dlg;
    }
    
    /** 버튼 없는 퀵 다이얼로그 반환 **/
    public static AlertDialog.Builder createNoButton(Context con, int titleRes, int messageRes) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(titleRes)
        .setMessage(messageRes)
        .setCancelable(false);
        return dlg;
    }
    
    
    /** 버튼 없는 퀵 다이얼로그 반환 **/
    public static AlertDialog.Builder createNoButton(Context con, int titleRes, String message) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(titleRes)
        .setMessage(message)
        .setCancelable(false);
        return dlg;
    }
    
    
    /** 종료 퀵 팝업 **/
    public static AlertDialog.Builder createFinishButton(final Activity con, int titleRes, int messageRes, int buttonRes) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(titleRes)
        .setMessage(messageRes)
        .setPositiveButton(buttonRes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                con.finish();
            }
        }).setCancelable(false);
        return dlg;
    }
    
    
    /** 종료 퀵 팝업 **/
    public static AlertDialog.Builder createFinishButton(final Activity con, int titleRes, String message, int buttonRes) {
        
        AlertDialog.Builder dlg = new AlertDialog.Builder(con);
        dlg.setTitle(titleRes)
        .setMessage(message)
        .setPositiveButton(buttonRes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                con.finish();
            }
        }).setCancelable(false);
        return dlg;
    }
    
}
