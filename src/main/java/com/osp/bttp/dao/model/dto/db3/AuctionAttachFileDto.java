package com.osp.bttp.dao.model.dto.db3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionAttachFileDto {

    private String fileName;

    private String filePath;
}
