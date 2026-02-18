package com.finalproject.presentation.spring.mvc;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.query.SearchBooksQuery;
import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.presentation.spring.mvc.mapper.BookWebMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mvc")
public class MvcHomeController {
    private final BookQueryApplicationService bookQueryApplicationService;
    private final MvcSessionService mvcSessionService;
    private final BookWebMapper bookWebMapper;

    public MvcHomeController(BookQueryApplicationService bookQueryApplicationService,
                             MvcSessionService mvcSessionService,
                             BookWebMapper bookWebMapper) {
        this.bookQueryApplicationService = bookQueryApplicationService;
        this.mvcSessionService = mvcSessionService;
        this.bookWebMapper = bookWebMapper;
    }

    @GetMapping({"", "/"})
    public String home(@RequestParam(value = "q", required = false) String keyword,
                       @PageableDefault(size = 12) Pageable pageable,
                       HttpSession session,
                       Model model) {
        PageResult<FindBookResponse> trending = bookQueryApplicationService.findAllBooks(new PageQuery(0, 10));
        int catalogPage = pageable.getPageNumber();
        if ((keyword == null || keyword.isBlank()) && catalogPage == 0) {
            catalogPage = 1;
        }
        PageResult<FindBookResponse> belowPageRaw = bookQueryApplicationService.searchBooks(
                new SearchBooksQuery(keyword, new PageQuery(catalogPage, pageable.getPageSize())));

        Set<Integer> trendingIds = trending.content().stream().map(FindBookResponse::getBookId).collect(Collectors.toSet());
        List<FindBookResponse> filteredBelow = belowPageRaw.content().stream()
                .filter(book -> !trendingIds.contains(book.getBookId()))
                .toList();

        PageResult<FindBookResponse> below = new PageResult<>(
                filteredBelow,
                belowPageRaw.page(),
                belowPageRaw.size(),
                belowPageRaw.totalElements(),
                belowPageRaw.totalPages(),
                belowPageRaw.first(),
                belowPageRaw.last()
        );

        model.addAttribute("user", mvcSessionService.get(session));
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("trendingBooks", bookWebMapper.toBookCards(trending.content()));
        model.addAttribute("booksPage", below);
        model.addAttribute("bookCards", bookWebMapper.toBookCards(filteredBelow));
        return "mvc/books/home";
    }
}
