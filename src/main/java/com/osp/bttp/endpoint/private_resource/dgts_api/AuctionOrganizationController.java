package com.osp.bttp.endpoint.private_resource.dgts_api;

import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationBasicInfo;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationRequest;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationSearchReq;
import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCertDto;
import com.osp.bttp.dao.model.dto.db3.MemberPartnerRequest;
import com.osp.bttp.dao.model.type.OrganizationStatus;
import com.osp.bttp.dao.model.type.OrganizationType;
import com.osp.bttp.dao.service.dgts.AuctionOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuctionOrganizationController implements AuctionOrganizationResource {

    private final AuctionOrganizationService auctionOrganizationService;

    @Override
    public ResponseEntity<PaginationDto<AuctionOrganizationBasicInfo>> search(String text,
                                                                              String departmentCode,
                                                                              List<OrganizationType> types,
                                                                              OrganizationStatus status,
                                                                              Integer page, Integer size) {
        AuctionOrganizationSearchReq req = AuctionOrganizationSearchReq.builder()
                .text(text)
                .departmentCode(departmentCode)
                .status(status)
                .types(types)
                .build();
        return ResponseEntity.ok(auctionOrganizationService.search(req, page, size));
    }

    @Override
    public ResponseEntity<PaginationDto<AuctionOrganizationBasicInfo>> searchOrgForPublicPage(String organizationName,
                                                                                              String provinceCode,
                                                                                              List<OrganizationType> types,
                                                                                              OrganizationStatus status,
                                                                                              Integer page, Integer size) {
        AuctionOrganizationSearchReq req = AuctionOrganizationSearchReq.builder()
                .organizationName(organizationName)
                .provinceCode(provinceCode)
                .status(status)
                .types(types)
                .build();
        return ResponseEntity.ok(auctionOrganizationService.search(req, page, size));
    }

    @Override
    public ResponseEntity<AuctionOrganizationDto> create(AuctionOrganizationRequest request) throws InternalException {
        return ResponseEntity.ok(auctionOrganizationService.create(request));
    }

    @Override
    public ResponseEntity<List<AuctionOrganizationBasicInfo>> getAll(String departmentCode, List<OrganizationType> types) {
        AuctionOrganizationSearchReq req = AuctionOrganizationSearchReq.builder()
                .departmentCode(departmentCode)
                .types(types)
                .build();
        return ResponseEntity.ok(auctionOrganizationService.getAll(req));
    }

    @Override
    public ResponseEntity<List<AuctioneerWithCertDto>> getAllCert() {
        return ResponseEntity.ok(auctionOrganizationService.getAllCert());
    }

    @Override
    public ResponseEntity<Boolean> checkCardCodeExistInOrg(String organizationId, String cardId, String cardCode) throws InternalException {
        return ResponseEntity.ok(auctionOrganizationService.checkCardCodeExistInOrg(organizationId, cardId, cardCode));
    }

    @Override
    public ResponseEntity<AuctionOrganizationDto> getDetail(String organizationId, Boolean isBasicInfo) throws InternalException {
        return ResponseEntity.ok(auctionOrganizationService.getDetail(organizationId, isBasicInfo));
    }

    @Override
    public ResponseEntity<AuctionOrganizationDto> update(String organizationId, AuctionOrganizationRequest request) throws InternalException {
        return ResponseEntity.ok(auctionOrganizationService.update(organizationId, request));
    }

    @Override
    public ResponseEntity<AuctionOrganizationDto> updateStatus(String organizationId, AuctionOrganizationRequest request) throws InternalException {
        return ResponseEntity.ok(auctionOrganizationService.updateStatus(organizationId, request.getStatus()));
    }

    @Override
    public ResponseEntity<List<MemberPartnerRequest>> updateMemberPartner(String organizationId,
                                                                          String memberId,
                                                                          MemberPartnerRequest request) throws InternalException {
        auctionOrganizationService.updateMemberPartner(organizationId, memberId, request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<MemberPartnerRequest>> addMemberPartner(String organizationId, List<MemberPartnerRequest> request) throws InternalException {
        auctionOrganizationService.addMemberPartner(organizationId, request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteMemberPartner(String organizationId, List<String> ids) throws InternalException {
        auctionOrganizationService.deleteMemberPartner(organizationId, ids);
        return ResponseEntity.ok().build();
    }
}
