package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WardRepository extends JpaRepository<Ward, Integer> {

    Optional<Ward> findByWardCode(String wardCode);

    List<Ward> findByProvinceCode(String provinceCode);

    List<Ward> findAllByWardCodeIn(List<String> wardCodes);
}
