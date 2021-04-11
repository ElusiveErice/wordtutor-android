package com.csu.wordtutor.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public abstract class SimpleActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(getContentView());
        findView();
        setListener();
    }

    protected void findView() {

    }

    protected void setListener() {
    }

    protected abstract int getContentView();
}
