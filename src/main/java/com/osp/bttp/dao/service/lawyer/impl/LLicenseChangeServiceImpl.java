package com.osp.bttp.dao.service.lawyer.impl;


import com.osp.bttp.dao.model.entity.db4.LLicense;
import com.osp.bttp.dao.model.entity.db4.LLicenseChange;
import com.osp.bttp.dao.repository.db4.LLicenseChangeRepository;
import com.osp.bttp.dao.service.lawyer.LLicenseChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class LLicenseChangeServiceImpl implements LLicenseChangeService {
    @Autowired
    private LLicenseChangeRepository licenseChangeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertLicChange(LLicense license) {
        try {
            LLicenseChange licenseChange = new LLicenseChange();
            if (license.getLicenseId() != null) {
                licenseChange.setLicenseId(license.getLicenseId());
                licenseChange.setIssueDate(new Date());
            }
            licenseChange.setLicenseType(license.getLicenseType());
            licenseChange.setLicenseNumber(license.getLicenseNumber());

            if (license.getRevocationDate() != null) {
                licenseChange.setRevocationDate(license.getRevocationDate());
            }
            licenseChange.setOwnerId(license.getOwnerId());
            licenseChange.setOwnerType(license.getOwnerType());
            licenseChange.setIssueDate(license.getIssueDate());
            licenseChange.setPracticeForm(license.getPracticeForm());
            licenseChange.setPracticePlace(license.getPracticePlace());
            licenseChange.setStatus(license.getStatus());
            licenseChange.setCountChange(license.getCountChange());
            licenseChange.setChangeDate(new Date());
            List<LLicenseChange> licenseChanges = licenseChangeRepository.getAllByLicenseId(license.getLicenseId())
                    .stream()
                    .sorted((a, b) -> b.getLicenseChangeId().compareTo(a.getLicenseChangeId())) // Sắp xếp theo id mới nhất
                    .toList();
            if (!licenseChanges.isEmpty()) {
                LLicenseChange licensePresentChange = licenseChanges.get(0);

                String licNewNum = license.getLicenseNumber();
                String licOldNum = licensePresentChange.getLicenseNumber();

                Integer statusNew = license.getStatus();
                Integer statusOld = licensePresentChange.getStatus();
                if (!Objects.equals(licNewNum, licOldNum) || !Objects.equals(statusNew, statusOld)) {

                    licenseChangeRepository.save(licenseChange);
                }
            } else {
                licenseChangeRepository.save(licenseChange);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLicChange(Long idLicChange) {
        try {
            licenseChangeRepository.deleteAllByLicenseId(idLicChange);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
