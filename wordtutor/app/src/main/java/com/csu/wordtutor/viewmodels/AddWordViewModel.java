package com.csu.wordtutor.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class AddWordViewModel extends BaseObservable {

    private String chinese;
    private String english;

    @Bindable
    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
        notifyChange();
    }

    @Bindable
    public String getEnglish() {
        return english;

    }

    public void setEnglish(String english) {
        this.english = english;
        notifyChange();
    }

}
