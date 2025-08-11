package com.osp.bttp.dao.service.common.impl;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.dao.model.dto.response.db4.GetListLawyerAssoc;
import com.osp.bttp.dao.model.dto.response.db4.GetListOrg;
import com.osp.bttp.dao.model.entity.db3.Category;
import com.osp.bttp.dao.model.entity.db4.LLawyerAssociation;
import com.osp.bttp.dao.model.entity.db4.LNationality;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import com.osp.bttp.dao.model.mapper.db4.LstcMapper;
import com.osp.bttp.dao.repository.db4.LLawyerAssociationRepository;
import com.osp.bttp.dao.repository.db4.LNationalityRepository;
import com.osp.bttp.dao.repository.db4.LOrganizationRepository;
import com.osp.bttp.dao.service.common.CommonService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sangnk
 * @Created 10/10/2024 - 4:24 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CommonServiceImpl implements CommonService {
    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Autowired
    private LLawyerAssociationRepository lLawyerAssociationRepository;

    @Autowired
    private LOrganizationRepository lOrganizationRepository;

    @Autowired
    LNationalityRepository lNationalityRepository;

    @Override
    public List<Category> getAllProvince() {
        List lst = new ArrayList<>();
        try {
            String sql = "select * "
                    + " from C_CATEGORY  a"
                    + " where CAT_TYPE = :type ";
            Query query = entityManager.createNativeQuery(sql, Category.class
            );
            query.setParameter("type", "TP");
            lst = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }

    @Override
    public ResponseEntity<ApiResponseV1<List<GetListLawyerAssoc>>> getAllLawyerAssoc() {
        try {
            List<LLawyerAssociation> list = lLawyerAssociationRepository.findAll();
            List<GetListLawyerAssoc>  listDTO = list.stream().map(LstcMapper::toLawyerAssocDto).toList();
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy thông tin đoàn ls thành công", listDTO), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 141, "Đã xảy ra lỗi khi lấy thông tin đoàn luật sư", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<List<GetListOrg>>> getAllOrg() {
        try {
            List<LOrganization> list = lOrganizationRepository.findAll();
            List<GetListOrg>  listDTO = list.stream().map(LstcMapper::toLOrgDto).toList();
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy thông tin tổ chức ls thành công", listDTO), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 141, "Đã xảy ra lỗi khi lấy tổ chức  luật sư", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<List<LNationality>>> getAllNational() {
        try {
            List<LNationality> list = lNationalityRepository.findAll();
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy thông tin tổ chức quốc gia", list), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 141, "Đã xảy ra lỗi khi lấy quốc gia", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Category> getAllWardByProvinceId(Long id) {
        List lst = new ArrayList<>();
        try {
            String sql = "select c.* "
                    + " from C_CATEGORY  a" +
                    " Join C_CATEGORY b on a.ID = b.PARENT_ID" +
                    "   join C_CATEGORY c ON b.ID = c.PARENT_ID " +
                     " where C.CAT_TYPE = :type and a.id = :id ";
            Query query = entityManager.createNativeQuery(sql, Category.class
            );
            query.setParameter("type", "PX");
            query.setParameter("id", id);
            lst = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }
}
