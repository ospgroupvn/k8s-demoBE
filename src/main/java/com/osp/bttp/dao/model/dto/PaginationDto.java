package com.osp.bttp.dao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationDto<T> {

    private List<T> data;

    private long totalItem;

    private int totalPage;

    /**
     * Create empty data
     *
     * @param <T>
     * @return
     */
    public static <T> PaginationDto<T> empty() {
        return new PaginationDto<>(Collections.emptyList(), 0, 0);
    }
}
