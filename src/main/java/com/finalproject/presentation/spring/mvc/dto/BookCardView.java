package com.finalproject.presentation.spring.mvc.dto;

public record BookCardView(Integer id,
                           String title,
                           String author,
                           String about,
                           String coverUrl,
                           Integer year) {
}
