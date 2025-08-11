package com.osp.bttp.endpoint.private_resource.dgts_api;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionAttachFileDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCardInfoDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCertificateInfoDto;
import com.osp.bttp.dao.model.dto.db3.AuctioneerBasicInfo;
import com.osp.bttp.dao.model.dto.db3.AuctioneerDto;
import com.osp.bttp.dao.model.dto.db3.CreateOrUpdateAuctioneerRequest;
import com.osp.bttp.dao.model.type.AuCardStatus;
import com.osp.bttp.dao.model.type.AuCertStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping(AuctioneerResource.AUCTIONEER_RESOURCE)
public interface AuctioneerResource {

    String AUCTIONEER_RESOURCE = Constants.API_VERSION1 + "/auctioneer";

    @GetMapping
    ResponseEntity<PaginationDto<AuctioneerBasicInfo>> searchAuctioneers(
            @RequestParam(name = "org-id", required = false) String orgId,
            @RequestParam(name = "auction-info", required = false) String auctionInfo,
            @RequestParam(name = "org-info", required = false) String orgInfo,
            @RequestParam(name = "department-id", required = false) String departmentCode,
            @RequestParam(name = "card-status", required = false) AuCardStatus cardStatus,
            @RequestParam(name = "cert-status", required = false) AuCertStatus certStatus,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size);

    @GetMapping("/public")
    ResponseEntity<PaginationDto<AuctioneerBasicInfo>> searchAuctioneersForPublicPage(
            @RequestParam(name = "auctioneer-or-org", required = false) String auctioneerOrOrg,
            @RequestParam(name = "province-code", required = false) String provinceCode,
            @RequestParam(name = "card-status", required = false) AuCardStatus cardStatus,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size);

    @PostMapping
    ResponseEntity<AuctioneerDto> createAuctioneer(@RequestBody CreateOrUpdateAuctioneerRequest request) throws InternalException;

    @GetMapping("/{auctionId}")
    ResponseEntity<AuctioneerDto> getAuctioneerById(@PathVariable String auctionId) throws InternalException;

    @GetMapping("/detail")
    ResponseEntity<AuctioneerDto> getAuctioneer(
            @RequestParam(name = "cert-code") String certCode
    ) throws InternalException;

    @PutMapping("/{auctionId}")
    ResponseEntity<AuctioneerDto> updateAuctioneer(@PathVariable String auctionId,
                                                   @RequestBody CreateOrUpdateAuctioneerRequest request) throws InternalException;

    @DeleteMapping
    ResponseEntity<Void> deleteAuctioneer(@RequestBody List<String> auctioneerIds) throws InternalException;

    @GetMapping("/cert/exist")
    ResponseEntity<Boolean> checkCertCodeExist(@RequestParam(name = "cert-code") String certCode,
                                               @RequestParam(name = "cert-id", required = false) String certId) throws InternalException;

    @PostMapping("/{auctionId}/cert")
    ResponseEntity<AuctionCertificateInfoDto> addCertInfo(@PathVariable String auctionId,
                                                          @RequestBody AuctionCertificateInfoDto request) throws InternalException;

    @PutMapping("/{auctionId}/cert/{certId}")
    ResponseEntity<AuctionCertificateInfoDto> updateCertInfo(@PathVariable String auctionId,
                                                             @PathVariable String certId,
                                                             @RequestBody AuctionCertificateInfoDto request) throws InternalException;

    @DeleteMapping("/{auctionId}/cert")
    ResponseEntity<Void> deleteCertInfo(@PathVariable String auctionId,
                                        @RequestBody List<String> certIds) throws InternalException;

    @PostMapping("/cert/{certId}/attachments")
    ResponseEntity<List<AuctionAttachFileDto>> addCertAttachments(@PathVariable String certId,
                                                                  @RequestParam(name = "files") List<MultipartFile> files) throws InternalException;

    @DeleteMapping("/cert/{certId}/attachments")
    ResponseEntity<Void> deleteCertAttachments(@PathVariable String certId,
                                               @RequestBody List<String> attachPaths) throws InternalException;

    @PostMapping("/{auctionId}/card")
    ResponseEntity<AuctionCardInfoDto> addCardInfo(@PathVariable String auctionId,
                                                   @RequestBody AuctionCardInfoDto request) throws InternalException;

    @PutMapping("/{auctionId}/card/{cardId}")
    ResponseEntity<AuctionCardInfoDto> updateCardInfo(@PathVariable String auctionId,
                                                      @PathVariable String cardId,
                                                      @RequestBody AuctionCardInfoDto request) throws InternalException;

    @DeleteMapping("/{auctionId}/card")
    ResponseEntity<Void> deleteCardInfo(@PathVariable String auctionId,
                                        @RequestBody List<String> cardIds) throws InternalException;

    @PostMapping("/card/{cardId}/attachments")
    ResponseEntity<List<AuctionAttachFileDto>> addCardAttachments(@PathVariable String cardId,
                                                                  @RequestParam(name = "files") List<MultipartFile> files) throws InternalException;

    @DeleteMapping("/card/{cardId}/attachments")
    ResponseEntity<Void> deleteCardAttachments(@PathVariable String cardId,
                                               @RequestBody List<String> attachPaths) throws InternalException;

    @GetMapping("/export")
    ResponseEntity<byte[]> export(
            @RequestParam(name = "auction-info", required = false) String auctionInfo,
            @RequestParam(name = "org-info", required = false) String orgInfo,
            @RequestParam(name = "department-id", required = false) String departmentCode,
            @RequestParam(name = "card-status", required = false) AuCardStatus cardStatus,
            @RequestParam(name = "cert-status", required = false) AuCertStatus certStatus,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) throws InternalException;

}
