package com.secvault.android.secvault;

import android.app.Activity;
import android.util.Log;

import com.secvault.android.secvault.cryptography.Encode;
import com.secvault.android.secvault.cryptography.Encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.List;

public class EncryptAll extends Activity {

    private static final String TAG = "EncryptAll class";
    public static final int whereToStartAddingBytes = 1995;

    File_All file_all_class = new File_All();
    private Encode encodeClass = new Encode();

    private String nullTerminator = "\u0000";
    private String newFileName;
    private String mimeType;
    private String fileName;
    private String fileOutputDirString; //This is the file that has been copied and with the name embedded.

    private InputStream usersFile;
    private OutputStream outFile;
    private byte[] fileNameToBytes;

    private File fileSelectedByUser;
    private RandomAccessFile copiedFile;
    private Encryption encryptionClass = new Encryption();
    private RandomAccessFile RAFToReturn;

    public void copyFileToExternalDir(String fromFile, String folderDestinationFromUser) {

        newFileName = file_all_class.createNewFileName();
        mimeType = file_all_class.getFileExtension(fromFile);
        fileSelectedByUser = new File(fromFile);
        fileOutputDir(folderDestinationFromUser);

        try {

            usersFile = new FileInputStream(fromFile);
            outFile = new FileOutputStream(fileOutputDirString);

            byte[] buf = new byte[1995];

            int bytesRead;
            while ((bytesRead = usersFile.read(buf)) > 0){

                outFile.write(buf, 0, bytesRead);
            }

            //     copiedFile = new RandomAccessFile(fileOutputDirString, "rw");
            copiedFile = this.returnRAFFileOfPassedFilePath(fileOutputDirString);

            //the story of encoding and encrypting
            encodeFileNameToBytes();
            convertBytesToAsciiAndGetBinaryHashMap();
            encryptionClass.passFileAndBinaryHashMapToEncrypt(copiedFile, encodeClass.returnBinaryHashMap());
            writeBytesIntoFile();
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
        //   addNullTerminatorToFileName(); //not needed because i am adding the null term in writeBytesIntoFile
        fileNameToBytes = fileName.getBytes();
    }

    private void convertBytesToAsciiAndGetBinaryHashMap(){
        encodeClass.getBinaryOfAscii(fileNameToBytes);
    }


    private void addNullTerminatorToFileName(){


        //fileName =  fileName.concat(nullTerminator);
        fileName = nullTerminator + fileName;
        Log.i(TAG, "added null to file name " + fileName);
    }


    //TODO how to embed the bytes anywhere within the image
    private void writeBytesIntoFile(){

        try{

            copiedFile.seek(copiedFile.length());
            //   copiedFile.seek(whereToStartAddingBytes);
            List<Byte> bytesToEmbed = encryptionClass.returnListOfLSBBytes();
            copiedFile.write(0x63); //have to do it byte by byte
            copiedFile.write(0x68);
            copiedFile.write(0x6F); //chro //TODO see if we can find this within the bytes
            copiedFile.write(0x72);
            for(int i = 0; i < bytesToEmbed.size(); i++) {

                copiedFile.write(bytesToEmbed.get(i));
            }

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


    //This has to be done because I didn't properly plan this in the past.

    public RandomAccessFile returnRAFFileOfPassedFilePath(String filePath) {

        try {
            RAFToReturn = new RandomAccessFile(filePath, "rw");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return RAFToReturn;
    }

}
