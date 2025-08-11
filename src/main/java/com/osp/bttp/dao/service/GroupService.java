package com.osp.bttp.dao.service;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.request.CreateGroupRequest;
import com.osp.bttp.dao.model.entity.db3.Authority;
import com.osp.bttp.dao.model.entity.db3.Group;
import com.osp.bttp.dao.model.mview.GroupView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sangnk
 * @Created 09/10/2024 - 8:55 SA
 * @project = bttp
 * @_ Mô tả:
 */
public interface GroupService {
    List<String> loadListAuthorityByUsername(String username);

    List<String> loadListAuthorityOfUserByUserIdAll(Long userId);

    ApiResponseV1<?> checkBeforeAdd(CreateGroupRequest item);

    Optional<Boolean> saveGroupView(CreateGroupRequest item);

    Optional<PagingResult> page(String filterName, PagingResult page, Integer type);

    Optional<GroupView> getGroupView(Long id);

    Optional<List<Authority>> loadAllAuthority();

    ApiResponseV1<?> checkBeforeEdit(CreateGroupRequest item);

    Optional<Boolean> editGroupView(CreateGroupRequest item);

    Optional<Group> get(Long id);

    Optional<Long> deleteGroup(Long id);

    Optional<PagingResult> pageUserOfGroup(Long groupId, PagingResult page);

    Optional<PagingResult> pageAuthorityOfGroup(long groupId, PagingResult page);

    Optional<Boolean> addUserToGroup(String groupIds, long userId);

    Optional<Group> findByTypeAndIsDetault(Integer type, long l);
}
