package com.idolmedia.yzymanager.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtils {

    public static final String key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDA3TjDhQW7okNMYZsO/c4OFma0JoH9wCrkpzq2AjcTr+qKleyrCkwQxBkyx2zdH0pkVXojZ61WhWsFfWGftxX+vawRYYtx7ZivtwN2O9cLwse+LgxvIqv+ntlr3Lhiq8Tw5/Q+WSnWj4FEI7o0Z5sMHNQX2xkg6yqBUZRXP9h2XQIDAQAB";

    public static String encrypt(String plainTextData) {

        Cipher cipher;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(key, Base64.NO_WRAP));
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeToString(cipher.doFinal(plainTextData.getBytes()), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
