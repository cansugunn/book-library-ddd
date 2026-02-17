package com.finalproject.presentation.swing.dependency;

import com.finalproject.application.mapper.AuthorDataMapper;
import com.finalproject.application.mapper.BookDataMapper;
import com.finalproject.application.mapper.UserBookStateMapper;
import com.finalproject.application.mapper.UserDataMapper;
import com.finalproject.application.ports.input.services.AuthorApplicationService;
import com.finalproject.application.ports.input.services.BookCommandApplicationService;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.application.ports.input.services.UserApplicationService;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.application.ports.output.repository.UnitOfWork;
import com.finalproject.application.ports.output.repository.UserBookStateRepository;
import com.finalproject.application.ports.output.repository.UserRepository;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.application.ports.output.security.PasswordEncryptor;
import com.finalproject.application.services.AuthorApplicationServiceImpl;
import com.finalproject.application.services.BookCommandApplicationServiceImpl;
import com.finalproject.application.services.BookQueryApplicationServiceImpl;
import com.finalproject.application.services.UserApplicationServiceImpl;
import com.finalproject.application.services.UserBookStateApplicationServiceImpl;
import com.finalproject.infrastructure.swing.persistence.jdbc.config.DatabaseConfig;
import com.finalproject.infrastructure.swing.persistence.jdbc.repository.JdbcAuthorRepository;
import com.finalproject.infrastructure.swing.persistence.jdbc.repository.JdbcBookRepository;
import com.finalproject.infrastructure.swing.persistence.jdbc.repository.JdbcUnitOfWork;
import com.finalproject.infrastructure.swing.persistence.jdbc.repository.JdbcUserBookStateRepository;
import com.finalproject.infrastructure.swing.persistence.jdbc.repository.JdbcUserRepository;
import com.finalproject.infrastructure.common.security.CypherPasswordEncryptor;
import com.finalproject.infrastructure.common.security.ThreadLocalCurrentUser;

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
