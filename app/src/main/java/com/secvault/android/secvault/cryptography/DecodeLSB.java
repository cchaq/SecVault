package com.secvault.android.secvault.cryptography;

import android.util.Log;

import java.util.HashMap;

public class DecodeLSB {

    private final static String TAG = "Decode LSB class : ";
    private HashMap<Integer,Integer[]> hashMapToGetLSB = new HashMap<>();
    private Integer[] hexBinaryToGetLSB = new Integer[8];
    private HashMap<Integer,String> LSBBinary = new HashMap<>();
    private String LSBInStringFormat = "";
    private String finalString = "";
    private char ascii;
    private int binaryCharToInt;

/*
    public DecodeLSB(HashMap binaryHashMapToGetLsb){
        hashMapToGetLSB = binaryHashMapToGetLsb;
        getBinaryLSB();
    }*/
    //Only for practice

    public void passLsbHashMap(HashMap binaryHashMapTogetLsb){
        hashMapToGetLSB = binaryHashMapTogetLsb;
        getBinaryLSB();
    }

    private void getBinaryLSB(){

        int hashMapIndex = 0;

        for(int increasingIndex = 0; increasingIndex < hashMapToGetLSB.size(); increasingIndex++){
          hexBinaryToGetLSB = hashMapToGetLSB.get(increasingIndex);
          LSBInStringFormat += String.valueOf(hexBinaryToGetLSB[7]);
        //  Log.i(TAG, LSBInStringFormat + " 7th Index");

          if (LSBInStringFormat.length() == 8){
              LSBBinary.put(hashMapIndex, LSBInStringFormat);
              hashMapIndex++;
              binaryToChar(LSBInStringFormat);
              LSBInStringFormat = "";
          }
        }
    }

    private void binaryToChar(String LSBString){

      //  Log.i(TAG, LSBString);
        binaryCharToInt = Integer.parseInt(LSBString,2);

        ascii = (char) binaryCharToInt;
        makeFinalString(ascii);
    }

    private void makeFinalString(char character){
        finalString += character;
        Log.i(TAG, finalString);
    }

    public String returnFinalString(){
        return finalString;
    }
}
