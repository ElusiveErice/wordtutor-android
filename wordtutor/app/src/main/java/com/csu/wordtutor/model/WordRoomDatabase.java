package com.csu.wordtutor.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class, Record.class}, version = 1, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {
    public abstract WordDao getWordDao();
    public abstract RecordDao getRecordDao();

    private static WordRoomDatabase INSTANCE;

    public static WordRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "wordtutor")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}