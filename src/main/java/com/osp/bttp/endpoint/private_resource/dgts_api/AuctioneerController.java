package com.osp.bttp.endpoint.private_resource.dgts_api;

import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionAttachFileDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCardInfoDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCertificateInfoDto;
import com.osp.bttp.dao.model.dto.db3.AuctioneerBasicInfo;
import com.osp.bttp.dao.model.dto.db3.AuctioneerDto;
import com.osp.bttp.dao.model.dto.db3.CreateOrUpdateAuctioneerRequest;
import com.osp.bttp.dao.model.dto.db3.SearchAuctioneerRequest;
import com.osp.bttp.dao.model.type.AuCardStatus;
import com.osp.bttp.dao.model.type.AuCertStatus;
import com.osp.bttp.dao.service.dgts.AuctioneerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuctioneerController implements AuctioneerResource {

    private final AuctioneerService auctioneerService;

    @Override
    public ResponseEntity<PaginationDto<AuctioneerBasicInfo>> searchAuctioneers(String orgId,
                                                                                String auctionInfo,
                                                                                String orgInfo,
                                                                                String departmentCode,
                                                                                AuCardStatus cardStatus,
                                                                                AuCertStatus certStatus,
                                                                                Integer page, Integer size) {
        SearchAuctioneerRequest request = SearchAuctioneerRequest.builder()
                .orgId(orgId)
                .auctionInfo(auctionInfo)
                .orgInfo(orgInfo)
                .departmentCode(departmentCode)
                .cardStatus(cardStatus)
                .certStatus(certStatus)
                .build();
        return ResponseEntity.ok(auctioneerService.searchAuctioneers(request, page, size));
    }

    @Override
    public ResponseEntity<PaginationDto<AuctioneerBasicInfo>> searchAuctioneersForPublicPage(String auctioneerOrOrg,
                                                                                             String provinceCode,
                                                                                             AuCardStatus cardStatus,
                                                                                             Integer page, Integer size) {
        SearchAuctioneerRequest request = SearchAuctioneerRequest.builder()
                .provinceCode(provinceCode)
                .auctioneerOrOrg(auctioneerOrOrg)
                .cardStatus(cardStatus)
                .build();
        return ResponseEntity.ok(auctioneerService.searchAuctioneers(request, page, size));
    }

    @Override
    public ResponseEntity<AuctioneerDto> createAuctioneer(CreateOrUpdateAuctioneerRequest request) throws InternalException {
        return ResponseEntity.ok(auctioneerService.createAuctioneer(request));
    }

    @Override
    public ResponseEntity<AuctioneerDto> getAuctioneerById(String auctionId) throws InternalException {
        return ResponseEntity.ok(auctioneerService.getAuctioneerById(auctionId));
    }

    @Override
    public ResponseEntity<AuctioneerDto> getAuctioneer(String certCode) throws InternalException {
        return ResponseEntity.ok(auctioneerService.getAuctioneer(certCode));
    }

    @Override
    public ResponseEntity<AuctioneerDto> updateAuctioneer(String auctionId, CreateOrUpdateAuctioneerRequest request) throws InternalException {
        return ResponseEntity.ok(auctioneerService.updateAuctioneer(auctionId, request));
    }

    @Override
    public ResponseEntity<Void> deleteAuctioneer(List<String> auctioneerIds) throws InternalException {
        auctioneerService.deleteAuctioneer(auctioneerIds);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Boolean> checkCertCodeExist(String certCode, String certId) throws InternalException {
        return ResponseEntity.ok(auctioneerService.checkCertCodeExist(certCode, certId));
    }

    @Override
    public ResponseEntity<AuctionCertificateInfoDto> addCertInfo(String auctionId, AuctionCertificateInfoDto request) throws InternalException {
        return ResponseEntity.ok(auctioneerService.addCertInfo(auctionId, request));
    }

    @Override
    public ResponseEntity<AuctionCertificateInfoDto> updateCertInfo(String auctionId, String certId, AuctionCertificateInfoDto request) throws InternalException {
        return ResponseEntity.ok(auctioneerService.updateCertInfo(auctionId, certId, request));
    }

    @Override
    public ResponseEntity<Void> deleteCertInfo(String auctionId, List<String> certIds) throws InternalException {
        auctioneerService.deleteCertInfo(auctionId, certIds);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<AuctionAttachFileDto>> addCertAttachments(String certId, List<MultipartFile> files) throws InternalException {
        return ResponseEntity.ok(auctioneerService.addCertAttachments(certId, files));
    }

    @Override
    public ResponseEntity<Void> deleteCertAttachments(String certId, List<String> attachPaths) throws InternalException {
        auctioneerService.deleteCertAttachments(certId, attachPaths);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AuctionCardInfoDto> addCardInfo(String auctionId, AuctionCardInfoDto request) throws InternalException {
        return ResponseEntity.ok(auctioneerService.addCardInfo(auctionId, request));
    }

    @Override
    public ResponseEntity<AuctionCardInfoDto> updateCardInfo(String auctionId, String cardId, AuctionCardInfoDto request) throws InternalException {
        return ResponseEntity.ok(auctioneerService.updateCardInfo(auctionId, cardId, request));
    }

    @Override
    public ResponseEntity<Void> deleteCardInfo(String auctionId, List<String> cardIds) throws InternalException {
        auctioneerService.deleteCardInfo(auctionId, cardIds);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<AuctionAttachFileDto>> addCardAttachments(String cardId, List<MultipartFile> files) throws InternalException {
        return ResponseEntity.ok(auctioneerService.addCardAttachments(cardId, files));
    }

    @Override
    public ResponseEntity<Void> deleteCardAttachments(String cardId, List<String> attachPaths) throws InternalException {
        auctioneerService.deleteCardAttachments(cardId, attachPaths);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<byte[]> export(String auctionInfo,
                                         String orgInfo,
                                         String departmentCode,
                                         AuCardStatus cardStatus,
                                         AuCertStatus certStatus,
                                         Integer page, Integer size) throws InternalException {
        SearchAuctioneerRequest request = SearchAuctioneerRequest.builder()
                .auctionInfo(auctionInfo)
                .orgInfo(orgInfo)
                .departmentCode(departmentCode)
                .cardStatus(cardStatus)
                .certStatus(certStatus)
                .build();
        byte[] response = auctioneerService.export(request, page, size);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = formatter.format(new Date());
        String fileName = String.format("Danh_sach_ho_so_DGV_%s.xlsx", dateString);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(response);
    }
}
