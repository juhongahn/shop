package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws IOException {

        UUID uuid = UUID.randomUUID();

        // 원본 파일이름에서 확장자를 뽑아낸다.
        String extention = originalFileName.substring(originalFileName.lastIndexOf("."));

        // UUID를 통해 고유한 ID를 발급, 확장자를 붙혀 새로운 파일명을 만든다.
        String savedFileName = uuid.toString() + extention;

        // 로컬에 저장할 위치
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // 파일의 경로를 통해서 스트림을 만들고 byte배열을 파라미터로 파일을 쓴다.
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath) {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제했습니다: " + deleteFile);

        }else {
            log.info("파일이 존재하지 않습니다: " + deleteFile);
        }

    }
}
