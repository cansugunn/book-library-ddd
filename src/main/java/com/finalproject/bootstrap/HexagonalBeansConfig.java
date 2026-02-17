package com.finalproject.bootstrap;

import com.finalproject.application.mapper.AuthorDataMapper;
import com.finalproject.application.mapper.BookDataMapper;
import com.finalproject.application.mapper.UserDataMapper;
import com.finalproject.application.ports.input.services.AuthorApplicationService;
import com.finalproject.application.ports.input.services.BookCommandApplicationService;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.application.ports.input.services.UserApplicationService;
import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.application.ports.output.repository.UnitOfWork;
import com.finalproject.application.ports.output.repository.UserRepository;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.application.ports.output.security.PasswordEncryptor;
import com.finalproject.application.services.AuthorApplicationServiceImpl;
import com.finalproject.application.services.BookCommandApplicationServiceImpl;
import com.finalproject.application.services.BookQueryApplicationServiceImpl;
import com.finalproject.application.services.UserApplicationServiceImpl;
import com.finalproject.infrastructure.persistence.jpa.adapter.JpaAuthorRepositoryAdapter;
import com.finalproject.infrastructure.persistence.jpa.adapter.JpaBookRepositoryAdapter;
import com.finalproject.infrastructure.persistence.jpa.adapter.JpaUserRepositoryAdapter;
import com.finalproject.infrastructure.persistence.jpa.config.JpaUnitOfWork;
import com.finalproject.infrastructure.security.CypherPasswordEncryptor;
import com.finalproject.infrastructure.security.ThreadLocalCurrentUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HexagonalBeansConfig {

    @Bean
    public AuthorRepository authorRepository(JpaAuthorRepositoryAdapter adapter) {
        return adapter;
    }

    @Bean
    public BookRepository bookRepository(JpaBookRepositoryAdapter adapter) {
        return adapter;
    }

    @Bean
    public UserRepository userRepository(JpaUserRepositoryAdapter adapter) {
        return adapter;
    }

    @Bean
    public UnitOfWork unitOfWork(JpaUnitOfWork unitOfWork) {
        return unitOfWork;
    }

    @Bean
    public CurrentUser currentUser() {
        return new ThreadLocalCurrentUser();
    }

    @Bean
    public PasswordEncryptor passwordEncryptor() {
        return new CypherPasswordEncryptor();
    }

    @Bean
    public AuthorApplicationService authorApplicationService(AuthorRepository authorRepository,
                                                             CurrentUser currentUser) {
        return new AuthorApplicationServiceImpl(authorRepository, new AuthorDataMapper(), currentUser);
    }

    @Bean
    public BookCommandApplicationService bookCommandApplicationService(BookRepository bookRepository,
                                                                       AuthorRepository authorRepository,
                                                                       UnitOfWork unitOfWork,
                                                                       CurrentUser currentUser) {
        return new BookCommandApplicationServiceImpl(bookRepository,
                authorRepository,
                unitOfWork,
                new BookDataMapper(),
                currentUser);
    }

    @Bean
    public BookQueryApplicationService bookQueryApplicationService(BookRepository bookRepository,
                                                                   AuthorRepository authorRepository) {
        return new BookQueryApplicationServiceImpl(bookRepository, authorRepository, new BookDataMapper());
    }

    @Bean
    public UserApplicationService userApplicationService(UserRepository userRepository,
                                                         CurrentUser currentUser,
                                                         PasswordEncryptor passwordEncryptor) {
        return new UserApplicationServiceImpl(userRepository, new UserDataMapper(), currentUser, passwordEncryptor);
    }
}
