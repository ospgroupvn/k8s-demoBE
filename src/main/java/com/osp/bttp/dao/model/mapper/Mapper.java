package com.osp.bttp.dao.model.mapper;

import java.util.List;

/**
 * @param <E> Entity type
 * @param <D> Data transfer object type
 */
public interface Mapper<E, D> {

    /**
     * Convert entity to dto
     *
     * @param e
     * @return
     */
    D toDto(E e);

    /**
     * Convert list of entity to list of DTO
     *
     * @param eList
     * @return
     */
    List<D> toDtoList(List<E> eList);

    /**
     * @return
     */
    Class<D> getDtoClass();
}
