package com.csu.wordtutor.activities;

import android.content.Intent;

import com.csu.wordtutor.MainActivity;
import com.csu.wordtutor.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

public class HomeActivity extends SimpleActivity {

    private QMUIRoundButton mBTStartLearn;
    private QMUIRoundButton mBTManageLexicon;
    private QMUIRoundButton mBTAddWord;
    private QMUIRoundButton mBTNewWordBook;
    private QMUIRoundButton mBTCardRecord;


    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void findView() {
        mBTStartLearn = findViewById(R.id.bt_start_learn);
        mBTManageLexicon = findViewById(R.id.bt_manage_lexicon);
        mBTAddWord = findViewById(R.id.bt_add_word);
        mBTNewWordBook = findViewById(R.id.bt_new_word_book);
        mBTCardRecord = findViewById(R.id.bt_card_record);
    }

    @Override
    protected void setListener() {
        mBTStartLearn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LearnActivity.class);
            startActivity(intent);
        });
        mBTManageLexicon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LexiconActivity.class);
            startActivity(intent);
        });
        mBTAddWord.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddWordActivity.class);
            startActivity(intent);
        });
        mBTNewWordBook.setOnClickListener(v -> {

        });
        mBTCardRecord.setOnClickListener(v -> {

        });
    }
}
