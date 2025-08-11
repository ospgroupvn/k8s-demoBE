package com.osp.bttp.dao.service.bttp.iml;
import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.dao.model.dto.db3.NotarySuspendWorkCreateDto;
import com.osp.bttp.dao.model.entity.db3.*;
import com.osp.bttp.dao.model.mview.bttp.NotarySuspendWorkResponse;
import com.osp.bttp.dao.repository.bttp.*;
import com.osp.bttp.dao.service.bttp.NotarySuspendWorkService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public  class NotarySuspendWorkServiceImpl implements NotarySuspendWorkService {
    @Autowired
    private NotaryInfoRepository notaryInfoRepository;

    @Autowired
    private DmDocumentRepository dmDocumentRepository;

    @Autowired
    private NotarySuspendWorkRepository notarySuspendWorkRepository;


    @Override
    @Transactional(rollbackOn = Exception.class)
    public NotarySuspendWork add(NotarySuspendWorkCreateDto notarySuspendWorkCreateDto) {
        Optional<NotaryInfo> notaryInfExits = notaryInfoRepository.findById(notarySuspendWorkCreateDto.getNotaryInfoId());
        if (notaryInfExits.isEmpty()) {
            throw new CustomException("Không tìm thấy công chứng viên id " + notarySuspendWorkCreateDto.getNotaryInfoId());
        }
        notaryInfExits.get().setNote("");
        notaryInfoRepository.save(notaryInfExits.get());
        List<NotarySuspendWork> notarySuspendWorks = notarySuspendWorkRepository.findByNotaryInfoId(notarySuspendWorkCreateDto.getNotaryInfoId());
        if (!notarySuspendWorks.isEmpty()) {
            throw new CustomException("Công chứng viên đã bị đỉnh chỉ , chỉ được sửa !");
        }
        // check status hiện tại
//        Long statusCurrent = notaryInfExits.get().getStatus();
//        if (!Objects.equals(statusCurrent, ConstantBttp.NOTARY_STATUS.DANG_HANH_NGHE)) {
//            throw new CustomException("Trạng thái không hợp lệ");
//        }
//        Optional<OrgNotaryInfo> orgNotaryInfoExits = orgNotaryInfoRepository.findById(notarySuspendWorkCreateDto.getOrgNotaryId());
//        if (orgNotaryInfoExits.isEmpty()) {
//            throw new CustomException("Không tìm thấy tổ chức id " + notarySuspendWorkCreateDto.getOrgNotaryId());
//        }
        DmDocument doc = new DmDocument();
        doc.setActive(0L);
        Date decisionDate = notarySuspendWorkCreateDto.getDecisionDate();
        Date effectiveDate = notarySuspendWorkCreateDto.getEffectiveDate();
        if (decisionDate != null && effectiveDate != null && effectiveDate.compareTo(decisionDate) < 0) {
            throw new CustomException("Ngày không hợp lệ !");
        }
        if (decisionDate != null) {
            doc.setDateSign(decisionDate);
        }
        if (effectiveDate != null) {
            doc.setDateSign(effectiveDate);
        }
        String code = notarySuspendWorkCreateDto.getDispatchCode().trim();
//        List<DmDocument> documentExits = dmDocumentRepository.findByDispatchCode(code);
//        if (!documentExits.isEmpty()) {
//            throw new CustomException("Trùng code");
//        }
        doc.setDispatchCode(safeString(code));
        doc.setNote(safeString(notarySuspendWorkCreateDto.getNote()));
        doc.setSigner(safeString(notarySuspendWorkCreateDto.getSigner()));

        DmDocument docNew = dmDocumentRepository.save(doc);

        NotarySuspendWork notarySuspendWork = new NotarySuspendWork();

        notarySuspendWork.setNotaryInfoId(notarySuspendWorkCreateDto.getNotaryInfoId());
        if(notarySuspendWorkCreateDto.getDateNumber()!=null){
        notarySuspendWork.setDateNumber(notarySuspendWorkCreateDto.getDateNumber());
        }
        notarySuspendWork.setActive(0L);
        notarySuspendWork.setDocumentId(docNew.getId());
        notarySuspendWork.setTypeSupend(1L);
        notarySuspendWork.setReason(safeString(notarySuspendWorkCreateDto.getReason()));

        return notarySuspendWorkRepository.save(notarySuspendWork);

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NotarySuspendWork edit(Long id, NotarySuspendWorkCreateDto dto) {

        NotarySuspendWork existing = notarySuspendWorkRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bản ghi tạm đình chỉ id: " + id));

        notaryInfoRepository.findById(dto.getNotaryInfoId())
                .orElseThrow(() -> new CustomException("Không tìm thấy công chứng viên id: " + dto.getNotaryInfoId()));

        DmDocument doc = dmDocumentRepository.findById(existing.getDocumentId())
                .orElseThrow(() -> new CustomException("Không tìm thấy văn bản gốc"));

        // Kiểm tra trùng số văn bản
        Long idDocument = existing.getDocumentId();
        Optional<DmDocument> documentExits = dmDocumentRepository.findById(idDocument);
        if (documentExits.isEmpty()) {
            throw new CustomException("Tài liệu không tồn tại");
        }
        if (StringUtils.hasText(dto.getDispatchCode())) {
            List<DmDocument> existingDoc = dmDocumentRepository.findByDispatchCodeAndIdNot(dto.getDispatchCode(), idDocument);
            if (!existingDoc.isEmpty()) {
                throw new CustomException("Số văn bản đã tồn tại trong hệ thống!");
            }
        }
        // Cập nhật văn bản
        // Kiểm tra ngày quyết định & ngày hiệu lực
        Date decisionDate = dto.getDecisionDate();
        Date effectiveDate = dto.getEffectiveDate();
        if (decisionDate != null && effectiveDate != null && effectiveDate.compareTo(decisionDate) < 0) {
            throw new CustomException("Ngày không hợp lệ !");
        }
        if (decisionDate != null) {
            doc.setDateSign(decisionDate);
        }
        if (effectiveDate != null) {
            doc.setDateSign(effectiveDate);
        }
        doc.setDispatchCode(safeString(dto.getDispatchCode()));
        doc.setNote(safeString(dto.getNote()));
        doc.setSigner(safeString(dto.getSigner()));
        doc.setActive(safeLong(dto.getActive()));
        dmDocumentRepository.save(doc);

        // Cập nhật bản ghi tạm đình chỉ
        existing.setNotaryInfoId(dto.getNotaryInfoId());
        existing.setOrgNotaryId(dto.getOrgNotaryId());
        existing.setReason(safeString(dto.getReason()));
        existing.setTypeSupend(dto.getTypeSupend() != null ? dto.getTypeSupend() : 1L);
        existing.setActive(dto.getActive() != null ? dto.getActive() : 0L);
        return notarySuspendWorkRepository.save(existing);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Long id) {
        NotarySuspendWork existing = notarySuspendWorkRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bản ghi tạm đình chỉ id: " + id));
        dmDocumentRepository.deleteById(existing.getDocumentId());
        notarySuspendWorkRepository.delete(existing);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteByNotaryId(Long notaryId) {
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(notaryId);
        if (notaryInfo.isPresent()) {
            List<NotarySuspendWork> notarySuspendWorks = notarySuspendWorkRepository.findByNotaryInfoId(notaryId);
            notarySuspendWorkRepository.deleteAll(notarySuspendWorks);
        }else {
            throw new CustomException("Không tìm thấy ccv khi xóa tạm đỉnh chỉ");
        }
    }

    @Override
    public List<NotarySuspendWorkResponse> getSuspendWorkByNotary(Long notaryId) {
        return notarySuspendWorkRepository.getSuspendWorkByNotary(notaryId);
    }

    private String safeString(Object obj) {
        if (obj == null) return "";
        String str = obj.toString().trim();
        return str.isEmpty() ? "" : str;
    }

    private Long safeLong(Object obj) {
        return obj != null ? (Long) obj : 0L;
    }
}
