package com.osp.bttp.dao.model.mapper.db3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osp.bttp.dao.model.dto.db3.AuctionAttachFileDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCertificateInfoDto;
import com.osp.bttp.dao.model.entity.db3.AuctionCertificateInfo;
import com.osp.bttp.dao.model.mapper.AbstractMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionCertificateInfoMapper extends AbstractMapper<AuctionCertificateInfo, AuctionCertificateInfoDto> {

    private final ObjectMapper objectMapper;

    @Override
    public Class<AuctionCertificateInfoDto> getDtoClass() {
        return AuctionCertificateInfoDto.class;
    }

    @Override
    public AuctionCertificateInfoDto toDto(AuctionCertificateInfo auctionCertificateInfo) {
        AuctionCertificateInfoDto ret = super.toDto(auctionCertificateInfo);
        List<AuctionAttachFileDto> attachFiles = new ArrayList<>();
        String fileObjStr = auctionCertificateInfo.getFileObj();
        if (StringUtils.hasText(fileObjStr)) {
            try {
                Map<String, AuctionAttachFileDto> attachFileMap = objectMapper.readValue(fileObjStr, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, AuctionAttachFileDto.class));
                attachFiles.addAll(attachFileMap.values());
            } catch (Exception ex) {
                log.error("Error parsing fileObj in AuctionCertificateInfo");
            }
        }
        ret.setAttachFile(attachFiles);
        return ret;
    }
}
