package com.osp.bttp.dao.repository.bttp;

import com.osp.bttp.dao.model.entity.db3.NotaryInfo;
import com.osp.bttp.dao.model.mview.bttp.NotaryAppointResponse;
import com.osp.bttp.dao.model.mview.bttp.NotaryChiefResponse;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface NotaryInfoRepository extends JpaRepository<NotaryInfo, Long> {

                    @Query("""
                                SELECT ni FROM NotaryInfo ni 
                                WHERE NOT EXISTS (
                                    SELECT 1 FROM OrgNotaryInfo org WHERE org.notaryIdOfficeChief = ni.id
                                )
                                  and  EXISTS (
                                    SELECT 1 FROM NotaryRegPractice nrp WHERE nrp.notaryInfoId = ni.id
                                )
                                   and ni.id = :id
                            
                            """)
                    Optional<NotaryInfo> findNotaryOfficeChief(@Param("id") Long id);

                    @Query("""
                    SELECT new com.osp.bttp.dao.model.mview.bttp.NotaryChiefResponse(ni.id,ni.name,nrp.numberCad,ni.idNo) FROM NotaryInfo ni 
                    join NotaryRegPractice nrp on nrp.notaryInfoId = ni.id
                                   left join DmDocument dm on dm.id = nrp.id
                    WHERE NOT EXISTS (
                        SELECT 1 FROM OrgNotaryInfo org WHERE org.notaryIdOfficeChief = ni.id
                    )
                     AND ni.idNo is not null
                    AND LOWER(ni.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(nrp.numberCad) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(ni.idNo) LIKE LOWER(CONCAT('%', :name, '%'))
                """)
                    Page<NotaryChiefResponse> findNotaryOfficeChiefs(@Param("name") String name, Pageable pageable);

                    @Query("""
                    SELECT ni FROM NotaryInfo ni 
                    WHERE (
                        EXISTS (SELECT 1 FROM OrgNotaryInfo org WHERE org.notaryIdOfficeChief = ni.id)
                        OR EXISTS (SELECT 1 FROM NotaryRegPractice nr WHERE nr.notaryInfoId = ni.id)
                        OR EXISTS (SELECT 1 FROM NotaryAppoint na WHERE na.notaryInfoId = ni.id)
                        OR EXISTS (SELECT 1 FROM NotarySuspendWork ns WHERE ns.notaryInfoId = ni.id)
                        OR EXISTS (SELECT 1 FROM NotaryPenalize np WHERE np.notaryInfoId = ni.id)
                        OR EXISTS (SELECT 1 FROM ProbationaryInfo pi WHERE pi.notaryInfoId = ni.id)
                    )
                    AND ni.id = :id
                """)
                    Optional<NotaryInfo> checkDeleteCommonNotary(@Param("id") Long id);
                @Query("""
                                SELECT ni FROM NotaryInfo ni 
                                WHERE  EXISTS (
                                    SELECT 1 FROM OrgNotaryInfo org WHERE org.notaryIdOfficeChief = ni.id
                                )
                                  and  EXISTS (
                                    SELECT 1 FROM NotaryRegPractice nrp WHERE nrp.notaryInfoId = ni.id
                                )
                                   and ni.id = :id
                            
                            """)
                Optional<NotaryInfo> checkNotaryIsChief(@Param("id") Long id);


                List<NotaryInfo> findAllByIdNo(String idNo);

                List<NotaryInfo> findAllByIdNoAndIdNot( String idNo, Long id);

}
