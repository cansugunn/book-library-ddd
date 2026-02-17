package com.finalproject;

import com.finalproject.presentation.swing.bootstrap.DependencyInjector;
import com.finalproject.presentation.swing.views.Login;

public class SwingDesktopApplication {
    public static void main(String[] args) {
        DependencyInjector dependencyInjector = new DependencyInjector();
        new Login(dependencyInjector);
    }
}
