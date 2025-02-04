package com.secvault.android.secvault.cryptography;

import android.util.Log;

import com.secvault.android.secvault.Folders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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


// https://javapapers.com/java/java-file-encryption-decryption-using-aes-password-based-encryption-pbe/

public class FileEncryption {

   // private static final String PASSWORD = "epoch";
    private static final String saltString = "Lavos";
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_LENGTH = 128;
    private static final int ITERATION = 65536;
    private static final String fileExt = ".des";

    private Cipher encryptCipher;
    private OutputStream outFile;

    private SecretKey secret;
    private IvParameterSpec iv;
    private  byte[] bytesToEncrypt;
    private String filePath;
    private String PASSWORD;

    public static final String TAG = "File encryption class :";

    public FileEncryption(String fileToEncryptPath, RandomAccessFile fileToEncryptData, String password) {

        filePath = fileToEncryptPath;
        this.PASSWORD = password;

        try {
                FileInputStream fileToEncrypt = new FileInputStream(fileToEncryptPath);
                bytesToEncrypt = new byte[(int) fileToEncryptData.length()];
                fileToEncrypt.read(bytesToEncrypt);
                this.encryptFile(PASSWORD);

        } catch (Exception e)
        {
                e.printStackTrace();
        }

    }

    private void encryptFile(String key) throws Exception {

            this.makeSecretKey(key);
            this.makeIV();
            this.makeCipher();
            this.makeOutFile();

            try {
                    byte[] cipheredBytes = encryptCipher.doFinal(bytesToEncrypt);
                    outFile.write(cipheredBytes);

            }
            catch (Exception e){
                Log.i(TAG,e.toString());
            }
            finally {
                outFile.close();
            }

        }

        private void makeOutFile(){

            try {

                outFile =  new FileOutputStream(Folders.ENCRYPTED_FOLDER + returnFileNameOnly() + fileExt);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        private String returnFileNameOnly(){
            String[] fileNameSplitBySlash = filePath.split("/");
            String[] fileNameSplitByADot = fileNameSplitBySlash[fileNameSplitBySlash.length -1].split("[.]");
            return fileNameSplitByADot[0];

        }


        private void makeSecretKey(String key) throws  Exception {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(key.toCharArray(),saltString.getBytes(),ITERATION,KEY_LENGTH );
            SecretKey tmp = factory.generateSecret(spec);
            secret = new SecretKeySpec(tmp.getEncoded(),ALGORITHM);
        }


        private void makeCipher() throws Exception{
            encryptCipher = Cipher.getInstance(TRANSFORMATION);
            encryptCipher.init(Cipher.ENCRYPT_MODE,secret,iv);

        }

        private void makeIV(){
            byte[] ivspec = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            iv = new IvParameterSpec(ivspec);

        }

    }





