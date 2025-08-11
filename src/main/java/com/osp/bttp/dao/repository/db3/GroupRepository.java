package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.Group;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends BaseRepository<Group>, JpaSpecificationExecutor<Group> {

 List<Group> findAllByGroupNameIs(String groupName);

 @Query(value = "SELECT DISTINCT au.authority  " +
         "FROM Authority au " +
         "JOIN GroupAuthority ga ON au.id=ga.authority " +
         "JOIN Group gr ON gr.id=ga.groupId " +
         "JOIN GroupUser gru ON gr.id = gru.groupId " +
         "JOIN AccUser user ON gru.userId = user.id " +
         "WHERE user.username=:username ")
 List<String> loadListAuthorityByUsername(String username);

 @Query(value = "select au from Group au where au.id in (:ids)")
 List<Group> loadByListIds(@Param("ids") List<Long> ids);

 @Query(value = "SELECT DISTINCT au.authority " +
         "FROM Authority au JOIN GroupAuthority ga ON au.id=ga.authority JOIN Group gr ON gr.id=ga.groupId WHERE gr.status=1")
 List<String> loadListAuthority();


 @Query(value = "SELECT au.authKey  " +
         "FROM Authority au " +
         "JOIN GroupAuthority ga ON au.id=ga.authority " +
         "JOIN Group gr ON gr.id=ga.groupId " +
         "WHERE gr.id=:id group by au.authKey")
 List<String> loadListAuthorityById(@Param("id") Long id);


 @Query(value = "SELECT DISTINCT au.authority  " +
         "FROM Authority au " +
         "JOIN GroupAuthority ga ON au.id=ga.authority " +
         "JOIN Group gr ON gr.id=ga.groupId " +
         "WHERE gr.groupName=:name ")
 List<String> loadListAuthorityByName(@Param("name") String name);


 @Query(value = "SELECT gr FROM Group gr WHERE gr.type=:type AND gr.isDefault=:isDefault AND gr.source=:source")
 Optional<Group> findByTypeAndIsDetault(Integer type, Long isDefault, String source);
 @Query(value = "SELECT gr FROM Group gr WHERE gr.type=:type AND gr.isDefault=:isDefault")
 Optional<Group> findByTypeAndIsDetault(Integer type, Long isDefault);

 @Query(value = "SELECT gr FROM Group gr WHERE gr.type=:type AND gr.isDefault=:isDefault AND gr.source=:source")
 Optional<Group> findByTypeAndIsDetaultAndSource(Integer type, Long isDefault, String source);
}
