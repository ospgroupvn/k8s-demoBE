package com.osp.bttp.dao.model.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMapper<E, D> implements Mapper<E, D> {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * @see <a href="https://www.baeldung.com/java-modelmapper#what-is-property-mapping-in-modelmapper">Properties mapping</a>
     */
    @Override
    public D toDto(E e) {
        return modelMapper.map(e, getDtoClass());
    }

    @Override
    public List<D> toDtoList(List<E> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities
                .stream()
                .map(this::toDto)
                .toList();
    }
}
