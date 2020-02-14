package com.secvault.android.secvault;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.security.SecureRandom;


public class File_All {

    private static String TAG = "Move File_All Class";

    private Cursor fileCursor;
    private String destFolder;

    private static final String lowerCaseAlphabet = "abcedfghijklmnopqrstxyz";
    private static final String upperCaseAlphabet = lowerCaseAlphabet.toUpperCase();
    private char[] fullAlphabet = lowerCaseAlphabet.concat(upperCaseAlphabet).toCharArray();

    private SecureRandom secureRandom = new SecureRandom();
    private StringBuilder randomFileName = new StringBuilder();
    private File fileOriginalDetails;

    private EncryptAll encryptAll;

    public void fromFileGetRealData(Uri fileUri, ContentResolver contentResolver) {

        String[] projection = {MediaStore.MediaColumns.DATA};    //I have no idea why this is used but we need it
        fileCursor = contentResolver.query(fileUri  ,projection,null,null,null);

        fileCursor.moveToFirst();


        fileOriginalDetails = new File(fileCursor.getString(0));

        copyFile(fileCursor.getString(0));
        fileCursor.close();

    }

    private void copyFile(String fromFile) {

        encryptAll = new EncryptAll();
        encryptAll.copyFileToExternalDir(fromFile, destFolder);

    }

    public String createNewFileName(){

        for(int buildingFileLength = 0; buildingFileLength< 10; buildingFileLength++){ //TODO this should really be in an encrypt class
            int index = secureRandom.nextInt(fullAlphabet.length);
            char letter = fullAlphabet[index];
            randomFileName.append(letter);
        }

        Log.i(TAG, randomFileName.toString());
        return randomFileName.toString();
    }

    public String getFileExtension(String fromFile){

        String extensionFromFile = MimeTypeMap.getFileExtensionFromUrl(fromFile);

        Log.i(TAG,extensionFromFile);

        return extensionFromFile;
    }

    public String getMimeType(String fullFilePath){

        String fileExtension = getFileExtension(fullFilePath);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

        Log.i(TAG,mimeType);
        return  mimeType;
    }


    public void passDestFolder(String folderPath){

        destFolder = folderPath;
    }

}
