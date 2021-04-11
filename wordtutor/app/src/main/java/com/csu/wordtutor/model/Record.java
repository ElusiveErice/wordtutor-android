package com.csu.wordtutor.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Record {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "word_id")
    private long wordId;

    @ColumnInfo(name = "recite_time")
    private long reciteTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public long getReciteTime() {
        return reciteTime;
    }

    public void setReciteTime(long reciteTime) {
        this.reciteTime = reciteTime;
    }
}
