package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.PlaceOfIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceOfIssueRepository extends JpaRepository<PlaceOfIssue, Integer> {

    Optional<PlaceOfIssue> findByCode(String code);
}
