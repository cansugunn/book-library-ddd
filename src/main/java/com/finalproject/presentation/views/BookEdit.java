package com.finalproject.presentation.views;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.UpdateBookRequest;
import com.finalproject.application.ports.input.services.BookApplicationService;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.configuration.DependencyInjector;
import com.finalproject.presentation.util.UserBookStateChangePublisher;
import com.finalproject.presentation.util.UserBookStateChangeSubscriber;

import javax.swing.*;

public class BookEdit extends JFrame {
    private JPanel mainPanel;
    private JTextField authorNameField;
    private JTextField authorSurnameField;
    private JTextField titleField;
    private JTextField yearField;
    private JTextField numberOfPagesField;
    private JTextArea aboutArea;
    private JTextField coverPathField;
    private JButton updateButton;

    private final int bookId;
    private final DependencyInjector dependencyInjector;

    private final UserBookStateChangePublisher userBookStateChangePublisher;

    public BookEdit(FindBookResponse findBookResponse,
                    DependencyInjector dependencyInjector,
                    UserBookStateChangePublisher userBookStateChangePublisher) {
        this.dependencyInjector = dependencyInjector;
        this.userBookStateChangePublisher = userBookStateChangePublisher;

        setupUI();

        this.bookId = findBookResponse.getBookId();
        fillFields(findBookResponse);
    }

    private void setupUI() {
        setTitle("Edit Book");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        updateButton.addActionListener(e -> updateBook());
    }

    private void fillFields(FindBookResponse findBookResponse) {
        authorNameField.setText(findBookResponse.getAuthorName());
        authorSurnameField.setText(findBookResponse.getAuthorSurname());
        titleField.setText(findBookResponse.getTitle());
        yearField.setText(String.valueOf(findBookResponse.getYear()));
        numberOfPagesField.setText(String.valueOf(findBookResponse.getNumberOfPages()));
        aboutArea.setText(findBookResponse.getAbout());
        coverPathField.setText(findBookResponse.getCoverPath());
    }

    private void updateBook() {
        try {
            String path = coverPathField.getText().trim();
            if(path.isEmpty()){
                path = null;
            }
            UpdateBookRequest request = new UpdateBookRequest.Builder()
                    .bookId(bookId)
                    .authorName(authorNameField.getText().trim())
                    .authorSurname(authorSurnameField.getText().trim())
                    .title(titleField.getText().trim())
                    .year(Integer.parseInt(yearField.getText().trim()))
                    .numberOfPages(Integer.parseInt(numberOfPagesField.getText().trim()))
                    .about(aboutArea.getText().trim())
                    .coverPath(path)
                    .build();

            dependencyInjector.getBookApplicationService().updateBook(request);
            userBookStateChangePublisher.notifySubscribers();
            JOptionPane.showMessageDialog(this,
                    "Book updated successfully!",
                    "Successfull Operation",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error updating book: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
} 