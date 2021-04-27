package com.csu.wordtutor.model;

import android.content.Context;
import android.os.AsyncTask;

import com.csu.wordtutor.activities.LearnActivity;

import java.util.List;

public class InsertUnitTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "InsertUnitTask";

    private final WordDao mWordDao;
    private final UnitDao mUnitDao;
    private final List<Word> mWordList;

    public InsertUnitTask(WordDao wordDao, UnitDao unitDao, List<Word> wordList) {
        mWordDao = wordDao;
        mUnitDao = unitDao;
        mWordList = wordList;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Unit unit = new Unit();
        long rowId = mUnitDao.insert(unit);
        unit = mUnitDao.getByRowId(rowId);
        for (Word word : mWordList) {
            word.setUnitId(unit.getId());
        }
        mWordDao.updateList(mWordList);
        return null;
    }

}
