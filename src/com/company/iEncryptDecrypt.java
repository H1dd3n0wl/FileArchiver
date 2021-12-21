package com.company;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public interface iEncryptDecrypt {

    SecretKeySpec currentKey = generateKey();
    Cipher currentCipher = getCipherInstance();
    String symmetricAlgo = "AES/ECB/PKCS5Padding";

    private static Cipher getCipherInstance() {
        try {
            return Cipher.getInstance(symmetricAlgo);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            return null;
        }
    }

    static SecretKeySpec generateKey() {
        String pattern = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        Random random = new Random();
        int length = 16;
        char[] arr = new char[length];
        for (int i = 0; i < 16; ++i) {
            arr[i] = pattern.charAt(random.nextInt(0, pattern.length()));
        }
        String rndStr = new String(arr);
        return new SecretKeySpec(rndStr.getBytes(), "AES");
    }

    static void encrypt(File file, SecretKeySpec key) {
        TextReader reader = new TextReader(file);
        byte[] plainText = reader.readInfo().getBytes();
        try {
            assert currentCipher != null;
            currentCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] text = currentCipher.doFinal(plainText);
            TextWriter writer = new TextWriter(file);
            String finalInfo = Base64.getEncoder().encodeToString(text);
            writer.writeInfo(finalInfo);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println(e.getMessage());
        }
    }

    static void decrypt(File file, SecretKeySpec key) {
        TextReader reader = new TextReader(file);
        byte[] plainText = Base64.getDecoder().decode(reader.readInfo());
        try {
            assert currentCipher != null;
            currentCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] text = currentCipher.doFinal(plainText);
            TextWriter writer = new TextWriter(file);
            String finalInfo = new String(text);
            writer.writeInfo(finalInfo);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println(e.getMessage());
        }
    }
}
