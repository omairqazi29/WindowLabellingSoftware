package main.ui.menu;

import main.model.Label;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;

// Represents a menu for generating label
public class GenerateLabelMenu extends Menu {
    private final String[] seriesOptions = {"Select a Series", "300 Series", "5000 Series", "Nova Sliding Patio Door"};
    private String[] windowOptions = {"Please select a Series"};
    private String[] glassOptions = {"Please select a Window Type"};
    private final JComboBox<String> seriesDropDown = new JComboBox<>(seriesOptions);
    private final JComboBox<String> windowDropDown = new JComboBox<>(windowOptions);
    private final JComboBox<String> glassDropDown = new JComboBox<>(glassOptions);
    private final JCheckBox vancouver = new JCheckBox("Vancouver order?");
    private Label label = null;
    private final JLabel uFactorLabel = new JLabel("uFactor Value:");
    private final JTextField uFactorField = new JTextField(10);

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

            String selectedValue = (String) seriesDropDown.getSelectedItem();
            if (selectedValue.equals("300 Series")) {
                windowOptions = new String[]{"Sliding Window", "Vertical Sliding Window", "Picture Window"};
            } else if (selectedValue.equals("5000 Series")) {
                windowOptions = new String[]{"Casement Window", "Awning Window", "Picture Window",
                        "Balanced Sash Picture Window"};
            } else {
                windowOptions = new String[]{"Sliding Patio Door"};
            }

            for (String windowType : windowOptions) {
                windowDropDown.addItem(windowType);
            }
        });

        windowDropDown.addItemListener(e -> {
            glassDropDown.removeAllItems();

            String selectedValue = (String) windowDropDown.getSelectedItem();
            if (selectedValue!=null) {
                if (selectedValue.equals("Sliding Window")) {
                    glassOptions = new String[]{"Sliding Window Option A"};
                } else if (selectedValue.equals("Picture Window")) {
                    glassOptions = new String[]{"Picture Window Option A", "Option B"};
                } else {
                    glassOptions = new String[]{"Option Casement"};
                }

                for (String glassOption : glassOptions) {
                    glassDropDown.addItem(glassOption);
                }
            }
        });
    }

    // EFFECTS: creates and returns the main menu JPanel
    private JPanel mainPanel() {
        JLabel seriesLabel = new JLabel("Series:");
        JLabel windowLabel = new JLabel("Window Type:");
        JLabel glassLabel = new JLabel("Glass Option:");
        JButton generateButton = createButton("Generate Label", "genLabel");
        JButton backButton = createButton("Back", "back");

        JPanel mainPanel = new JPanel();
        mainPanel.add(seriesLabel);
        mainPanel.add(seriesDropDown);
        mainPanel.add(windowLabel);
        mainPanel.add(windowDropDown);
        mainPanel.add(glassLabel);
        mainPanel.add(glassDropDown);
        mainPanel.add(generateButton);
        mainPanel.add(backButton);

        vancouver.setActionCommand("vancouver");
        vancouver.addActionListener(this);
        uFactorLabel.setVisible(false);
        uFactorField.setVisible(false);
        mainPanel.add(vancouver);
        mainPanel.add(uFactorLabel);
        mainPanel.add(uFactorField);
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
        String windowType = (String) windowDropDown.getSelectedItem();
        String glassOption = (String) glassDropDown.getSelectedItem();

        if (windowType.equals("Sliding Window") && glassOption.equals("Sliding Window Option A")) {
            label = new Label("5000 Series Casement Window\n" + "OASW-CA-3,270(2)-3,Clear\n" +
                    "Report: T636-65 (Issued November 20, 2020)",
                    1.47, 0.24, 22, 0.44);
        }

        if (vancouver.isSelected()) {
            String uFactorText = uFactorField.getText();
            if (uFactorText.isEmpty()) {
                label.setUFactor(0);
            } else {
                label.setUFactor(Double.parseDouble(uFactorField.getText()));
            }
        }

        try {
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex != 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                try {
                    graphics.drawImage(label.generateImage(), 0, 0,
                            (int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight(),
                            null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
        } else if ("vancouver".equals(e.getActionCommand())) {
            if (vancouver.isSelected()) {
                uFactorLabel.setVisible(true);
                uFactorField.setVisible(true);
            } else {
                uFactorLabel.setVisible(false);
                uFactorField.setVisible(false);
            }
        } else {
            setVisible(false);
        }
    }
}
