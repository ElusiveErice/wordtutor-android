package com.csu.wordtutor.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "english")
    private String english;
    @ColumnInfo(name = "chinese")
    private String chinese;


    public Word(String english, String chinese){
        this.english = english;
        this.chinese = chinese;
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
