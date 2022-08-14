package com.eeit45team2.lungspringbootversion.backend.member.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;

@Service
public class CommonFunction {

    @Autowired
    ServletContext ctx;

    public byte[] blobToByteArray(Blob blob) {
        byte[] result = null;
        try (InputStream is = blob.getBinaryStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            byte[] b = new byte[819200];
            int len = 0;
            while ((len = is.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            result = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /* 專案下路徑 */
    public byte[] getProjectFileToByteArray(String path){
        byte[] result = null;
        Path projectPath = Paths.get(path);
        if (!Files.exists(projectPath)) {
            try {
                Files.createDirectories(projectPath);
            } catch (IOException e) {
                System.out.println("創建專案下會員大頭照資料夾時，發生錯誤");
                e.printStackTrace();
            }
        }
        try (InputStream is = new FileInputStream(new File(path));
             ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            byte[] b = new byte[819200];
            int len = 0;
            while ((len = is.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            result = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /* tomcat下路徑 */
    //有問題: 每次都產生暫時目錄
    public byte[] getServerFileToByteArray(String path) {
        byte[] result = null;
        try (InputStream is = ctx.getResourceAsStream(path);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            byte[] b = new byte[819200];
            int len = 0;
            while ((len = is.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            result = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
