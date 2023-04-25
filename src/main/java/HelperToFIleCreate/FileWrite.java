/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperToFIleCreate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author rasel
 */
public class FileWrite {

    public static void fileWrite(String filePath, String content, String fileName) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fw = new FileWriter(filePath + "\\" + fileName);
            bw = new BufferedWriter(fw);
            bw.write(content);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }

                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                System.err.format("IOException: %s%n", ex);
            }
        }
    }
}
