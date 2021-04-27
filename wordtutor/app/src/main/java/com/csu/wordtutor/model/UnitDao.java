package com.csu.wordtutor.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UnitDao {

    @Insert
    long insert(Unit unit);

    @Update
    void update(Unit unit);

    @Delete
    void delete(Unit unit);

    @Query("Select * from unit")
    List<Unit> getAll();

    @Query("Select * from unit where id=:id")
    Unit getById(String id);

    @Query("Select * from unit where rowId=:rowId")
    Unit getByRowId(long rowId);
}
