package com.secvault.android.secvault.cryptography;

import android.util.Log;

import com.secvault.android.secvault.Folders;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class FileDecryption {

    private static final String TAG = "File Decryption class: ";
    //private static final String PASSWORD = "epoch";
    private static final String saltString = "Lavos";
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_LENGTH = 128;
    private static final int ITERATION = 65536;

    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private OutputStream outFile;
    private SecretKey secret;
    private IvParameterSpec iv;
    private RandomAccessFile fileToDecrypt;
    private String filePath;
    private String PASSWORD;


    public void decryptFile(String fileToDecryptPath, RandomAccessFile fileRAFToDecrypt, String password) throws Exception {

        this.PASSWORD = password;
        this.createCipher();
        this.filePath = fileToDecryptPath;

        try {

            fileToDecrypt = fileRAFToDecrypt;
            outFile  = new FileOutputStream(Folders.DECRYPTED_FOLDER + returnFileNameOnly());

            byte[] bytesToDecrypt = new byte[(int) fileToDecrypt.length()];
            fileToDecrypt.read(bytesToDecrypt);
            byte[] decipheredBytes = decryptCipher.doFinal(bytesToDecrypt);
            outFile.write(decipheredBytes);

        }catch (Exception e){
            Log.i(TAG,e.toString());

        }finally {
            fileToDecrypt.close();
            outFile.close();
        }

    }

    private void createCipher()throws Exception {

        this.makeSecretKey(PASSWORD);
        this.makeCipher();
        this.makeIV();
        this.makeCipherForDecrypt();
    }

    private void makeSecretKey(String key) throws  Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(key.toCharArray(),saltString.getBytes(),ITERATION,KEY_LENGTH );
        SecretKey tmp = factory.generateSecret(spec);
        secret = new SecretKeySpec(tmp.getEncoded(),ALGORITHM);
    }

    private void makeCipher() throws Exception{
        encryptCipher = Cipher.getInstance(TRANSFORMATION);
        encryptCipher.init(Cipher.ENCRYPT_MODE,secret);

    }

    private void makeIV(){
        byte[] ivspec = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        iv = new IvParameterSpec(ivspec);

    }

    private void makeCipherForDecrypt() throws Exception{
        decryptCipher = Cipher.getInstance(TRANSFORMATION);
        decryptCipher.init(Cipher.DECRYPT_MODE,secret, iv);
    }


    private String returnFileNameOnly(){
        String[] fileNameSplitBySlash = filePath.split("/");
        String[] fileNameSplitByADot = fileNameSplitBySlash[fileNameSplitBySlash.length -1].split("[.]");
        return fileNameSplitByADot[0];

    }


}
