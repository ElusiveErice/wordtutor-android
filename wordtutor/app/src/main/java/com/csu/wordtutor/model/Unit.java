package com.csu.wordtutor.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Unit {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "recite_times")
    private long reciteTimes;


    public Unit() {
        this.reciteTimes = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReciteTimes() {
        return reciteTimes;
    }

    public void setReciteTimes(long reciteTimes) {
        this.reciteTimes = reciteTimes;
    }

}
