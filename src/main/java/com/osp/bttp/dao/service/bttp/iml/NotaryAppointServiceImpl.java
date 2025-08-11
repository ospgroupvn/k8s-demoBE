package com.osp.bttp.dao.service.bttp.iml;

import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.db3.NotaryAppointCreateDto;
import com.osp.bttp.dao.model.entity.db3.*;
import com.osp.bttp.dao.model.mview.bttp.NotaryAppointResponse;
import com.osp.bttp.dao.repository.bttp.*;
import com.osp.bttp.dao.service.FileStorageService;
import com.osp.bttp.dao.service.bttp.NotaryAppointService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class NotaryAppointServiceImpl implements NotaryAppointService {
    @Autowired
    private NotaryInfoRepository notaryInfoRepository;

    @Autowired
    private DmDocumentRepository dmDocumentRepository;

    @Autowired
    private NotaryAppointRepository notaryAppointRepository;

    @Autowired
    private FileStorageService fileStorageService;


    @Override
    @Transactional(rollbackOn = Exception.class)
    public NotaryAppoint addNotaryAppoint(NotaryAppointCreateDto notaryAppointCreate) {
        Optional<NotaryInfo> notaryInfExits = notaryInfoRepository.findById(notaryAppointCreate.getNotaryInfoId());
        if (notaryInfExits.isEmpty()) {
            throw new CustomException("Không tìm thấy công chứng viên id " + notaryAppointCreate.getNotaryInfoId());
        }
        notaryInfExits.get().setNote("");
        notaryInfoRepository.save(notaryInfExits.get());
//       List<NotaryInfo> notaryInfos =  notaryInfoRepository.findAllWithAnyAppointmentOrDismissal(notaryAppointCreate.getNotaryInfoId());
//        if(!notaryInfos.isEmpty()){
//            throw new CustomException("Trạng thái không hợp lệ");
//        }
        DmDocument doc = new DmDocument();
        doc.setActive(0L);
        if (StringUtils.hasText(notaryAppointCreate.getSigner())) {
            doc.setSigner(notaryAppointCreate.getSigner());
        }
        Date decisionDate = notaryAppointCreate.getDateSign();
        Date effectiveDate = notaryAppointCreate.getEffectiveDate();
        if (effectiveDate == null && notaryAppointCreate.getType() == 2) {
            throw new CustomException("Ngày hiệu lực bắt buộc khi loại quyết định!");
        }
        if (effectiveDate != null) {
            doc.setEffectiveDate(effectiveDate);
        }
        if (decisionDate == null && notaryAppointCreate.getType() == 2) {
            throw new CustomException("Ngày đề nghị bắt buộc khi loại quyết định!");
        }
        if (decisionDate != null) {
            doc.setDateSign(decisionDate);
        }
        if (decisionDate != null && effectiveDate != null) {
            if (effectiveDate.compareTo(decisionDate) < 0) {
                throw new CustomException("Ngày không hợp lệ!");
            }
        }
 //       String dispatchCode = notaryAppointCreate.getDispatchCode().trim();
//        List<DmDocument> documentDupDispatchCode = dmDocumentRepository.findByDispatchCode(dispatchCode);
//        if (!documentDupDispatchCode.isEmpty()) {
//            throw new CustomException("Trùng code document !");
//        }
        doc.setDispatchCode(notaryAppointCreate.getDispatchCode());
        if (StringUtils.hasText(notaryAppointCreate.getNote())) {
            doc.setNote(notaryAppointCreate.getNote());
        }
        doc = dmDocumentRepository.save(doc);
        NotaryAppoint notaryAppoint = new NotaryAppoint();
        notaryAppoint.setKind(notaryAppointCreate.getKind());
        notaryAppoint.setNotaryInfoId(notaryAppointCreate.getNotaryInfoId());
        notaryAppoint.setActive(0L);
        notaryAppoint.setTypeAppoint(notaryAppointCreate.getType());
        notaryAppoint.setDocumentId(doc.getId());
        if (StringUtils.hasText(notaryAppointCreate.getReason())) {
            notaryAppoint.setReason(notaryAppointCreate.getReason());
        }
        return notaryAppointRepository.save(notaryAppoint);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NotaryAppoint editNotaryAppoint(Long id, NotaryAppointCreateDto notaryAppointCreate) {
        NotaryAppoint notaryAppoint = notaryAppointRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy quyết định bổ nhiệm ID " + id));
        NotaryInfo notaryInfo = notaryInfoRepository.findById(notaryAppointCreate.getNotaryInfoId())
                .orElseThrow(() -> new CustomException("Không tìm thấy công chứng viên ID " + notaryAppointCreate.getNotaryInfoId()));
        notaryInfo.setNote("");
        notaryInfoRepository.save(notaryInfo);
        DmDocument doc = dmDocumentRepository.findById(notaryAppoint.getDocumentId())
                .orElseThrow(() -> new CustomException("Không tìm thấy văn bản liên quan"));

        doc.setActive(0L);
        if (StringUtils.hasText(notaryAppointCreate.getSigner())) {
            doc.setSigner(notaryAppointCreate.getSigner());
        }
        Date decisionDate = notaryAppointCreate.getDateSign();
        Date effectiveDate = notaryAppointCreate.getEffectiveDate();
        if (effectiveDate == null && notaryAppointCreate.getType() == 2) {
            throw new CustomException("Ngày hiệu lực bắt buộc khi loại quyết định!");
        }
        if (effectiveDate != null) {
            doc.setEffectiveDate(effectiveDate);
        } else {
            doc.setActive(null);
        }
        if (decisionDate == null && notaryAppointCreate.getType() == 2) {
            throw new CustomException("Ngày đề nghị bắt buộc khi loại quyết định!");
        }
        if (decisionDate != null) {
            doc.setDateSign(decisionDate);
        } else {
            doc.setDateSign(null);
        }
        if (decisionDate != null && effectiveDate != null) {
            if (effectiveDate.compareTo(decisionDate) < 0) {
                throw new CustomException("Ngày không hợp lệ!");
            }
        }

        String newDispatchCode = notaryAppointCreate.getDispatchCode().trim();
        doc.setDispatchCode(safeString(newDispatchCode));
//        if (!newDispatchCode.equals(doc.getDispatchCode())) {
//            List<DmDocument> documentDup = dmDocumentRepository.findByDispatchCodeAndIdNot(newDispatchCode, doc.getId());
//            if (documentDup.size()>0) {
//                throw new CustomException("Trùng mã số công văn!");
//            }
//            doc.setDispatchCode(newDispatchCode);
//        }
        if (StringUtils.hasText(notaryAppointCreate.getNote())) {
            doc.setNote(notaryAppointCreate.getNote());
        }
        dmDocumentRepository.save(doc);
        notaryAppoint.setKind((notaryAppointCreate.getKind()));
        notaryAppoint.setNotaryInfoId(notaryAppointCreate.getNotaryInfoId());
        notaryAppoint.setTypeAppoint(notaryAppointCreate.getType());
        notaryAppoint.setActive(0L);
        notaryAppoint.setReason(safeString(notaryAppointCreate.getReason()));

        return notaryAppointRepository.save(notaryAppoint);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteNotaryAppoint(Long id) {
        NotaryAppoint notaryAppoint = notaryAppointRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy quyết định bổ nhiệm ID " + id));
        dmDocumentRepository.deleteById(notaryAppoint.getDocumentId());
        notaryAppointRepository.delete(notaryAppoint);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteByNotaryId(Long notaryId) {
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(notaryId);
        if (notaryInfo.isPresent()) {
            List<NotaryAppoint> notaryAppointList = notaryAppointRepository.findByNotaryInfoIdOrderByLastUpdate(notaryId);
            notaryAppointRepository.deleteAll(notaryAppointList);
        } else {
            throw new CustomException("Không tìm thấy ccv khi xóa tạm đỉnh chỉ");
        }
    }

    @Override
    public List<NotaryAppointResponse> getProcessAppoints(Long notaryId) {
        return getNotaryHistory(notaryId);
    }

    public List<NotaryAppointResponse> getNotaryHistory(Long idNotary) {
        List<Object[]> rows = notaryAppointRepository.getNotaryHistoryRaw(idNotary);
        List<NotaryAppointResponse> result = new ArrayList<>();
        for (Object[] row : rows) {
            NotaryAppointResponse dto = new NotaryAppointResponse(
                    (Date) row[0],                              // dateSign
                    safeString(row[1]),                         // dispatchCode
                    (Date) row[2],                              // effectiveDate
                    safeString(row[3]),                         // signer
                    safeString(row[4]),                         // fileName
                    safeString(row[5]),                         // linkFile
                    safeLong(row[6]),                           // notaryInfoId from doc
                    safeLong(row[7]),                           // notaryInfoId from appoint/dismissed
                    safeString(row[8]),                         // reason
                    safeLong(row[9]),                           // id
                    safeLong(row[10]),                          // type
                    safeInt(row[11]),                           // kind
                    safeLong(row[12]),                          // status
                    safeLong(row[13])
            );


            result.add(dto);
        }
        return result;
    }

    private String safeString(Object obj) {
        if (obj == null) return "";
        String str = obj.toString().trim();
        return str.isEmpty() ? "" : str;
    }

    private Long safeLong(Object obj) {
        return obj != null ? (Long) obj : 0L;
    }

    private Integer safeInt(Object obj) {
        return obj != null ? ((BigDecimal) obj).intValue() : 0;
    }


    @Override
    public String uploadFileToDocument(Long idDocument, MultipartFile multipartFile) throws InternalException {
        Optional<DmDocument> optionalDmDocument = dmDocumentRepository.findById(idDocument);
        if (optionalDmDocument.isPresent()) {
            DmDocument dmDocument = optionalDmDocument.get();
            String path = fileStorageService.uploadFile("document", multipartFile);
            String name = multipartFile.getOriginalFilename();
            dmDocument.setLinkFile(path);
            dmDocument.setFileName(name);
            dmDocumentRepository.save(dmDocument);
            return path;
        } else {
            throw new CustomException("Không tìm thấy tài liệu");
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String pathFile, String fileName) throws InternalException {

        byte[] data = fileStorageService.download(pathFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(data);
    }


    @Override
    public boolean deleteFile(String filePath) {
        Optional<DmDocument> optionalDmDocument = dmDocumentRepository.findByLinkFile((filePath));
        if (optionalDmDocument.isPresent()) {
            DmDocument dmDocument = optionalDmDocument.get();
            dmDocument.setLinkFile("");
            dmDocument.setFileName("");
            dmDocumentRepository.save(dmDocument);
        } else {
            throw new CustomException("Không tìm thấy document file path");
        }
        if (!StringUtils.hasText(filePath)) {
            throw new CustomException("Invalid file path");
        }
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return false;
        }
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new CustomException("Error deleting file", e);
        }
        return true;
    }


}
