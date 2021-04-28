package com.csu.wordtutor.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csu.wordtutor.R;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.mysuper.ObserverNext;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewWordBookActivity extends AppCompatActivity {

    private RecyclerView mRVNewWords;

    private WordDao mWordDao;
    private NewWordAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word_book);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mWordDao = db.getWordDao();

        mRVNewWords = findViewById(R.id.rv_new_words);
        mRVNewWords.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewWordAdapter();
        mRVNewWords.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    public void update() {
        Observable.create((ObservableOnSubscribe<List<Word>>)
                e -> e.onNext(mWordDao.getNewWord()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<List<Word>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(List<Word> wordList) {
                        mAdapter.setWordList(wordList);
                        mRVNewWords.setAdapter(mAdapter);
                    }
                });
    }

    public class NewWordHolder extends RecyclerView.ViewHolder {

        private final TextView mTVEnglish;
        private final TextView mTVChinese;
        private final QMUIRoundButton mBTRemove;

        public NewWordHolder(@NonNull View itemView) {
            super(itemView);
            mTVEnglish = itemView.findViewById(R.id.tv_english);
            mTVChinese = itemView.findViewById(R.id.tv_chinese);
            mBTRemove = itemView.findViewById(R.id.bt_remove);
        }

        public void bind(Word word) {
            mTVEnglish.setText(String.valueOf(word.getEnglish()));
            mTVChinese.setText(String.valueOf(word.getChinese()));
            mBTRemove.setOnClickListener(v -> {
                word.setNewWord(false);
                Observable.create((ObservableOnSubscribe<Integer>)
                        e -> e.onNext(mWordDao.update(word)))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ObserverNext<Integer>() {

                            @Override
                            public void onNext(Integer integer) {
                                update();
                            }
                        });
            });
        }
    }

    public class NewWordAdapter extends RecyclerView.Adapter<NewWordHolder> {

        private List<Word> mWordList;

        public NewWordAdapter() {
            mWordList = new ArrayList<>();
        }

        public void setWordList(List<Word> mWordList) {
            this.mWordList = mWordList;
        }

        @NonNull
        @Override
        public NewWordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(NewWordBookActivity.this);
            View itemView = inflater.inflate(R.layout.list_item_new_word, parent, false);
            return new NewWordHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull NewWordHolder holder, int position) {
            holder.bind(mWordList.get(position));
        }

        @Override
        public int getItemCount() {
            return mWordList.size();
        }
    }
}
