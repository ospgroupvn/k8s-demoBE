package com.osp.bttp.dao.repository.bttp;

import com.osp.bttp.dao.model.entity.db3.DmDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DmDocumentRepository extends JpaRepository<DmDocument, Long> {

    List<DmDocument> findByDispatchCode(String dispatchCode);

    List<DmDocument> findByDispatchCodeAndIdNot(String dispatchCode, Long id);

    Optional<DmDocument> findByLinkFile(String path);
}
