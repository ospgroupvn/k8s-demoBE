package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.GroupAuthority;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupAuthoritiesRepository extends BaseRepository<GroupAuthority>, JpaSpecificationExecutor<GroupAuthority> {

    @Query(value = "select count(au) from GroupAuthority au where au.groupId=:groupId")
    Long findAdmGroupAuthoritiesByGroupId(@Param("groupId") Long groupId);

    List<GroupAuthority> findAllByGroupId(Long groupId);

    GroupAuthority findByGroupIdAndAuthority(Long groupId, Long authority);
}
