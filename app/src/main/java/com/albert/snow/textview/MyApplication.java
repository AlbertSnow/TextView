package com.albert.snow.textview;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.albert.snow.select.SelectTextManager;

import java.lang.reflect.Field;

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
        SelectTextManager.getInstance().init(this);
        initFont();
    }

    private void initFont() {
        final Typeface tf = Typeface.createFromFile("/system/fonts/Padauk.ttf");
        try {
            Field defaultField =  Typeface.class.getDeclaredField("SERIF");
            defaultField.setAccessible(true);
            defaultField.set(null, tf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
