package main.ui.menu;

import main.model.Label;
import main.utils.CSVManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.IOException;
import java.util.List;

// Represents a menu for generating label
public class GenerateLabelMenu extends Menu {
    private final String[] seriesOptions = {"Select a Series", "300 Series", "5000 Series", "Nova Sliding Patio Door"};
    private final String[] windowOptions = {"Please select a Series"};
    private final String[] glassOptions = {"Please select a Window Type"};
    private final String[] performanceOptions = {"Please select a Series"};
    private final JComboBox<String> seriesDropDown = new JComboBox<>(seriesOptions);
    private final JComboBox<String> windowDropDown = new JComboBox<>(windowOptions);
    private final JComboBox<String> glassDropDown = new JComboBox<>(glassOptions);
    private final JComboBox<String> performanceDropDown = new JComboBox<>(performanceOptions);

    // EFFECTS: constructs a menu with generate label name;
    // throws IOException if error occurs in file operations
    public GenerateLabelMenu(JFrame parentFrame) throws IOException {
        super("Generate Label");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(parentFrame.getWidth(), parentFrame.getHeight());
        setLocationRelativeTo(parentFrame);

        add(mainPanel(), BorderLayout.CENTER);
        startDropDownListerner();
        setVisible(true);
    }

    private void startDropDownListerner() {
        seriesDropDown.addItemListener(e -> {
            windowDropDown.removeAllItems();
            glassDropDown.removeAllItems();
            performanceDropDown.removeAllItems();
            CSVManager csvManager = CSVManager.getInstance();

            String selectedValue = (String) seriesDropDown.getSelectedItem();
            List<String> windowTypes = csvManager.getWindowType(selectedValue);
            for (String window : windowTypes) {
                windowDropDown.addItem(window);
            }
            List<String> performanceTypes = csvManager.getPerformanceType(selectedValue);
            for (String window : performanceTypes) {
                performanceDropDown.addItem(window);
            }
        });

        windowDropDown.addItemListener(e -> {
            glassDropDown.removeAllItems();
            String series = (String) seriesDropDown.getSelectedItem();
            CSVManager csvManager = CSVManager.getInstance();

            String selectedValue = (String) windowDropDown.getSelectedItem();
            List<String> glassOptions = csvManager.getGlassOption(series, selectedValue);
            for (String glass : glassOptions) {
                glassDropDown.addItem(glass);
            }
        });
    }

    // EFFECTS: creates and returns the main menu JPanel
    private JPanel mainPanel() {
        JLabel seriesLabel = new JLabel("Series:");
        JLabel windowLabel = new JLabel("Window Type:");
        JLabel glassLabel = new JLabel("Glass Option:");
        JLabel performanceLabel = new JLabel("Performance for Window: ");
        JButton generateButton = createButton("Generate Label", "genLabel");
        JButton backButton = createButton("Back", "back");

        // Create layout and add components to main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(seriesLabel, gbc);
        gbc.gridx++;
        mainPanel.add(seriesDropDown, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(windowLabel, gbc);
        gbc.gridx++;
        mainPanel.add(windowDropDown, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(glassLabel, gbc);
        gbc.gridx++;
        mainPanel.add(glassDropDown, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(performanceLabel, gbc);
        gbc.gridx++;
        mainPanel.add(performanceDropDown, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(generateButton, gbc);
        gbc.gridx++;
        mainPanel.add(backButton, gbc);

        return mainPanel;
    }

    // EFFECTS: creates and returns the JButton with given text and command
    private JButton createButton(String buttonText, String actionCommand) {
        JButton button = new JButton(buttonText);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        return button;
    }

    // EFFECTS: asks details and generates the image of the label accordingly and returns it;
    // throws IOException if error occurs while reading or writing image
    private void runMenu() throws IOException {
        String series = (String) seriesDropDown.getSelectedItem();
        String windowType = (String) windowDropDown.getSelectedItem();
        String glassOption = (String) glassDropDown.getSelectedItem();
        String performanceWindow = (String) performanceDropDown.getSelectedItem();
        String model = CSVManager.getInstance().getModelCode(series, windowType, glassOption);
        List<Double> ratings = CSVManager.getInstance().getRatings(series, windowType, glassOption);
        String report = CSVManager.getInstance().getReport(series, windowType, glassOption);
        String performance = CSVManager.getInstance().getPerformanceRatings(series, performanceWindow);

        Label label = new Label(series + " " + windowType + "\n" + model + "\n" + report,
                ratings.get(0), ratings.get(1), ratings.get(2), ratings.get(3), performance);
        try {
            PrinterJob printJob = PrinterJob.getPrinterJob();

            // Create a PageFormat object with the desired paper size
            PageFormat pageFormat = printJob.defaultPage();
            Paper paper = pageFormat.getPaper();

            // Adjust paper size to image size
            BufferedImage image = label.generateImage();
            double paperWidth = image.getWidth();
            double paperHeight = image.getHeight();

            // Create a margin (in points)
            double margin = 10;

            // Set paper size and imageable area accounting for margins
            paper.setSize(paperWidth, paperHeight);
            paper.setImageableArea(margin, margin, paperWidth - margin, paperHeight - 2*margin);
            pageFormat.setPaper(paper);

            printJob.setPrintable((graphics, pf, pageIndex) -> {
                if (pageIndex != 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                double imageWidth = image.getWidth();
                double imageHeight = image.getHeight();

                double scaleX = (pf.getImageableWidth() - 2 * margin) / imageWidth;
                double scaleY = (pf.getImageableHeight() - 2 * margin) / imageHeight;
                double scale = Math.min(scaleX, scaleY);

                int scaledWidth = (int) (imageWidth * scale);
                int scaledHeight = (int) (imageHeight * scale);

                int x = (int) ((pf.getImageableWidth() - scaledWidth) / 2);
                int y = (int) ((pf.getImageableHeight() - scaledHeight) / 2);

                graphics.drawImage(image, x, y, scaledWidth, scaledHeight, null);
                return Printable.PAGE_EXISTS;
            });

            if (printJob.printDialog()) {
                printJob.print();
            }
        } catch (PrinterException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("genLabel".equals(e.getActionCommand())) {
            try {
                runMenu();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            setVisible(false);
        }
    }
}
