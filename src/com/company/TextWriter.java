package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TextWriter implements iWrite {

    private final File file;

    public TextWriter(File file) {
        this.file = file;
    }

    @Override
    public void writeInfo() {
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(info);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void writeInfo(String info) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(info);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void writeZip() {
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        try (ZipOutputStream zOut = new ZipOutputStream(new FileOutputStream(file))) {
            ZipEntry entry = new ZipEntry("newFile.txt");
            zOut.putNextEntry(entry);
            byte[] buff = info.getBytes(StandardCharsets.UTF_8);
            zOut.write(buff);
            zOut.closeEntry();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

