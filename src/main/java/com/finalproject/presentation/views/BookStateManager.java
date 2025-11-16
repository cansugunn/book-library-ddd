package com.finalproject.presentation.views;

import com.finalproject.application.dto.CreateUserBookStateRequest;
import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.application.dto.UpdateUserBookStateRequest;
import com.finalproject.configuration.DependencyInjector;
import com.finalproject.domain.valueobject.Read;
import com.finalproject.presentation.util.UserBookStateChangePublisher;
import com.finalproject.presentation.util.UserBookStateChangeSubscriber;

import javax.swing.*;
import java.util.*;

public class BookStateManager extends JFrame {
    private JPanel mainPanel;
    private JComboBox<Integer> ratingComboBox;
    private JComboBox<Read> readStatusComboBox;
    private JSpinner releaseDateSpinner;
    private JTextArea commentsTextArea;
    private JButton saveButton;
    private JButton cancelButton;

    private final DependencyInjector dependencyInjector;
    private final int selectedBookId;
    private FindUserBookStateResponse currentState;
    private final UserBookStateChangePublisher userBookStateChangePublisher;

    public BookStateManager(DependencyInjector dependencyInjector,
                            int selectedBookId,
                            UserBookStateChangeSubscriber... subscriberViews) {
        this.dependencyInjector = dependencyInjector;
        this.selectedBookId = selectedBookId;

        userBookStateChangePublisher = new UserBookStateChangePublisher();
        userBookStateChangePublisher.subscribe(subscriberViews);

        setupUI();
        loadCurrentState();
    }

    private void setupUI() {
        setTitle("Manage Book State");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        setupRatingCombo();
        setupReadCombo();
        setupReleaseDateSpinner();

        saveButton.addActionListener(e -> saveState());
        cancelButton.addActionListener(e -> dispose());
    }

    private void setupReleaseDateSpinner() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        calendar.add(Calendar.YEAR, 10);
        Date maxDate = calendar.getTime();

        calendar.add(Calendar.YEAR, -20);
        Date minDate = calendar.getTime();

        SpinnerDateModel dateModel = new SpinnerDateModel(
                currentDate,
                minDate,
                maxDate,
                Calendar.DAY_OF_MONTH);
        releaseDateSpinner.setModel(dateModel);
        releaseDateSpinner.setEditor(new JSpinner.DateEditor(releaseDateSpinner, "dd/MM/yyyy"));
    }

    private void setupReadCombo() {
        for (Read status : Read.values()) {
            readStatusComboBox.addItem(status);
        }

        readStatusComboBox.addActionListener(e -> {
            Read selectedStatus = (Read) readStatusComboBox.getSelectedItem();
            releaseDateSpinner.setEnabled(Read.WISH_TO_BE_READ.equals(selectedStatus));
        });
    }

    private void setupRatingCombo() {
        for (int i = 1; i <= 5; i++) {
            ratingComboBox.addItem(i);
        }
    }

    private void loadCurrentState() {
        try {
            currentState = dependencyInjector.getUserBookStateApplicationService()
                    .findUserBookOfCurrentUser(selectedBookId);

            ratingComboBox.setSelectedItem(currentState.getRating());
            readStatusComboBox.setSelectedItem(currentState.getRead());
            releaseDateSpinner.setValue(Optional.ofNullable(currentState.getReleaseDate()).orElse(new Date()));
            commentsTextArea.setText(String.join("\n", currentState.getComments()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading book state: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveState() {
        try {
            List<String> comments = getComments();
            Integer rating = (Integer) ratingComboBox.getSelectedItem();
            Read readStatus = (Read) readStatusComboBox.getSelectedItem();
            Date releaseDate = Read.WISH_TO_BE_READ.equals(readStatus) ? (Date) releaseDateSpinner.getValue() : null;
            Integer userBookStateId = currentState.getUserBookStateId();

            if (userBookStateId == null) {
                CreateUserBookStateRequest request = new CreateUserBookStateRequest.Builder()
                        .bookId(selectedBookId)
                        .read(readStatus)
                        .rating(rating)
                        .comments(comments)
                        .releaseDate(releaseDate)
                        .build();
                dependencyInjector.getUserBookStateApplicationService()
                        .createUserBookForCurrentUser(request);
            } else {
                UpdateUserBookStateRequest request = new UpdateUserBookStateRequest.Builder()
                        .id(userBookStateId)
                        .read(readStatus)
                        .rating(rating)
                        .comments(comments)
                        .releaseDate(releaseDate)
                        .build();
                dependencyInjector.getUserBookStateApplicationService()
                        .updateUserBookForCurrentUser(request);
            }

            JOptionPane.showMessageDialog(this,
                    "Book state saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            userBookStateChangePublisher.notifySubscribers();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving book state: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<String> getComments() {
        String trimmedCommentsString = commentsTextArea.getText().trim();
        if (!trimmedCommentsString.isEmpty()) {
            return Arrays.stream(trimmedCommentsString.split("\n"))
                    .filter(comment -> !comment.isEmpty())
                    .toList();
        }
        return Collections.emptyList();
    }
} 