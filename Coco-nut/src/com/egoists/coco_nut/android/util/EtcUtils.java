/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.egoists.coco_nut.android.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.codehaus.jackson.JsonNode;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.egoists.coco_nut.android.cache.ImageCache;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.kth.baasio.entity.entity.BaasioEntity;

/**
 * An assortment of UI helpers.
 */
public class EtcUtils {
    public static final String GOOGLE_PLUS_PACKAGE_NAME = "com.google.android.apps.plus";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setActivatedCompat(View view, boolean activated) {
        if (hasHoneycomb()) {
            view.setActivated(activated);
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    public static ImageFetcher getImageFetcher(final FragmentActivity activity) {
        // The ImageFetcher takes care of loading remote images into our
        // ImageView
        ImageFetcher fetcher = new ImageFetcher(activity);
        fetcher.setImageCache(ImageCache.findOrCreateCache(activity, "imageFetcher"));
        return fetcher;
    }

    public static String getStringFromEntity(BaasioEntity entity, String key) {
        JsonNode node = entity.getProperties().get(key);
        if (node != null) {
            // String result = node.toString();
            // if(result != null && result.length() > 0) {
            // result = result.replace("<BR>", "\n");
            // result = result.replace("<br>", "\n");
            // return result.replace("\"", "").trim();
            // }
            String value = node.getTextValue();
            if (value != null && value.length() > 0) {
                return value.replace("<br>", "\n").replace("<BR>", "\n").trim();
            } else {
                return null;
            }
        }

        return null;
    }

    public static long getLongFromEntity(BaasioEntity entity, String key, long value) {
        JsonNode node = entity.getProperties().get(key);
        if (node != null) {
            // String result = node.toString();
            // if(result != null && result.length() > 0) {
            // return Long.valueOf(result.replace("\"", "").trim());
            // }

            return node.getLongValue();
        }

        return value;
    }

    public static int getIntFromEntity(BaasioEntity entity, String key, int value) {
        JsonNode node = entity.getProperties().get(key);
        if (node != null) {
            // String result = node.toString();
            // if(result != null && result.length() > 0) {
            // return Integer.valueOf(result.replace("\"", "").trim());
            // }

            return node.getIntValue();
        }

        return value;
    }

    public static String getDateString(long millis) {
        String time = null;

        if (Locale.getDefault().equals(Locale.KOREA) || Locale.getDefault().equals(Locale.KOREAN)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd a hh:mm",
                    Locale.getDefault());
            time = formatter.format(new Date(millis));
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd a hh:mm",
                    Locale.getDefault());
            time = formatter.format(new Date(millis));
        }

        return time;
    }

    public static String getSimpleDateString(long millis) {
        String time = null;

        if (Locale.getDefault().equals(Locale.KOREA) || Locale.getDefault().equals(Locale.KOREAN)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 M월 d일 a h시 mm분",
                    Locale.getDefault());
            time = formatter.format(new Date(millis));
        } else {
            time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT,
                    Locale.getDefault()).format(new Date(millis));
        }

        return time;
    }

}
