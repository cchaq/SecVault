package com.secvault.android.secvault.cryptography;

import java.util.HashMap;

public class Encode {

    //https://www.dreamincode.net/forums/topic/27950-steganography/
    //This works because I checked the hex via HxD, got the last bit from the first 8 and last 8 bytes and it does convert into the word I encoded using LSB

    private static final String TAG = "Encode class: ";

    private int bytePositioningInPassedByteArray;
    private int bit;
    private Integer[] binaryOfAscii;
    private int asciiCode;
    private HashMap<Integer,Integer[]> asciiBinaryHashMap = new HashMap<>();

    public void getBinaryOfAscii(byte[] passedInByteArray){

        for (bytePositioningInPassedByteArray = 0; bytePositioningInPassedByteArray < passedInByteArray.length;
             bytePositioningInPassedByteArray++){

            asciiCode = passedInByteArray[bytePositioningInPassedByteArray];
            asciiToBinary(bytePositioningInPassedByteArray);

        }
    }

    private void asciiToBinary(int bytePositioningInPassedByteArray){  //I WANTED TO USE ASCIICODE AS THE KEY BUT HASHMAP DOES NOT ALLOW DUPLICATE VALUES
        int bitPositioning = 0;
         binaryOfAscii = new Integer[8]; //byte = 8 bit

        for (bit = 7; bit >= 0; bit--) {
            binaryOfAscii[bitPositioning] = (asciiCode >>> bit) & 1;
            bitPositioning++;
        }
        addAsciiBinaryToHashMap(bytePositioningInPassedByteArray);

    }

    private void addAsciiBinaryToHashMap(int key){
        asciiBinaryHashMap.put(key, binaryOfAscii);
    }

    public HashMap<Integer,Integer[]> returnBinaryHashMap(){
        return asciiBinaryHashMap;
    }

}
