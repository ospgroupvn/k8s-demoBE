package com.osp.bttp.dao.service.impl;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.dto.request.CreateGroupRequest;
import com.osp.bttp.dao.model.entity.db3.*;
import com.osp.bttp.dao.model.mview.GroupView;
import com.osp.bttp.dao.repository.db3.GroupRepository;
import com.osp.bttp.dao.service.GroupService;
import com.osp.bttp.dao.service.RedisService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sangnk
 * @Created 09/10/2024 - 8:55 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl implements GroupService {
    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RedisService redisService;

    @Override
    public List<String> loadListAuthorityByUsername(String username) {
        return groupRepository.loadListAuthorityByUsername(username);
    }

    @Override
    public List<String> loadListAuthorityOfUserByUserIdAll(Long userId) {
        EntityManager em = entityManager;
        try {
            List<Authority> items = em.createQuery("SELECT new com.osp.bttp.dao.model.entity.db3.Authority(au.authKey) FROM Authority au JOIN GroupAuthority ga ON au.id=ga.authority JOIN Group gr ON gr.id=ga.groupId JOIN GroupUser gu ON gr.id=gu.groupId WHERE gu.userId=:userId ")
                    .setParameter("userId", userId).getResultList();

            return items.stream().map(Authority::getAuthKey).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponseV1<?> checkBeforeAdd(CreateGroupRequest item) {
        String message = "Nhóm người dùng " + item.getGroupName() + " đã tồn tại cấu hình mặc định. Vui lòng kiểm tra lại!";

        if (item.getIsDefault().intValue() == 1) {
            List<Group> list = entityManager.createQuery("select a from com.osp.bttp.dao.model.entity.db3.Group a where a.type = :type and a.isDefault = 1", Group.class)
                    .setParameter("type", item.getType())
                    .getResultList();
            if (list != null && list.size() > 0) {
                return new ApiResponseV1<>(false, 15, message, null);
            }
        }
        return null;

    }

    @Override
    public Optional<Boolean> saveGroupView(CreateGroupRequest item) {
        try {
            Group group = new Group();
            group.setGroupName(item.getGroupName());
            group.setStatus(1);
            group.setDescription(item.getDescription());
            group.setType(H.isTrue(item.getType()) ? Math.toIntExact(item.getType()) : null);
            group.setIsDefault(item.getIsDefault());
            AccUser user = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            group.setCreateBy(user.getUsername());
            group.setUpdateBy(user.getUsername());
            group.setGenDate(new Date());
            group.setLastUpdated(new Date());
            add(group);
            genAuthority(item, group.getId(), user.getUsername());
//            logAccessDao.addLog("Thêm mới nhóm quyền Id:" + group.getId(), Constants.Log.system, ip);
            return Optional.of(true);
        } catch (Exception e) {
            return Optional.of(false);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<PagingResult> page(String name, PagingResult page, Integer type) {
        int offset = 0;
        if (page.getPageNumber() > 0) {
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }
        String hql = "select gr from Group gr where UPPER(gr.groupName) like :name ";
        String hqlCount = "select count(gr.id) from Group gr where UPPER(gr.groupName) like :name ";

        if (H.isTrue(type)) {
            hql += " and gr.type = :type_ ";
            hqlCount += " and gr.type = :type_ ";
        }

        hql += " ORDER BY gr.isDefault DESC, gr.genDate DESC ";
        Query query = entityManager.createQuery(hql);
        Query queryCount = entityManager.createQuery(hqlCount);
        query.setParameter("name", "%" + ((name != null && name.length() > 0) ? name.trim().toUpperCase() : "") + "%");
        queryCount.setParameter("name", "%" + ((name != null && name.length() > 0) ? name.trim().toUpperCase() : "") + "%");

        if (H.isTrue(type)) {
            query.setParameter("type_", type);
            queryCount.setParameter("type_", type);
        }
        Long count = (Long) queryCount.getSingleResult();
        List<Group> list = query.setFirstResult(offset).setMaxResults(page.getNumberPerPage()).getResultList();

        if (list != null && count > 0) {
            page.setItems(list);
            page.setRowCount(count.longValue());
        }
        return Optional.of(page);
    }

    @Override
    public Optional<GroupView> getGroupView(Long id) {
        try {
            Group group = get(id).orElse(new Group());
            if (group == null || group.getId() == null) {
                return null;
            }
            GroupView item = new GroupView();
            item.setId(group.getId());
            item.setGroupName(group.getGroupName());
            item.setDescription(group.getDescription());
            item.setIsDefault(group.getIsDefault());
            if (H.isTrue(group.getType())) item.setType(Long.valueOf(group.getType()));

            List<GroupAuthority> groupAuthorities = loadByGroupId(id, group.getType()).orElse(null);
            if (groupAuthorities != null && groupAuthorities.size() > 0) {
                StringBuilder authoritiesString = new StringBuilder("");
                groupAuthorities.stream().forEach(g -> authoritiesString.append(g.getAuthority() + ","));
                item.setListAuthority(authoritiesString.toString());
            }
            return Optional.ofNullable(item);
        } catch (Exception e) {
            return Optional.of(null);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<List<Authority>> loadAllAuthority() {
//        List<Authority> list = entityManager.createQuery("select a from Authority a order by a.orderId asc", Authority.class).getResultList();
//        return Optional.ofNullable(list);
        String sql = "SELECT au.* FROM acc_authorities au where ( upper(au.hidden) NOT LIKE '%_ALL_%' OR au.hidden IS NULL) order by au.order_id asc";
        List<Authority> list = entityManager.createNativeQuery(sql, Authority.class).getResultList();
        return Optional.ofNullable(list);
    }

    @Override
    public ApiResponseV1<?> checkBeforeEdit(CreateGroupRequest item) {
        String message = "Nhóm người dùng " + item.getGroupName() + " đã tồn tại cấu hình mặc định. Vui lòng kiểm tra lại!";

        if (item.getIsDefault().intValue() == 1) {
            List<Group> list = entityManager.createQuery("select a from com.osp.bttp.dao.model.entity.db3.Group a where a.type = :type and a.isDefault = 1 ", Group.class)
                    .setParameter("type", item.getType())
                    .getResultList();
            if (list != null && list.size() > 0 && !list.get(0).getId().equals(item.getId())) {
                return new ApiResponseV1<>(false, 24, message, null);
            }
        }
        return null;
    }

    @Override
    public Optional<Boolean> editGroupView(CreateGroupRequest item) {

        Group group = get(item.getId()).orElse(null);
        if (group == null) {
            return Optional.of(false);
        }
        group.setGroupName(item.getGroupName());
        group.setDescription(item.getDescription());
        group.setType(H.isTrue(item.getType()) ? Math.toIntExact(item.getType()) : null);
        group.setIsDefault(item.getIsDefault());
        AccUser user = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        group.setUpdateBy(user.getUsername());
        group.setLastUpdated(new Date());
        Long groupId = edit(group).orElse(0L);
        if (groupId == 0L) {
            return Optional.of(false);
        }
        //get list user of group
        List<Long> listUser = loadAllUserIdOfGroup(groupId).orElse(new ArrayList<>());
        redisService.deleteCacheAuthoInfoByUserIds(listUser);
        genAuthority(item, groupId, user.getUsername());
//        logAccessDao.addLog("Sửa thông tin nhóm quyền Id:" + group.getId(), Constants.Log.system, ip);
        return Optional.of(true);
    }

    public Optional<Long> edit(Group item) {
        entityManager.merge(item);
        entityManager.flush();
        return Optional.of(item.getId());
    }

    public Optional<List<Long>> loadAllUserIdOfGroup(Long groupId) {
        List<Long> items = entityManager.createQuery("SELECT us.id FROM AccUser us JOIN GroupUser gu ON us.id=gu.userId where gu.groupId=:groupId", Long.class)
                .setParameter("groupId", groupId).getResultList();
        return Optional.ofNullable(items);
    }

    public Optional<Group> get(Long id) {
        Group item = entityManager.find(Group.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public Optional<Long> deleteGroup(Long id) {
        try {
            Query query = entityManager.createQuery("delete from Group g where g.id = :id").setParameter("id", id);
            int temp = query.executeUpdate();
            if (temp > 0) {
                deleteGroupAuthority(id).orElse(false);
                return Optional.of(1L);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error(e.getMessage());
        }
        return Optional.of(13L);
    }

    @Override
    public Optional<PagingResult> pageUserOfGroup(Long groupId, PagingResult page) {
        int offset = 0;
        if (page.getPageNumber() > 0) {
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }
        Long count = (Long) entityManager.createQuery("SELECT count(DISTINCT us) FROM AccUser us JOIN GroupUser gu ON us.id=gu.userId where gu.groupId=:groupId and us.status=1").setParameter("groupId", groupId).getSingleResult();
        List<AccUser> list = entityManager.createQuery("SELECT DISTINCT us FROM AccUser us JOIN GroupUser gu ON us.id=gu.userId where gu.groupId=:groupId and us.status=1")
                .setParameter("groupId", groupId)
                .setFirstResult(offset).setMaxResults(page.getNumberPerPage()).getResultList();
        if (list != null && count > 0) {
            page.setItems(list);
            page.setRowCount(count.longValue());
        }
        return Optional.of(page);
//         return null;
    }

    @Override
    public Optional<PagingResult> pageAuthorityOfGroup(long groupId, PagingResult page) {
        int offset = 0;
        if (page.getPageNumber() > 0) {
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }
        String hql = "SELECT au.* FROM acc_authorities au JOIN acc_group_authorities ga ON au.id=ga.authority JOIN acc_group gr ON gr.id = ga.group_id where ga.group_id=:groupId  and ( upper(au.hidden) NOT LIKE '%_ALL_%' OR au.hidden IS NULL) ";
        String countSql = "SELECT count(au.id) FROM acc_authorities au JOIN acc_group_authorities ga ON au.id=ga.authority JOIN acc_group gr ON gr.id = ga.group_id where ga.group_id=:groupId  and ( upper(au.hidden) NOT LIKE '%_ALL_%' OR au.hidden IS NULL) ";
        BigInteger countBI = (BigInteger) entityManager.createNativeQuery(countSql)
                .setParameter("groupId", groupId)
                .getSingleResult();
        Long count = countBI.longValue();
        hql += ("  ");

        List<Authority> list = entityManager.createNativeQuery(hql, Authority.class)
                .setParameter("groupId", groupId)
                .setFirstResult(offset)
                .setMaxResults(page.getNumberPerPage())
                .getResultList();
        if (list != null && count > 0) {
            page.setItems(list);
            page.setRowCount(count.longValue());
        }
        return Optional.of(page);
    }

    @Override
    public Optional<Boolean> addUserToGroup(String groupIds, long userId) {
        List<GroupUser> list = entityManager.createQuery("select a from GroupUser a where a.userId = :userId", GroupUser.class)
                .setParameter("userId", userId)
                .getResultList();
        if (list != null && list.size() > 0) {
            for (GroupUser item : list) {
                entityManager.remove(item);
            }
        }
        if (groupIds != null && groupIds.length() > 0) {
            String[] arr = groupIds.split(",");
            for (String item : arr) {
                GroupUser groupUser = new GroupUser();
                groupUser.setGroupId(Long.valueOf(item));
                groupUser.setUserId(userId);
                entityManager.persist(groupUser);
            }
        }
        //delete redis cache data
        redisService.deleteCacheAuthoInfoByUserId(userId);
//            logAccessDao.addLog("Thêm người dùng vào nhóm quyền Id:" + groupIds, Constants.Log.system, ipClient);
        return Optional.of(true);

    }

    @Override
    public Optional<Group> findByTypeAndIsDetault(Integer type, long l) {
        return groupRepository.findByTypeAndIsDetault(type, l);
    }

    public Optional<List<GroupAuthority>> loadByGroupId(Long groupId, Integer type) {
        String sql = "SELECT ga.* FROM acc_group_authorities ga " +
                " JOIN acc_authorities au ON ga.authority=au.id where " +
                " ga.group_id=:groupId " +
                " AND ( upper( au.hidden ) NOT LIKE '%_ALL_%' OR au.hidden IS NULL )  " +
                " AND ( upper(au.hidden) NOT LIKE '%_" + type + "_%'  OR au.hidden IS NULL  )";
        List<GroupAuthority> list = entityManager.createNativeQuery(sql, GroupAuthority.class).setParameter("groupId", groupId).getResultList();
        return Optional.ofNullable(list);
    }

    public Optional<Long> add(Group item) {
        entityManager.persist(item);
        entityManager.flush();
        return Optional.of(item.getId());
    }

    public void genAuthority(CreateGroupRequest item, Long groupId, String username) {
        List<String> authorities = new ArrayList<>(Arrays.asList(item.getListAuthority().split(",")));
        List<GroupAuthority> groupAuthorities = new ArrayList<>();
        if (authorities.size() > 0) {
            authorities.stream().forEach(au -> groupAuthorities.add(new GroupAuthority(groupId, Long.valueOf(au), username, new Date())));
            if (item.getId() != null) {
                deleteGroupAuthority(item.getId()).orElse(false);
            }
            addListGroupAuthority(groupAuthorities).orElse(false);
        }
    }

    public Optional<Boolean> deleteGroupAuthority(Long groupId) {
        try {
            Query query = entityManager.createQuery("delete from GroupAuthority a WHERE a.groupId= :groupId").setParameter("groupId", groupId);
            int temp = query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(true);
    }

    public Optional<Boolean> addListGroupAuthority(List<GroupAuthority> items) {
        items.stream().forEach(item -> entityManager.persist(item));
        entityManager.flush();
        return Optional.of(true);
    }
}
