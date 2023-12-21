package com.shanebeestudios.backup.utils;

import com.shanebeestudios.backup.ServerBackup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// This code was found here:
// https://www.digitalocean.com/community/tutorials/java-zip-file-folder-example
// Some code has been edited
public class ZipFiles {

    private final List<String> filesListInDir = new ArrayList<>();

    @SuppressWarnings("CallToPrintStackTrace")
    public void zipDirectory(File dir, String zipDirName) {
        try {
            populateFilesList(dir);
            //now zip files one by one
            //create ZipOutputStream to write to the zip file
            FileOutputStream fos = new FileOutputStream(zipDirName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (String filePath : filesListInDir) {
                if (filePath.contains("./libraries/") ||
                        filePath.contains(".versions/") ||
                        filePath.endsWith("server-backup.zip") ||
                        filePath.endsWith(".tmp")) {
                    ServerBackup.print("&eSkipping " + filePath);
                    continue;
                }
                ServerBackup.print("Zipping " + filePath);
                //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1));
                zos.putNextEntry(ze);
                //read the file and write to ZipOutputStream
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateFilesList(File dir) throws IOException {
        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else populateFilesList(file);
        }
    }

}
