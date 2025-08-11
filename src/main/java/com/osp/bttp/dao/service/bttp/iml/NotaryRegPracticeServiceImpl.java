package com.osp.bttp.dao.service.bttp.iml;

import com.osp.bttp.common.contants.ConstantBttp;
import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.dao.model.dto.db3.NotaryRegPracticeCreateDto;
import com.osp.bttp.dao.model.entity.db3.*;
import com.osp.bttp.dao.model.mview.bttp.NotaryRegAndAuctionCardResponse;
import com.osp.bttp.dao.repository.bttp.*;
import com.osp.bttp.dao.service.bttp.NotaryRegPracticeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NotaryRegPracticeServiceImpl implements NotaryRegPracticeService {
    @Autowired
    private NotaryInfoRepository notaryInfoRepository;

    @Autowired
    private OrgNotaryInfoRepository orgNotaryInfoRepository;

    @Autowired
    private DmDocumentRepository dmDocumentRepository;

    @Autowired
    private NotaryRegPracticeRepository notaryRegPracticeRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NotaryRegPractice add(NotaryRegPracticeCreateDto notaryRegPracticeCreateDto) {
        Optional<NotaryInfo> notaryInfExits = notaryInfoRepository.findById(notaryRegPracticeCreateDto.getNotaryInfoId());
        if (notaryInfExits.isEmpty()) {
            throw new CustomException("Không tìm thấy công chứng viên id " + notaryRegPracticeCreateDto.getNotaryInfoId());
        }
        notaryInfExits.get().setNote("");
        notaryInfoRepository.save(notaryInfExits.get());
        List<NotaryRegPractice> notaryExitsReq = notaryRegPracticeRepository.findAllByNotaryInfoId(notaryInfExits.get().getId());
        if (!notaryExitsReq.isEmpty()) {
            throw new CustomException("Công chứng viên đang hành nghề , chỉ được sửa !");
        }
        // check status hiện tại
  //      Long statusCurrent = notaryInfExits.get().getStatus();
        Long status = notaryRegPracticeCreateDto.getStatus();
//        if (Objects.equals(status, ConstantBttp.NOTARY_STATUS.DANG_HANH_NGHE) ||
//                Objects.equals(status, ConstantBttp.NOTARY_STATUS.CHO_CAP_THE) ||
//                Objects.equals(status, ConstantBttp.NOTARY_STATUS.CHO_BO_SUNG) ||
//                Objects.equals(status, ConstantBttp.NOTARY_STATUS.TU_CHOI_CAP_THE)
//        ) {
//            if (!Objects.equals(statusCurrent, ConstantBttp.NOTARY_STATUS.DA_BO_NHIEM)) {
//                throw new CustomException("Trạng thái không hợp lệ");
//            }
//        } else if (Objects.equals(status, ConstantBttp.NOTARY_STATUS.THU_HOI_THE)) {
//            if (!Objects.equals(statusCurrent, ConstantBttp.NOTARY_STATUS.DANG_HANH_NGHE)) {
//                throw new CustomException("Trạng thái không hợp lệ");
//            }
//        } else {
//            throw new CustomException("Trạng thái không hợp lệ");
//        }

        Optional<OrgNotaryInfo> orgNotaryInfoExits = orgNotaryInfoRepository.findById(notaryRegPracticeCreateDto.getOrgNotaryInfoId());
        if (orgNotaryInfoExits.isEmpty()) {
            throw new CustomException("Không tìm thấy tổ chức id " + notaryRegPracticeCreateDto.getOrgNotaryInfoId());
        }
        DmDocument doc = new DmDocument();
        doc.setActive(0L);
        Date decisionDate = notaryRegPracticeCreateDto.getDecisionDate();
        Date effectiveDate = notaryRegPracticeCreateDto.getEffectiveDate();
        if (decisionDate != null && effectiveDate != null && effectiveDate.compareTo(decisionDate) < 0) {
            throw new CustomException("Ngày không hợp lệ !");
        }
        if (decisionDate != null) {
            doc.setDateSign(decisionDate);
        }
        if (effectiveDate != null) {
            doc.setDateSign(effectiveDate);
        }
        String dispatchCode = notaryRegPracticeCreateDto.getDispatchCode().trim();
//        List<DmDocument> dmDocuments = dmDocumentRepository.findByDispatchCode(dispatchCode);
//        if (!dmDocuments.isEmpty()) {
//            throw new CustomException("Trùng code");
//        }
        doc.setDispatchCode(safeString(dispatchCode));
        dmDocumentRepository.save(doc);

        NotaryRegPractice notaryRegPractice = new NotaryRegPractice();
        notaryRegPractice.setDateReq(notaryRegPracticeCreateDto.getDateReq());
        notaryRegPractice.setNotaryInfoId(notaryRegPracticeCreateDto.getNotaryInfoId());
        notaryRegPractice.setDocumentId(doc.getId());
        if (Objects.equals(status, ConstantBttp.NOTARY_STATUS.DANG_HANH_NGHE) || Objects.equals(status, ConstantBttp.NOTARY_STATUS.THU_HOI_THE)) {
            String card = notaryRegPracticeCreateDto.getNumberCad();
            notaryRegPractice.setNumberCad(card);
        }
        notaryRegPractice.setOrgNotaryInfoId(notaryRegPracticeCreateDto.getOrgNotaryInfoId());
        notaryRegPractice.setActive(0L);
        notaryRegPractice.setStatus(notaryRegPracticeCreateDto.getStatus());
        notaryRegPractice.setReason(safeString(notaryRegPracticeCreateDto.getReason()));
        if (notaryRegPracticeCreateDto.getAdministrationId() != null) {
            notaryRegPractice.setAdministrationId(notaryRegPracticeCreateDto.getAdministrationId());
        }

        return notaryRegPracticeRepository.save(notaryRegPractice);

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NotaryRegPractice edit(Long id, NotaryRegPracticeCreateDto dto) {
        NotaryRegPractice existing = notaryRegPracticeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bản ghi đăng ký hành nghề id: " + id));

        NotaryInfo notaryInfo = notaryInfoRepository.findById(dto.getNotaryInfoId())
                .orElseThrow(() -> new CustomException("Không tìm thấy công chứng viên id: " + dto.getNotaryInfoId()));
        notaryInfo.setNote("");
        notaryInfoRepository.save(notaryInfo);

        OrgNotaryInfo org = orgNotaryInfoRepository.findById(dto.getOrgNotaryInfoId())
                .orElseThrow(() -> new CustomException("Không tìm thấy tổ chức hành nghề id: " + dto.getOrgNotaryInfoId()));

        DmDocument doc = dmDocumentRepository.findById(existing.getDocumentId())
                .orElseThrow(() -> new CustomException("Không tìm thấy văn bản kèm theo"));
        // Kiểm tra trùng dispatchCode
//        if (StringUtils.hasText(dto.getDispatchCode())) {
//            List<DmDocument> docCheck = dmDocumentRepository.findByDispatchCode(dto.getDispatchCode());
//            if (!docCheck.isEmpty() && !docCheck.get(0).getId().equals(existing.getDocumentId())) {
//                throw new CustomException("Số văn bản đã tồn tại trong hệ thống!");
//            }
//        }

        // Kiểm tra trùng số thẻ (numberCad)
//        if (StringUtils.hasText(dto.getNumberCad())) {
//            Optional<NotaryRegPractice> cardCheck = notaryRegPracticeRepository.findByNumberCad(dto.getNumberCad());
//            if (cardCheck.isPresent() && !cardCheck.get().getId().equals(id)) {
//                throw new CustomException("Số thẻ công chứng viên đã tồn tại trong hệ thống!");
//            }
//        }

        // Cập nhật Document
        // Check ngày quyết định & ngày hiệu lực
        Date decisionDate = dto.getDecisionDate();
        Date effectiveDate = dto.getEffectiveDate();
        if (decisionDate != null && effectiveDate != null && effectiveDate.compareTo(decisionDate) < 0) {
            throw new CustomException("Ngày hiệu lực không được nhỏ hơn ngày quyết định!");
        }
        if (decisionDate != null) {
            doc.setDateSign(decisionDate);
        }
        if (effectiveDate != null) {
            doc.setDateSign(effectiveDate);
        }
        doc.setDispatchCode(safeString(dto.getDispatchCode()));
        doc.setNote(safeString(dto.getReason()));
        doc.setSigner(safeString(dto.getSigner()));
        dmDocumentRepository.save(doc);

        // Cập nhật bản ghi hành nghề
        existing.setNotaryInfoId(dto.getNotaryInfoId());
        existing.setOrgNotaryInfoId(dto.getOrgNotaryInfoId());
        existing.setStatus(dto.getStatus());
        existing.setReason(safeString(dto.getReason()));
        existing.setTypeNotaryInfo(dto.getTypeNotaryInfo());
        existing.setDateReq(dto.getDateReq());
        if (dto.getAdministrationId() != null) {
            existing.setAdministrationId(dto.getAdministrationId());
        }
        existing.setNumberCad(safeString(dto.getNumberCad()));
        existing.setActive(safeLong(dto.getActive()));

        return notaryRegPracticeRepository.save(existing);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Long id) {
        NotaryRegPractice existing = notaryRegPracticeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bản ghi đăng ký hành nghề id: " + id));
        Optional<NotaryInfo> checkNotaryIsChief = notaryInfoRepository.checkNotaryIsChief(existing.getNotaryInfoId());
        if (checkNotaryIsChief.isPresent()) {
            List<OrgNotaryInfo> list = orgNotaryInfoRepository.findAllByNotaryIdOfficeChief(existing.getNotaryInfoId());
            if (!list.isEmpty()) {
                String nameOrg = list.get(0).getName();
                throw new CustomException("Công chứng viên đang là đại diện tổ chức " + nameOrg);
            }
        }
        dmDocumentRepository.deleteById(existing.getDocumentId());
        notaryRegPracticeRepository.delete(existing);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteByNotaryId(Long notaryId) {
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(notaryId);
        if (notaryInfo.isPresent()) {
            List<NotaryRegPractice> notaryRegPractices = notaryRegPracticeRepository.findAllByNotaryInfoId(notaryId);
            notaryRegPracticeRepository.deleteAll(notaryRegPractices);
        } else {
            throw new CustomException("Không tìm thấy ccv khi xóa đkhn");
        }
    }

    @Override
    public List<NotaryRegAndAuctionCardResponse> getNotaryRegAndAuctionCard(Long id) {
        return notaryRegPracticeRepository.getRegPracticeByNotary(id);
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
}
