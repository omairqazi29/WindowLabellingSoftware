package main.ui;

import main.ui.menu.GenerateLabelMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class WindowLabellingSoftware extends JFrame implements ActionListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    // EFFECTS: constructs the UI and runs the app
    public WindowLabellingSoftware() {
        super("Energy Label Generator | Oasis Windows");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        add(mainPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    // EFFECTS: creates and returns the main menu JPanel
    private JPanel mainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        JButton generateButton = createButton("Generate Label", "genLabel");
        JButton quitButton = createButton("Quit", "quit");
        mainPanel.add(generateButton);
        mainPanel.add(quitButton);
        return mainPanel;
    }

    // EFFECTS: creates and returns the JButton with given text and command
    private JButton createButton(String buttonText, String actionCommand) {
        JButton button = new JButton(buttonText);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("genLabel".equals(e.getActionCommand())) {
            try {
                new GenerateLabelMenu(this);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            dispose();
            System.exit(0);
        }
    }
}
