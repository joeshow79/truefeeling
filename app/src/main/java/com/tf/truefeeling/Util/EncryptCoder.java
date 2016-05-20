package com.tf.truefeeling.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptCoder {
    private static final String TAG = "EncryptCoder";

    private static final String KEY_MD5 = "MD5";

    public static String encryptMD5(String s, boolean lowerCase) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        byte[] input = s.getBytes();

        try {
            MessageDigest mdDigest = MessageDigest.getInstance(KEY_MD5);
            mdDigest.update(input);
            byte[] md = mdDigest.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >>> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            String result = new String(str);
            if(lowerCase){  //for keep same with IOS. all md5 result is lowercase char.
                result = result.toLowerCase();
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG," encrypt string fail");
        }

        return null;
    }
}
