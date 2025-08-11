package com.osp.bttp.dao.service.dgts;

import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionAttachFileDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCardInfoDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCertificateInfoDto;
import com.osp.bttp.dao.model.dto.db3.AuctioneerBasicInfo;
import com.osp.bttp.dao.model.dto.db3.AuctioneerDto;
import com.osp.bttp.dao.model.dto.db3.CreateOrUpdateAuctioneerRequest;
import com.osp.bttp.dao.model.dto.db3.SearchAuctioneerRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AuctioneerService {

    Optional<PagingResult> searchAuctioneer(PagingResult page, int numberPerPage, String fullname, Long province, String cerCode, Long cardStatus, Long sex, Long publishStatus, Long cerStatus, Long actType, Long other, Long orgId);

    PaginationDto<AuctioneerBasicInfo> searchAuctioneers(SearchAuctioneerRequest request, Integer page, Integer size);

    AuctioneerDto createAuctioneer(CreateOrUpdateAuctioneerRequest request) throws InternalException;

    AuctioneerDto updateAuctioneer(String auctionId, CreateOrUpdateAuctioneerRequest request) throws InternalException;

    AuctionCertificateInfoDto addCertInfo(String auctionId, AuctionCertificateInfoDto request) throws InternalException;

    AuctionCertificateInfoDto updateCertInfo(String auctionId, String certId, AuctionCertificateInfoDto request) throws InternalException;

    void deleteCertInfo(String auctionId, List<String> certIds) throws InternalException;

    AuctionCardInfoDto addCardInfo(String auctionId, AuctionCardInfoDto request) throws InternalException;

    AuctionCardInfoDto updateCardInfo(String auctionId, String cardId, AuctionCardInfoDto request) throws InternalException;

    void deleteCardInfo(String auctionId, List<String> cardIds) throws InternalException;

    List<AuctionAttachFileDto> addCertAttachments(String certId, List<MultipartFile> files) throws InternalException;

    void deleteCertAttachments(String certId, List<String> attachPaths) throws InternalException;

    List<AuctionAttachFileDto> addCardAttachments(String cardId, List<MultipartFile> files) throws InternalException;

    void deleteCardAttachments(String cardId, List<String> attachPaths) throws InternalException;

    AuctioneerDto getAuctioneerById(String auctionId) throws InternalException;

    void deleteAuctioneer(List<String> auctioneerIds) throws InternalException;

    byte[] export(SearchAuctioneerRequest request, Integer page, Integer size);

    AuctioneerDto getAuctioneer(String certCode) throws InternalException;

    Boolean checkCertCodeExist(String certCode, String certId) throws InternalException;
}
