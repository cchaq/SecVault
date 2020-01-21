package com.secvault.android.secvault;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesWithinFolder {

    private static final String TAG = "FilesWithFolder Class";
    private List<String> fileListing = new ArrayList<>();

    public List<String> returnFilesList(Intent intentForFolderPath){
        File rootDirPath = new File(intentForFolderPath.getStringExtra("folderPath"));

        Log.i(TAG,rootDirPath.getPath());

        File[] fileWithinFolders = rootDirPath.listFiles();
        for (File file : fileWithinFolders) {
            fileListing.add(file.getName());
        }
        return fileListing;

    }
}
