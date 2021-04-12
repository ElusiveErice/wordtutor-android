package com.csu.wordtutor;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class MainApplication extends Application {

    private boolean isOpen;

    @Override
    public void onCreate() {
        super.onCreate();
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
