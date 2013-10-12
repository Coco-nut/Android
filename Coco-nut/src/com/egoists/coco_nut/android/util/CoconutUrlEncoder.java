package com.egoists.coco_nut.android.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CoconutUrlEncoder {

    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // IGNORE
        }
        return null;
    }
}
