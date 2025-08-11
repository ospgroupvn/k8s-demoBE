package com.osp.bttp.dao.repository.db4;

import com.osp.bttp.dao.model.entity.db4.LCategory;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author sangnk
 * @Created 15/10/2024 - 9:40 SA
 * @project = bttp
 * @_ Mô tả:
 */
public interface LCategoryRepository extends BaseRepository<LCategory>, JpaSpecificationExecutor<LCategory> {
    //    @Query(value = "SELECT u FROM Category u where u.code = :code and u.catType = :catType")
//    Optional<LCategory> getByCodeAndCatType(Long code, String catType);
    LCategory findByCatTypeAndNameContaining(String type, String name);

    Long id(Long id);
}
