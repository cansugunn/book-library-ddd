package com.finalproject.application.dto;

public record FindUserBookStatisticsResponse(long totalReads,
                                             double averageRating,
                                             long commentsCount) {
}
