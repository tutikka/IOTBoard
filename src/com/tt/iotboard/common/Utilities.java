package com.tt.iotboard.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class Utilities {

    public static String HmacSHA256(String secret, String toSign) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] signed = mac.doFinal(toSign.getBytes());
            StringBuffer hex = new StringBuffer();
            for (int i = 0; i < signed.length; i++) {
                String c = Integer.toHexString(0xFF & signed[i]);
                if (c.length() == 1) {
                    hex.append("0");
                }
                hex.append(c);
            }
            return (hex.toString());
        } catch (Exception e) {
            return (null);
        }
    }

    public static String MD5(String toSign) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(toSign.getBytes("UTF-8"));
            byte[] digest = messageDigest.digest();
            StringBuffer hex = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                String c = Integer.toHexString(0xFF & digest[i]);
                if (c.length() == 1) {
                    hex.append("0");
                }
                hex.append(c);
            }
            return (hex.toString());
        } catch (Exception e) {
            return (null);
        }
    }

}
