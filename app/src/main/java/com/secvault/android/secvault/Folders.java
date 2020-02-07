package com.secvault.android.secvault;

import android.content.Context;
import android.os.Environment;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Everything you want to do with folders in the whole app. should be written here

public class Folders {

    private static final String TAG = "Folders_Class";
    private List<String> dirListings = new ArrayList<>();
    private List<File> externalFolders = new ArrayList<>();

    public static final String EXTERNAL_FOLDER_DIR = Environment.getExternalStorageDirectory().toString().concat("/SecVaultFolder/");
    public static final String ENCRYPTED_FOLDER = EXTERNAL_FOLDER_DIR + "/Encrypted/";


    //This creates an internal folder
    public boolean createFolder(Context context, String folderName){

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
        if(directories != null) {
            for (File dir : directories) {
                dirListings.add(dir.toString());
            }
            return dirListings;
        }
        return Collections.emptyList();
    }

    public void createExternalFolders(){
        externalFolders.add(new File(EXTERNAL_FOLDER_DIR));
        externalFolders.add(new File(ENCRYPTED_FOLDER));

        for(File folder : externalFolders){
            if(!folder.exists()){
                folder.mkdirs();
            }
        }

    }

}
