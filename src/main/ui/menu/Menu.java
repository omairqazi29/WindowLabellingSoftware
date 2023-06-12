package main.ui.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

// Represents a menu with name
public abstract class Menu extends JFrame implements ActionListener {
    // EFFECTS: constructs a menu with the given title
    public Menu(String name) {
        super(name);
    }
}
