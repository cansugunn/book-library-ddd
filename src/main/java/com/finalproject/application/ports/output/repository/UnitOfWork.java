package com.finalproject.application.ports.output.repository;

import java.util.function.Supplier;

@FunctionalInterface
public interface UnitOfWork {
    <T> T executeInTransaction(Supplier<T> action);
}
