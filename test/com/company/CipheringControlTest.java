package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class CipheringControlTest {
    private static final File file = new File("/home/andrey/Andrey/test.txt");

    @Test
    public void testProcessFinal() {
        File expected = file;
        CipheringControl c1 = new CipheringControl(expected, CipheringControl.processingMode.ENCRYPTING);
        c1.processFinal();
        CipheringControl c2 = new CipheringControl(expected, CipheringControl.processingMode.DECRYPTING);
        c2.processFinal();
        Assertions.assertEquals(expected, file);

        c1 = new CipheringControl(expected,
                CipheringControl.processingMode.ENCRYPTING,
                CipheringControl.encryptingMode.GENERATING_KEY);
        c1.processFinal();
        c2.processFinal();
        Assertions.assertEquals(expected, file);
    }
}