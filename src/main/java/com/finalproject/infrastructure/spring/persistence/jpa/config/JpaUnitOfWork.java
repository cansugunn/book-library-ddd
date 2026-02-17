package com.finalproject.infrastructure.spring.persistence.jpa.config;

import com.finalproject.application.ports.output.repository.UnitOfWork;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
public class JpaUnitOfWork implements UnitOfWork {
    private final TransactionTemplate template;

    public JpaUnitOfWork(PlatformTransactionManager transactionManager) {
        this.template = new TransactionTemplate(transactionManager);
    }

    @Override
    public <T> T executeInTransaction(Supplier<T> action) {
        return template.execute(status -> action.get());
    }
}
