package com.csu.wordtutor.viewmodels;

import android.content.Context;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.csu.wordtutor.model.Word;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.List;

public class LearnViewModel extends BaseObservable {

    private final List<Word> mWordList;
    private int mAnswerVisibility;
    private int mIndex;

    private final Context mContext;

    public LearnViewModel(Context context, List<Word> wordList) {
        mContext = context;
        mWordList = wordList;
        mAnswerVisibility = View.INVISIBLE;
        mIndex = 0;
    }

    public void setAnswerVisibility(int answerVisibility) {
        this.mAnswerVisibility = answerVisibility;
        notifyChange();
    }

    @Bindable
    public String getEnglish() {
        return mWordList.get(mIndex).getEnglish();
    }

    @Bindable
    public String getChinese() {
        return mWordList.get(mIndex).getChinese();
    }

    @Bindable
    public int getAnswerVisibility() {
        return mAnswerVisibility;
    }

    @Bindable
    public String getIndex() {
        return String.valueOf(mIndex);
    }

    @Bindable
    public String getSize() {
        return String.valueOf(mWordList.size());
    }

    public void setIndex(int index) {
        mIndex = index;
        notifyChange();
    }

    public void onAnswerClick() {
        setAnswerVisibility(View.VISIBLE);
    }

    public void onLastClick() {
        if (mIndex > 0) {
            setIndex(mIndex - 1);
            setAnswerVisibility(View.INVISIBLE);
        } else {
            new QMUIDialog.MessageDialogBuilder(mContext)
                    .setTitle("注意")
                    .setMessage("当前是第一个")
                    .addAction("确定", (dialog, index) -> dialog.dismiss())
                    .show();
        }
    }

    public void onNextClick() {
        if (mIndex < mWordList.size() - 1) {
            setIndex(mIndex + 1);
            setAnswerVisibility(View.INVISIBLE);
        } else {
            new QMUIDialog.MessageDialogBuilder(mContext)
                    .setTitle("注意")
                    .setMessage("已经是最后一个了")
                    .addAction("确定", (dialog, index) -> dialog.dismiss())
                    .show();
        }
    }
}
