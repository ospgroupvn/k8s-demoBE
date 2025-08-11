package com.osp.bttp.endpoint.private_resource.dgts_api;

import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FileStorageController implements FileStorageResource {

    private final FileStorageService fileStorageService;

    @Override
    public ResponseEntity<byte[]> downloadFile(String pathFile, String fileName) throws InternalException {

        byte[] data = fileStorageService.download(pathFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(data);
    }
}
