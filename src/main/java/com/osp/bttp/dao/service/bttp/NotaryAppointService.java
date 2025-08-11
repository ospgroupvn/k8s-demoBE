package com.osp.bttp.dao.service.bttp;

import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.db3.NotaryAppointCreateDto;
import com.osp.bttp.dao.model.dto.db3.NotaryDismissedCreateDto;
import com.osp.bttp.dao.model.dto.db3.NotaryReAppointCreateDto;
import com.osp.bttp.dao.model.entity.db1.NotaryDismissed;
import com.osp.bttp.dao.model.entity.db3.NotaryAppoint;
import com.osp.bttp.dao.model.mview.bttp.NotaryAppointResponse;
import com.osp.bttp.dao.model.mview.bttp.StatusBeforeDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotaryAppointService {
    NotaryAppoint addNotaryAppoint(NotaryAppointCreateDto notaryAppointCreate);

    NotaryAppoint editNotaryAppoint(Long id, NotaryAppointCreateDto notaryAppointCreate);

    void deleteNotaryAppoint(Long id);

    void deleteByNotaryId(Long notaryId);

//    void addNotaryDismissed(NotaryDismissedCreateDto notaryDismissedCreateDto);
//
//    void addNotaryReAppoint(NotaryReAppointCreateDto notaryReAppointCreate);

    List<NotaryAppointResponse> getProcessAppoints(Long notaryId);

    String uploadFileToDocument(Long idDocument, MultipartFile multipartFile) throws InternalException;

    ResponseEntity<byte[]> downloadFile(String pathFile, String fileName) throws InternalException;

    boolean deleteFile(String filePath);


}
