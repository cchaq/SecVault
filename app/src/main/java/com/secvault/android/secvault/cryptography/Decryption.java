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

    private static int nullTerminator = 0x000000;
    private String bytesToString;
    private  byte[] readBytesFromFile;
    private byte[] encryptedText;
    private int untilNull;
    private String fileNameFromEncryptedFile;
    private File filePickedByUser;
    int indexOfChor = 0;

    private RandomAccessFile fileToDecrypt;
    private InputStream usersFile;
    private OutputStream outFile;
    private Decode decodeClass = new Decode();



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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getEmbeddedFileNameFromFile() {
        readBytesFromFile = new byte[1001];                //Unlikely someone will have a 1001+ byte sized named. //TODO what if it is a message?

        try {

            fileToDecrypt.seek(fileToDecrypt.length()  - 1001);
            fileToDecrypt.read(readBytesFromFile);

            findChor();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void findChor(){

        for(int findingIndexOfChor = readBytesFromFile.length -1; findingIndexOfChor > 0; findingIndexOfChor --){
            if(readBytesFromFile[findingIndexOfChor] == 114){

                if(readBytesFromFile[findingIndexOfChor - 1]  == 111){
                    indexOfChor = findingIndexOfChor + 1;
                    encryptedText = new byte[readBytesFromFile.length - indexOfChor];
                   break;
                }
            }
        }
        addHexBytesToEncryptedTextByeArray();
    }

    private void addHexBytesToEncryptedTextByeArray(){
        int increasingEncryptedTextIndex = 0;

        for(int countingIndex = indexOfChor; countingIndex < readBytesFromFile.length; countingIndex++){
            encryptedText[increasingEncryptedTextIndex] = readBytesFromFile[countingIndex];
            increasingEncryptedTextIndex++;
        }
        decodeBytes();
    }

    private void decodeBytes(){
        decodeClass.passEncryptedTextInByteArray(encryptedText);
    }


    private void makeOriginalFile(){  //TODO maybe we do not need this?

        try {
            usersFile = new FileInputStream(filePickedByUser.getPath());
            outFile = new FileOutputStream(filePickedByUser.getParent() + "/" + decodeClass.returnFineName());

            try {

                byte[] buf = new byte[1995];
                int read;
                while ((read = usersFile.read(buf)) > 0){
                    outFile.write(buf,0,read);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
