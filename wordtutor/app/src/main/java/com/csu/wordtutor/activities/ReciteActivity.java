package com.csu.wordtutor.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.csu.wordtutor.model.Unit;
import com.csu.wordtutor.model.UnitDao;
import com.csu.wordtutor.model.WordRoomDatabase;
import com.csu.wordtutor.mysuper.ObserverNext;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReciteActivity extends AppCompatActivity {

    private RecyclerView mRVUnits;
    private UnitAdapter mAdapter;

    private UnitDao mUnitDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recite);

        WordRoomDatabase db = WordRoomDatabase.getInstance(this);
        mUnitDao = db.getUnitDao();

        mRVUnits = findViewById(R.id.rv_units);
        mRVUnits.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UnitAdapter();
        mRVUnits.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    private void update() {
        Observable.create((ObservableOnSubscribe<List<Unit>>)
                e -> e.onNext(mUnitDao.getAll()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverNext<List<Unit>>() {
                    @Override
                    public void onNext(List<Unit> unitList) {
                        mAdapter.setUnitList(unitList);
                        mRVUnits.setAdapter(mAdapter);
                    }
                });
    }

    public class UnitHolder extends RecyclerView.ViewHolder {

        private final QMUIRoundButton mBTUnit;

        public UnitHolder(@NonNull View itemView) {
            super(itemView);
            mBTUnit = itemView.findViewById(R.id.bt_unit);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Unit unit) {
            mBTUnit.setText("unit" + unit.getId());
            mBTUnit.setOnClickListener(v -> {
                Intent intent = ReLearnActivity.newIntent(ReciteActivity.this, unit.getId());
                startActivity(intent);
            });
        }
    }

    public class UnitAdapter extends RecyclerView.Adapter<UnitHolder> {

        private List<Unit> mUnitList;

        public UnitAdapter() {
            mUnitList = new ArrayList<>();
        }

        public void setUnitList(List<Unit> mUnitList) {
            this.mUnitList = mUnitList;
        }

        @NonNull
        @Override
        public UnitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ReciteActivity.this);
            View itemView = inflater.inflate(R.layout.list_item_unit, parent, false);
            return new UnitHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull UnitHolder holder, int position) {
            holder.bind(mUnitList.get(position));
        }

        @Override
        public int getItemCount() {
            return mUnitList.size();
        }
    }
}
