package com.csu.wordtutor.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ActivityLexiconManageBinding;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.utils.FileUtils;
import com.csu.wordtutor.viewmodels.LexiconViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LexiconManageActivity extends Activity {

    private static final int READ_REQUEST_CODE = 1;

    private WordDao mWordDao;
    private LexiconViewModel mLexiconViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mWordDao = db.getWordDao();


        ActivityLexiconManageBinding binding =
                DataBindingUtil.setContentView(LexiconManageActivity.this, R.layout.activity_lexicon_manage);
        mLexiconViewModel = new LexiconViewModel(new ArrayList<>());
        binding.setViewModel(mLexiconViewModel);

        binding.btCheckLexicon.setOnClickListener(v -> onCheckClick());
        binding.btImportIn.setOnClickListener(v -> onImportInClick());
        binding.btAdd.setOnClickListener(v -> onAddInClick());
        update();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                Uri uri = resultData.getData();
                List<Word> wordList =
                        FileUtils.getLexiconFromExcel(FileUtils.uriToFile(uri, this));

                Observable.create((ObservableOnSubscribe<String>) e -> {
                    mWordDao.insertWordList(wordList);
                    e.onNext("done");
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                update();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }
    }

    public void onCheckClick() {

    }

    public void onImportInClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.ms-excel");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void onAddInClick() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("添加新单词")
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> dialog.dismiss())
                .show();
    }

    public void update() {
        Observable.create((ObservableOnSubscribe<List<Word>>) e -> {
            e.onNext(mWordDao.getAll());
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Word>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Word> words) {
                        mLexiconViewModel.setWordList(words);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                });
    }

}
