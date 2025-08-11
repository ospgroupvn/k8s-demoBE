package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.DeptOfJustice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeptOfJusticeRepository extends JpaRepository<DeptOfJustice, Integer> {

    Optional<DeptOfJustice> findByCode(String code);

    List<DeptOfJustice> findAllByCodeIn(List<String> codes);
}
