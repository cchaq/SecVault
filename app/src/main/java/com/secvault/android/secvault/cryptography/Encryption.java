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

    private RandomAccessFile copiedFile;
    private HashMap<Integer,Integer[]> binaryAsciiHashMap;
    private byte[] anArrayOfBytes; //TODO we should use the lenght of the hashmap size passed through
    private int increasingKey;
    private String binary = "";
    private byte byteFromBinary;
    private int increaseWhereToReadByOne;
    private int readByte;
    private int sizeOfBinaryHashMap;
    private List<Byte> finalLSBArray = new ArrayList<>();

    public void passFileAndBinaryHashMapToEncrypt(RandomAccessFile fileForEmbedding, HashMap binaryHashMapFromEncodeClass){
        copiedFile = fileForEmbedding;
        binaryAsciiHashMap = binaryHashMapFromEncodeClass;
        sizeOfBinaryHashMap = binaryHashMapFromEncodeClass.size();
        makeByteFromBinaryInHashMap();

    }

    private void makeByteFromBinaryInHashMap(){

        for(increasingKey =0; increasingKey < binaryAsciiHashMap.size(); increasingKey++){
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

        makeAsciiFromByte();
    }

    //Just for converting the byte to ascii.
    private void makeAsciiFromByte(){

        char asciiFromByte = (char) byteFromBinary;
        Log.i(TAG, String.valueOf(asciiFromByte));

    }

    private void embedBinaryUsingLSB(byte byteToAdd){
        try {

            copiedFile.seek(whereToStartEmbedding);
            anArrayOfBytes = new byte[sizeOfBinaryHashMap];
            copiedFile.read(anArrayOfBytes);

            increaseWhereToReadByOne  = 1;
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

    private List<Byte> returnListOfLSBBytes(){
        return finalLSBArray;
    }
}
