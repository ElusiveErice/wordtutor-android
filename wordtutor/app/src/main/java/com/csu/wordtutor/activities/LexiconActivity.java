package com.csu.wordtutor.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LexiconActivity extends AppCompatActivity {

    private static final String TAG = "LexiconActivity";
    private RecyclerView mRVLexicon;
    private LexiconAdapter mAdapter;
    private EditText mETSelect;
    private WordDao mWordDao;
    private TextView mTVWordSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lexicon);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mWordDao = db.getWordDao();

        mRVLexicon = findViewById(R.id.rv_lexicon);
        mTVWordSize = findViewById(R.id.tv_word_size);
        mETSelect = findViewById(R.id.et_select);
        mETSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                update();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRVLexicon.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LexiconAdapter(new ArrayList<>());
        mRVLexicon.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    private void update() {
        Observable.create((ObservableOnSubscribe<List<Word>>)
                e -> e.onNext(mWordDao.getAllByText("%" + mETSelect.getText().toString() + "%")))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<List<Word>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(List<Word> wordList) {
                        mAdapter.setWordList(wordList);
                        mRVLexicon.setAdapter(mAdapter);
                        mTVWordSize.setText("单词总数:" + wordList.size());
                    }
                });
    }

    public class LexiconHolder extends RecyclerView.ViewHolder {

        private final TextView mTVEnglish;
        private final TextView mTVChinese;
        private final ImageButton mTBTSetting;
        private final TextView mTVUnitId;


        public LexiconHolder(@NonNull View view) {
            super(view);
            mTVEnglish = view.findViewById(R.id.tv_english);
            mTVChinese = view.findViewById(R.id.tv_chinese);
            mTBTSetting = view.findViewById(R.id.ibt_setting);
            mTVUnitId = view.findViewById(R.id.tv_unit_id);
        }

        public void bind(Word word, int index) {
            mTVEnglish.setText(word.getEnglish());
            mTVChinese.setText(word.getChinese());
            mTVUnitId.setText(String.valueOf(word.getUnitId()));
            mTBTSetting.setOnClickListener(e -> {
                Intent intent = UpdateWordActivity.newIntent(LexiconActivity.this, word);
                startActivity(intent);
            });
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
            return new LexiconHolder(layoutInflater.inflate(R.layout.list_item_word, parent, false));
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
