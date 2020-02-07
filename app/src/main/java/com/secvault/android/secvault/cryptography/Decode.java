package com.secvault.android.secvault.cryptography;

import java.util.HashMap;

public class Decode {

    private static final String TAG  = "Decode class : ";
    private Integer[] binaryFromHashMapKey = new Integer[8];
    private String binaryIntToBinaryChar;
    private char ascii;
    private int binaryCharToInt;
    private String finalString = "";
    private byte[] bytesToDecrypt;
    private Integer[] binaryOfHex;
    private HashMap<Integer,Integer[]> binaryOfHexHashMap = new HashMap<>();
    private HashMap<Integer,Integer[]> binaryOfBytesForLsbDecode = new HashMap<>();

    private DecodeLSB decodeLSBClass = new DecodeLSB();

    public void passBinary(HashMap binaryHashMap){

        for(int gettingValueFromKey = 0; gettingValueFromKey < binaryHashMap.size(); gettingValueFromKey++){

            binaryFromHashMapKey = (Integer[]) binaryHashMap.get(gettingValueFromKey);
            binaryOfBytesForLsbDecode.put(gettingValueFromKey,binaryFromHashMapKey);
            binaryToChar();
        }

       // decodeLSBClass = new DecodeLSB(binaryOfBytesForLsbDecode);
       // DecodeLSB decodeLSBClass = new DecodeLSB(binaryOfBytesForLsbDecode);
        decodeLSBClass.passLsbHashMap(binaryOfBytesForLsbDecode);
    }

    public void passEncryptedTextInByteArray(byte[] encryptedTextInBytes){
        bytesToDecrypt = encryptedTextInBytes;
        bytesToBinary();
    }

    private void bytesToBinary(){

        for(int increasingIndex = 0; increasingIndex < bytesToDecrypt.length; increasingIndex++){

            int bitPositioning = 0;
            binaryOfHex = new Integer[8];

            for(int bit =7 ; bit >= 0; bit--){
                binaryOfHex[bitPositioning] = ((bytesToDecrypt[increasingIndex] >>> bit) & 1);  //index at 7 is the starting point when doing binary to hex
                bitPositioning ++;
            }
            binaryOfHexHashMap.put(increasingIndex,binaryOfHex);
        }
        passBinary(binaryOfHexHashMap);
    }

    private void binaryToChar(){

        binaryIntToBinaryChar = "";

        for (Integer gettingBinaryInValueFromKey : binaryFromHashMapKey) {

            binaryIntToBinaryChar += gettingBinaryInValueFromKey;
        }

        //Log.i(TAG, binaryIntToBinaryChar);
        binaryCharToInt = Integer.parseInt(binaryIntToBinaryChar,2);

        //Below is for testing/checking the binary
        ascii = (char) binaryCharToInt;

    }

    public String returnFineName(){
        return decodeLSBClass.returnFinalString();
    }

}
