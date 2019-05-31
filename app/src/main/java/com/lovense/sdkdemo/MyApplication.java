package com.lovense.sdkdemo;

import android.app.Application;

import com.lovense.sdklibrary.Lovense;

/**
 *  Created by Lovense on 2019/5/14
 *
 *  Copyright © 2019 Hytto. All rights reserved.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       Lovense.getInstance(this).setDeveloperToken("You Token");
    }
}
