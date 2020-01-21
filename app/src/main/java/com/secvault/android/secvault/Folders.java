package com.secvault.android.secvault;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Everything you want to do with folders in the whole app. should be written here

public class Folders {

    private static final String TAG = "Folders_Class";
    private List<String> dirListings = new ArrayList<>();

    public static String EXTERNAL_FOLDER_DIR = Environment.getExternalStorageDirectory().toString().concat("/secVaultFolder"); //TODO train wreck

    //This creates an internal folder
    public boolean createFolder(Context context, String folderName){
       // File newFolder = new File(context.getFilesDir().getPath(),"/"+ folderName);
        File newFolder = new File(EXTERNAL_FOLDER_DIR,"/"+ folderName);
        Log.i(TAG,"Dir = " + context.getFilesDir().getPath());

        if (!doesFolderExist(newFolder)){
            newFolder.mkdirs();

            Log.i(TAG,"Creating folder at "+ newFolder.getAbsolutePath());
            return true;
        }
        return false;
    }
    private boolean doesFolderExist(File folderName){
        if(folderName.exists()){
            Log.i(TAG, "Folder exists in "+ folderName.getAbsolutePath());
            return true;
        }
            return false;
    }

    public List<String> returnListOfDirs(){
        File rootDirPath = new File(EXTERNAL_FOLDER_DIR);

        File[] directories = rootDirPath.listFiles();
        for (File dir : directories) {
            dirListings.add(dir.toString());
        }
        return dirListings;
    }

    public void createExternalFolder(){
        String externalFolderName = "secVaultFolder";
        File externalFolder = new File(Environment.getExternalStorageDirectory(), externalFolderName);
        Log.i(TAG,externalFolder.toString());
        if(!externalFolder.exists()){
            externalFolder.mkdirs();
        }
    }
}
