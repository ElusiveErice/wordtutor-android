package com.csu.wordtutor.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.csu.wordtutor.R;
import com.csu.wordtutor.databinding.ActivityUpdateWordBinding;
import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.model.WordDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.viewmodels.UpdateWordViewModel;

public class UpdateWordActivity extends AppCompatActivity {

    public static Intent newIntent(Context context, Word word) {
        Intent intent = new Intent(context, UpdateWordActivity.class);
        intent.putExtra("word", word);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        WordDao wordDao = db.getWordDao();

        Word word = (Word) getIntent().getSerializableExtra("word");
        ActivityUpdateWordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_update_word);
        UpdateWordViewModel updateWordViewModel = new UpdateWordViewModel(this, word, wordDao);
        binding.setViewModel(updateWordViewModel);


    }
}
