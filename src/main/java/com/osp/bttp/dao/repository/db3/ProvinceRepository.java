package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, String> {

    Optional<Province> findByProvinceCode(String provinceCode);

    Optional<Province> findByCode(String code);

    List<Province> findAllByProvinceCodeIn(List<String> provinceCodes);
}
