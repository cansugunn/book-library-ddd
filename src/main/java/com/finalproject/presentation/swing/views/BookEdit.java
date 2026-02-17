package com.finalproject.presentation.swing.views;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.command.UpdateBookCommand;
import com.finalproject.presentation.swing.dependency.DependencyInjector;
import com.finalproject.presentation.swing.util.UserBookStateChangePublisher;

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
            if (path.isEmpty()) {
                path = null;
            }
            UpdateBookCommand command = new UpdateBookCommand(
                    bookId,
                    authorNameField.getText().trim(),
                    authorSurnameField.getText().trim(),
                    titleField.getText().trim(),
                    Integer.parseInt(yearField.getText().trim()),
                    Integer.parseInt(numberOfPagesField.getText().trim()),
                    aboutArea.getText().trim(),
                    path
            );

            dependencyInjector.getBookCommandApplicationService().updateBook(command);
            userBookStateChangePublisher.notifySubscribers();
            JOptionPane.showMessageDialog(this,
                    "Book updated successfully!",
                    "Successfull Operation",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error updating book: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
