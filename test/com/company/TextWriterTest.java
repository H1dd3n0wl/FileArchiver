package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TextWriterTest {

    @Test
    void testWriteInfo() {
        File file = new File("/home/andrey/IdeaProjects/practice/test/test1.txt");
        TextWriter writer = new TextWriter(file);
        String expected = """
                testing test to encrypt decrypt or
                something else
                bla bla econimics idk it's strange
                and of course exrpression: 5-(6-7)*3
                yoooo""";
        writer.writeInfo(expected);
        TextReader reader = new TextReader(file);
        Assertions.assertEquals(expected, reader.readInfo());
    }
}