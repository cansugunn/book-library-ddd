package com.finalproject.presentation.swing.views;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.command.DeleteBookCommand;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.presentation.swing.bootstrap.DependencyInjector;
import com.finalproject.presentation.swing.util.UserBookStateChangePublisher;
import com.finalproject.presentation.swing.util.UserBookStateChangeSubscriber;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class BookOperations extends JPanel {
    private JPanel mainPanel;
    private JTextField bookIdField;
    private JButton deleteButton;
    private JButton displayButton;

    private final DependencyInjector dependencyInjector;

    private final UserBookStateChangePublisher userBookStateChangePublisher;

    public BookOperations(DependencyInjector dependencyInjector,
                          UserBookStateChangeSubscriber... subscriberViews) {
        this.dependencyInjector = dependencyInjector;

        userBookStateChangePublisher = new UserBookStateChangePublisher();
        userBookStateChangePublisher.subscribe(subscriberViews);

        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        deleteButton.addActionListener(e -> deleteBook());
        displayButton.addActionListener(e -> displayBook());
    }

    private void deleteBook() {
        Optional<Integer> bookIdOptional = getValidQueriedBookId();
        if (bookIdOptional.isEmpty()) {
            return;
        }

        Integer bookId = bookIdOptional.get();
        try {
            dependencyInjector.getBookCommandApplicationService().deleteBook(new DeleteBookCommand(bookId));
            userBookStateChangePublisher.notifySubscribers();
            JOptionPane.showMessageDialog(this,
                    "Book deleted successfully!",
                    "Succesfull Operation",
                    JOptionPane.INFORMATION_MESSAGE);
            bookIdField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayBook() {
        Optional<Integer> bookIdOptional = getValidQueriedBookId();
        if (bookIdOptional.isEmpty()) {
            return;
        }

        Integer bookId = bookIdOptional.get();
        try {
            FindBookResponse bookDetails = dependencyInjector.getBookQueryApplicationService().findBook(new GetBookQuery(bookId));
            if (bookDetails != null) {
                SwingUtilities.invokeLater(() ->
                        new BookEdit(bookDetails, dependencyInjector, userBookStateChangePublisher));
            } else {
                JOptionPane.showMessageDialog(this,
                        "Book not found!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Optional<Integer> getValidQueriedBookId() {
        try {
            return Optional.of(Integer.parseInt(bookIdField.getText().trim()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid Book ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }
}
