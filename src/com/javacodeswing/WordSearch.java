package com.javacodeswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
public class WordSearch extends JFrame {
    private static final int GRID_SIZE = 10;
    private static final List<String> WORD_POOL = Arrays.asList(
            "JAVA", "PYTHON", "RUBY", "KOTLIN", "SWIFT", "JAVASCRIPT",
            "GO", "RUST", "PHP", "PERL", "SCALA", "HTML", "CSS"
    );

    private char[][] grid = new char[GRID_SIZE][GRID_SIZE];
    private JButton[][] buttons = new JButton[GRID_SIZE][GRID_SIZE];
    private Set<Point> selectedPositions = new HashSet<>();
    private ArrayList<WordPlacement> placedWords = new ArrayList<>();
    private JTextArea wordListArea;
    private JLabel statusLabel, scoreLabel;
    private int score = 0;
    private JButton foundWordButton;

    public WordSearch() {
        setTitle("Word Search Game");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int row = i, col = j;
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 16));
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].addActionListener(e -> handleSelection(row, col));
                gridPanel.add(buttons[i][j]);
            }
        }

        JPanel controlsPanel = new JPanel();
        JButton newGameButton = new JButton("New Game");
        foundWordButton = new JButton("Found Word");
        foundWordButton.setEnabled(false);
        controlsPanel.add(newGameButton);
        controlsPanel.add(foundWordButton);

        wordListArea = new JTextArea(6, 20);
        wordListArea.setEditable(false);
        wordListArea.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane wordListScroll = new JScrollPane(wordListArea);

        statusLabel = new JLabel("Find words in the grid!");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel statusPanel = new JPanel();
        statusPanel.add(statusLabel);
        statusPanel.add(scoreLabel);

        add(gridPanel, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.SOUTH);
        add(wordListScroll, BorderLayout.EAST);
        add(statusPanel, BorderLayout.NORTH);

        newGameButton.addActionListener(e -> generateNewPuzzle());
        foundWordButton.addActionListener(e -> checkFoundWord());

        generateNewPuzzle();
        setVisible(true);
    }
    

    private void handleSelection(int row, int col) {
        Point point = new Point(row, col);

        if (selectedPositions.contains(point)) {
            // Deselect the letter
            selectedPositions.remove(point);
            buttons[row][col].setBackground(Color.WHITE);
        } else {
            // Select the letter
            selectedPositions.add(point);
            buttons[row][col].setBackground(Color.YELLOW);
        }

        // Enable "Found Word" button if at least one letter is selected
        foundWordButton.setEnabled(!selectedPositions.isEmpty());
    }


    private void checkFoundWord() {
        List<String> matchedWords = new ArrayList<>();
        Iterator<WordPlacement> iterator = placedWords.iterator();

        while (iterator.hasNext()) {
            WordPlacement wp = iterator.next();
            if (selectedPositions.equals(wp.positions)) {
                matchedWords.add(wp.word);
                score += 10;

                for (Point p : wp.positions) {
                    buttons[p.x][p.y].setBackground(Color.LIGHT_GRAY); // Gray out matched words
                }

                iterator.remove();
            }
        }

        if (!matchedWords.isEmpty()) {
            statusLabel.setText("Matched: " + String.join(", ", matchedWords));
            scoreLabel.setText("Score: " + score);
            updateWordList();
        } else {
            statusLabel.setText("No match found. Try again.");
        }

        selectedPositions.clear();
        foundWordButton.setEnabled(false);

        if (placedWords.isEmpty()) {
            JOptionPane.showMessageDialog(this, "🎉 Congratulations! You found all words!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateWordList() {
        StringBuilder updatedList = new StringBuilder("Find these words:\n");
        for (WordPlacement wp : placedWords) {
            updatedList.append(wp.word).append("\n");
        }
        wordListArea.setText(updatedList.toString());
    }

    private void generateNewPuzzle() {
        resetGrid();
        List<String> shuffledWords = new ArrayList<>(WORD_POOL);
        Collections.shuffle(shuffledWords);
        placedWords.clear();

        for (String word : shuffledWords.subList(0, 5)) {
            placeWordRandomly(word);
        }
        fillEmptySpaces();
        updateGridDisplay();

        wordListArea.setText("Find these words:\n" + String.join("\n", shuffledWords.subList(0, 5)));
        score = 0;
        scoreLabel.setText("Score: 0");
        statusLabel.setText("Find words in the grid!");
    }

    private void resetGrid() {
        selectedPositions.clear();
        for (int i = 0; i < GRID_SIZE; i++) {
            Arrays.fill(grid[i], ' ');
        }
    }

    private void updateGridDisplay() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setText(String.valueOf(grid[i][j]));
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(Color.WHITE);
            }
        }
    }

    private void fillEmptySpaces() {
        Random rand = new Random();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == ' ') {
                    grid[i][j] = (char) ('A' + rand.nextInt(26));
                }
            }
        }
    }

    private void placeWordRandomly(String word) {
        Random rand = new Random();
        for (int attempt = 0; attempt < 100; attempt++) {
            int row = rand.nextInt(GRID_SIZE);
            int col = rand.nextInt(GRID_SIZE);
            int direction = rand.nextInt(8);
            if (canPlaceWord(word, row, col, direction)) {
                WordPlacement wp = new WordPlacement(word);
                placeWord(word, row, col, direction, wp);
                placedWords.add(wp);
                return;
            }
        }
    }

    private boolean canPlaceWord(String word, int row, int col, int direction) {
        int dx = 0, dy = 0;
        switch (direction) {
            case 0: dx = 1; break;
            case 1: dy = 1; break;
            case 2: dx = 1; dy = 1; break;
            case 3: dx = -1; break;
            case 4: dy = -1; break;
            case 5: dx = -1; dy = -1; break;
            case 6: dx = 1; dy = -1; break;
            case 7: dx = -1; dy = 1; break;
        }

        for (int i = 0; i < word.length(); i++) {
            int x = row + i * dy;
            int y = col + i * dx;
            if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE || (grid[x][y] != ' ' && grid[x][y] != word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private void placeWord(String word, int row, int col, int direction, WordPlacement wp) {
        int dx = 0, dy = 0;
        switch (direction) {
            case 0: dx = 1; break;
            case 1: dy = 1; break;
            case 2: dx = 1; dy = 1; break;
            case 3: dx = -1; break;
            case 4: dy = -1; break;
            case 5: dx = -1; dy = -1; break;
            case 6: dx = 1; dy = -1; break;
            case 7: dx = -1; dy = 1; break;
        }

        for (int i = 0; i < word.length(); i++) {
            int x = row + i * dy;
            int y = col + i * dx;
            grid[x][y] = word.charAt(i);
            wp.positions.add(new Point(x, y));
        }
    }

    private static class WordPlacement {
        String word;
        Set<Point> positions = new HashSet<>();
        WordPlacement(String w) { word = w; }
    }

    public static void main(String[] args) {
        new SplashScreen();
    }
}
class SplashScreen extends JWindow {
    public SplashScreen() {
        JLabel label = new JLabel(new ImageIcon("splash.png")); 
        getContentPane().add(label, BorderLayout.CENTER);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setVisible(false);
        dispose();
        new MainMenu();
    }
}

class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Word Search - Main Menu");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Word Search Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        startButton.addActionListener(e -> {
            new WordSearch();
            dispose();
        });

        exitButton.addActionListener(e -> System.exit(0));

        add(titleLabel);
        add(startButton);
        add(exitButton);

        setVisible(true);
    }
}
