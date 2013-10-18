
package com.egoists.coco_nut.android;

import android.app.Application;
import android.os.AsyncTask;

import com.kth.baasio.Baas;
import com.kth.common.utils.LogUtils;

public class BaasioApplication extends Application {
    private static final String TAG = LogUtils.makeLogTag(BaasioApplication.class);

    AsyncTask mGCMRegisterTask;

    @Override
    public void onCreate() {
        super.onCreate();

        Baas.io().init(this, BaasioConfig.BAASIO_URL, BaasioConfig.BAASIO_ID,
                BaasioConfig.APPLICATION_ID);

//        mGCMRegisterTask = Baas.io().setGcmEnabled(this, null, new BaasioDeviceCallback() {
//
//            @Override
//            public void onException(BaasioException e) {
//                LogUtils.LOGE(TAG, "init onException:" + e.toString());
//            }
//
//            @Override
//            public void onResponse(BaasioDevice response) {
//                LogUtils.LOGD(TAG, "init onResponse:" + response.toString());
//            }
//        }, BaasioConfig.GCM_SENDER_ID);

    }

    @Override
    public void onTerminate() {
//        if (mGCMRegisterTask != null) {
//            mGCMRegisterTask.cancel(true);
//        }

        Baas.io().uninit(this);
        super.onTerminate();
    }
}
