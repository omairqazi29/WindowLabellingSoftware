package main.ui;

import main.ui.menu.GenerateLabelMenu;

import java.util.Scanner;

public class WindowLabellingSoftware {

    // EFFECTS: constructs the UI and runs the app
    public WindowLabellingSoftware() {
        runApp();
    }

    // EFFECTS: Initializes and displays the main options
    private void runApp() {
        boolean quit = false;
        String option;
        Scanner input = new Scanner(System.in);

        while (!quit) {
            displayMenu();
            option = input.next().toLowerCase();

            if (option.equals("q")) {
                quit = true;
            } else {
                selectOption(option);
            }
        }

        System.out.println("Thank you for using Window Labelling Software :)\nHave a nice day, bye for now!");
    }

    // EFFECTS: displays the options (or features/functionality)
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tg -> generate label");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: selects and runs the appropriate menu
    private void selectOption(String option) {
        try {
            switch (option) {
                case "g":
                    new GenerateLabelMenu();
                    break;
                default:
                    System.out.println("Invalid option specified\nPlease enter a valid option");
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            runApp();
        }
    }
}
