package com.finalproject.presentation.views;

import com.finalproject.application.dto.FindAuthorResponse;
import com.finalproject.configuration.DependencyInjector;
import com.finalproject.presentation.util.UserBookStateChangeSubscriber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FavoriteAuthors extends JPanel implements UserBookStateChangeSubscriber {
    private JPanel userPanel;
    private JScrollPane jScrollPanel;
    private JTable favoriteAuthorsTable;

    private final DependencyInjector dependencyInjector;

    public FavoriteAuthors(DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector;
        setupUI();
        loadFavoriteAuthors();
    }

    private void setupUI() {
        favoriteAuthorsTable.setFillsViewportHeight(true);
        favoriteAuthorsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        userPanel.setLayout(new BorderLayout());
        userPanel.add(jScrollPanel, BorderLayout.CENTER);
        add(userPanel, BorderLayout.CENTER);
    }

    private void loadFavoriteAuthors() {
        String[] columnNames = {"ID", "Name", "Surname", "Website"};
        favoriteAuthorsTable.setModel(new DefaultTableModel(columnNames, 0));
        refresh();
    }

    @Override
    public void refresh() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) favoriteAuthorsTable.getModel();
        defaultTableModel.setRowCount(0);
        List<FindAuthorResponse> authors = dependencyInjector.getAuthorApplicationService()
                .displayFavouriteAuthorsOfCurrentUser();
        authors.stream()
                .map(author -> new Object[]{
                        author.getId(),
                        author.getName(),
                        author.getSurname(),
                        author.getWebsiteUrl()
                })
                .forEach(defaultTableModel::addRow);
    }
}