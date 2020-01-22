package com.secvault.android.secvault;

import android.util.Log;

import com.secvault.android.secvault.cryptography.Encode;
import com.secvault.android.secvault.cryptography.Encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class PlantIntoFile {

    private static final String TAG = "PlantIntoFile class";  //TODO I need a better name, noun name needed
    private static final int whereToStartAddingBytes = 1995;

    File_All file_all_class = new File_All();
    private Encode encodeClass = new Encode();

    private String nullTerminator = "\u0000";
    private String newFileName;
    private String mimeType;
    private String fileName;
    private String fileOutputDirString;

    private InputStream usersFile;
    private OutputStream outFile;
    private byte[] fileNameToBytes;

    private File fileSelectedByUser;
    private RandomAccessFile copiedFile;
    private Encryption encryptionClass = new Encryption();


    public void copyFileToExternalDir(String fromFile, String folderDestinationFromUser) {

        newFileName = file_all_class.createNewFileName();
        mimeType = file_all_class.getFileExtension(fromFile);
        fileSelectedByUser = new File(fromFile);
        fileOutputDir(folderDestinationFromUser);

        try {

            usersFile = new FileInputStream(fromFile);
            outFile = new FileOutputStream(fileOutputDirString);

            byte[] buf = new byte[1024];

            int bytesRead;
            while ((bytesRead = usersFile.read(buf)) > 0){

                outFile.write(buf, 0, bytesRead);
            }

            copiedFile = new RandomAccessFile(fileOutputDirString, "rw");

            //the story of encoding
            encodeFileNameToBytes();
            writeBytesIntoFile(fileNameToBytes);
            convertBytesToAsciiAndGetBinaryHashMap();
            encryptionClass.passFileAndBinaryHashMapToEncrypt(copiedFile, encodeClass.returnBinaryHashMap());
            closeStreams();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileOutputDir(String folderDestination){

        fileOutputDirString = folderDestination + "/" + newFileName + "." + mimeType;

    }
    private void encodeFileNameToBytes(){

        fileName = fileSelectedByUser.getName();
    //    addNullTerminatorToFileName();
        fileNameToBytes = fileName.getBytes();
    }

    private void convertBytesToAsciiAndGetBinaryHashMap(){
        encodeClass.getBinaryOfAscii(fileNameToBytes);
    }


    //THIS WAS USED BEFORE I USED LSB. I added the null byte at the end of the file name, so that
    //when i embedded into the image, I would know when to stop getting bytes from it.

    private void addNullTerminatorToFileName(){

        fileName = fileName.concat(nullTerminator);

        Log.i(TAG, "added null to file name " + fileName);
    }


    //TODO once I get LSB working, the below can be commented out
    private void writeBytesIntoFile(byte[] bytesToWrite){  //added a parameter, so we can reuse this function

        try{

            copiedFile.seek(copiedFile.length());
        //    copiedFile.seek(whereToStartAddingBytes);
            copiedFile.write(bytesToWrite);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void closeStreams(){
        try {
            usersFile.close();
            outFile.close();
            copiedFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
