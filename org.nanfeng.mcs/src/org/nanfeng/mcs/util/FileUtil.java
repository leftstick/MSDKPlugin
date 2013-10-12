package org.nanfeng.mcs.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.IOUtils;

/**
 * Static utility methods for manipulating Files
 */
public class FileUtil {

    /**
     * Create folder with specified path
     * 
     * @param path
     */
    public static void createFolder(File path) {
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    /**
     * Write the given string in the specified file with
     * <code>Charset</code> to "UTF-8".
     * 
     * @param str the given string will be written to a file.
     * @param target the file specified to be written
     */
    public static void writeInFile(String str, File target) {
        createFolder(target.getParentFile());
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(target);
            osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(str);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copy a file from inside plugin to an external place out of
     * plugin
     * 
     * @param inputFile
     * @param outputFile
     * @return true if success to copy. Otherwise false
     */
    public static boolean copyFileFromInsideToOutside(String inputFile, String outputFile) {
        InputStream input = FileUtil.class.getResourceAsStream(inputFile);
        try {
            File outputParent = new File(outputFile).getParentFile();
            if (!outputParent.exists()) {
                outputParent.mkdir();
            }
            OutputStream output = new FileOutputStream(outputFile);
            IOUtils.copy(input, output);
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * Unzip a given source file into specific destFolder
     * 
     * @param source source file
     * @param destFolder destination folder
     * @return true if success to unzip. Otherwise false
     */
    public static boolean unZipFile(String source, String destFolder) {
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destFolder);
        } catch (ZipException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
