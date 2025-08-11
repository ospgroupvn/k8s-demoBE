package com.osp.bttp.dao.repository.db3;//package com.osp.ctv.dao.repository;

import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccUserRepository extends BaseRepository<AccUser>, JpaSpecificationExecutor<AccUser> {
    AccUser findByUsername(@Param("username") String userName);

    AccUser findByMobile(String mobile);

    Optional<AccUser> findById(Long id);

    @Query(value = "SELECT u FROM AccUser u where 1=1 and u.username=:username")
    AccUser findOneByUsername(@Param("username") String username);

    @Query(value = "SELECT u FROM AccUser u where 1=1 and u.jwt=:jwt")
    AccUser findOneByJwt(@Param("jwt") String jwt);


    @Transactional(readOnly = true)
    @Query(value = "SELECT u FROM AccUser u where 1=1 and u.username=:username")
    Optional<AccUser> findByUsernameNew(String username);


    @Query(value = "SELECT u FROM AccUser u where 1=1 and u.username in :usernames")
    List<AccUser> findByUsernameIn(List<String> usernames);

    @Query(value = "SELECT u.username FROM AccUser u where 1=1 and u.id in :listUser")
    List<String> findAllByIds(List<Long> listUser);

    @Query(value = "SELECT u FROM AccUser u where 1=1 and u.username=:username")
    Optional<AccUser> findByUsernameOpt(String username);
}
