package com.csu.wordtutor.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insertWord(Word word);

    @Insert
    void insertWordList(List<Word> wordList);

    @Update
    void update(Word word);

    @Delete
    void delete(Word word);

    @Query("DELETE FROM WORD")
    void deleteAll();

    @Query("SELECT * FROM WORD ORDER BY ID ASC")
    List<Word> getAll();
}
