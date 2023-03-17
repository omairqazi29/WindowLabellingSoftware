package main.ui.menu;

// Represents a menu with name
public abstract class Menu {
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
