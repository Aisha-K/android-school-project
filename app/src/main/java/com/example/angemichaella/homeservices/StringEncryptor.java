package com.example.angemichaella.homeservices;

// this is the method that encrypts passwords!!

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;


public class StringEncryptor {

    public static String encrypt(String s){
            String encrytedString = "";

            try{
                byte[] byteString = s.getBytes("UTF-8");
                encrytedString = Sha1FromByteArray(byteString);
            } catch (UnsupportedEncodingException e){

            }
            return encrytedString;
    }


    private static String Sha1FromByteArray(byte[] toEncode){
        try{
            MessageDigest m = MessageDigest.getInstance("SHA-1");
            return Base64.encodeToString(m.digest(toEncode), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e){

        }
        return null;
    }
}
