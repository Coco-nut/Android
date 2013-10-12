package com.egoists.coco_nut.android.util;

import java.util.Random;

public class UniqueString {

    public UniqueString() {
        // TODO Auto-generated constructor stub
    }
    public static String generate() {
        byte[] b = new byte[20];
        new Random().nextBytes(b);
        return bytesToHex(b);
    }
    
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
