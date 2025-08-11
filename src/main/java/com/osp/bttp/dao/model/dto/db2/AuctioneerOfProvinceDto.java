package com.osp.bttp.dao.model.dto.db2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctioneerOfProvinceDto {

    private Long totalCount;

    private List<Data> data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Data {

        private Long cityId;

        private String cityCode;

        private String cityName;

        private Long auctioneerCount;
    }
}
