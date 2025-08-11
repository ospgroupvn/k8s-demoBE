package com.osp.bttp.dao.service.dgts.impl;

import com.osp.bttp.common.annotation.Require;
import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.common.utils.PaginationUtil;
import com.osp.bttp.common.utils.ValidationUtil;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationBasicInfo;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationRequest;
import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationSearchReq;
import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCertDto;
import com.osp.bttp.dao.model.dto.db3.MemberPartnerDto;
import com.osp.bttp.dao.model.dto.db3.MemberPartnerRequest;
import com.osp.bttp.dao.model.entity.db3.AuctionCardInfo;
import com.osp.bttp.dao.model.entity.db3.AuctionCertificateInfo;
import com.osp.bttp.dao.model.entity.db3.AuctionOrganization;
import com.osp.bttp.dao.model.entity.db3.Auctioneer;
import com.osp.bttp.dao.model.entity.db3.DeptOfJustice;
import com.osp.bttp.dao.model.entity.db3.MemberPartner;
import com.osp.bttp.dao.model.entity.db3.PlaceOfIssue;
import com.osp.bttp.dao.model.entity.db3.Province;
import com.osp.bttp.dao.model.entity.db3.Ward;
import com.osp.bttp.dao.model.mapper.db3.AuctionOrganizationMapper;
import com.osp.bttp.dao.model.type.OrganizationStatus;
import com.osp.bttp.dao.model.type.OrganizationType;
import com.osp.bttp.dao.repository.db3.AuctionCardInfoRepository;
import com.osp.bttp.dao.repository.db3.AuctionCertificateInfoRepository;
import com.osp.bttp.dao.repository.db3.AuctionOrganizationRepository;
import com.osp.bttp.dao.repository.db3.AuctioneerRepository;
import com.osp.bttp.dao.repository.db3.DeptOfJusticeRepository;
import com.osp.bttp.dao.repository.db3.MemberPartnerRepository;
import com.osp.bttp.dao.repository.db3.PlaceOfIssueRepository;
import com.osp.bttp.dao.repository.db3.ProvinceRepository;
import com.osp.bttp.dao.repository.db3.WardRepository;
import com.osp.bttp.dao.repository.db3.predicate.AuctionCardInfoPredicate;
import com.osp.bttp.dao.repository.db3.predicate.AuctionCertificateInfoPredicate;
import com.osp.bttp.dao.repository.db3.predicate.AuctionOrganizationPredicate;
import com.osp.bttp.dao.repository.db3.predicate.BasePredicate;
import com.osp.bttp.dao.repository.db3.predicate.MemberPartnerPredicate;
import com.osp.bttp.dao.service.dgts.AuctionOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionOrganizationImpl implements AuctionOrganizationService {

    private final AuctionOrganizationRepository auctionOrganizationRepository;

    private final MemberPartnerRepository memberPartnerRepository;

    private final AuctionOrganizationMapper auctionOrganizationMapper;

    private final AuctioneerRepository auctioneerRepository;

    private final WardRepository wardRepository;

    private final ProvinceRepository provinceRepository;

    private final DeptOfJusticeRepository deptOfJusticeRepository;

    private final AuctionCardInfoRepository auctionCardInfoRepository;

    private final PlaceOfIssueRepository placeOfIssueRepository;

    private final AuctionCertificateInfoRepository auctionCertificateInfoRepository;

    @Override
    public PaginationDto<AuctionOrganizationBasicInfo> search(AuctionOrganizationSearchReq req, Integer page, Integer size) {

        List<Integer> orgTypeCodes = new ArrayList<>();
        if (req.getTypes() != null && !req.getTypes().isEmpty()) {
            req.getTypes().forEach(type -> orgTypeCodes.add(type.getCode()));
        }

        Pageable pageable = PaginationUtil.init(page, size, BasePredicate.DEFAULT_ORDER_PROPERTY);
        AuctionOrganizationPredicate predicate = new AuctionOrganizationPredicate()
                .withText(req.getText())
                .withStatus(req.getStatus())
                .withProvinceCode(req.getProvinceCode())
                .withOrgName(req.getOrganizationName())
                .withTypes(orgTypeCodes);
        if (StringUtils.hasText(req.getDepartmentCode())) {
            Optional<Province> provinceOpt = provinceRepository.findByCode(req.getDepartmentCode());
            if (provinceOpt.isPresent()) {
                predicate = predicate.withProvinceCode(provinceOpt.get().getProvinceCode());
            }
        }

        Page<AuctionOrganization> organizationPage = auctionOrganizationRepository.findAll(predicate.getCriteria(), pageable);

        List<String> wardCodes = new ArrayList<>();
        List<String> provinceCodes = new ArrayList<>();
        List<String> managerUuids = new ArrayList<>();
        List<String> organizationUuids = new ArrayList<>();
        organizationPage.getContent().forEach(organization -> {
            wardCodes.add(organization.getWardCode());
            provinceCodes.add(organization.getProvinceCode());
            managerUuids.add(organization.getManagerUuid());
            organizationUuids.add(organization.getUuid());
        });

        // 1. Manager
        Map<String, Auctioneer> managerMap = new HashMap<>();
        List<Auctioneer> managers = auctioneerRepository.findAllById(managerUuids);
        managers.forEach(manager -> managerMap.put(manager.getUuid(), manager));
        // 2. Ward
        Map<String, Ward> wardMap = new HashMap<>();
        List<Ward> wards = wardRepository.findAllByWardCodeIn(wardCodes);
        wards.forEach(ward -> wardMap.put(ward.getWardCode(), ward));
        // 3. Province
        Map<String, Province> provinceMap = new HashMap<>();
        List<String> deptOfJusticeCodes = new ArrayList<>();
        List<Province> provinces = provinceRepository.findAllByProvinceCodeIn(provinceCodes);
        provinces.forEach(province -> {
            provinceMap.put(province.getProvinceCode(), province);
            deptOfJusticeCodes.add(province.getCode());
        });
        // 4. Dept of Justice
        Map<String, DeptOfJustice> deptOfJusticeMap = new HashMap<>();
        List<DeptOfJustice> deptOfJustices = deptOfJusticeRepository.findAllByCodeIn(deptOfJusticeCodes);
        deptOfJustices.forEach(deptOfJustice -> deptOfJusticeMap.put(deptOfJustice.getCode(), deptOfJustice));
        // 5. Auctioneer count
        Map<String, Long> auctioneerCountMap = auctionCardInfoRepository.countCardsByOrganization(organizationUuids);

        List<AuctionOrganizationBasicInfo> ret = new ArrayList<>();
        for (AuctionOrganization organization : organizationPage.getContent()) {
            ret.add(getOrgBasicInfo(organization, deptOfJusticeMap, managerMap, wardMap, provinceMap, auctioneerCountMap));
        }
        return new PaginationDto<>(ret, organizationPage.getTotalElements(), organizationPage.getTotalPages());
    }

    private AuctionOrganizationBasicInfo getOrgBasicInfo(AuctionOrganization organization,
                                                         Map<String, DeptOfJustice> deptOfJusticeMap,
                                                         Map<String, Auctioneer> managerMap,
                                                         Map<String, Ward> wardMap,
                                                         Map<String, Province> provinceMap,
                                                         Map<String, Long> auctioneerCountMap) {
        AuctionOrganizationBasicInfo ret = AuctionOrganizationBasicInfo.builder()
                .uuid(organization.getUuid())
                .fullName(organization.getFullName())
                .status(organization.getStatus())
                .address(organization.getAddress())
                .build();

        if (managerMap.containsKey(organization.getManagerUuid())) {
            ret.setManagerUuid(managerMap.get(organization.getManagerUuid()).getUuid());
            ret.setManagerName(managerMap.get(organization.getManagerUuid()).getFullName());
        }

        if (wardMap.containsKey(organization.getWardCode())) {
            ret.setWardCode(wardMap.get(organization.getWardCode()).getWardCode());
            ret.setWardName(wardMap.get(organization.getWardCode()).getName());
        }

        if (provinceMap.containsKey(organization.getProvinceCode())) {
            Province province = provinceMap.get(organization.getProvinceCode());
            ret.setProvinceCode(province.getProvinceCode());
            ret.setProvinceName(province.getName());

            if (deptOfJusticeMap.containsKey(province.getCode())) {
                ret.setDepartmentCode(deptOfJusticeMap.get(province.getCode()).getCode());
                ret.setDepartmentName(deptOfJusticeMap.get(province.getCode()).getName());
            }
        }

        if (auctioneerCountMap.containsKey(organization.getUuid())) {
            ret.setAuctioneerCount(auctioneerCountMap.getOrDefault(organization.getUuid(), 0L));
        } else {
            ret.setAuctioneerCount(0L);
        }
        return ret;
    }

    @Override
    @Transactional
    public AuctionOrganizationDto create(AuctionOrganizationRequest request) throws InternalException {

        validateOrganizationRequest(request);

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(request.getManagerUuid());
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException("Manager not found with UUID: " + request.getManagerUuid());
        }

        AuctionOrganization auctionOrganization = AuctionOrganization.builder()
                .orgType(request.getType().getCode())
                .fullName(request.getFullName())
                .licenseNo(request.getLicenseNo())
                .licenseDate(request.getLicenseDate())
                .telNumber(request.getTelNumber())
                .address(request.getAddress())
                .email(request.getEmail())
                .status(request.getStatus())
                .provinceCode(request.getProvinceCode())
                .wardCode(request.getWardCode())
                .build();

        auctionOrganization.setManagerUuid(auctioneerOpt.get().getUuid());

        switch (request.getType()) {
            case AUCTION_SERVICE_CENTER:
            case PRIVATE_AUCTION_ENTERPRISE:
                break;
            case PARTNERSHIP_AUCTION_COMPANY:
                List<MemberPartnerRequest> memberPartnerRequests = request.getMemberPartners();
                List<MemberPartner> memberPartners = createMemberPartnersFromReq(memberPartnerRequests, auctionOrganization);

                if (!memberPartners.isEmpty()) {
                    auctionOrganization = auctionOrganizationRepository.save(auctionOrganization);
                    memberPartners = memberPartnerRepository.saveAll(memberPartners);
                    auctionOrganization.setMemberPartners(memberPartners);
                }
                break;
            case AUCTION_BRANCH:
                Optional<AuctionOrganization> auctionOrganizationParent = auctionOrganizationRepository.findById(request.getOrgRoot());
                if (auctionOrganizationParent.isEmpty()) {
                    throw new InternalException("Parent organization not found with ID: " + request.getOrgRoot());
                }
                auctionOrganization.setOrgRoot(auctionOrganizationParent.get().getUuid());
                break;
        }
        auctionOrganization = auctionOrganizationRepository.save(auctionOrganization);
        return auctionOrganizationMapper.toDto(auctionOrganization);
    }

    private List<MemberPartner> createMemberPartnersFromReq(List<MemberPartnerRequest> requests, AuctionOrganization auctionOrganization) {
        List<MemberPartner> memberPartners = new ArrayList<>();
        for (MemberPartnerRequest memberPartnerRequest : requests) {
            if (StringUtils.hasText(memberPartnerRequest.getFullName()) && StringUtils.hasText(memberPartnerRequest.getDob())) {
                MemberPartner memberPartner = MemberPartner.builder()
                        .fullName(memberPartnerRequest.getFullName())
                        .dob(memberPartnerRequest.getDob())
                        .certCode(memberPartnerRequest.getCertCode())
                        .dateOfDecision(memberPartnerRequest.getDateOfDecision())
                        .organization(auctionOrganization)
                        .build();
                memberPartners.add(memberPartner);
            }
        }
        return memberPartners;
    }

    private void validateOrganizationRequest(AuctionOrganizationRequest request) throws InternalException {
        List<String> invalidFields = ValidationUtil.verifyCondition(request, Require.class);
        if (!invalidFields.isEmpty()) {
            throw new InternalException("Invalid request", invalidFields);
        }

        Optional<Ward> wardOpt = wardRepository.findByWardCode(request.getWardCode());
        if (wardOpt.isEmpty() || !wardOpt.get().getWardCode().equals(request.getWardCode())
                || !wardOpt.get().getProvinceCode().equals(request.getProvinceCode())) {
            throw new InternalException("Invalid ward code or province code");
        }
    }

    @Override
    public AuctionOrganizationDto getDetail(String organizationId, Boolean isBasicInfo) throws InternalException {

        Optional<AuctionOrganization> auctionOrganizationOpt = auctionOrganizationRepository.findById(organizationId);
        if (auctionOrganizationOpt.isEmpty()) {
            throw new InternalException("Organization not found with ID: " + organizationId);
        }
        AuctionOrganization auctionOrganization = auctionOrganizationOpt.get();
        AuctionOrganizationDto ret = auctionOrganizationMapper.toDto(auctionOrganization);

        Optional<Ward> wardOpt = wardRepository.findByWardCode(auctionOrganization.getWardCode());
        wardOpt.ifPresent(ward -> ret.setWardName(ward.getName()));

        Optional<Province> provinceOpt = provinceRepository.findByProvinceCode(auctionOrganization.getProvinceCode());
        provinceOpt.ifPresent(province -> ret.setProvinceName(province.getName()));

        if (ret.getType() == OrganizationType.AUCTION_BRANCH && StringUtils.hasText(auctionOrganization.getOrgRoot())) {
            Optional<AuctionOrganization> orgRootOpt = auctionOrganizationRepository.findById(auctionOrganization.getOrgRoot());
            if (orgRootOpt.isPresent()) {
                AuctionOrganization auctionOrgRoot = orgRootOpt.get();
                ret.setOrgRoot(auctionOrgRoot.getUuid());
                ret.setOrgRootName(auctionOrgRoot.getFullName());

                Optional<Province> provinceOrgRootOpt = provinceRepository.findByProvinceCode(auctionOrgRoot.getProvinceCode());

                if (provinceOrgRootOpt.isPresent()) {
                    Optional<DeptOfJustice> deptOfJusticeOpt = deptOfJusticeRepository.findByCode(provinceOrgRootOpt.get().getCode());
                    deptOfJusticeOpt.ifPresent(dept -> {
                        ret.setOrgRootDepartmentCode(dept.getCode());
                        ret.setOrgRootDepartmentName(dept.getName());
                    });
                }
            }


            orgRootOpt.ifPresent(orgRoot -> {
                if (provinceOpt.isPresent()) {
                    ret.setProvinceName(provinceOpt.get().getName());

                    Optional<Province> provinceOrgRootOpt = provinceRepository.findByProvinceCode(provinceOpt.get().getProvinceCode());
                    ret.setOrgRootName(auctionOrganization.getFullName());
                }
            });
        }

        if (isBasicInfo == null || isBasicInfo == Boolean.FALSE) {
            AuctionOrganizationDto.Manager manager = getManager(auctionOrganization.getManagerUuid(), organizationId);
            ret.setManager(manager);
            if (auctionOrganization.getOrgType().equals(OrganizationType.PARTNERSHIP_AUCTION_COMPANY.getCode())) {
                List<MemberPartner> memberPartners = auctionOrganization.getMemberPartners();
                List<MemberPartnerDto> memberPartnerResp = new ArrayList<>();
                for (MemberPartner memberPartner : memberPartners) {
                    MemberPartnerDto memberPartnerDto = MemberPartnerDto.builder()
                            .uuid(memberPartner.getUuid())
                            .fullName(memberPartner.getFullName())
                            .dob(memberPartner.getDob())
                            .certCode(memberPartner.getCertCode())
                            .dateOfDecision(memberPartner.getDateOfDecision())
                            .build();
                    memberPartnerResp.add(memberPartnerDto);
                }
                ret.setMemberPartners(memberPartnerResp);
            }
        }
        return ret;
    }

    private AuctionOrganizationDto.Manager getManager(String managerUuid, String organizationId) throws InternalException {
        if (!StringUtils.hasText(managerUuid)) {
            return null;
        }
        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(managerUuid);
        AuctionOrganizationDto.Manager ret = AuctionOrganizationDto.Manager.builder()
                .uuid(managerUuid)
                .build();
        if (auctioneerOpt.isPresent()) {
            Auctioneer auctioneer = auctioneerOpt.get();
            ret.setFullName(auctioneer.getFullName());
            ret.setGender(auctioneer.getGender());
            ret.setDob(auctioneer.getDob());
            ret.setTelNumber(auctioneer.getTelNumber());
            ret.setEmail(auctioneer.getEmail());
            ret.setIdCode(auctioneer.getIdCode());
            ret.setIdDoi(auctioneer.getIdDoi());
            ret.setIdPoi(auctioneer.getIdPoi());
            ret.setAddPermanent(auctioneer.getAddPermanent());
            ret.setProvinceCode(auctioneer.getProvinceCode());
            ret.setWardCode(auctioneer.getWardCode());

            Optional<PlaceOfIssue> placeOfIssue = placeOfIssueRepository.findByCode(auctioneer.getIdPoi());
            placeOfIssue.ifPresent(poi -> ret.setTextPoi(poi.getName()));

            Optional<Ward> wardOpt = wardRepository.findByWardCode(auctioneer.getWardCode());
            wardOpt.ifPresent(ward -> ret.setTextWard(ward.getName()));

            Optional<Province> provinceOpt = provinceRepository.findByProvinceCode(auctioneer.getProvinceCode());
            provinceOpt.ifPresent(province -> ret.setTextProvince(province.getName()));

            AuctionCertificateInfoPredicate auctionCertPredicate = new AuctionCertificateInfoPredicate()
                    .withAuctioneerId(auctioneer.getUuid());
            List<AuctionCertificateInfo> certInfos = auctionCertificateInfoRepository.findAll(
                    auctionCertPredicate.getCriteria(),
                    PaginationUtil.Sorting.by(AuctionCertificateInfoPredicate.EFFECTIVE_DATE_ORDER_PROPERTY)
            );
            if (!certInfos.isEmpty()) {
                AuctionCertificateInfo auctionCertInfo = certInfos.get(0);
                ret.setCertCode(auctionCertInfo.getCertCode());
                ret.setDateOfDecisionCert(auctionCertInfo.getDateOfDecision());
            }

            AuctionCardInfoPredicate auctionCardPredicate = new AuctionCardInfoPredicate()
                    .withAuctioneerId(auctioneer.getUuid())
                    .withOrgId(organizationId);
            List<AuctionCardInfo> cardInfos = auctionCardInfoRepository.findAll(
                    auctionCardPredicate.getCriteria(),
                    PaginationUtil.Sorting.by(AuctionCardInfoPredicate.EFFECTIVE_DATE_ORDER_PROPERTY)
            );
            if (!cardInfos.isEmpty()) {
                AuctionCardInfo auctionCardInfo = cardInfos.get(0);
                ret.setCardCode(auctionCardInfo.getCardCode());
                ret.setDateOfDecisionCard(auctionCardInfo.getDateOfDecision());
            }

        }
        return ret;
    }

    @Override
    public List<AuctionOrganizationBasicInfo> getAll(AuctionOrganizationSearchReq req) {
        AuctionOrganizationPredicate predicate = new AuctionOrganizationPredicate();

        List<Integer> orgTypeCodes = new ArrayList<>();
        if (req.getTypes() != null && !req.getTypes().isEmpty()) {
            req.getTypes().forEach(type -> orgTypeCodes.add(type.getCode()));
            predicate = predicate.withTypes(orgTypeCodes);
        }

        if (StringUtils.hasText(req.getDepartmentCode())) {
            Optional<Province> provinceOpt = provinceRepository.findByCode(req.getDepartmentCode());
            if (provinceOpt.isPresent()) {
                predicate = predicate.withProvinceCode(provinceOpt.get().getProvinceCode());
            }
        }
        return auctionOrganizationRepository.getAll(predicate);
    }

    @Override
    @Transactional
    public AuctionOrganizationDto update(String organizationId, AuctionOrganizationRequest request) throws InternalException {

        validateOrganizationRequest(request);

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(request.getManagerUuid());
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException("Manager not found with UUID: " + request.getManagerUuid());
        }

        Optional<AuctionOrganization> auctionOrganizationOpt = auctionOrganizationRepository.findById(organizationId);
        if (auctionOrganizationOpt.isEmpty()) {
            throw new InternalException("Organization not found with ID: " + organizationId);
        }

        AuctionOrganization auctionOrganization = auctionOrganizationOpt.get();
        auctionOrganization.setFullName(request.getFullName());
        auctionOrganization.setLicenseNo(request.getLicenseNo());
        auctionOrganization.setLicenseDate(request.getLicenseDate());
        auctionOrganization.setTelNumber(request.getTelNumber());
        auctionOrganization.setAddress(request.getAddress());
        auctionOrganization.setEmail(request.getEmail());
        auctionOrganization.setStatus(request.getStatus());
        auctionOrganization.setProvinceCode(request.getProvinceCode());
        auctionOrganization.setWardCode(request.getWardCode());
        auctionOrganization.setManagerUuid(auctioneerOpt.get().getUuid());

        switch (request.getType()) {
            case AUCTION_SERVICE_CENTER:
            case PRIVATE_AUCTION_ENTERPRISE:
            case PARTNERSHIP_AUCTION_COMPANY:
                break;
            case AUCTION_BRANCH:
                Optional<AuctionOrganization> auctionOrganizationParent = auctionOrganizationRepository.findById(request.getOrgRoot());
                if (auctionOrganizationParent.isEmpty()) {
                    throw new InternalException("Parent organization not found with ID: " + request.getOrgRoot());
                }
                auctionOrganization.setOrgRoot(auctionOrganizationParent.get().getUuid());
                break;
        }

        return auctionOrganizationMapper.toDto(auctionOrganization);
    }

    @Override
    @Transactional
    public AuctionOrganizationDto updateStatus(String organizationId, OrganizationStatus status) throws InternalException {

        Optional<AuctionOrganization> auctionOrganizationOpt = auctionOrganizationRepository.findById(organizationId);
        if (auctionOrganizationOpt.isEmpty()) {
            throw new InternalException("Organization not found with ID: " + organizationId);
        }
        AuctionOrganization auctionOrganization = auctionOrganizationOpt.get();
        auctionOrganization.setStatus(status);
        auctionOrganization = auctionOrganizationRepository.save(auctionOrganization);
        return auctionOrganizationMapper.toDto(auctionOrganization);
    }

    @Override
    @Transactional
    public void updateMemberPartner(String organizationId, String memberId, MemberPartnerRequest request) throws InternalException {

        Optional<AuctionOrganization> auctionOrganizationOpt = auctionOrganizationRepository.findById(organizationId);
        if (auctionOrganizationOpt.isEmpty()) {
            throw new InternalException("Organization not found with ID: " + organizationId);
        }

        OrganizationType organizationType = OrganizationType.fromCode(auctionOrganizationOpt.get().getOrgType());
        if (organizationType != OrganizationType.PARTNERSHIP_AUCTION_COMPANY) {
            throw new InternalException("Cannot add member partners to this organization type: " + auctionOrganizationOpt.get().getOrgType());
        }

        MemberPartnerPredicate predicate = new MemberPartnerPredicate()
                .withOrgId(auctionOrganizationOpt.get().getUuid())
                .withId(memberId);
        List<MemberPartner> memberPartners = memberPartnerRepository.findAll(predicate.getCriteria());

        if (memberPartners.isEmpty()) {
            throw new InternalException("Member partner not found with ID: " + memberId);
        }
        MemberPartner memberPartner = memberPartners.get(0);

        if (!StringUtils.hasText(request.getFullName()) || !StringUtils.hasText(request.getDob())) {
            throw new InternalException("Full name and date of birth are required for manual member partners");
        }

        memberPartner.setFullName(request.getFullName());
        memberPartner.setDob(request.getDob());
        memberPartner.setCertCode(request.getCertCode());
        memberPartner.setDateOfDecision(request.getDateOfDecision());
    }

    @Override
    @Transactional
    public void addMemberPartner(String organizationId, List<MemberPartnerRequest> request) throws InternalException {

        if (request == null || request.isEmpty()) {
            throw new InternalException("Member partner request cannot be null or empty");
        }

        Optional<AuctionOrganization> auctionOrganizationOpt = auctionOrganizationRepository.findById(organizationId);
        if (auctionOrganizationOpt.isEmpty()) {
            throw new InternalException("Organization not found with ID: " + organizationId);
        }

        OrganizationType organizationType = OrganizationType.fromCode(auctionOrganizationOpt.get().getOrgType());
        if (organizationType != OrganizationType.PARTNERSHIP_AUCTION_COMPANY) {
            throw new InternalException("Cannot add member partners to this organization type: " + auctionOrganizationOpt.get().getOrgType());
        }

        for (MemberPartnerRequest req : request) {
            if (!StringUtils.hasText(req.getFullName()) || !StringUtils.hasText(req.getDob())) {
                throw new InternalException("Invalid request");
            }
        }

        AuctionOrganization auctionOrganization = auctionOrganizationOpt.get();
        List<MemberPartner> memberPartnersSaved = auctionOrganization.getMemberPartners();
        List<MemberPartner> memberPartners = createMemberPartnersFromReq(request, auctionOrganization);
        memberPartnersSaved.addAll(memberPartners);
        auctionOrganization.setMemberPartners(memberPartnersSaved);
        memberPartnerRepository.saveAll(memberPartners);
    }

    @Override
    @Transactional
    public void deleteMemberPartner(String organizationId, List<String> ids) throws InternalException {

        if (ids == null || ids.isEmpty()) {
            throw new InternalException("Member partner IDs cannot be null or empty");
        }

        Optional<AuctionOrganization> auctionOrganizationOpt = auctionOrganizationRepository.findById(organizationId);
        if (auctionOrganizationOpt.isEmpty()) {
            throw new InternalException("Organization not found with ID: " + organizationId);
        }
        AuctionOrganization auctionOrganization = auctionOrganizationOpt.get();

        MemberPartnerPredicate predicate = new MemberPartnerPredicate()
                .withOrgId(auctionOrganizationOpt.get().getUuid())
                .withIds(ids);
        List<MemberPartner> memberPartners = memberPartnerRepository.findAll(predicate.getCriteria());
        if (memberPartners.isEmpty()) {
            throw new InternalException("No member partners found with the provided IDs");
        }

        List<MemberPartner> memberPartnerExisting = auctionOrganization.getMemberPartners();
        ids.forEach(id -> memberPartnerExisting.removeIf(mp -> mp.getUuid().equals(id)));
        auctionOrganization.setMemberPartners(memberPartnerExisting);
        memberPartnerRepository.deleteAll(memberPartners);
    }

    @Override
    public List<AuctioneerWithCertDto> getAllCert() {
        return auctionCertificateInfoRepository.getAllCert();
    }

    @Override
    public Boolean checkCardCodeExistInOrg(String organizationId, String cardId, String cardCode) throws InternalException {

        Optional<AuctionOrganization> auctionOrganizationOpt = auctionOrganizationRepository.findById(organizationId);
        if (auctionOrganizationOpt.isEmpty()) {
            throw new InternalException("Organization not found with ID: " + organizationId);
        }

        AuctionCardInfoPredicate predicate = new AuctionCardInfoPredicate()
                .withOrgId(auctionOrganizationOpt.get().getUuid())
                .withCardCode(cardCode);

        if (StringUtils.hasText(cardId)) {
            predicate = predicate.withNotInIds(List.of(cardId));
        }
        List<AuctionCardInfo> auctionCardInfos = auctionCardInfoRepository.findAll(predicate.getCriteria());
        return !auctionCardInfos.isEmpty();
    }
}
