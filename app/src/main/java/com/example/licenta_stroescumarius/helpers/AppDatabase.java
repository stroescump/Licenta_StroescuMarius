package com.example.licenta_stroescumarius.helpers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.licenta_stroescumarius.interfaces.DaoInterface;
import com.example.licenta_stroescumarius.models.ItemIstoric;

@Database(entities = {ItemIstoric.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;
    private static final String DB_NAME = "itemIstoric.db";

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = createInstance(context);
        }
        return instance;
    }

    AppDatabase() {
    }

    private static AppDatabase createInstance(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }

    public abstract DaoInterface daoInterface();
}
