package com.secvault.android.secvault.cryptography;

import java.util.HashMap;

public class Encode {

    //https://www.dreamincode.net/forums/topic/27950-steganography/

    private static final String TAG = "Encode class: ";

    private int bytePositioningInPassedByteArray;
    private int bit;
    private Integer[] binaryOfAscii;
    private int asciiCode;
    private HashMap<Integer,Integer[]> asciiBinaryHashMap = new HashMap<>();

    private Decode decode = new Decode();
    private Encryption encryptionClass = new Encryption();

    public void getBinaryOfAscii(byte[] passedInByteArray){

        for (bytePositioningInPassedByteArray = 0; bytePositioningInPassedByteArray < passedInByteArray.length;
             bytePositioningInPassedByteArray++){

            asciiCode = passedInByteArray[bytePositioningInPassedByteArray];
            asciiToBinary(bytePositioningInPassedByteArray);

        }
        callDecode(); //TODO only used to test it, but this will be called at a different time
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

    private void encryptBytesIntoImage(){

    }


    private void callDecode(){
        decode.passBinary(asciiBinaryHashMap);
    }
}
