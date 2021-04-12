package com.csu.wordtutor.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ActivityLearnBinding;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.viewmodels.LearnViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LearnActivity extends Activity {

    private static final String TAG = "LearnActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        WordDao wordDao = db.getWordDao();
        Observable.create((ObservableOnSubscribe<List<Word>>) e -> e.onNext(wordDao.getAll()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Word>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Word> words) {
                        if (words.isEmpty()) {
                            onHandleEmptyWord();
                        } else {
                            ActivityLearnBinding binding =
                                    DataBindingUtil.setContentView(LearnActivity.this, R.layout.activity_learn);
                            LearnViewModel learnViewModel =
                                    new LearnViewModel(LearnActivity.this, words);
                            binding.setViewModel(learnViewModel);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onHandleEmptyWord() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("注意")
                .setMessage("当前词库没有单词，去管理它吧")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        LearnActivity.this.finish();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Intent intent = new Intent(LearnActivity.this, LexiconManageActivity.class);
                        startActivity(intent);
                        LearnActivity.this.finish();
                    }
                })
                .show();
    }

}
