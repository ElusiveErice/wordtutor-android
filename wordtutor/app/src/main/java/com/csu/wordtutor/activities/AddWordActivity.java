package com.csu.wordtutor.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ActivityAddWordBinding;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.mysuper.ObserverComplete;
import com.csu.wordtutor.mysuper.ObserverNext;
import com.csu.wordtutor.utils.FileUtils;
import com.csu.wordtutor.utils.PermissionManage;
import com.csu.wordtutor.viewmodels.AddWordViewModel;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.lang.ref.WeakReference;
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

    private static final int NEXT = 0x11;
    private static final int INIT = 0x12;

    private WordDao mWordDao;
    private AddWordViewModel mAddWordViewModel;
    private QMUIProgressBar mPBImport;
    private ImportHandler mHandler;
    private QMUIRoundButton mBTImport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mWordDao = db.getWordDao();

        ActivityAddWordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_word);
        mAddWordViewModel = new AddWordViewModel();
        binding.setViewModel(mAddWordViewModel);

        //进度条管理
        mPBImport = binding.pbImport;
        mPBImport.setQMUIProgressBarTextGenerator((progressBar, value, maxValue) -> value + "/" + maxValue);

        //导入按钮管理
        mBTImport = binding.btImportIn;
        mBTImport.setOnClickListener(v -> onImportInClick());

        binding.btAdd.setOnClickListener(v -> onAddClick());

        mHandler = new ImportHandler(mPBImport, mBTImport);
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
        } else if (result == SUCCESS) {
            message = "添加成功";
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

    public void handleInsertWords(List<Word> wordList) {
        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            Message init_message = new Message();
            init_message.what = INIT;
            init_message.arg1 = wordList.size();
            mHandler.sendMessage(init_message);
            int i = 0;
            for (Word word : wordList) {
                i++;
                Message message = new Message();
                message.what = NEXT;
                message.arg1 = i;
                mHandler.sendMessage(message);

                if (mWordDao.getWordByEnglish(word.getEnglish()) == null) {
                    mWordDao.insertWord(word);
                }
            }
            e.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverComplete<Integer>() {
                    @Override
                    public void onComplete() {
                        mBTImport.setVisibility(View.VISIBLE);
                    }
                });
    }


    private static class ImportHandler extends Handler {

        private final WeakReference<QMUIProgressBar> weakReferencePB;
        private final WeakReference<QMUIRoundButton> weakReferenceBT;

        public ImportHandler(QMUIProgressBar qmuiProgressBar, QMUIRoundButton qmuiRoundButton) {
            super();
            weakReferencePB = new WeakReference<>(qmuiProgressBar);
            weakReferenceBT = new WeakReference<>(qmuiRoundButton);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            QMUIProgressBar qmuiProgressBar = weakReferencePB.get();
            QMUIRoundButton qmuiRoundButton = weakReferenceBT.get();

            switch (msg.what) {
                case INIT:
                    qmuiProgressBar.setVisibility(View.VISIBLE);
                    qmuiRoundButton.setVisibility(View.INVISIBLE);
                    qmuiProgressBar.setMaxValue(msg.arg1);
                    break;
                case NEXT:
                    qmuiProgressBar.setProgress(msg.arg1);
                    break;
            }
        }
    }
}
