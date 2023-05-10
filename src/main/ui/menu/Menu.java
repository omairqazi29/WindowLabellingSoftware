package main.ui.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

// Represents a menu with name
public abstract class Menu extends JFrame implements ActionListener {
    private final String name;

    // EFFECTS: constructs a menu with the given name
    public Menu(String name) {
        this.name = name;
    }

    // EFFECTS: returns the name
    public String getName() {
        return name;
    }
}
