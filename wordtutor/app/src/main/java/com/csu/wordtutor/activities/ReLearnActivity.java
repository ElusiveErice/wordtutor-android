package com.csu.wordtutor.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ActivityReLearnBinding;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.mysuper.ObserverNext;
import com.csu.wordtutor.viewmodels.ReLearnViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReLearnActivity extends Activity {

    public static Intent newIntent(Context context, long unitId) {
        Intent intent = new Intent(context, ReLearnActivity.class);
        intent.putExtra("unit_id", unitId);
        return intent;
    }

    private WordDao mWordDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mWordDao = db.getWordDao();

        load_unit(getIntent().getLongExtra("unit_id", 1));
    }

    /**
     * 加载上次学习到的单元的单词
     *
     * @param unitId 上次学习到的单元id
     */
    private void load_unit(long unitId) {
        Observable.create((ObservableOnSubscribe<List<Word>>) e -> e.onNext(mWordDao.getUnitList(unitId)))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<List<Word>>() {
                    @Override
                    public void onNext(List<Word> wordList) {
                        ActivityReLearnBinding binding =
                                DataBindingUtil.setContentView(ReLearnActivity.this, R.layout.activity_re_learn);
                        ReLearnViewModel viewModel =
                                new ReLearnViewModel(ReLearnActivity.this, wordList);
                        binding.setViewModel(viewModel);
                    }
                });
    }


    /**
     * 将单词添加到生词本中
     *
     * @param word 要添加到生词本中的单词
     */
    public void handleAddNewWord(Word word) {
        Observable.create((ObservableOnSubscribe<Integer>) e -> e.onNext(mWordDao.update(word))).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        if (integer > 0) {
                            Toast.makeText(ReLearnActivity.this, "更新成功", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}
