package com.finalproject.infrastructure.spring.configuration;

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
import com.finalproject.infrastructure.spring.persistence.jpa.adapter.AuthorJpaRepositoryAdapter;
import com.finalproject.infrastructure.spring.persistence.jpa.adapter.BookJpaRepositoryAdapter;
import com.finalproject.infrastructure.spring.persistence.jpa.adapter.UserBookStateJpaRepositoryAdapter;
import com.finalproject.infrastructure.spring.persistence.jpa.adapter.UserJpaRepositoryAdapter;
import com.finalproject.infrastructure.spring.persistence.jpa.config.JpaUnitOfWork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HexagonalBeansConfig {
    @Bean
    public AuthorRepository authorRepository(AuthorJpaRepositoryAdapter adapter) {
        return adapter;
    }

    @Bean
    public BookRepository bookRepository(BookJpaRepositoryAdapter adapter) {
        return adapter;
    }

    @Bean
    public UserRepository userRepository(UserJpaRepositoryAdapter adapter) {
        return adapter;
    }

    @Bean
    public UserBookStateRepository userBookStateRepository(UserBookStateJpaRepositoryAdapter adapter) {
        return adapter;
    }

    @Bean
    public UnitOfWork unitOfWork(JpaUnitOfWork unitOfWork) {
        return unitOfWork;
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

    @Bean
    public UserBookStateApplicationService userBookStateApplicationService(UserBookStateRepository userBookStateRepository,
                                                                           BookRepository bookRepository,
                                                                           AuthorRepository authorRepository,
                                                                           UserRepository userRepository,
                                                                           CurrentUser currentUser) {
        return new UserBookStateApplicationServiceImpl(userBookStateRepository,
                bookRepository,
                authorRepository,
                userRepository,
                new UserBookStateMapper(),
                currentUser);
    }
}
