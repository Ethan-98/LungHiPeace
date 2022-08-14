package com.eeit45team2.lungspringbootversion.backend.activity.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
     
    public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        }      
    }
}




//如果上傳目錄為/static/images/upload/，則可以如下獲取：
//File upload = new File(path.getAbsolutePath(),"static/images/upload/");
//if(!upload.exists()) upload.mkdirs();
//System.out.println("upload url:"+upload.getAbsolutePath());
//在開發測試模式時，得到的地址為：{專案跟目錄}/target/static/images/upload/


