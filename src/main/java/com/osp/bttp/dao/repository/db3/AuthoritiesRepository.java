package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.Authority;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthoritiesRepository extends BaseRepository<Authority>, JpaSpecificationExecutor<Authority> {

    @Query(value="select au from Authority au where au.fid = 0")
    List<Authority> loadListParent();

    @Query(value="select au from Authority au where au.fid = 0 and au.id=:id")
    Authority checkParent(@Param("id") Long id);

    @Query(value="select au from Authority au where au.fid=:id or au.id=:id")
    List<Authority> getAllByParentId(@Param("id") Long id);

    @Query(value="select au from Authority au where au.fid not in (0) ")
    List<Authority> loadListChild();

    @Query(value="SELECT DISTINCT au.* FROM ACC_AUTHORITIES au JOIN ACC_GROUP_AUTHORITIES ga ON au.id=ga.AUTHORITY JOIN ACC_GROUP gr ON gr.id=ga.group_Id \n" +
            "WHERE gr.ID=:groupId and au.PARENT_ID=0 and ga.STATUS=0 ", nativeQuery = true)
    List<Authority> loadListParentByGroupId(@Param("groupId") Long groupId);

    @Query(value="SELECT DISTINCT au.* FROM ACC_AUTHORITIES au JOIN ACC_GROUP_AUTHORITIES ga ON au.id=ga.AUTHORITY JOIN ACC_GROUP gr ON gr.id=ga.group_Id \n" +
            "WHERE gr.ID=:groupId and au.PARENT_ID not in (0) and ga.STATUS=0 ", nativeQuery = true)
    List<Authority> loadListChildByGroupId(@Param("groupId") Long groupId);

    @Query(value="select au from Authority au where au.id in (:ids)")
    List<Authority> loadByListIds(@Param("ids") List<Long> ids);

    @Query(value="SELECT DISTINCT au.* FROM ACC_AUTHORITIES au JOIN ACC_GROUP_AUTHORITIES ga ON au.id=ga.AUTHORITY JOIN ACC_GROUP gr ON gr.id=ga.group_Id \n" +
            "WHERE (gr.ID=:groupId and au.PARENT_ID=0 and ga.STATUS=0) or " +
            "(au.ID in (SELECT DISTINCT au.PARENT_ID FROM ACC_AUTHORITIES au JOIN ACC_GROUP_AUTHORITIES ga ON au.id=ga.AUTHORITY WHERE ga.GROUP_ID=:groupId and au.PARENT_ID not in (0) and ga.STATUS=0))", nativeQuery = true)
    List<Authority> loadListParentActiveByGroupId(@Param("groupId") Long groupId);

    @Query("select au from Authority au where au.authKey=:authKey")
    List<Authority> findAllByAuthKey(String authKey);
}
