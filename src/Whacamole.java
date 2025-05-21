import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Whacamole {
    int boardWidth = 600, boardHeight = 700;

    JFrame frame = new JFrame("Mario: Whac A Mole");
    JLabel textLabel = new JLabel();      // Displays score or game status
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel bottomPanel = new JPanel();

    JButton[] board = new JButton[9];
    ImageIcon moleIcon, plantIcon;

    JButton currMoleTile, currPlantTile;

    Random random = new Random();
    Timer setMoleTimer, setPlantTimer;
    int score = 0;
    int highScore = 0;

    Whacamole() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //Top Panel: Score
        textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score: " + score + "    High Score: " + highScore);
        textLabel.setOpaque(true);
        textLabel.setBackground(new Color(0xFFDD99));
        textLabel.setForeground(new Color(0x663300));
        textLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        textPanel.setLayout(new BorderLayout());
        textPanel.setBackground(new Color(0xFFDD99));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        //Board Panel: 3x3 Grid
        boardPanel.setLayout(new GridLayout(3, 3, 8, 8)); // spacing between buttons
        boardPanel.setBackground(new Color(0x223322));  // dark green background
        boardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        frame.add(boardPanel);

        // Bottom Panel: Restart Button
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(0xFFDD99));
        JButton restartButton = new JButton("Restart Game");
        restartButton.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        restartButton.setBackground(new Color(0x996633));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(restartButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        restartButton.addActionListener(e -> restartGame());

        // Load and scale icons
        plantIcon = new ImageIcon(getClass().getResource("./piranha.png"));
        plantIcon = new ImageIcon(plantIcon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH));
        moleIcon = new ImageIcon(new ImageIcon(getClass().getResource("./monty.png")).getImage()
                .getScaledInstance(140, 140, Image.SCALE_SMOOTH));

        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);
            tile.setBackground(new Color(0x99CC66));  // light green
            tile.setBorder(BorderFactory.createLineBorder(new Color(0x556633), 3));
            tile.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tile.setOpaque(true);
            tile.setFont(new Font("Arial", Font.BOLD, 24));

            tile.addActionListener(e -> {
                JButton clicked = (JButton) e.getSource();
                if (clicked == currMoleTile) {
                    score += 10;
                    updateScoreLabel();
                } else if (clicked == currPlantTile) {
                    gameOver();
                }
            });
        }

        // Mole appears every 1 sec
        setMoleTimer = new Timer(1000, e -> {
            if (currMoleTile != null) currMoleTile.setIcon(null);
            int num = random.nextInt(9);
            JButton tile = board[num];
            if (tile == currPlantTile) return;
            currMoleTile = tile;
            currMoleTile.setIcon(moleIcon);
        });

        // Plant appears every 1.5 sec
        setPlantTimer = new Timer(1500, e -> {
            if (currPlantTile != null) currPlantTile.setIcon(null);
            int num = random.nextInt(9);
            JButton tile = board[num];
            if (tile == currMoleTile) return;
            currPlantTile = tile;
            currPlantTile.setIcon(plantIcon);
        });

        setMoleTimer.start();
        setPlantTimer.start();

        frame.setVisible(true);
    }

    void updateScoreLabel() {
        textLabel.setText("Score: " + score + "    High Score: " + highScore);
    }

    void gameOver() {
        textLabel.setText("Game Over! Final Score: " + score + "    High Score: " + highScore);
        setMoleTimer.stop();
        setPlantTimer.stop();
        for (JButton b : board) b.setEnabled(false);
        if (score > highScore) {
            highScore = score;
        }
    }

    void restartGame() {
        score = 0;
        updateScoreLabel();
        for (JButton b : board) {
            b.setEnabled(true);
            b.setIcon(null);
        }
        currMoleTile = null;
        currPlantTile = null;
        setMoleTimer.start();
        setPlantTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Whacamole());
    }
}
