package com.kongzue.enjoylife;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kongzue.dialog.v2.DialogSettings;

import static com.kongzue.dialog.v2.DialogSettings.TYPE_IOS;

public class EnjoyLifeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DialogSettings.type = TYPE_IOS;
        Fresco.initialize(this);
    }
}
