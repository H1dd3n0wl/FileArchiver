package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TextReaderTest {

    @Test
    void testReadInfo() {
        File file = new File("/home/andrey/IdeaProjects/practice/test/test.txt");
        String expected = "Some text to be read and tested";
        TextReader reader = new TextReader(file);
        Assertions.assertEquals(expected, reader.readInfo());
    }
}