package com.csu.wordtutor.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.csu.wordtutor.model.Word;
import com.csu.wordtutor.viewmodels.LearnViewModel;

public class SharedHelper {
    public static final int DEFAULT_UNIT_SIZE = 30;

    private final Context mContext;

    public SharedHelper(Context context) {
        mContext = context;
    }

    public void saveUnitSize(int unitSize) {
        SharedPreferences sp = mContext.getSharedPreferences("unit_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("unit_size", unitSize);
        editor.apply();
    }

    public int readUnitSize() {
        SharedPreferences sp = mContext.getSharedPreferences("unit_info", Context.MODE_PRIVATE);
        return sp.getInt("unit_size", DEFAULT_UNIT_SIZE);
    }

    public void saveUnitId(long unitId) {
        SharedPreferences sp = mContext.getSharedPreferences("last_unit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("unit_id", unitId);
        editor.apply();
    }

    public void saveIndex(int index) {
        SharedPreferences sp = mContext.getSharedPreferences("last_unit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("index", index);
        editor.apply();
    }

    public long readUnitId() {
        SharedPreferences sp = mContext.getSharedPreferences("last_unit", Context.MODE_PRIVATE);
        return sp.getLong("unit_id", Word.DEFAULT_UNIT_ID);
    }

    public int readIndex() {
        SharedPreferences sp = mContext.getSharedPreferences("last_unit", Context.MODE_PRIVATE);
        return sp.getInt("index", LearnViewModel.INIT_INDEX);
    }
}
