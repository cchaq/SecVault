package com.secvault.android.secvault.cryptography;

import android.util.Log;

import java.util.HashMap;

public class Decode {

    private static final String TAG  = "Decode class : ";
    private Integer[] binaryFromHashMapKey = new Integer[8];
    private String binaryIntToBinaryChar;
    private char ascii;
    private int binaryCharToInt;
    private String finalString = "";

    public void passBinary(HashMap binaryHashMap){

        for(int gettingValueFromKey = 0; gettingValueFromKey < binaryHashMap.size(); gettingValueFromKey++){

            binaryFromHashMapKey = (Integer[]) binaryHashMap.get(gettingValueFromKey);
            binaryToChar();
        }
    }

    private void binaryToChar(){

        binaryIntToBinaryChar = "";

        for (Integer gettingBinaryInValueFromKey : binaryFromHashMapKey) {

            binaryIntToBinaryChar += gettingBinaryInValueFromKey;
        }

        Log.i(TAG, binaryIntToBinaryChar);
        binaryCharToInt = Integer.parseInt(binaryIntToBinaryChar,2);
        ascii = (char) binaryCharToInt;

        Log.i(TAG, String.valueOf(binaryCharToInt));
        makeFinalString(ascii);
    }


    private void makeFinalString(char character){
        finalString += character;
        Log.i(TAG, finalString);
    }
}
