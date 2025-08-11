package com.osp.bttp.dao.repository.db3;//package com.osp.ctv.dao.repository;

import com.osp.bttp.dao.model.entity.db3.GroupUser;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupUserRepository extends BaseRepository<GroupUser>, JpaSpecificationExecutor<GroupUser> {
}
