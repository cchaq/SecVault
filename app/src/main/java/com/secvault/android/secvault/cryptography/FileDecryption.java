package com.secvault.android.secvault.cryptography;

import android.util.Log;

import com.secvault.android.secvault.Folders;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class FileDecryption {

    private static final String TAG = "File Decryption class: ";
    private static final String PASSWORD = "epoch";
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_LENGTH = 128;
    private static final int ITERATION = 65536;

    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private OutputStream outFile;
    private byte[] salt;
    private SecretKey secret;
    private byte[] iv;


    private void decryptFile() throws Exception {

        this.createCipher();

        RandomAccessFile fileToDecrypt = new RandomAccessFile(Folders.EXTERNAL_FOLDER_DIR + "/Robo/encrypted.jpg", "rw");
        outFile  = new FileOutputStream(Folders.EXTERNAL_FOLDER_DIR + "/Robo/chrono.jpg");

        try {

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

        this.makeSalt();
        this.makeSecretKey(PASSWORD);
        this.makeCipher();
        this.makeIV();
        this.makeCipherForDecrypt();
    }

    private void makeSalt() {
        salt = new byte[8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

    }

    private void makeSecretKey(String key) throws  Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(key.toCharArray(),salt,ITERATION,KEY_LENGTH );
        SecretKey tmp = factory.generateSecret(spec);
        secret = new SecretKeySpec(tmp.getEncoded(),ALGORITHM);
    }

    private void makeCipher() throws Exception{
        encryptCipher = Cipher.getInstance(TRANSFORMATION);
        encryptCipher.init(Cipher.ENCRYPT_MODE,secret);

    }

    private void makeIV()throws Exception{
        AlgorithmParameters params = encryptCipher.getParameters();
        iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    }

    private void makeCipherForDecrypt() throws Exception{
        decryptCipher = Cipher.getInstance(TRANSFORMATION);
        decryptCipher.init(Cipher.DECRYPT_MODE,secret, new IvParameterSpec(iv));
    }

}
