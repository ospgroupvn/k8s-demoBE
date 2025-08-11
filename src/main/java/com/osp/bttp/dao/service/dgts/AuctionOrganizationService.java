package com.osp.bttp.dao.service.dgts;

import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationBasicInfo;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationRequest;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationSearchReq;
import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCertDto;
import com.osp.bttp.dao.model.dto.db3.MemberPartnerRequest;
import com.osp.bttp.dao.model.type.OrganizationStatus;

import java.util.List;

public interface AuctionOrganizationService {

    PaginationDto<AuctionOrganizationBasicInfo> search(AuctionOrganizationSearchReq req, Integer page, Integer size);

    AuctionOrganizationDto create(AuctionOrganizationRequest request) throws InternalException;

    AuctionOrganizationDto getDetail(String organizationId, Boolean isBasicInfo) throws InternalException;

    List<AuctionOrganizationBasicInfo> getAll(AuctionOrganizationSearchReq req);

    AuctionOrganizationDto update(String organizationId, AuctionOrganizationRequest request) throws InternalException;

    AuctionOrganizationDto updateStatus(String organizationId, OrganizationStatus status) throws InternalException;

    void updateMemberPartner(String organizationId, String memberId, MemberPartnerRequest request) throws InternalException;

    void addMemberPartner(String organizationId, List<MemberPartnerRequest> request) throws InternalException;

    void deleteMemberPartner(String organizationId, List<String> ids) throws InternalException;

    List<AuctioneerWithCertDto> getAllCert();

    Boolean checkCardCodeExistInOrg(String organizationId, String cardId, String cardCode) throws InternalException;
}
