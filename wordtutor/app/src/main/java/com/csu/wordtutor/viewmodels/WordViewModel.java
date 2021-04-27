package com.csu.wordtutor.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.csu.wordtutor.model.Word;

public class WordViewModel extends BaseObservable {

    private final Word word;
    private final int index;

    public WordViewModel(Word word, int index) {
        this.word = word;
        this.index = index;
    }

    @Bindable
    public String getEnglish() {
        return word.getEnglish();
    }

    @Bindable
    public String getChinese() {
        return word.getChinese();
    }

    @Bindable
    public String getIndex() {
        return String.valueOf(index);
    }

    @Bindable
    public String getUnitId() {
        return String.valueOf(word.getUnitId());
    }
}
