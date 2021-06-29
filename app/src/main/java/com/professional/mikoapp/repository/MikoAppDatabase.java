package com.professional.mikoapp.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.professional.mikoapp.utilities.Constants;

@Database(entities = {Miko.class}, version = 1)
public abstract class MikoAppDatabase extends RoomDatabase {

    private static MikoAppDatabase mikoAppDatabaseInstance;

    public abstract MikoDao mikoDao();

    public static synchronized MikoAppDatabase getInstance(Context context) {
        if (mikoAppDatabaseInstance == null) {
            mikoAppDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), MikoAppDatabase.class, Constants.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return mikoAppDatabaseInstance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
