package com.csu.wordtutor.viewmodels;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.mysuper.ObserverNext;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UpdateWordViewModel extends BaseObservable {

    private Word word;
    private Context context;
    private WordDao wordDao;

    public UpdateWordViewModel(Context context, Word word, WordDao wordDao) {
        this.context = context;
        this.word = word;
        this.wordDao = wordDao;
    }

    @Bindable
    public String getEnglish() {
        return word.getEnglish();
    }

    @Bindable
    public String getChinese() {
        return word.getChinese();
    }

    public void setEnglish(String english) {
        word.setEnglish(english);
    }

    public void setChinese(String chinese) {
        word.setChinese(chinese);
    }

    @Bindable
    public boolean isNewWord() {
        return word.isNewWord();
    }

    public void onNewWordClick() {
        word.setNewWord(!word.isNewWord());
        notifyChange();
    }

    public void onSubmitClick() {
        if (word.getEnglish().equals("")) {
            new QMUIDialog.MessageDialogBuilder(context)
                    .setTitle("英文不能为空")
                    .addAction("确定", (dialog, index) -> dialog.dismiss())
                    .show();
        } else if (word.getChinese().equals("")) {
            new QMUIDialog.MessageDialogBuilder(context)
                    .setTitle("中文不能为空")
                    .addAction("确定", (dialog, index) -> dialog.dismiss())
                    .show();
        } else {
            Observable.create((ObservableOnSubscribe<Integer>) e -> {
                Word newWord = wordDao.getWordByEnglish(word.getEnglish());
                if (newWord != null && newWord.getId() != word.getId()) {
                    e.onNext(0);
                } else {
                    wordDao.update(word);
                    e.onNext(1);
                }
            })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ObserverNext<Integer>() {
                        @Override
                        public void onNext(Integer integer) {
                            if (integer == 0) {
                                new QMUIDialog.MessageDialogBuilder(context)
                                        .setTitle(word.getEnglish() + "已经存在了")
                                        .addAction("确定", (dialog, index) -> dialog.dismiss())
                                        .show();
                            } else {
                                new QMUIDialog.MessageDialogBuilder(context)
                                        .setTitle("修改成功")
                                        .addAction("确定", (dialog, index) -> dialog.dismiss())
                                        .show();
                            }
                        }
                    });
        }
    }
}
