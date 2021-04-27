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
    int update(Word word);

    @Update
    void updateList(List<Word> wordList);

    @Delete
    void delete(Word word);

    @Query("DELETE FROM WORD")
    void deleteAll();

    @Query("SELECT * FROM WORD ORDER BY ID ASC")
    List<Word> getAll();

    @Query("SELECT * FROM WORD where unit_id=:unitId ORDER BY ID ASC limit :count")
    List<Word> getUnitList(long unitId, int count);

    @Query("SELECT * FROM WORD where unit_id=:unitId ORDER BY ID ASC")
    List<Word> getUnitList(long unitId);

    @Query("SELECT * FROM Word where new_word=1")
    List<Word> getNewWord();
}
