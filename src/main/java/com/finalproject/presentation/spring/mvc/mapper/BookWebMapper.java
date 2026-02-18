package com.finalproject.presentation.spring.mvc.mapper;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.presentation.spring.mvc.dto.BookCardView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookWebMapper {

    public List<BookCardView> toBookCards(List<FindBookResponse> books) {
        return books.stream()
                .map(this::toBookCard)
                .toList();
    }

    private BookCardView toBookCard(FindBookResponse book) {
        return new BookCardView(
                book.getBookId(),
                book.getTitle(),
                (book.getAuthorName() + " " + book.getAuthorSurname()).trim(),
                book.getAbout(),
                book.getCoverPath(),
                book.getYear()
        );
    }
}
