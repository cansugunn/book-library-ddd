package com.finalproject.presentation.spring.api.dto;

import com.finalproject.domain.valueobject.Read;
import com.finalproject.presentation.spring.api.customvalidation.ValueOfEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record CreateUserBookStateRequest(@NotNull Integer bookId,
                                         @NotNull @ValueOfEnum(enumClass = Read.class) String read,
                                         @NotNull @Min(0) @Max(5) Integer rating,
                                         List<String> comments,
                                         Date releaseDate) {
}