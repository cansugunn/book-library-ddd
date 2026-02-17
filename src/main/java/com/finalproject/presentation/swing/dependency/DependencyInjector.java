package com.finalproject.presentation.swing.dependency;

import com.finalproject.application.mapper.AuthorDataMapper;
import com.finalproject.application.mapper.BookDataMapper;
import com.finalproject.application.mapper.UserBookStateMapper;
import com.finalproject.application.mapper.UserDataMapper;
import com.finalproject.application.ports.input.services.*;
import com.finalproject.application.ports.output.repository.*;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.application.ports.output.security.PasswordEncryptor;
import com.finalproject.application.services.*;
import com.finalproject.infrastructure.common.security.CypherPasswordEncryptor;
import com.finalproject.infrastructure.common.security.ThreadLocalCurrentUser;
import com.finalproject.infrastructure.swing.persistence.jdbc.config.DatabaseConfig;
import com.finalproject.infrastructure.swing.persistence.jdbc.repository.*;

public class DependencyInjector {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final UserBookStateRepository userBookStateRepository;
    private final UserRepository userRepository;
    private final UnitOfWork unitOfWork;

    private final AuthorApplicationService authorApplicationService;
    private final BookCommandApplicationService bookCommandApplicationService;
    private final BookQueryApplicationService bookQueryApplicationService;
    private final UserBookStateApplicationService userBookStateApplicationService;
    private final UserApplicationService userApplicationService;

    private final CurrentUser currentUser;
    private final PasswordEncryptor passwordEncryptor;

    public DependencyInjector() {
        DatabaseConfig config = DatabaseConfig.getInstance();
        authorRepository = new JdbcAuthorRepository(config);
        bookRepository = new JdbcBookRepository(config);
        userBookStateRepository = new JdbcUserBookStateRepository(config);
        userRepository = new JdbcUserRepository(config);
        unitOfWork = new JdbcUnitOfWork(config);

        currentUser = new ThreadLocalCurrentUser();
        passwordEncryptor = new CypherPasswordEncryptor();

        authorApplicationService = new AuthorApplicationServiceImpl(
                authorRepository,
                new AuthorDataMapper(),
                currentUser);
        bookCommandApplicationService = new BookCommandApplicationServiceImpl(
                bookRepository,
                authorRepository,
                unitOfWork,
                new BookDataMapper(),
                currentUser);
        bookQueryApplicationService = new BookQueryApplicationServiceImpl(
                bookRepository,
                authorRepository,
                new BookDataMapper());
        userBookStateApplicationService = new UserBookStateApplicationServiceImpl(
                userBookStateRepository,
                bookRepository,
                authorRepository,
                userRepository,
                new UserBookStateMapper(),
                currentUser);
        userApplicationService = new UserApplicationServiceImpl(
                userRepository,
                new UserDataMapper(),
                currentUser,
                passwordEncryptor);
    }

    public AuthorApplicationService getAuthorApplicationService() {
        return authorApplicationService;
    }

    public BookCommandApplicationService getBookCommandApplicationService() {
        return bookCommandApplicationService;
    }

    public BookQueryApplicationService getBookQueryApplicationService() {
        return bookQueryApplicationService;
    }

    public UserBookStateApplicationService getUserBookStateApplicationService() {
        return userBookStateApplicationService;
    }

    public UserApplicationService getUserApplicationService() {
        return userApplicationService;
    }
}
