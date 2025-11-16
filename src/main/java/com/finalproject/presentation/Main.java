package com.finalproject.presentation;

import com.finalproject.configuration.DependencyInjector;
import com.finalproject.presentation.views.Login;

public class Main {
    public static void main(String[] args) {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Login login = new Login(dependencyInjector);
    }
}
