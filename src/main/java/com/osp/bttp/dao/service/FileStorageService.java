package com.osp.bttp.dao.service;

import com.osp.bttp.common.exception.InternalException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    void init();

    String uploadFile(String dir, MultipartFile multipartFile) throws InternalException;

    boolean deleteFile(String filePath) throws InternalException;

    byte[] download(String pathFile) throws InternalException;
}