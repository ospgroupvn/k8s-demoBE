package com.osp.bttp.dao.service.impl;

import com.osp.bttp.common.QueryBuilder;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.entity.db3.Authority;
import com.osp.bttp.dao.repository.db3.AuthoritiesRepository;
import com.osp.bttp.dao.service.AuthorityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author sangnk
 * @Created 09/10/2024 - 2:04 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AuthorityServiceImpl implements AuthorityService {
    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;
//    @Autowired
//    LogAccessDAO logAccessDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Optional<PagingResult> page(PagingResult page, String authKey, String type) {
        int offset = 0;
        if (page.getPageNumber() > 0) {
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }

//        StringBuffer sqlBuffer = new StringBuffer("SELECT p.id,p.authKey,p.authority,p.description, p.orderId, p.fid ,p.genDate,p.lastUpdated,to_char(p.genDate,'DD/MM/YYYY hh24:mi:ss')"
        StringBuffer sqlBuffer = new StringBuffer("SELECT p.id,p.auth_key,p.authority,p.description, p.order_id, p.fid ,p.gen_date,p.last_updated,p.source "
                + "from acc_authorities p ");
        StringBuffer sqlBufferCount = new StringBuffer("SELECT count(p.id) "
                + "from acc_authorities p ");

//            if(H.isTrue(source)) {
//                sqlBuffer.append(" where p.source = :source ");
//                sqlBufferCount.append(" where p.source = :source ");
//            }

        Query query = filterBuilderSingle(sqlBuffer, true, authKey, type);


//            if(H.isTrue(source)) {
//                query.setParameter("source", source);
//            }

        List<Object[]> list = (page.getPageNumber() == 0) ? query.getResultList() : query.setFirstResult(offset).setMaxResults(page.getNumberPerPage()).getResultList();
        List<Authority> listAuth = new ArrayList<>();
        if (list != null && list.size() > 0) {
//                page.setItems(list);
            for (Object[] obj : list) {
                Authority auth = new Authority();
                auth.setId(Long.parseLong(obj[0].toString()));
                auth.setAuthKey(obj[1] != null ? obj[1].toString() : "");
                auth.setAuthority(obj[2] != null ? obj[2].toString() : "");
                auth.setDescription(obj[3] != null ? obj[3].toString() : "");
                auth.setOrderId(obj[4] != null ? Integer.parseInt(obj[4].toString()) : 0);
                auth.setFid(obj[5] != null ? Long.parseLong(obj[5].toString()) : 0);
                auth.setGenDate(obj[6] != null ? (java.util.Date) obj[6] : null);
                auth.setLastUpdated(obj[7] != null ? (java.util.Date) obj[7] : null);
                auth.setSource(obj[8] != null ? obj[8].toString() : "");
                listAuth.add(auth);
            }
            page.setItems(listAuth);
        }
        Query queryCount = filterBuilderSingle(sqlBufferCount, false, authKey, type);

        BigInteger rowCount = (BigInteger) queryCount.getSingleResult();
        if (rowCount != null) {
            page.setRowCount(rowCount.longValue());
        }

        return Optional.ofNullable(page);
    }

    private Query filterBuilderSingle(StringBuffer stringBuffer, boolean order, String authKey, String type) {
        Query result = null;
        try {
            StringBuffer hql = stringBuffer;
            hql.append(" where 1=1 ");


//            QueryBuilder builder = new QueryBuilder(entityManager, hql);
            //NOT REGEX _ALL_
            hql.append(" and ( upper(p.hidden) NOT LIKE '%_ALL_%' OR p.hidden IS NULL) ");

            if (StringUtils.isNotBlank(authKey)) {
                hql.append(" and upper(p.auth_key) like upper(:authKey) ");
            }
            if (H.isTrue(type)) {
                hql.append(" and ( upper(p.hidden) NOT LIKE :type OR p.hidden IS NULL ) ");
            }

            result = entityManager.createNativeQuery(hql.toString());

            if (StringUtils.isNotBlank(authKey)) {
//                builder.and(QueryBuilder.LIKE, "UPPER(p.authKey)", "%" + authKey.trim().toUpperCase() + "%");
                result.setParameter("authKey", "%" + authKey.trim().toUpperCase() + "%");
            }
            if (H.isTrue(type)) {
                result.setParameter("type", "%_" + type.trim().toUpperCase() + "_%");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(value = "transactionManager")
    public boolean addAuthority(Authority authoItem) {
        try {
            AccUser user = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            authoItem.setGenDate(new Date());
            authoItem.setLastUpdated(new Date());
            authoItem.setCreateBy(user.getUsername());
            authoItem.setUpdateBy(user.getUsername());
            authoItem.setAuthority(authoItem.getAuthority().trim().toUpperCase());
            authoItem.setSource(H.isTrue(authoItem.getSource()) ? authoItem.getSource() : "");
            boolean isUpdate = add(authoItem);
            if (isUpdate) {
//                logAccessDao.addLog("Thêm mới chức năng với mã chức năng " + authoItem.getAuthKey().trim().toUpperCase(),Constants.Log.system,  ipClient);
                return true;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public boolean add(Authority authItem) {
        entityManager.persist(authItem);
        entityManager.flush();
        return true;
    }


    @Override
    public boolean isExits(Authority authoItem) {
        boolean result = true;
        StringBuffer sqlBuffer = new StringBuffer("SELECT Count(o) FROM Authority o");
        QueryBuilder builder = new QueryBuilder(entityManager, sqlBuffer);
        builder.and(QueryBuilder.EQ, "UPPER(o.authKey)", authoItem.getAuthKey().trim().toUpperCase());
        Query query = builder.initQuery(false);
        List list = query.getResultList();
        int count = ((Long) list.get(0)).intValue();
        if (count == 0) {
            result = false;
        }
        return result;
    }

    @Override
    @Transactional(value = "transactionManager")
    public boolean editAuthority(Authority authoItem) {
        try {
            AccUser user = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            authoItem.setLastUpdated(new Date());
            authoItem.setUpdateBy(user.getUsername());
            authoItem.setDescription(authoItem.getDescription().trim());
            boolean isUpdate = edit(authoItem);
            if (isUpdate) {
//                logAccessDao.addLog("Sửa thông chức năng " + authoItem.getAuthKey().trim().toUpperCase(),Constants.Log.system,  ipClient);
                return true;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public boolean edit(Authority authItem) {
        entityManager.merge(authItem);
        entityManager.flush();
        return true;

    }

    @Override
    public Authority getAuthorityById(Long id) {
        return entityManager.find(Authority.class, id);
    }


    @Override
    public boolean deleteAuthority(Authority authDel) {
        Query query = entityManager.createQuery("delete from Authority p where p.id=:id").setParameter("id", authDel.getId());
        query.executeUpdate();
        return true;
    }

    @Override
    public boolean checkAuthorityAssigned(long authId) {
        boolean result = true;
        StringBuffer sqlBuffer = new StringBuffer("SELECT Count(o) FROM GroupAuthority o");
        QueryBuilder builder = new QueryBuilder(entityManager, sqlBuffer);
        builder.and(QueryBuilder.EQ, "UPPER(o.authority)", authId);
        Query query = builder.initQuery(false);
        List list = query.getResultList();
        int count = ((Long) list.get(0)).intValue();
        if (count == 0) {
            result = false;
        }
        return result;
    }

    @Override
    public List<Authority> getListAuthParent(long authId, String type) {
        List<Authority> authoritys = new ArrayList<>();

        StringBuffer hql = new StringBuffer();
        hql.append("SELECT p.id,p.auth_key,p.authority,p.description, p.order_id, p.fid ,p.gen_date,p.last_updated,p.source from acc_authorities p where p.fid = 0 ");
        hql.append(" AND (upper(p.hidden) NOT LIKE '%_ALL_%' OR p.hidden IS NULL) ");

        if (H.isTrue(type)) {
            hql.append(" AND (upper(p.hidden) NOT LIKE :type OR p.hidden IS NULL) ");
        }
        Query query = entityManager.createNativeQuery(hql.toString());


        if (H.isTrue(type)) {
            query.setParameter("type", "%_" + type.trim().toUpperCase() + "_%");
        }
        List<Object[]> list = query.getResultList();
        if (list != null && list.size() > 0) {
//                page.setItems(list);
            for (Object[] obj : list) {
                Authority auth = new Authority();
                auth.setId(Long.parseLong(obj[0].toString()));
                auth.setAuthKey(obj[1] != null ? obj[1].toString() : "");
                auth.setAuthority(obj[2] != null ? obj[2].toString() : "");
                auth.setDescription(obj[3] != null ? obj[3].toString() : "");
                auth.setOrderId(obj[4] != null ? Integer.parseInt(obj[4].toString()) : 0);
                auth.setFid(obj[5] != null ? Long.parseLong(obj[5].toString()) : 0);
                auth.setGenDate(obj[6] != null ? (java.util.Date) obj[6] : null);
                auth.setLastUpdated(obj[7] != null ? (java.util.Date) obj[7] : null);
                auth.setSource(obj[8] != null ? obj[8].toString() : "");
                authoritys.add(auth);
            }
        }
        return authoritys;
    }

    @Override
    public List<Authority> getAuthorityChildrenById(long authId) {
        return entityManager.createQuery("select au from Authority au where au.fid = :authId", Authority.class).setParameter("authId", authId).getResultList();
    }
}
