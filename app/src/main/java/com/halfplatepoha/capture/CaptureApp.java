package com.halfplatepoha.capture;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by surjo on 03/03/17.
 */

public class CaptureApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

    }
}
