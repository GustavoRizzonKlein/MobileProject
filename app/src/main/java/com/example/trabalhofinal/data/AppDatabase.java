package com.example.trabalhofinal.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Medication.class, History.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract AppDao appDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "medcare_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Simplification for this project, ideally use background threads
                    .build();
        }
        return instance;
    }
}