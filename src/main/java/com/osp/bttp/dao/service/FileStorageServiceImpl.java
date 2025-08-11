package com.osp.bttp.dao.service;

import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${application.storage.bucket}")
    private String storageBucket;

    @Override
    public void init() {
        try {
            Path rootLocation = Paths.get(storageBucket);
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
                log.info("Container {} already created", storageBucket);
            }
        } catch (IOException e) {
            log.error("Error creating bucket to store file: {}", storageBucket);
        }
    }

    @Override
    public String uploadFile(String dir, MultipartFile multipartFile) throws InternalException {
        if (dir == null || multipartFile == null || !StringUtils.hasText(multipartFile.getOriginalFilename())) {
            throw new InternalException("Invalid request");
        }

        if (StringUtils.hasText(dir)) {
            while (dir.trim().startsWith("/")) {
                dir = dir.replaceFirst("/", "");
            }
        }

        try {
            return FileUtil.uploadFile(
                    storageBucket,
                    dir,
                    System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename(),
                    multipartFile.getInputStream()
            );
        } catch (Exception ex) {
            throw new InternalException("Error uploading file", ex);
        }
    }

    @Override
    public boolean deleteFile(String filePath) throws InternalException {

        if (!StringUtils.hasText(filePath)) {
            throw new InternalException("Invalid file path");
        }
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            log.warn("File {} does not exist", filePath);
            return false;
        }
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new InternalException("Error deleting file", e);
        }
        return true;
    }

    @Override
    public byte[] download(String pathFile) throws InternalException {
        try {
            return FileUtil.readFile(pathFile);
        } catch (Exception ex) {
            throw new InternalException("File not found: " + pathFile);
        }
    }
}