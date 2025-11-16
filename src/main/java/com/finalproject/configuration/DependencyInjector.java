package com.finalproject.configuration;

import com.finalproject.application.mapper.AuthorDataMapper;
import com.finalproject.application.mapper.BookDataMapper;
import com.finalproject.application.mapper.UserBookStateMapper;
import com.finalproject.application.mapper.UserDataMapper;
import com.finalproject.application.ports.input.services.AuthorApplicationService;
import com.finalproject.application.ports.input.services.BookApplicationService;
import com.finalproject.application.ports.input.services.UserApplicationService;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import com.finalproject.application.ports.output.repository.*;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.application.ports.output.security.PasswordEncryptor;
import com.finalproject.application.services.AuthorApplicationServiceImpl;
import com.finalproject.application.services.BookApplicationServiceImpl;
import com.finalproject.application.services.UserApplicationServiceImpl;
import com.finalproject.application.services.UserBookStateApplicationServiceImpl;
import com.finalproject.infrastructure.persistence.config.DatabaseConfig;
import com.finalproject.infrastructure.persistence.repository.*;
import com.finalproject.infrastructure.security.CypherPasswordEncryptor;
import com.finalproject.infrastructure.security.ThreadLocalCurrentUser;

public class DependencyInjector {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final UserBookStateRepository userBookStateRepository;
    private final UserRepository userRepository;
    private final UnitOfWork unitOfWork;

    private final AuthorApplicationService authorApplicationService;
    private final BookApplicationService bookApplicationService;
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
        bookApplicationService = new BookApplicationServiceImpl(
                bookRepository,
                userBookStateRepository,
                authorRepository,
                unitOfWork,
                new BookDataMapper(),
                currentUser);
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

    public BookApplicationService getBookApplicationService() {
        return bookApplicationService;
    }

    public UserBookStateApplicationService getUserBookStateApplicationService() {
        return userBookStateApplicationService;
    }

    public UserApplicationService getUserApplicationService() {
        return userApplicationService;
    }
}
