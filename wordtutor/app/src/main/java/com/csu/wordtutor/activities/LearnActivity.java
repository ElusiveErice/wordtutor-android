package com.csu.wordtutor.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ActivityLearnBinding;
import com.csu.wordtutor.model.Unit;
import com.csu.wordtutor.model.UnitDao;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.mysuper.ObserverNext;
import com.csu.wordtutor.utils.SharedHelper;
import com.csu.wordtutor.viewmodels.LearnViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LearnActivity extends Activity {

    private static final String TAG = "LearnActivity";

    private WordDao mWordDao;
    private UnitDao mUnitDao;

    private SharedHelper mSharedHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mWordDao = db.getWordDao();
        mUnitDao = db.getUnitDao();

        //从缓存中找出上次学习到的单元id和索引
        mSharedHelper = new SharedHelper(this);
        long unitId = mSharedHelper.readUnitId();
        int index = mSharedHelper.readIndex();

        if (unitId == Word.DEFAULT_UNIT_ID) {
            load_new_unit();
        } else {
            load_unit(unitId, index);
        }
    }

    /**
     * 加载上次学习到的单元的单词
     *
     * @param unitId 上次学习到的单元id
     * @param index  上次学习的单词索引
     */
    private void load_unit(long unitId, int index) {
        Observable.create((ObservableOnSubscribe<List<Word>>) e -> {
            e.onNext(mWordDao.getUnitList(unitId));
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<List<Word>>() {
                    @Override
                    public void onNext(List<Word> wordList) {
                        ActivityLearnBinding binding =
                                DataBindingUtil.setContentView(LearnActivity.this, R.layout.activity_learn);
                        LearnViewModel learnViewModel =
                                new LearnViewModel(LearnActivity.this, wordList);
                        learnViewModel.setIndex(index);
                        binding.setViewModel(learnViewModel);
                    }
                });
    }

    /**
     * 加载新的单元单词
     * 从没有划分单元的单词中抽选出一个单元的单词
     */
    private void load_new_unit() {
        Observable.create((ObservableOnSubscribe<List<Word>>) e -> {
            int unitSize = mSharedHelper.readUnitSize();
            List<Word> wordList = mWordDao.getUnitList(Word.DEFAULT_UNIT_ID, unitSize);
            updateNewUnit(wordList);
            e.onNext(wordList);
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<List<Word>>() {
                    @Override
                    public void onNext(List<Word> wordList) {
                        if (wordList.isEmpty()) {
                            new QMUIDialog.MessageDialogBuilder(LearnActivity.this)
                                    .setTitle("当前词库已经全部学习完了")
                                    .setMessage("是否添加新的单词")
                                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                            LearnActivity.this.finish();
                                            dialog.dismiss();
                                        }
                                    })
                                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                            Intent intent = new Intent(LearnActivity.this, AddWordActivity.class);
                                            startActivity(intent);
                                            LearnActivity.this.finish();
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            ActivityLearnBinding binding =
                                    DataBindingUtil.setContentView(LearnActivity.this, R.layout.activity_learn);
                            LearnViewModel learnViewModel =
                                    new LearnViewModel(LearnActivity.this, wordList);
                            binding.setViewModel(learnViewModel);
                        }
                    }
                });
    }

    /**
     * 将这一单元的单词的单元进行更新
     * 创建新的单元，并对赋值给单词的单元属性
     *
     * @param wordList 要进行操作的单词列表
     */
    private void updateNewUnit(List<Word> wordList) {
        if (wordList.isEmpty()) {
            return;
        }
        long rowId = mUnitDao.insert(new Unit());
        Unit newUnit = mUnitDao.getByRowId(rowId);
        for (Word word : wordList) {
            word.setUnitId(newUnit.getId());
        }
        mWordDao.updateList(wordList);
        mSharedHelper.saveUnitId(newUnit.getId());
        mSharedHelper.saveIndex(LearnViewModel.INIT_INDEX);
    }

    /**
     * 处理完成学习后的操作
     * 更新学习记录缓存，单元设置成默认的，索引设置成初始的
     */
    public void handleFinishLearn() {
        mSharedHelper.saveUnitId(Word.DEFAULT_UNIT_ID);
        mSharedHelper.saveIndex(LearnViewModel.INIT_INDEX);
        this.finish();
    }

    /**
     * 将单词添加到生词本中
     *
     * @param word 要添加到生词本中的单词
     */
    public void handleAddNewWord(Word word) {
        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            e.onNext(mWordDao.update(word));
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        if (integer > 0) {
                            Toast.makeText(LearnActivity.this, "更新成功", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}
