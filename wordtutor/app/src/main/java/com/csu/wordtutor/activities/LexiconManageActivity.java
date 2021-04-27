package com.csu.wordtutor.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ActivityLexiconManageBinding;
import com.csu.wordtutor.model.Unit;
import com.csu.wordtutor.model.UnitDao;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.mysuper.ObserverNext;
import com.csu.wordtutor.utils.FileUtils;
import com.csu.wordtutor.utils.PermissionManage;
import com.csu.wordtutor.viewmodels.LexiconManageViewModel;
import com.qmuiteam.qmui.skin.QMUISkinManager;
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

    private static final String TAG = "LexiconManageActivity";
    private static final int READ_REQUEST_CODE = 1;

    private WordDao mWordDao;
    private UnitDao mUnitDao;
    private LexiconManageViewModel mLexiconViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mWordDao = db.getWordDao();
        mUnitDao = db.getUnitDao();


        ActivityLexiconManageBinding binding =
                DataBindingUtil.setContentView(LexiconManageActivity.this, R.layout.activity_lexicon_manage);
        mLexiconViewModel = new LexiconManageViewModel(new ArrayList<>());
        binding.setViewModel(mLexiconViewModel);

        binding.btCheckLexicon.setOnClickListener(v -> onCheckClick());
        binding.btImportIn.setOnClickListener(v -> onImportInClick());
        binding.btAdd.setOnClickListener(v -> onAddInClick());
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                handleInsertWords(wordList);
            }
        }
    }


    /**
     * 将新的词库插入到表里
     *
     * @param wordList
     */
    public void handleInsertWords(List<Word> wordList) {
        Observable.create((ObservableOnSubscribe<String>) e -> {
            mWordDao.insertWordList(wordList);
            e.onNext("done");
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "导入成功");
                        update();
                    }
                });
    }

    public void onCheckClick() {
        Intent intent = new Intent(this, LexiconActivity.class);
        startActivity(intent);
    }

    public void onImportInClick() {
        if (PermissionManage.storePermission(this)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/vnd.ms-excel");
            startActivityForResult(intent, READ_REQUEST_CODE);
        }
    }

    public void onAddInClick() {
        Intent intent = new Intent(this, AddWordActivity.class);
        startActivity(intent);
    }

    public void update() {
        Observable.create((ObservableOnSubscribe<List<Word>>) e -> e.onNext(mWordDao.getAll()))
                .subscribeOn(Schedulers.newThread())
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
