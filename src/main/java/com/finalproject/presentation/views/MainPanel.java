package com.finalproject.presentation.views;

import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.configuration.DependencyInjector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

//todo ultra refactoring
public class MainPanel extends JFrame {
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel userInfoPanel;
    private JLabel userNameLabel;
    private JLabel userTypeLabel;
    private JLabel userImageLabel;
    private JTabbedPane rightTabbedPane;
    private JSplitPane splitPane;

    private final DependencyInjector dependencyInjector;
    private final FindUserResponse findUserResponse;

    public MainPanel(DependencyInjector dependencyInjector,
                     FindUserResponse findUserResponse) {
        this.dependencyInjector = dependencyInjector;
        this.findUserResponse = findUserResponse;

        setupUI();
    }

    private void setupUI() {
        setTitle("Library Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        setupLeftPanel();
        setupRightPanel();

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightTabbedPane);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        setContentPane(mainPanel);

        modernizeUI();
    }

    private void modernizeUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLeftPanel() {
        userNameLabel.setText(findUserResponse.getUsername());
        userTypeLabel.setText(findUserResponse.getUserType().name());
    }

    private void setupRightPanel() {
        // Favorites Tab
        FavoriteBooks favoriteBooks = new FavoriteBooks(dependencyInjector);
        FavoriteAuthors favoriteAuthors = new FavoriteAuthors(dependencyInjector);

        JPanel favoritesPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        favoritesPanel.setBackground(new Color(245, 245, 245));

        favoritesPanel.add(wrapInPanel(favoriteBooks, "Favorite Books"));
        favoritesPanel.add(wrapInPanel(favoriteAuthors, "Favorite Authors"));

        JScrollPane favoritesScroll = new JScrollPane(favoritesPanel);
        favoritesScroll.getVerticalScrollBar().setUnitIncrement(16);
        rightTabbedPane.addTab("Favorites", favoritesScroll);


        // My Books Tab
        JPanel myBooksPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        myBooksPanel.setBackground(new Color(245, 245, 245));

        UnreadBooks unreadBooks = new UnreadBooks(dependencyInjector);
        DisplayBook displayBook = new DisplayBook(dependencyInjector, favoriteAuthors, unreadBooks, favoriteBooks);

        myBooksPanel.add(wrapInPanel(unreadBooks, "Unread Books"));
        myBooksPanel.add(wrapInPanel(displayBook, "Book Details"));
        JScrollPane myBooksScroll = new JScrollPane(myBooksPanel);
        myBooksScroll.getVerticalScrollBar().setUnitIncrement(16);
        rightTabbedPane.insertTab("My Books", null, myBooksScroll, "My Books", 1);

        if (dependencyInjector.getUserApplicationService().isCurrentUserAdmin()) {
            SwingUtilities.invokeLater(() -> new WishlistNotification(this, dependencyInjector));
        }

        SearchAuthor searchAuthor = new SearchAuthor(dependencyInjector);
        JPanel searchPanel = wrapInPanel(searchAuthor, "Search Authors");

        JScrollPane searchScroll = new JScrollPane(searchPanel);
        searchScroll.getVerticalScrollBar().setUnitIncrement(16);
        rightTabbedPane.addTab("Authors", searchScroll);

        // Book Management
        if (dependencyInjector.getUserApplicationService().isCurrentUserAdmin()) {
            JPanel managePanel = new JPanel(new GridLayout(2, 1, 0, 10));
            managePanel.setBackground(new Color(245, 245, 245));
            managePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            BookOperations bookOperations = new BookOperations(dependencyInjector, favoriteBooks, favoriteAuthors, unreadBooks);
            BookCreation bookCreation = new BookCreation(dependencyInjector, favoriteAuthors, unreadBooks);

            managePanel.add(wrapInPanel(bookCreation, "Create New Book"));
            managePanel.add(wrapInPanel(bookOperations, "Book Operations"));

            JScrollPane manageScroll = new JScrollPane(managePanel);
            manageScroll.getVerticalScrollBar().setUnitIncrement(16);
            rightTabbedPane.addTab("Manage Books", manageScroll);
        }
    }

    private JPanel wrapInPanel(Container content, String title) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(255, 255, 255));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Add title if provided
        if (title != null && !title.isEmpty()) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(new Color(51, 51, 51));
            titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
            wrapper.add(titleLabel, BorderLayout.NORTH);
        }

        wrapper.add(content, BorderLayout.CENTER);
        return wrapper;
    }
} 