package com.YBDev.runlikethewind.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Run.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class MyRoomDB extends RoomDatabase {

    private static MyRoomDB instance;

    public abstract RunDao runDao();

    public static synchronized MyRoomDB getInstance(){ return instance;}

    public static MyRoomDB init(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MyRoomDB.class, "RunLikeTheWindDB").build();
        }
        return instance;
    }

}
