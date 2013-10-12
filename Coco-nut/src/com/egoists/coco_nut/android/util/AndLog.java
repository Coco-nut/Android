package com.egoists.coco_nut.android.util;

import android.util.Log;

public class AndLog {
	private static int mLevel 	= 3;
	
	public static final int OFF 	= -1;
	public static final int TRACE 	= 1;
	public static final int DEBUG 	= 2;
	public static final int INFO 	= 3;
	public static final int WARN 	= 4;
	public static final int ERROR 	= 5;
	public static final int FATAL 	= 6;
	
	public static void setLevel(int level){
        mLevel = level;
    }
	
	protected static boolean checkLevel(int level) {
		if ((mLevel<0) || ((level-mLevel) < 0))
			return false;
		else
			return true;
	}
	
	protected static String getCallingClass() {
		StringBuffer buf = new StringBuffer();
		try {
			StackTraceElement[] elements = Thread.currentThread().getStackTrace();
			buf.append("[").append(elements[4].getClassName()).append("]");
			buf.append(elements[4].getMethodName());
		} catch (Exception e) {}
		
		return buf.toString();
	}
	
	public static void t(String msg) {
		if (checkLevel(TRACE))
			t(getCallingClass(), msg);
	}
	
	public static void t(String tag, String msg){
		if (checkLevel(TRACE))
			Log.v(tag, msg);
	}
	
	public static void d(String msg) {
		if (checkLevel(DEBUG))
			d(getCallingClass(), msg);
	}
	
	public static void d(String tag, String msg) {
		if (checkLevel(DEBUG))
			Log.d(tag, msg);
	}
	
	public static void i(String msg) {
		i(getCallingClass(), msg);
	}
	
	public static void i(String tag, String msg) {
		if (checkLevel(INFO))
			Log.i(tag, msg);
	}
	
	public static void w(String msg) {
		if (checkLevel(INFO))
			w(getCallingClass(), msg);
	}
	
	public static void w(String tag, String msg) {
		if (checkLevel(WARN))
			Log.w(tag, msg);
	}
	
	public static void w(String tag, String msg, Throwable tr) {
		if (checkLevel(WARN))
			Log.w(tag, msg, tr);
	}
	
	public static void e(String msg){
		if (checkLevel(ERROR))
			e(getCallingClass(), msg);
	}
	
	public static void e(String tag, String msg) {
		if (checkLevel(ERROR))
			Log.e(tag, msg);
	}
	
	public static void e(String msg, Throwable tr) {
		if (checkLevel(ERROR))
			e(getCallingClass(), msg, tr);
	}
	
	public static void e(String tag, String msg, Throwable tr) {
		if (checkLevel(ERROR))
			Log.e(tag, msg, tr);
	}
	
	public static void f(String msg) {
		if (checkLevel(FATAL))
			f(getCallingClass(), msg);
	}
	
	public static void f(String tag, String msg) {
		if (checkLevel(FATAL))
			Log.e(tag, msg);
	}
	
	public static void f(String msg, Throwable tr) {
		if (checkLevel(FATAL))
			f(getCallingClass(), msg, tr);
	}
	
	public static void f(String tag, String msg, Throwable tr) {
		if (checkLevel(FATAL))
			Log.e(tag, msg, tr);
	}
	
}
