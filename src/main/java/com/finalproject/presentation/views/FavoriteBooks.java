package com.finalproject.presentation.views;

import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.configuration.DependencyInjector;
import com.finalproject.presentation.util.UserBookStateChangeSubscriber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FavoriteBooks extends JPanel implements UserBookStateChangeSubscriber {
    private JPanel userPanel;
    private JScrollPane jScrollPanel;
    private JTable favoriteBooksTable;

    private final DependencyInjector dependencyInjector;

    public FavoriteBooks(DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector;
        setupUI();
        loadFavoriteBooks();
    }

    private void setupUI() {
        favoriteBooksTable.setFillsViewportHeight(true);
        favoriteBooksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        userPanel.setLayout(new BorderLayout());
        userPanel.add(jScrollPanel, BorderLayout.CENTER);
        add(userPanel, BorderLayout.CENTER);
    }

    private void loadFavoriteBooks() {
        String[] columnNames = {"ID", "Title", "Author Name", "Author Surname", "Year", "Pages", "Rating"};
        favoriteBooksTable.setModel(new DefaultTableModel( columnNames, 0));
        refresh();
    }

    @Override
    public void refresh() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) favoriteBooksTable.getModel();
        defaultTableModel.setRowCount(0);
        List<FindUserBookStateResponse> books = dependencyInjector.getUserBookStateApplicationService()
                .findFavouriteBooksOfCurrentUser();
        books.stream()
                .map(book -> new Object[]{
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthorName(),
                        book.getAuthorSurname(),
                        book.getYear(),
                        book.getNumberOfPages(),
                        book.getRating()
                })
                .forEach(defaultTableModel::addRow);
    }
}