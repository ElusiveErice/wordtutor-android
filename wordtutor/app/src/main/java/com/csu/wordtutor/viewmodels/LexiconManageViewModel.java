package com.csu.wordtutor.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.utils.FileUtils;

import java.util.List;


public class LexiconManageViewModel extends BaseObservable {

    private static final String TAG = "LexiconViewModel";
    private List<Word> mWordList;

    public LexiconManageViewModel(List<Word> wordList) {
        mWordList = wordList;
    }

    public void setWordList(List<Word> wordList){
        mWordList = wordList;
        notifyChange();
    }

    @Bindable
    public String getWordSum() {
        return String.valueOf(mWordList.size());
    }

    @Bindable
    public String getWordLearned() {
        return String.valueOf(0);
    }

}
