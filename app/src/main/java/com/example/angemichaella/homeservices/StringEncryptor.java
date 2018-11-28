package com.example.angemichaella.homeservices;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;

public class StringEncryptor {

    public static String encrypt(String s){
        String encryptedString = "ITDIDNTWORK!";

        try{
            byte[] byteString = s.getBytes("UTF-8");
            encryptedString = Sha1FromByteArray(byteString);
        }catch(UnsupportedEncodingException e){
            //yikes!
        }
        return encryptedString;
    }

    private static String Sha1FromByteArray(byte[] toEncode) {
        try{
            MessageDigest m = MessageDigest.getInstance("SHA-1");
            return Base64.encodeToString(m.digest(toEncode), Base64.DEFAULT);
        }catch(NoSuchAlgorithmException e){
            //yikes
        }
        return null;
    }
}
