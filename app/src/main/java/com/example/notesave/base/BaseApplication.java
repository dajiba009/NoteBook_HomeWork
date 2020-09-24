package com.example.notesave.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;


public class BaseApplication extends Application {
    private static Context sContext;
    private static Handler sHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getBaseContext();
        sHandler = new Handler();
    }

    public static Context getContext(){
        return sContext;
    }

    public static Handler getHandler(){
        return sHandler;
    }
}
