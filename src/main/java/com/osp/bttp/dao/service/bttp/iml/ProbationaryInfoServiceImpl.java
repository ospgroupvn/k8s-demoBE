package com.osp.bttp.dao.service.bttp.iml;

import com.osp.bttp.common.contants.ConstantBttp;
import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.dao.model.dto.db3.ProbationaryInfoCreateDto;
import com.osp.bttp.dao.model.entity.db3.DmDocument;
import com.osp.bttp.dao.model.entity.db3.NotaryInfo;
import com.osp.bttp.dao.model.entity.db3.OrgNotaryInfo;
import com.osp.bttp.dao.model.entity.db3.ProbationaryInfo;
import com.osp.bttp.dao.model.mview.bttp.NotaryProbationaryResponse;
import com.osp.bttp.dao.repository.bttp.*;
import com.osp.bttp.dao.service.bttp.ProbationaryInfoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProbationaryInfoServiceImpl implements ProbationaryInfoService {

    @Autowired
    private ProbationaryInfoRepository probationaryInfoRepository;

    @Autowired
    private NotaryInfoRepository notaryInfoRepository;

    @Autowired
    private OrgNotaryInfoRepository orgNotaryInfoRepository;

    @Autowired
    private DmDocumentRepository dmDocumentRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ProbationaryInfo add(Long idNotary, ProbationaryInfoCreateDto probationaryInfo) {
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(idNotary);
        if (notaryInfo.isEmpty()) {
            throw new CustomException("Không tìm thấy CCV id " + idNotary);
        }
        notaryInfo.get().setNote("");
        notaryInfoRepository.save(notaryInfo.get());
        List<ProbationaryInfo> list = probationaryInfoRepository.findByOrgNotaryInfoId(idNotary);
        if (!list.isEmpty()) {
            throw new CustomException("Công chứng viên đang thực tập , chỉ được sửa !");
        }
        Long idOrg = probationaryInfo.getIdOrg();
        Optional<OrgNotaryInfo> orgNotaryInfo = orgNotaryInfoRepository.findByIdAndActive(idOrg, 0L);
        if (orgNotaryInfo.isEmpty()) {
            throw new CustomException("Không tìm thấy tổ chức đang hoạt động id " + idOrg);
        }
        // document
        DmDocument dmDocument = new DmDocument();
//        String dispatchCode = probationaryInfo.getDispatchCode();
//        List<DmDocument> existDocument = dmDocumentRepository.findByDispatchCode(dispatchCode);
//        if (!existDocument.isEmpty()) {
//            throw new CustomException("Trùng code ");
//        }
        dmDocument.setDispatchCode(safeString(probationaryInfo.getDispatchCode()));
        if(probationaryInfo.getDateSign()!=null)
        {
            dmDocument.setDateSign(probationaryInfo.getDateSign());
        }
        dmDocument.setSigner(safeString(probationaryInfo.getSigner()));
        dmDocument.setActive(0L);

        DmDocument documentNew = dmDocumentRepository.save(dmDocument);
        // probationaryInfo
        ProbationaryInfo probationaryInfoNew = new ProbationaryInfo();
        Date startDate = probationaryInfo.getDateStart();
        Date endDate = probationaryInfo.getDateEnd();
        if (startDate != null && endDate != null && endDate.compareTo(startDate) < 0) {
            throw new CustomException("Ngày không hợp lệ !");
        }
        if(startDate!=null)
        {
            probationaryInfoNew.setDateStart(startDate);
        }
        if(endDate!=null){
            probationaryInfoNew.setDateEnd(endDate);
        }
        probationaryInfoNew.setDocumentCertificateId(documentNew.getId());
        probationaryInfoNew.setStatus(ConstantBttp.NOTARY_STATUS.HOAN_THANH_TAP_SU);
        probationaryInfoNew.setDateNumber(safeLong(probationaryInfo.getDateNumber()));
        probationaryInfoNew.setOrgNotaryInfoId(idOrg);
        probationaryInfoNew.setNotaryInfoId(idNotary);
        probationaryInfoNew.setActive(0L);

        return probationaryInfoRepository.save(probationaryInfoNew);

    }

    @Transactional(rollbackOn = Exception.class)
    public ProbationaryInfo edit(Long id, ProbationaryInfoCreateDto probationaryInfo) {
        Optional<ProbationaryInfo> probOpt = probationaryInfoRepository.findById(id);
        if (probOpt.isEmpty()) {
            throw new CustomException("Không tìm thấy thông tin tập sự ID = " + id);
        }
        Long idNotary = probOpt.get().getNotaryInfoId();
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(idNotary);
        if (notaryInfo.isEmpty()) {
            throw new CustomException("Không tìm thấy CCV id " + idNotary);
        }
        notaryInfo.get().setNote("");
        notaryInfoRepository.save(notaryInfo.get());
        Long idOrg = probationaryInfo.getIdOrg();
        Optional<OrgNotaryInfo> orgNotaryInfo = orgNotaryInfoRepository.findByIdAndActive(idOrg, 0L);
        if (orgNotaryInfo.isEmpty()) {
            throw new CustomException("Không tìm thấy tổ chức đang hoạt động id " + idOrg);
        }
        ProbationaryInfo prob = probOpt.get();
        // Cập nhật dữ liệu
        Date startDate = probationaryInfo.getDateStart();
        Date endDate = probationaryInfo.getDateEnd();
        if (startDate != null && endDate != null && endDate.compareTo(startDate) < 0) {
            throw new CustomException("Ngày không hợp lệ !");
        }
        if(startDate!=null)
        {
            prob.setDateStart(startDate);
        }
        if(endDate!=null){
            prob.setDateEnd(endDate);
        }
        prob.setDateNumber(probationaryInfo.getDateNumber());
        prob.setOrgNotaryInfoId(idOrg);
        prob.setNotaryInfoId(idNotary);
        prob.setStatus(probationaryInfo.getStatus());
        prob.setNote(safeString(probationaryInfo.getNote()));

        // Cập nhật thông tin văn bản nếu cần
        Optional<DmDocument> docOpt = dmDocumentRepository.findById(prob.getDocumentCertificateId());
        if (docOpt.isPresent()) {
            DmDocument doc = docOpt.get();
//            if (StringUtils.hasText(probationaryInfo.getDispatchCode())) {
//                List<DmDocument> existDocument = dmDocumentRepository.findByDispatchCodeAndIdNot(probationaryInfo.getDispatchCode(), docOpt.get().getId());
//                if (!existDocument.isEmpty()) {
//                    throw new CustomException("Trùng code ");
//                }
//                doc.setDispatchCode(probationaryInfo.getDispatchCode());
//            }
            doc.setDispatchCode(safeString(probationaryInfo.getDispatchCode()));
            doc.setActive(0L);
            doc.setDateSign(probationaryInfo.getDateSign());
            doc.setSigner(safeString(probationaryInfo.getSigner()));
            dmDocumentRepository.save(doc);
        }

        return probationaryInfoRepository.save(prob);
    }

    @Override
    public NotaryProbationaryResponse getProbationaryInfoByIdNotary(Long idNotary) {
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(idNotary);
        if (notaryInfo.isEmpty()) {
            throw new CustomException("Không tìm thấy CCV id " + idNotary);
        }
        List<NotaryProbationaryResponse> notaryProbationaryResponse = probationaryInfoRepository.getNotaryProbationaryResponse(idNotary);
        if (!notaryProbationaryResponse.isEmpty()) {
            return notaryProbationaryResponse.get(0);
        }
        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteByNotaryId(Long idNotary) {
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(idNotary);
        if (notaryInfo.isPresent()) {
            List<ProbationaryInfo> probationaryInfos = probationaryInfoRepository.findAllByNotaryInfoId(idNotary);
            probationaryInfoRepository.deleteAll(probationaryInfos);
        } else {
            throw new CustomException("Không tìm thấy ccv khi xóa tập sự");
        }
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
