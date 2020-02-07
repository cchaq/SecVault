package com.secvault.android.secvault;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {

    private static final int EXTERNAL_PERMISSIONS_REQUEST = 1;

    public void checkPermissions(Context activity){
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
                askForReadExternalPerm((Activity) activity);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            !=PackageManager.PERMISSION_GRANTED){
            askForWriteExternalPerm((Activity) activity);
        }
    }

    private void askForReadExternalPerm(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                EXTERNAL_PERMISSIONS_REQUEST);
    }

    private void askForWriteExternalPerm(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                EXTERNAL_PERMISSIONS_REQUEST);
    }
}
