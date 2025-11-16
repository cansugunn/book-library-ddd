package com.finalproject.presentation.views;

import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.configuration.DependencyInjector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class WishlistNotification extends JDialog {
    private JPanel userPanel;
    private JScrollPane jScrollPane;
    private JTable wishlistTable;
    private JPanel bottomPanel;
    private JButton closeButton;
    private final DependencyInjector dependencyInjector;

    public WishlistNotification(Frame parent, DependencyInjector dependencyInjector) {
        super(parent, "Wishlist Books", true);
        this.dependencyInjector = dependencyInjector;
        setupUI();
        loadWishlistData();
        setLocationAndVisibility(parent);
    }

    private void setLocationAndVisibility(Frame parent) {
        this.setLocationRelativeTo(parent);
        Point parentLocation = parent.getLocation();
        Dimension parentSize = parent.getSize();
        Dimension currentSize = this.getSize();
        int x = parentLocation.x + parentSize.width - currentSize.width - 20;
        int y = parentLocation.y + parentSize.height - currentSize.height - 20;
        this.setLocation(x, y);
        this.setVisible(true);
    }

    private void setupUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 400);
        setLocationRelativeTo(getParent());
        setContentPane(userPanel);

        closeButton.addActionListener(e -> dispose());
    }

    private void loadWishlistData() {
        String[] columns = {"ID", "Title", "Author Name", "Author Surname", "Year", "Pages", "About", "Cover", "Rating", "Release Date"};
        try {
            List<FindUserBookStateResponse> books = dependencyInjector.getUserBookStateApplicationService()
                    .findWishedBooksToReadThatWillBeDoneIn1WeekOfCurrentUser();

            Object[][] data = books.isEmpty()
                    ? new Object[][]{Arrays.copyOf(new Object[]{"No books", "due this week"}, 10)}
                    : books.stream().map(book -> new Object[]{
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
            }).toArray(Object[][]::new);

            wishlistTable.setModel(new DefaultTableModel(data, columns));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading wishlist: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
} 