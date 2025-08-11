package com.osp.bttp.dao.model.mapper.db3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osp.bttp.dao.model.dto.db3.AuctionAttachFileDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCardInfoDto;
import com.osp.bttp.dao.model.entity.db3.AuctionCardInfo;
import com.osp.bttp.dao.model.entity.db3.DeptOfJustice;
import com.osp.bttp.dao.model.mapper.AbstractMapper;
import com.osp.bttp.dao.repository.db3.DeptOfJusticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionCardInfoMapper extends AbstractMapper<AuctionCardInfo, AuctionCardInfoDto> {

    private final ObjectMapper objectMapper;

    private final DeptOfJusticeRepository deptOfJusticeRepository;

    @Override
    public Class<AuctionCardInfoDto> getDtoClass() {
        return AuctionCardInfoDto.class;
    }

    @Override
    public AuctionCardInfoDto toDto(AuctionCardInfo auctionCardInfo) {
        AuctionCardInfoDto ret = super.toDto(auctionCardInfo);

        if (auctionCardInfo.getOrganization() != null) {
            ret.setOrgId(auctionCardInfo.getOrganization().getUuid());
            ret.setOrgName(auctionCardInfo.getOrganization().getFullName());
        }

        Optional<DeptOfJustice> deptOfJusticeOpt = deptOfJusticeRepository.findByCode(auctionCardInfo.getDepartmentCode());
        deptOfJusticeOpt.ifPresent(deptOfJustice -> ret.setDepartmentText(deptOfJustice.getName()));

        String fileObjStr = auctionCardInfo.getFileObj();
        List<AuctionAttachFileDto> attachFiles = new ArrayList<>();
        if (StringUtils.hasText(fileObjStr)) {
            try {
                Map<String, AuctionAttachFileDto> attachFileMap = objectMapper.readValue(fileObjStr, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, AuctionAttachFileDto.class));
                attachFiles.addAll(attachFileMap.values());
            } catch (Exception ex) {
                log.error("Error parsing fileObj in AuctionCardInfoDto");
            }
        }
        ret.setAttachFile(attachFiles);
        return ret;
    }
}
