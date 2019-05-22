package com.lovense.sdkdemo;

import android.app.Application;

import com.lovense.sdklibrary.LovenseSDK;

/**
 * Created  on 2019/4/29 17:06
 *
 * @author zyy
 */
public class MyApplication extends Application {

    public  static LovenseSDK lovenseSDK;

    @Override
    public void onCreate() {
        super.onCreate();
        lovenseSDK = LovenseSDK.getInstance();
        lovenseSDK.init(this);
        lovenseSDK.setDeveloperToken("YOU TOKEN");
    }
}
