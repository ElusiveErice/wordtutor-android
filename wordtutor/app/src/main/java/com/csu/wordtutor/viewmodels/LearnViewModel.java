package com.csu.wordtutor.viewmodels;

import android.content.Context;
import android.opengl.Visibility;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.csu.wordtutor.R;
import com.csu.wordtutor.activities.LearnActivity;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.utils.SharedHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;

import java.util.List;

public class LearnViewModel extends BaseObservable {

    public static final int INIT_INDEX = 0;

    private final List<Word> mWordList;
    private int mAnswerVisibility;
    private int mIndex;

    private final Context mContext;

    public LearnViewModel(Context context, List<Word> wordList) {
        mContext = context;
        mWordList = wordList;
        mAnswerVisibility = View.INVISIBLE;
        mIndex = INIT_INDEX;
    }

    public void setAnswerVisibility(int answerVisibility) {
        this.mAnswerVisibility = answerVisibility;
        notifyChange();
    }

    @Bindable
    public long getUnitId() {
        return mWordList.get(mIndex).getUnitId();
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
    public int getIndex() {
        return mIndex;
    }

    @Bindable
    public int getSize() {
        return mWordList.size();
    }

    @Bindable
    public boolean isNewWord() {
        return mWordList.get(mIndex).isNewWord();
    }

    public String getUnitTitle() {
        return "Unit ";
    }

    public void setIndex(int index) {
        mIndex = index;
        notifyChange();
        new SharedHelper(mContext).saveIndex(index);
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
                    .addAction("还要学一会", (dialog, index) -> dialog.dismiss())
                    .addAction("今天学完了", (dialog, index) -> {
                        dialog.dismiss();
                        if (mContext instanceof LearnActivity) {
                            ((LearnActivity) mContext).handleFinishLearn();
                        }
                    })
                    .show();
        }
    }

    public void onNewWordClick() {
        Word word = mWordList.get(mIndex);
        word.setNewWord(!word.isNewWord());
        notifyChange();
        if (mContext instanceof LearnActivity) {
            ((LearnActivity) mContext).handleAddNewWord(word);
        }
    }
}
