package com.albert.snow.textview;

import android.app.Application;
import android.content.Context;

import com.jaeger.library.ClickableTextManager;

public class MyApplication extends Application {
    private static Application app;

    public static Application getInstance() {
        return app;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        app = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ClickableTextManager.getInstance().init(this);
    }

}
