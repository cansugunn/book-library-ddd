package com.finalproject.presentation.views;

import com.finalproject.application.dto.FindAuthorResponse;
import com.finalproject.configuration.DependencyInjector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchAuthor extends JPanel {
    private JPanel mainPanel;
    private JTextField authorNameField;
    private JButton searchButton;
    private JTable authorTable;

    private DependencyInjector dependencyInjector;

    public SearchAuthor(DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector;
        setupUI();
        setupTable();
    }

    private void setupTable() {
        String[] columns = {"ID", "Name", "Surname", "Website"};
        authorTable.setModel(new DefaultTableModel(columns, 0));
        searchButton.addActionListener(e -> searchAuthor());
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private void searchAuthor() {
        String name = authorNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter an author name",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<FindAuthorResponse> authors = dependencyInjector.getAuthorApplicationService().findAuthor(name);

        DefaultTableModel model = (DefaultTableModel) authorTable.getModel();
        model.setRowCount(0);
        authors.stream()
                .map(author -> new Object[]{
                        author.getId(),
                        author.getName(),
                        author.getSurname(),
                        author.getWebsiteUrl()
                }).forEach(model::addRow);

        if (authors.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No authors found.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
} 