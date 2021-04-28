package com.csu.wordtutor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.csu.wordtutor.activities.HomeActivity;
import com.csu.wordtutor.utils.PermissionManage;
import com.csu.wordtutor.utils.SharedHelper;

import java.lang.ref.WeakReference;

public class MainActivity extends Activity {

    private static final int WHAT_START_LEARN = 0;
    private static final int WHAT_FIVE = 5;
    private static final int WHAT_FOUR = 4;
    private static final int WHAT_THREE = 3;
    private static final int WHAT_TWO = 2;
    private static final int WHAT_ONE = 1;
    private MainHandler mHandler;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);

        //获取权限管理
        PermissionManage.storePermission(this);

        //获取缓存信息
        SharedHelper sp = new SharedHelper(this);
        String aphorism = sp.readAphorism();
        int useCount = sp.readUseCount();
        sp.saveUseCount(useCount + 1);

        //初始化格言和使用次数TextView文本内容
        TextView TVAphorism = findViewById(R.id.tv_aphorism);
        TextView TVTitle = findViewById(R.id.tv_title);
        TVAphorism.setText(aphorism);
        TVTitle.setText("已经使用了" + useCount + "次了");

        mHandler = new MainHandler(this);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(WHAT_FIVE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(WHAT_FOUR);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(WHAT_THREE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(WHAT_TWO);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(WHAT_ONE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(WHAT_START_LEARN);
            }
        }.start();
    }

    private static class MainHandler extends Handler {

        private final WeakReference<MainActivity> mReference;

        public MainHandler(MainActivity mainActivity) {
            super();
            mReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mReference.get();
            TextView TVStartLearn = activity.findViewById(R.id.tv_start_learn);
            if (msg.what == WHAT_START_LEARN) {
                activity.startActivity(new Intent(activity, HomeActivity.class));
                activity.finish();
            } else if (msg.what == WHAT_THREE) {
                TVStartLearn.setText("3");
            } else if (msg.what == WHAT_TWO) {
                TVStartLearn.setText("2");
            } else if (msg.what == WHAT_ONE) {
                TVStartLearn.setText("1");
            } else if (msg.what == WHAT_FIVE) {
                TVStartLearn.setVisibility(View.VISIBLE);
                TVStartLearn.setText("5");
            } else if (msg.what == WHAT_FOUR) {
                TVStartLearn.setText("4");
            }
        }
    }
}