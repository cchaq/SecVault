package com.secvault.android.secvault.cryptography;

import android.util.Log;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Encryption {

    //https://bit-calculator.com/most-and-least-significant-bit

    private static final String TAG = "Encryption class : ";
    private static final int whereToStartEmbedding = 1995;

    private String practiceConstructor;

    private RandomAccessFile copiedFile;
    private HashMap<Integer,Integer[]> binaryAsciiHashMap;
    private byte[] anArrayOfBytes;
    private int increasingKey;
    private String binary = "";
    private byte byteFromBinary;
    private int increaseWhereToReadByOne;
    private int readByte;
    private int sizeOfBinaryHashMap;
    private List<Byte> finalLSBArray = new ArrayList<>();

    public Encryption(){
        this.practiceConstructor = "Encryption class";
        Log.i(TAG,practiceConstructor + " has been loaded");
    }

    public void passFileAndBinaryHashMapToEncrypt(RandomAccessFile fileForEmbedding, HashMap binaryHashMapFromEncodeClass){
        copiedFile = fileForEmbedding;
        binaryAsciiHashMap = binaryHashMapFromEncodeClass;
        sizeOfBinaryHashMap = binaryHashMapFromEncodeClass.size();


        setFilePointer(); //To stop it from reading over the same place and to always start at the same place
        makeByteFromBinaryInHashMap();

    }

    private void setFilePointer(){

        try {

            copiedFile.seek(whereToStartEmbedding);
            increaseWhereToReadByOne  = 1;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void makeByteFromBinaryInHashMap(){

        for(increasingKey = 0; increasingKey < binaryAsciiHashMap.size(); increasingKey++){
            Integer[] bits = binaryAsciiHashMap.get(increasingKey);

            for (int bitInValue = 0; bitInValue < 8; bitInValue++){
                binary += String.valueOf(bits[bitInValue]);
            }
            passByteToLSB();
            binary = ""; //Clear it once we have our ascii
        }
    }

    private void passByteToLSB(){
        byteFromBinary = Byte.parseByte(binary,2);
        embedBinaryUsingLSB(byteFromBinary);

        //makeAsciiFromByte(); //Testing purposes
    }

    //Just for converting the byte to ascii.
    private void makeAsciiFromByte(){

        char asciiFromByte = (char) byteFromBinary;
      //  Log.i(TAG, String.valueOf(asciiFromByte));

    }

    private void embedBinaryUsingLSB(byte byteToAdd){
        try {

            anArrayOfBytes = new byte[sizeOfBinaryHashMap];
            copiedFile.read(anArrayOfBytes);

            readByte = 0;

            for(int bitsInValue = 7; bitsInValue >= 0; bitsInValue--){

                int bit = (byteToAdd >>> bitsInValue) & 1;

                byte changedByte = (byte) ((anArrayOfBytes[readByte] & 0xFE) | bit);
                finalLSBArray.add(changedByte);

                copiedFile.seek(whereToStartEmbedding + increaseWhereToReadByOne);
                increaseWhereToReadByOne++;
                readByte++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Byte> returnListOfLSBBytes(){
        return this.finalLSBArray;
    }
}
