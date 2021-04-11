package com.csu.wordtutor.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordDao {

    @Query("SELECT * FROM RECORD ORDER BY ID DESC")
    List<Record> getAll();

    @Insert
    void insertRecord(Record record);
}
