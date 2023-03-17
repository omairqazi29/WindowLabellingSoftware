package main.ui.menu;

import main.model.Label;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

// Represents a menu for generating label
public class GenerateLabelMenu extends Menu {
    private static final String PATH = "./data/";
    private final Scanner input = new Scanner(System.in);

    // EFFECTS: constructs a menu with generate label name;
    // throws IOException if error occurs in file operations
    public GenerateLabelMenu() throws IOException {
        super("Generate Label");
        runMenu();
    }

    // EFFECTS: asks details and generates the image of the label accordingly and returns it;
    // throws IOException if error occurs while reading or writing image
    private void runMenu() throws IOException {
        String windowType = askWindowType();
        String glassOption = askGlassOption();

        if (windowType.equals("Casement") && glassOption.equals("LoE-270/Clear")) {
            Label label = new Label("Oasis Windows (Canada) Inc.",
                    "5000 Series Casement Window\n" +
                    "OASW-CA-3,270(2)-3,Clear\n" +
                    "Report: T636-65 (Issued November 20, 2020)",
                    1.47, 0.24, 22, 0.44);
            System.out.println("Enter name of the file to save label as:");
            String name = input.next().toLowerCase();
            try {
                ImageIO.write(label.getImage(), "png", new File(PATH + name + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // EFFECTS: asks the window type and returns the chosen type as string
    private String askWindowType() {
        System.out.println("\nSelect Window Type from:");
        System.out.println("\tc -> Casement");
        String option = input.next().toLowerCase();
        String windowType = "";

        switch (option) {
            case "c":
                windowType = "Casement";
                break;
            default:
                System.out.println("Invalid option specified\nPlease enter a valid option");
                askWindowType();
                break;
        }
        return windowType;
    }

    // EFFECTS: asks the glass option and returns the chosen option as string
    private String askGlassOption() {
        System.out.println("\nSelect Glass Option from:");
        System.out.println("\tl -> LoE-270/Clear");
        String option = input.next().toLowerCase();
        String glassOption = "";

        switch (option) {
            case "l":
                glassOption = "LoE-270/Clear";
                break;
            default:
                System.out.println("Invalid option specified\nPlease enter a valid option");
                askGlassOption();
                break;
        }
        return glassOption;
    }
}
