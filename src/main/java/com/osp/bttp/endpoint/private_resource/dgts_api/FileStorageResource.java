package com.osp.bttp.endpoint.private_resource.dgts_api;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.exception.InternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(FileStorageResource.FILE_STORAGE_RESOURCE)
public interface FileStorageResource {

    String FILE_STORAGE_RESOURCE = Constants.API_VERSION1 + "/file-storage";

    @GetMapping
    ResponseEntity<byte[]> downloadFile(@RequestParam(name = "file-path") String pathFile,
                                        @RequestParam(name = "file-name") String fileName) throws InternalException;
}
