package com.finalproject.application.projection;

public record UserBookStatisticsProjection(long totalReads,
                                           double averageRating,
                                           long commentsCount) {
}