package com.csu.wordtutor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import com.csu.wordtutor.activities.HomeActivity;
import com.csu.wordtutor.utils.FileUtils;
import com.csu.wordtutor.utils.PermissionManage;

import org.apache.log4j.chainsaw.Main;

import java.io.IOException;

import jxl.read.biff.BiffException;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);

        PermissionManage.storePermission(this);

        MainApplication mainApplication = (MainApplication) getApplication();

        if (mainApplication.isOpen()) {
            startActivity(new Intent(this, HomeActivity.class));
            mainApplication.setOpen(true);
            this.finish();
        }

        findViewById(R.id.ll_background).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            mainApplication.setOpen(true);
            this.finish();
        });
    }

}