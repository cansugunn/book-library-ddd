package com.finalproject.application.ports.input.services;

import com.finalproject.application.dto.*;
import com.finalproject.application.ports.output.security.CurrentUser;

public interface BookApplicationService {
    CreateBookResponse createBook(CreateBookRequest request);

    UpdateBookResponse updateBook(UpdateBookRequest request);

    void deleteBook(int id);

    FindBookResponse findBook(int id);
}
