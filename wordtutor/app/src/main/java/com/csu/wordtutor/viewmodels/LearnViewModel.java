package com.csu.wordtutor.viewmodels;

import android.content.Context;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.csu.wordtutor.model.Word;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

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

    public void onAnswerClick() {
        setAnswerVisibility(View.VISIBLE);
    }

    public void onNextClick() {
        if (mIndex < mWordList.size() - 1) {
            mIndex++;
            setAnswerVisibility(View.INVISIBLE);
        } else {
            final QMUITipDialog tipDialog = new QMUITipDialog.Builder(mContext)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                    .setTipWord("已经是最后一个了")
                    .create();
            tipDialog.show();
        }
    }
}
