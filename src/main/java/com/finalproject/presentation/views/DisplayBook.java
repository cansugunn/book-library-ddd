package com.finalproject.presentation.views;

import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.configuration.DependencyInjector;
import com.finalproject.domain.valueobject.Read;
import com.finalproject.presentation.util.UserBookStateChangeSubscriber;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class DisplayBook extends JPanel implements UserBookStateChangeSubscriber {
    private JPanel mainPanel;
    private JTextField bookIdField;
    private JButton displayButton;
    private JButton manageStateButton;
    private JLabel titleLabel;
    private JLabel authorNameLabel;
    private JLabel authorSurnameLabel;
    private JLabel yearLabel;
    private JLabel pagesLabel;
    private JLabel aboutLabel;
    private JLabel ratingLabel;
    private JLabel releaseDateLabel;
    private JLabel coverLabel;
    private JLabel commentsLabel;
    private JLabel coverPathLabel;
    private JLabel readLabel;

    private final DependencyInjector dependencyInjector;
    private FindUserBookStateResponse currentBook;

    private final UserBookStateChangeSubscriber[] subscriberViews;

    public DisplayBook(DependencyInjector dependencyInjector,
                       UserBookStateChangeSubscriber... subscriberViews) {
        this.dependencyInjector = dependencyInjector;
        this.subscriberViews = subscriberViews;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        styleButtons(displayButton, manageStateButton);
        manageStateButton.setEnabled(false);

        setDefaultInitialCover();

        displayButton.addActionListener(e -> displayBookInfo());
        manageStateButton.addActionListener(e -> openBookStateManager());
    }

    private void setDefaultInitialCover() {
        coverLabel.setText("No Cover");
        coverLabel.setIcon(null);
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverLabel.setVerticalAlignment(SwingConstants.CENTER);
        coverLabel.setBackground(new Color(245, 245, 245));
        coverLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    private void styleButtons(JButton... buttons) {
        for (JButton button : buttons) {
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    private void displayBookInfo() {
        String bookIdText = bookIdField.getText().trim();
        if (bookIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Book ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdText);
            currentBook = dependencyInjector.getUserBookStateApplicationService()
                    .findUserBookOfCurrentUser(bookId);
            refresh();
            manageStateButton.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Book ID: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            manageStateButton.setEnabled(false);
        }
    }

    private void openBookStateManager() {
        if (currentBook != null) {
            SwingUtilities.invokeLater(() ->{
                BookStateManager bookStateManager =
                        new BookStateManager(dependencyInjector,
                                currentBook.getBookId(),
                                Stream.concat(Arrays.stream(subscriberViews), Stream.of(this))
                                        .toArray(UserBookStateChangeSubscriber[]::new));
                refresh();
            });
        }
    }

    @Override
    public void refresh() {
        currentBook = dependencyInjector.getUserBookStateApplicationService()
                .findUserBookOfCurrentUser(currentBook.getBookId());

        titleLabel.setText(currentBook.getTitle());
        authorNameLabel.setText(currentBook.getAuthorName());
        authorSurnameLabel.setText(currentBook.getAuthorSurname());
        yearLabel.setText(String.valueOf(currentBook.getYear()));
        pagesLabel.setText(String.valueOf(currentBook.getNumberOfPages()));
        aboutLabel.setText(currentBook.getAbout());
        readLabel.setText(currentBook.getRead() != null ? currentBook.getRead().toString() : Read.NOT_READ.name());
        ratingLabel.setText(currentBook.getRating() != null ? String.valueOf(currentBook.getRating()) : String.valueOf(0));
        releaseDateLabel.setText(currentBook.getReleaseDate() != null ? currentBook.getReleaseDate().toString() : "");
        commentsLabel.setText(currentBook.getComments() != null ? String.join(",", currentBook.getComments()) : "");
        coverPathLabel.setText(currentBook.getCoverPath() != null ? currentBook.getCoverPath() : "");

        displayImage();
    }

    private void displayImage() {
        if (!coverPathLabel.getText().isEmpty()) {
            File imageFile = new File(currentBook.getCoverPath());
            if (imageFile.exists()) {
                try {
                    Image image = ImageIO.read(imageFile);
                    Image scaledImage = image.getScaledInstance(160, 200, Image.SCALE_SMOOTH);
                    coverLabel.setIcon(new ImageIcon(scaledImage));
                    coverLabel.setText("");
                } catch (IOException e) {
                    setDefaultInitialCover();
                }
            } else {
                setDefaultInitialCover();
            }
        } else {
            setDefaultInitialCover();
        }
    }
}