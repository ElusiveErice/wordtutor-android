package com.csu.wordtutor.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ActivityAddWordBinding;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.mysuper.ObserverNext;
import com.csu.wordtutor.utils.FileUtils;
import com.csu.wordtutor.utils.PermissionManage;
import com.csu.wordtutor.viewmodels.AddWordViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class AddWordActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 1;
    private static final int SUCCESS = 1;
    private static final int EXISTED = 0;
    private static final int ENGLISH_NULL = 2;
    private static final int CHINESE_NULL = 3;
    private WordDao mWordDao;
    private AddWordViewModel mAddWordViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mWordDao = db.getWordDao();

        ActivityAddWordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_word);
        mAddWordViewModel = new AddWordViewModel();
        binding.setViewModel(mAddWordViewModel);

        binding.btAdd.setOnClickListener(v -> onAddClick());
        binding.btImportIn.setOnClickListener(v -> onImportInClick());
    }

    public void onAddClick() {
        String english = mAddWordViewModel.getEnglish();
        String chinese = mAddWordViewModel.getChinese();
        if (english == null || english.equals("")) {
            handleResult(ENGLISH_NULL);
        } else if (chinese == null || chinese.equals("")) {
            handleResult(CHINESE_NULL);
        } else {
            Observable.create((ObservableOnSubscribe<Integer>) e -> {
                Word word = mWordDao.getWordByEnglish(english);
                if (word != null) {
                    e.onNext(EXISTED);
                } else {
                    mWordDao.insertWord(new Word(english, chinese));
                    e.onNext(SUCCESS);
                }
            })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ObserverNext<Integer>() {
                        @Override
                        public void onNext(Integer integer) {
                            handleResult(integer);
                        }
                    });
        }


    }

    public void onImportInClick() {
        if (PermissionManage.storePermission(this)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/vnd.ms-excel");
            startActivityForResult(intent, READ_REQUEST_CODE);
        }
    }

    private void handleResult(int result) {
        String message = "添加失败";
        if (result == ENGLISH_NULL) {
            message = "英文不能为空";
        } else if (result == CHINESE_NULL) {
            message = "中文不能为空";
        } else if (result == EXISTED) {
            message = "已经存在该单词了";
            mAddWordViewModel.setChinese("");
            mAddWordViewModel.setEnglish("");
        }
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle(message)
                .addAction("确定", (dialog, index) -> dialog.dismiss())
                .show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);
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
        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            List<Long> longList = mWordDao.insertWordList(wordList);
            e.onNext(longList.size());
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        new QMUIDialog.MessageDialogBuilder(AddWordActivity.this)
                                .setTitle("导入了" + integer + "个单词")
                                .addAction("确定", (dialog, index) -> dialog.dismiss())
                                .show();
                    }
                });
    }


}
