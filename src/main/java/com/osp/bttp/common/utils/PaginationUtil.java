package com.osp.bttp.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HaiVN
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaginationUtil {

    /**
     * Init pagination request
     */
    public static Pageable init(Integer page, Integer pageSize) {
        return PageRequest.of(
                Page.resolvePage(page),
                Page.resolveSize(pageSize)
        );
    }

    /**
     * Init pagination request
     *
     * @param page        page number
     * @param pageSize    size of page
     * @param sortColumns list of sort column in format: <column_name>:<direction> (such as: id:asc, name:desc) or column name only
     */
    public static Pageable init(Integer page, Integer pageSize, String... sortColumns) {
        return PageRequest.of(
                Page.resolvePage(page),
                Page.resolveSize(pageSize),
                Sorting.by(sortColumns)
        );
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Page<T> {

        public static final int DEFAULT_PAGE = 0;

        public static final int DEFAULT_SIZE = 10;

        public static final int DEFAULT_MAX_SIZE = 100;

        /**
         * Resolve page number
         */
        public static int resolvePage(Integer page) {
            if (page == null || page < 0) {
                return DEFAULT_PAGE;
            }

            return page;
        }

        /**
         * Resolve number of elements in each page
         */
        public static int resolveSize(Integer size) {
            if (size == null || size <= 0) {
                return DEFAULT_SIZE;
            }
            if (size > DEFAULT_MAX_SIZE) {
                return DEFAULT_MAX_SIZE;
            }

            return size;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Slf4j
    public static final class Sorting {

        // The delimiter to separate column name and the direction
        public static final String DELIMITER = ":";

        public static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.ASC;

        public static Sort by(String... sortColumns) {
            List<Sort.Order> orders = new ArrayList<>();
            for (String order : sortColumns) {
                String[] parts = order.trim().split(DELIMITER);
                String property = parts[0];
                Sort.Direction direction = DEFAULT_DIRECTION;

                if (parts.length == 2) {
                    try {
                        direction = Sort.Direction.fromString(parts[1].trim());
                    } catch (Exception e) {
                        log.error("Invalid sort direction: {}", parts[1]);
                        continue;
                    }
                }

                orders.add(new Sort.Order(direction, property));
            }

            return Sort.by(orders);
        }
    }
}
