package com.osp.bttp.dao.service.bttp.iml;

import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.entity.db3.LogSystem;
import com.osp.bttp.dao.model.mview.LogSystemView;
import com.osp.bttp.dao.model.type.ActionType;
import com.osp.bttp.dao.model.type.ActorType;
import com.osp.bttp.dao.model.type.GroupType;
import com.osp.bttp.dao.repository.db3.LogSystemRepository;
import com.osp.bttp.dao.service.bttp.LogSystemService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
public class LogSystemServiceImpl implements LogSystemService {
    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Autowired
    private LogSystemRepository logSystemRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public PaginationDto<LogSystemView> searchLogSystem(
            Integer page,
            Integer size,
            String groups,
            String actor,
            String input) {

        try {
            int currentPage = page != null ? page : 0;
            int pageSize = size != null ? size : 10;

            Map<String, Object> params = new HashMap<>();
            StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");
            buildWhereClause(whereClause, params, groups, actor, input);
            String sql = new StringBuilder()
                    .append("SELECT ls.id, ls.object_name, ls.object_id, ls.actions, ls.groups, ls.actor, ls.ip,ls.created_by, au.full_name, ls.gen_date ")
                    .append("FROM log_system ls JOIN acc_user au ON ls.created_by = au.username").toString();
            sql += whereClause;
            sql += "ORDER BY ls.gen_date DESC";

            Query query = entityManager.createNativeQuery(sql);
            params.forEach(query::setParameter);
            query.setFirstResult(currentPage * pageSize);
            query.setMaxResults(pageSize);

            List<Object[]> resultList = query.getResultList();
            List<LogSystemView> logSystemViews = new ArrayList<>();
            for (Object[] record : resultList) {
                LogSystemView log = new LogSystemView();
                log.setId(record[0] != null ? Long.parseLong(record[0].toString()) : 0L);
                log.setObjectName((String) record[1]);
                log.setObjectId((String) record[2]);
                log.setActions((String) record[3]);
                log.setGroup((String) record[4]);
                log.setActor((String) record[5]);
                log.setIp((String) record[6]);
                log.setCreatedBy((String) record[7]);
                log.setCreateByStr((String) record[8]);
                log.setGenDate((Date) record[9]);
                logSystemViews.add(log);
            }

            String countSql = new StringBuilder()
                    .append("SELECT COUNT(*) FROM log_system ls JOIN acc_user au ON ls.created_by = au.username")
                    .append(whereClause).toString();
            Query countQuery = entityManager.createNativeQuery(countSql);
            params.forEach(countQuery::setParameter);

            long totalCount = ((Number) countQuery.getSingleResult()).longValue();
            int totalPage = (int) Math.ceil((double) totalCount / pageSize);
            PaginationDto<LogSystemView> response = new PaginationDto<>();
            response.setTotalPage(totalPage);
            response.setTotalItem(totalCount);
            response.setData(logSystemViews);
            return response;


        } catch (Exception e) {
            throw new CustomException("Có lỗi khi search query");

        }
    }

    private void buildWhereClause(StringBuilder where, Map<String, Object> params, String groups, String actor, String input) {
        if (H.isTrue(groups)) {
            where.append(" AND ls.groups = :groups");
            params.put("groups", groups);
        }
        if (H.isTrue(actor)) {
            where.append(" AND ls.actor = :actor");
            params.put("actor", actor);
        }
        if (H.isTrue(input)) {
            where.append(" AND (ls.created_by LIKE :input OR ls.ip LIKE :input)");
            params.put("input", "%" + input + "%");
        }
    }

    @Override
    public LogSystem saveLog(String objectName, String objectId, ActionType actions, GroupType groups, ActorType actor) {
        try {
            LogSystem log = new LogSystem();
            if (StringUtils.hasText(objectId)) {
                log.setObjectId(objectId);
            }
            if (StringUtils.hasText(objectName)) {
                log.setObjectName(objectName);
            }
            if (actions != null) {
                log.setActions(actions);
            }
            if (groups != null) {
                log.setGroups(groups);
            }
            if (actor != null) {
                log.setActor(actor);
            }
            String ip = request.getHeader("X-FORWARDED-FOR");
            if (ip == null || ip.isEmpty()) {
                ip = request.getRemoteAddr();
            }
            log.setIp(ip);
            return logSystemRepository.save(log);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
