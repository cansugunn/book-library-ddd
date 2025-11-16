package com.finalproject.presentation.views;

import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.configuration.DependencyInjector;
import com.finalproject.presentation.util.UserBookStateChangeSubscriber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UnreadBooks extends JPanel implements UserBookStateChangeSubscriber {
    private JPanel userPanel;
    private JTable unreadBooksTable;
    private JScrollPane jScrollPanel;

    private final DependencyInjector dependencyInjector;

    public UnreadBooks(DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector;
        setupUI();
        loadUnreadBooks();
    }

    private void setupUI() {
        unreadBooksTable.setFillsViewportHeight(true);
        unreadBooksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        userPanel.setLayout(new BorderLayout());
        userPanel.add(jScrollPanel, BorderLayout.CENTER);
        add(userPanel, BorderLayout.CENTER);
    }

    private void loadUnreadBooks() {
        String[] columnNames = {"ID", "Title", "Author Name", "Author Surname", "Year", "Pages", "About", "Cover", "Rating", "Release Date"};
        unreadBooksTable.setModel(new DefaultTableModel(columnNames, 0));
        refresh();
    }

    @Override
    public void refresh() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) unreadBooksTable.getModel();
        defaultTableModel.setRowCount(0);
        List<FindUserBookStateResponse> books = dependencyInjector.getUserBookStateApplicationService()
                        .findNotReadBooksYetOfCurrentUser();
        books.stream()
                .map(book -> new Object[]{
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthorName(),
                        book.getAuthorSurname(),
                        book.getYear(),
                        book.getNumberOfPages(),
                        book.getAbout(),
                        book.getCoverPath(),
                        book.getRating(),
                        book.getReleaseDate()
                }).forEach(defaultTableModel::addRow);
    }
}