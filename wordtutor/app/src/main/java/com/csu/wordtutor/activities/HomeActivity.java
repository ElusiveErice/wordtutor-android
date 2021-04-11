package com.csu.wordtutor.activities;

import android.content.Intent;

import com.csu.wordtutor.MainActivity;
import com.csu.wordtutor.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

public class HomeActivity extends SimpleActivity {

    private QMUIRoundButton mBTStartLearn;
    private QMUIRoundButton mBTCheckLexicon;


    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void findView() {
        mBTStartLearn = (QMUIRoundButton) findViewById(R.id.bt_start_learn);
        mBTCheckLexicon = (QMUIRoundButton)findViewById(R.id.bt_check_lexicon);
    }

    @Override
    protected void setListener() {
        mBTStartLearn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LearnActivity.class);
            startActivity(intent);
        });
        mBTCheckLexicon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LexiconManageActivity.class);
            startActivity(intent);
        });
    }
}
