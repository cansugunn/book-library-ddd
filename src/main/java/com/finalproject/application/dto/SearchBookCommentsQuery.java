package com.finalproject.application.dto;

import com.finalproject.application.dto.page.PageQuery;

public record SearchBookCommentsQuery(Integer bookId, PageQuery pageQuery) {
}
