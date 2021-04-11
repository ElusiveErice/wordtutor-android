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

import com.csu.wordtutor.utils.FileUtils;

import java.io.IOException;

import jxl.read.biff.BiffException;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final int READ_REQUEST_CODE = 1;

    private static final int REQUEST_EXTERNAL_STORAGE = 0;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);

        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }

        Button button = (Button) findViewById(R.id.bt_open);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/vnd.ms-excel");
            startActivityForResult(intent, READ_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                FileUtils.getLexiconFromExcel(FileUtils.uriToFile(uri, this));
            }
        }
    }

}