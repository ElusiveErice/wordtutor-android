package com.csu.wordtutor.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ListItemWordBinding;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.viewmodels.WordViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LexiconActivity extends AppCompatActivity {

    private RecyclerView mRVLexicon;
    private LexiconAdapter mAdapter;
    private WordDao mWordDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lexicon);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mWordDao = db.getWordDao();

        mRVLexicon = (RecyclerView) findViewById(R.id.rv_lexicon);
        mRVLexicon.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LexiconAdapter(new ArrayList<>());
        mRVLexicon.setAdapter(mAdapter);

        update();
    }

    private void update() {
        Observable.create((ObservableOnSubscribe<List<Word>>) e -> e.onNext(mWordDao.getAll())).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Word>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Word> wordList) {
                        mAdapter.setWordList(wordList);
                        mRVLexicon.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static class LexiconHolder extends RecyclerView.ViewHolder {


        private ListItemWordBinding mBinding;


        public LexiconHolder(@NonNull ListItemWordBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Word word, int index) {
            WordViewModel wordViewModel = new WordViewModel(word, index);
            mBinding.setViewModel(wordViewModel);
        }
    }

    public class LexiconAdapter extends RecyclerView.Adapter<LexiconHolder> {

        private List<Word> mWordList;

        public LexiconAdapter(List<Word> wordList) {
            mWordList = wordList;
        }

        public void setWordList(List<Word> wordList) {
            mWordList = wordList;
        }

        @NonNull
        @Override
        public LexiconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(LexiconActivity.this);
            return new LexiconHolder(DataBindingUtil.inflate(layoutInflater, R.layout.list_item_word, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull LexiconHolder holder, int position) {
            holder.bind(mWordList.get(position), position + 1);
        }

        @Override
        public int getItemCount() {
            return mWordList.size();
        }
    }
}
