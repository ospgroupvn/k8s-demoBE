package com.osp.bttp.dao.service.bttp.iml;

import com.osp.bttp.common.contants.ConstantBttp;
import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.dao.model.dto.db3.NotaryPenalizeCreateDto;
import com.osp.bttp.dao.model.entity.db3.DmDocument;
import com.osp.bttp.dao.model.entity.db3.NotaryInfo;
import com.osp.bttp.dao.model.entity.db3.NotaryPenalize;
import com.osp.bttp.dao.model.mview.bttp.NotaryPenalizeResponse;
import com.osp.bttp.dao.repository.bttp.*;
import com.osp.bttp.dao.service.bttp.NotaryPenalizeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NotaryPenalizeServiceImpl implements NotaryPenalizeService {
    @Autowired
    private NotaryInfoRepository notaryInfoRepository;

    @Autowired
    private DmDocumentRepository dmDocumentRepository;

    @Autowired
    private NotaryPenalizeRepository notaryPenalizeRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NotaryPenalize add(NotaryPenalizeCreateDto notaryPenalizeCreateDto) {
        Optional<NotaryInfo> notaryInfExits = notaryInfoRepository.findById(notaryPenalizeCreateDto.getNotaryInfoId());
        if (notaryInfExits.isEmpty()) {
            throw new CustomException("Không tìm thấy công chứng viên id " + notaryPenalizeCreateDto.getNotaryInfoId());
        }
        notaryInfExits.get().setNote("");
        notaryInfoRepository.save(notaryInfExits.get());
        // check status hiện tại
 //       Long statusCurrent = notaryInfExits.get().getStatus();
//        if (!Objects.equals(statusCurrent, ConstantBttp.NOTARY_STATUS.DANG_HANH_NGHE)) {
//            throw new CustomException("Trạng thái không hợp lệ");
//        }
//        Optional<OrgNotaryInfo> orgNotaryInfoExits = orgNotaryInfoRepository.findById(notaryPenalizeCreateDto.getOrgNotaryId());
//        if (orgNotaryInfoExits.isEmpty()) {
//            throw new CustomException("Không tìm thấy tổ chức id " + notaryPenalizeCreateDto.getOrgNotaryId());
//        }
        DmDocument doc = new DmDocument();
        doc.setActive(0L);
        Date decisionDate = notaryPenalizeCreateDto.getDecisionDate();
        Date effectiveDate = notaryPenalizeCreateDto.getEffectiveDate();
        if(decisionDate!=null){
            doc.setDateSign(decisionDate);
        }
        if(effectiveDate!=null){
            doc.setDateSign(effectiveDate);
        }
        if (decisionDate != null && effectiveDate != null && effectiveDate.before(decisionDate)) {
            throw new CustomException("Ngày không hợp lệ !");
        }
        doc.setDispatchCode(safeString(notaryPenalizeCreateDto.getDispatchCode()));
        dmDocumentRepository.save(doc);

        NotaryPenalize notaryPenalize = new NotaryPenalize();
        notaryPenalize.setDocumentId(doc.getId());
        notaryPenalize.setNotaryInfoId(notaryPenalizeCreateDto.getNotaryInfoId());
        notaryPenalize.setOrgNotaryId(notaryPenalizeCreateDto.getOrgNotaryId());
        Long typePenalize = notaryPenalizeCreateDto.getTypePenalize();
        Long level = notaryPenalizeCreateDto.getLeverPenalize();

        notaryPenalize.setTypePenalize(typePenalize);
        if (Objects.equals(typePenalize, ConstantBttp.LEVER_PENALIZE.XU_LY_VI_PHAM_HANH_CHINH)) {
            if (level >= ConstantBttp.TYPE_PENALIZE.CANH_CAO && level <= ConstantBttp.TYPE_PENALIZE.KHIEN_TRACH) {
                notaryPenalize.setLeverPenalize(level);
                if (level.equals(ConstantBttp.TYPE_PENALIZE.XU_PHAT_TIEN)) {
                    notaryPenalize.setMoneyPenalty(notaryPenalizeCreateDto.getMoneyPenalty());
                    Long additionalPenalize = notaryPenalizeCreateDto.getAdditionalPenalty();
                    Long moneyPenalty = notaryPenalizeCreateDto.getMoneyPenalty();
//                    if (Objects.equals(additionalPenalize, ConstantBttp.ADDITIONAL_PENALTY.XU_PHAT_BX_1)) {
//                        notaryInfExits.get().setStatus(ConstantBttp.NOTARY_STATUS.TAM_DINH_CHI_HANH_NGHE);
//                    }
                    notaryPenalize.setAdditionalPenalty(additionalPenalize);
                    notaryPenalize.setMoneyPenalty(moneyPenalty);
                }
            } else {
                throw new CustomException("Level xử phạt type 1 không hợp lệ");
            }

        } else if (Objects.equals(typePenalize, ConstantBttp.LEVER_PENALIZE.KY_LUAT)) {
            if (level >= ConstantBttp.TYPE_PENALIZE.KY_LUAT_CANH_CAO && level <= ConstantBttp.TYPE_PENALIZE.BUOC_THOI_VIEC) {
                notaryPenalize.setLeverPenalize(level);
//                if (level.equals(ConstantBttp.TYPE_PENALIZE.CACH_CHUC) || level.equals(ConstantBttp.TYPE_PENALIZE.BUOC_THOI_VIEC)) {
//                    notaryInfExits.get().setStatus(ConstantBttp.NOTARY_STATUS.TAM_DINH_CHI_HANH_NGHE);
//                }
            } else {
                throw new CustomException("Level xử phạt type 2 không hợp lệ");
            }
        }
        Long administrationIdPenalty =  notaryPenalizeCreateDto.getAdministrationIdPenalty();
        notaryPenalize.setAdministrationIdPenalty(administrationIdPenalty);
        notaryPenalize.setReason(safeString(notaryPenalizeCreateDto.getReason()));

        return  notaryPenalizeRepository.save(notaryPenalize);

    }
    @Override
    @Transactional(rollbackOn = Exception.class)
    public NotaryPenalize edit(Long id, NotaryPenalizeCreateDto dto) {
        NotaryPenalize existing = notaryPenalizeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bản ghi xử phạt với id: " + id));
        notaryInfoRepository.findById(dto.getNotaryInfoId())
                .orElseThrow(() -> new CustomException("Không tìm thấy công chứng viên id: " + dto.getNotaryInfoId()));
        // Check ngày
        Date decisionDate = dto.getDecisionDate();
        Date effectiveDate = dto.getEffectiveDate();
        if (decisionDate != null && effectiveDate != null && effectiveDate.before(decisionDate)) {
            throw new CustomException("Ngày hiệu lực không được nhỏ hơn ngày quyết định!");
        }
        // Check trùng dispatchCode
//        if (StringUtils.hasText(dto.getDispatchCode())) {
//            List<DmDocument> duplicateDoc = dmDocumentRepository.findByDispatchCode(dto.getDispatchCode());
//            if (duplicateDoc.size()>1 && !duplicateDoc.get(0).getId().equals(existing.getDocumentId())) {
//                throw new CustomException("Số văn bản đã tồn tại!");
//            }
//        }

        // Cập nhật document
        DmDocument docUpdate = dmDocumentRepository.findById(existing.getDocumentId())
                .orElseThrow(() -> new CustomException("Không tìm thấy văn bản gốc"));

        docUpdate.setDispatchCode(safeString(dto.getDispatchCode()));
        docUpdate.setDateSign(decisionDate);
        docUpdate.setEffectiveDate(effectiveDate);
        docUpdate.setNote(safeString(dto.getNote()));
        docUpdate.setSigner(safeString(dto.getSigner()));
        docUpdate.setActive(safeLong(dto.getActive()));
        dmDocumentRepository.save(docUpdate);

        // Cập nhật xử phạt
        existing.setNotaryInfoId(dto.getNotaryInfoId());
        existing.setOrgNotaryId(dto.getOrgNotaryId());
        existing.setAdministrationIdPenalty(dto.getAdministrationIdPenalty());
        existing.setReason(StringUtils.hasText(dto.getReason()) ? dto.getReason() : null);
        existing.setActive(dto.getActive() != null ? dto.getActive() : 0L);

        Long type = dto.getTypePenalize();
        Long level = dto.getLeverPenalize();
        existing.setTypePenalize(type);
        existing.setLeverPenalize(level);

        if (type != null) {
            if (type.equals(ConstantBttp.LEVER_PENALIZE.XU_LY_VI_PHAM_HANH_CHINH)) {
                if (level == null || level < ConstantBttp.TYPE_PENALIZE.CANH_CAO || level > ConstantBttp.TYPE_PENALIZE.KHIEN_TRACH) {
                    throw new CustomException("Level xử phạt type 1 không hợp lệ");
                }
                // Xử phạt tiền
                existing.setMoneyPenalty(dto.getMoneyPenalty());
                existing.setAdditionalPenalty(dto.getAdditionalPenalty());

            } else if (type.equals(ConstantBttp.LEVER_PENALIZE.KY_LUAT)) {
                if (level == null || level < ConstantBttp.TYPE_PENALIZE.KY_LUAT_CANH_CAO || level > ConstantBttp.TYPE_PENALIZE.BUOC_THOI_VIEC) {
                    throw new CustomException("Level xử phạt type 2 không hợp lệ");
                }
            } else {
                throw new CustomException("Loại xử phạt không hợp lệ");
            }
        }

      return  notaryPenalizeRepository.save(existing);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteByNotaryId(Long notaryId) {
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(notaryId);
        if (notaryInfo.isPresent()) {
           List<NotaryPenalize> notaryPenalizes = notaryPenalizeRepository.findByNotaryInfoId(notaryId);
           notaryPenalizeRepository.deleteAll(notaryPenalizes);
        }else {
            throw new CustomException("Không tìm thấy ccv khi xóa xử phạt");
        }
    }

    @Override
    public void delete(Long id) {
        NotaryPenalize existing = notaryPenalizeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bản ghi xử phạt với id: " + id));
        dmDocumentRepository.deleteById(existing.getDocumentId());
        notaryPenalizeRepository.delete(existing);
    }


    @Override
    public List<NotaryPenalizeResponse> getPenaltiesByNotary(Long notaryId) {
        return notaryPenalizeRepository.getPenaltiesByNotary(notaryId);
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
