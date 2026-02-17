package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.application.dto.page.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PageMapper {
    public <T, V> PageResult<T> toPageResult(Page<V> result,
                                             Function<V, T> mapper) {
        return new PageResult<>(
                result.getContent().stream().map(mapper).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast());
    }
}
