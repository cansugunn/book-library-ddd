package com.finalproject.application.dto.page;

import java.util.List;

public record PageResult<T>(List<T> content,
                            int page,
                            int size,
                            long totalElements,
                            int totalPages,
                            boolean first,
                            boolean last) {
}

