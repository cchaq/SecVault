package com.secvault.android.secvault.cryptography;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;


public class Decryption {

    private static final String TAG = "Decryption class";
    private static final int whereToStartReadingBytesFrom = 1995; //TODO Instead of duplicating this, use a pointer to the one in encryption class

    private static String nullTerminator = "\u0000";
    private String bytesToString;
    private  byte[] readBytesFromFile;
    private int untilNull;
    private String fileNameFromEncryptedFile;
    private File filePickedByUser;

    private RandomAccessFile fileToDecrypt;
    private InputStream usersFile;
    private OutputStream outFile;


    public void passFullFilePath(String originalFullFilePath){
        filePickedByUser = new File(originalFullFilePath);
        Log.i(TAG,filePickedByUser.getPath());
        setFileToDecrypt(filePickedByUser.getAbsolutePath());

    }

    private void setFileToDecrypt(String originalFullFilePath){

        try {

            fileToDecrypt = new RandomAccessFile(originalFullFilePath, "rw");
            getEmbeddedFileNameFromFile();
            makeOriginalFile();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getEmbeddedFileNameFromFile();
    }

    private void getEmbeddedFileNameFromFile() {
        readBytesFromFile = new byte[100];                //Unlikely someone will have a 100+ byte sized named.

        try {

            fileToDecrypt.seek(whereToStartReadingBytesFrom);
            fileToDecrypt.read(readBytesFromFile);

            convertBytesToString();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    private void convertBytesToString(){

        bytesToString = new String(readBytesFromFile);
//        untilNull =  bytesToString.indexOf(nullTerminator);
  //      fileNameFromEncryptedFile = bytesToString.substring(0,untilNull);
    //    Log.i(TAG,fileNameFromEncryptedFile);

    }

    private void makeOriginalFile(){

        try {
            usersFile = new FileInputStream(filePickedByUser.getPath());
        //    outFile = new FileOutputStream(filePickedByUser.getParent() + "/" + fileNameFromEncryptedFile);
            outFile = new FileOutputStream(filePickedByUser.getParent() + "/Chrono.jpg");

            try {

                byte[] buf = new byte[1995];
                //usersFile.read(buf);
                //outFile.write(buf);
              //  usersFile.skip(untilNull);
                //buf = new byte[1024];
                int read;
                while ((read = usersFile.read(buf)) > 0){
                    outFile.write(buf,0,read);
                }

                /*
                byte [] bytesFromOriginalImage = new byte[125000];
                fileToDecrypt.read(bytesFromOriginalImage,0,1995);
                fileToDecrypt.seek(1995+ untilNull);
                fileToDecrypt.read(bytesFromOriginalImage,1995,bytesFromOriginalImage.length - 1995);
                outFile.write(bytesFromOriginalImage);

                 */

                /*
                byte [] bytesForOriginalImage2 = new byte[125000];
                fileToDecrypt.seek(1995+untilNull);
                fileToDecrypt.read(bytesForOriginalImage2,0,125000);
                outFile.write(bytesForOriginalImage2,0,bytesForOriginalImage2.length);

                 */


            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


}
