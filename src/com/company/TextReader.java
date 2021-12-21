package com.company;

import java.io.*;
import java.util.zip.ZipInputStream;

public class TextReader implements iRead {

    private final File file;

    public TextReader(File file) {
        this.file = file;
    }

    @Override
    public String readInfo() {
        try (FileReader reader = new FileReader(file)) {
            StringBuilder res = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                res.append(Character.toString(c));
            }
            return res.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String readZip() {
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
            StringBuilder res = new StringBuilder();
            int c;
            while (zin.getNextEntry() != null) {
                while ((c = zin.read()) != -1) {
                    res.append(Character.toString((c)));
                }
            }
            return res.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
