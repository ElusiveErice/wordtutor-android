package com.csu.wordtutor.activities;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ActivityAddWordBinding;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.viewmodels.AddWordViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddWordActivity extends AppCompatActivity {

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
    }

    public void onAddClick() {
        String english = mAddWordViewModel.getEnglish();
        String chinese = mAddWordViewModel.getChinese();

        Observable.create((ObservableOnSubscribe<String>) e -> {
            mWordDao.insertWord(new Word(english, chinese));
            e.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        handleAddComplete();
                    }
                });
    }

    private void handleAddComplete() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("添加成功")
                .addAction("确定", (dialog, index) -> dialog.dismiss())
                .show();
        mAddWordViewModel.setChinese("");
        mAddWordViewModel.setEnglish("");
    }
}
