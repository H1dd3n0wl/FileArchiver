package com.company;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;

public class CipheringControl implements iEncryptDecrypt {
    private final File file;
    private final File filePwd;
    private SecretKeySpec key = currentKey;

    enum processingMode {
        ENCRYPTING,
        DECRYPTING
    }

    enum encryptingMode {
        GENERATING_KEY,
        USING_EXISTED_KEY
    }

    private final processingMode curProcMode;
    private final encryptingMode curEncMode;

    public CipheringControl(File file, processingMode mode) {
        this.file = file;
        this.curProcMode = mode;
        this.curEncMode = encryptingMode.USING_EXISTED_KEY;
        this.filePwd = createPwdFile();
    }

    public CipheringControl(File file, processingMode procMode, encryptingMode encMode) {
        this.file = file;
        this.curProcMode = procMode;
        this.curEncMode = encMode;
        this.filePwd = createPwdFile();
    }

    private File createPwdFile() {
        String curName = file.getName();
        String[] curNameToParts = curName.split("\\.");
        String newName = curNameToParts[0] + "pwd." + curNameToParts[1];
        String newPath = file.getPath().replace(curName, newName);
        File newFile = new File(newPath);
        try {
            if (newFile.exists() || newFile.createNewFile()) {
                return newFile;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void pushKey() {
        try {
            if (filePwd.delete() && filePwd.createNewFile()) {
                TextWriter writer = new TextWriter(filePwd);
                String myKey = new String(key.getEncoded());
                writer.writeInfo(myKey);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private SecretKeySpec getKey() {
        try {
            TextReader reader = new TextReader(filePwd);
            return new SecretKeySpec(reader.readInfo().getBytes(), "AES");
        } catch (Exception e) {
            System.out.println("You haven't initialized any key! It will be initialized by default random key.");
            key = iEncryptDecrypt.generateKey();
            pushKey();
            return key;
        }
    }

    public void processFinal() {
        switch (curProcMode) {
            case ENCRYPTING -> {
                if (curEncMode == encryptingMode.GENERATING_KEY) {
                    pushKey();
                } else {
                    key = getKey();
                }
                iEncryptDecrypt.encrypt(file, key);
            }
            case DECRYPTING -> {
                key = getKey();
                iEncryptDecrypt.decrypt(file, key);
            }
        }
    }
}
