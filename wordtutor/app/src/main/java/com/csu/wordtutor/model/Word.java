package com.csu.wordtutor.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Word {

    @Ignore
    public static final long DEFAULT_UNIT_ID = -1; //默认的单元id，这个id的单词表示还没有划分单元

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "english")
    private String english;

    @ColumnInfo(name = "chinese")
    private String chinese;

    @ColumnInfo(name = "unit_id")
    private long unitId;


    public Word(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
        this.unitId = DEFAULT_UNIT_ID;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
}
