package com.osp.bttp.endpoint.private_resource.dgts_api;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationBasicInfo;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationRequest;
import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCertDto;
import com.osp.bttp.dao.model.dto.db3.MemberPartnerRequest;
import com.osp.bttp.dao.model.type.OrganizationStatus;
import com.osp.bttp.dao.model.type.OrganizationType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(AuctionOrganizationResource.AUCTION_ORGANIZATION_RESOURCE)
public interface AuctionOrganizationResource {

    String AUCTION_ORGANIZATION_RESOURCE = Constants.API_VERSION1 + "/auction-organization";

    @GetMapping
    ResponseEntity<PaginationDto<AuctionOrganizationBasicInfo>> search(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "department-code", required = false) String departmentCode,
            @RequestParam(value = "types", required = false) List<OrganizationType> types,
            @RequestParam(value = "status", required = false) OrganizationStatus status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size);

    @GetMapping("/public")
    ResponseEntity<PaginationDto<AuctionOrganizationBasicInfo>> searchOrgForPublicPage(
            @RequestParam(value = "organization-name", required = false) String organizationName,
            @RequestParam(value = "province-code", required = false) String provinceCode,
            @RequestParam(value = "types", required = false) List<OrganizationType> types,
            @RequestParam(value = "status", required = false) OrganizationStatus status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size);

    @PostMapping
    ResponseEntity<AuctionOrganizationDto> create(@RequestBody AuctionOrganizationRequest request) throws InternalException;

    @GetMapping("/all")
    ResponseEntity<List<AuctionOrganizationBasicInfo>> getAll(@RequestParam(name = "department-code", required = false) String departmentCode,
                                                              @RequestParam(name = "type", required = false) List<OrganizationType> types);

    @GetMapping("/cert")
    ResponseEntity<List<AuctioneerWithCertDto>> getAllCert();

    @GetMapping("/{organizationId}/card/exist")
    ResponseEntity<Boolean> checkCardCodeExistInOrg(@PathVariable String organizationId,
                                                    @RequestParam(name = "card-id", required = false) String cardId,
                                                    @RequestParam(name = "card-code") String cardCode) throws InternalException;

    @GetMapping("/{organizationId}")
    ResponseEntity<AuctionOrganizationDto> getDetail(@PathVariable String organizationId,
                                                     @RequestParam(name = "is-basic-info", required = false) Boolean isBasicInfo) throws InternalException;

    @PutMapping("/{organizationId}")
    ResponseEntity<AuctionOrganizationDto> update(@PathVariable String organizationId,
                                                  @RequestBody AuctionOrganizationRequest request) throws InternalException;

    @PatchMapping("/{organizationId}/status")
    ResponseEntity<AuctionOrganizationDto> updateStatus(@PathVariable String organizationId,
                                                        @RequestBody AuctionOrganizationRequest request) throws InternalException;

    @PutMapping("/{organizationId}/member-partner/{memberId}")
    ResponseEntity<List<MemberPartnerRequest>> updateMemberPartner(
            @PathVariable String organizationId,
            @PathVariable String memberId,
            @RequestBody MemberPartnerRequest request) throws InternalException;

    @PostMapping("/{organizationId}/member-partner")
    ResponseEntity<List<MemberPartnerRequest>> addMemberPartner(
            @PathVariable String organizationId,
            @RequestBody List<MemberPartnerRequest> request) throws InternalException;

    @DeleteMapping("/{organizationId}/member-partner")
    ResponseEntity<Void> deleteMemberPartner(
            @PathVariable String organizationId,
            @RequestBody List<String> ids) throws InternalException;
}
