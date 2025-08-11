package com.osp.bttp.dao.repository.bttp;

import com.osp.bttp.dao.model.entity.db3.NotaryAppoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotaryAppointRepository extends JpaRepository<NotaryAppoint, Long> {
    List<NotaryAppoint> findByNotaryInfoIdOrderByLastUpdate(Long notaryId);
    // 1: bổ nhiệm , 2: miễn nhiệm , 3 : bổ nhiệm lại
    @Query(value = """
            SELECT
                doc.date_sign AS dateSign,
                doc.dispatch_code AS dispatchCode,
                doc.effective_date AS effectiveDate,
                doc.signer AS signer,
                doc.file_name AS fileName,
                doc.link_file AS linkFile,
                re.notary_info_id AS notaryInfoId,
                       re.active as active,
                re.reason AS reason,
                re.id AS id,
                re.type_appoint AS type,
                re.kind AS kind,  
                re.STATUS_APPOINT ,
                doc.id as documentId
            FROM notary_appoint re
            JOIN dm_document doc ON re.document_id = doc.id
            WHERE re.notary_info_id = :idNotary
            
            """, nativeQuery = true)
    List<Object[]> getNotaryHistoryRaw(@Param("idNotary") Long idNotary);

}
