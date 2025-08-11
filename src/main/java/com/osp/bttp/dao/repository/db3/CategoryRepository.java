package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.Category;
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
public interface CategoryRepository extends BaseRepository<Category>, JpaSpecificationExecutor<Category> {
    @Query(value = "SELECT u FROM Category u where u.code = :code and u.catType = :catType")
    Optional<Category> getByCodeAndCatType(Long code, String catType);
}
