package com.osp.bttp.dao.repository;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<E extends BaseModel> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {
	
}
