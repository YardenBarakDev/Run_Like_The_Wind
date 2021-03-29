package com.YBDev.runlikethewind.util;

import android.app.Application;

import com.YBDev.runlikethewind.database.MyRoomDB;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyRoomDB.init(this);
        MySP.initHelper(this);
    }
}
