package com.finalproject.presentation.views;

import com.finalproject.application.dto.CreateBookRequest;
import com.finalproject.application.dto.CreateBookResponse;
import com.finalproject.configuration.DependencyInjector;
import com.finalproject.presentation.util.UserBookStateChangePublisher;
import com.finalproject.presentation.util.UserBookStateChangeSubscriber;

import javax.swing.*;
import java.awt.*;

public class BookCreation extends JPanel {
    private JPanel mainPanel;
    private JTextField authorNameField;
    private JTextField authorSurnameField;
    private JTextField titleField;
    private JTextField yearField;
    private JTextField numberOfPagesField;
    private JTextArea aboutArea;
    private JTextField coverPathField;
    private JButton createButton;

    private final DependencyInjector dependencyInjector;
    private final UserBookStateChangePublisher userBookStateChangePublisher;

    public BookCreation(DependencyInjector dependencyInjector,
                        UserBookStateChangeSubscriber... subscriberViews) {
        this.dependencyInjector = dependencyInjector;

        userBookStateChangePublisher = new UserBookStateChangePublisher();
        userBookStateChangePublisher.subscribe(subscriberViews);

        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        createButton.addActionListener(e -> handleCreate());
    }

    private void handleCreate() {
        try {
            String path = coverPathField.getText().trim();
            if (path.isEmpty()) {
                path = null;
            }

            CreateBookRequest request = new CreateBookRequest.Builder()
                    .authorName(authorNameField.getText())
                    .authorSurname(authorSurnameField.getText())
                    .title(titleField.getText())
                    .year(Integer.parseInt(yearField.getText()))
                    .numberOfPages(Integer.parseInt(numberOfPagesField.getText()))
                    .about(aboutArea.getText().trim())
                    .coverPath(path)
                    .build();
            CreateBookResponse createBookResponse = dependencyInjector.getBookApplicationService().createBook(request);

            userBookStateChangePublisher.notifySubscribers();
            JOptionPane.showMessageDialog(this,
                    "Created with the id: %d".formatted(createBookResponse.getBookId()),
                    "Successfull Operation",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: %s".formatted(ex.getMessage()),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
