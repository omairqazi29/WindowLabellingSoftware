package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

// Represents a label with all values and an image of it
public class Label {
    public static final String NRCAN_TEMPLATE_URL =
            "https://raw.githubusercontent.com/omairqazi29/WindowLabellingSoftware/main/data/LabelTemplate4x6.jpg";
    public static final String OASIS_TEMPLATE_URL =
            "https://raw.githubusercontent.com/omairqazi29/WindowLabellingSoftware/main/data/OasisTemplate4x6.jpg";
    public  static final String NAFS_TEMPLATE_URL =
            "https://raw.githubusercontent.com/omairqazi29/WindowLabellingSoftware/main/data/nafsTemplate.jpg";
    public static final Font FONT_VALUES = new Font("Arial", Font.BOLD, 95);
    public static final Font FONT_DESCRIPTION = new Font("Arial", Font.PLAIN, 50);
    public static final Font FONT_PERFORMANCE = new Font("Arial", Font.PLAIN, 35);
    public static final String PERFORMANCE = "AAMA/WDMA/CSA 101/I.S.2/CSA A440-11 NAFS\n";

    private final String description;
    private final double uFactor;
    private final double shgc;
    private final int er;
    private final double vt;
    private final String performance;
    private final boolean nrCan;

    // EFFECTS: Constructs a label with the given details and assigns its image to it
    public Label(String desc, double uVal, double shgc, int er, double vt, String perf, boolean nrCan) {
        this.description = desc;
        this.uFactor = uVal;
        this.shgc = shgc;
        this.er = er;
        this.vt = vt;
        this.performance = PERFORMANCE + perf;
        this.nrCan = nrCan;
    }

    public String getDescription() {
        return description;
    }

    public double getUFactor() {
        return uFactor;
    }

    public double getShgc() {
        return shgc;
    }

    public int getEr() {
        return er;
    }

    public String getVt() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(vt);
    }

    public String getPerformance() {
        return performance;
    }

    public boolean isNrCan() {
        return nrCan;
    }

    // EFFECTS: generates an image of the label with its details and returns it;
    // throws IOException if error occurs while handling file operations
    public BufferedImage generateImage() throws IOException {
        BufferedImage template;
        if (isNrCan()) {
            template = ImageIO.read(new URL(NRCAN_TEMPLATE_URL));
        } else {
            template = ImageIO.read(new URL(OASIS_TEMPLATE_URL));
        }
        BufferedImage label = new BufferedImage(template.getWidth(), template.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = label.createGraphics();
        g.drawImage(template, null, 0, 0);

        g.setFont(FONT_VALUES);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(Math.round((getUFactor() / 5.678) * 100.0) / 100.0), 370, 1000);
        g.drawString(String.valueOf(getUFactor()), 80, 1000);
        g.drawString(String.valueOf(getShgc()), 765, 1000);
        g.drawString(String.valueOf(getEr()), 250, 1170);
        g.drawString(getVt(), 765, 1170);

        g.setFont(FONT_DESCRIPTION);
        int y = 1370;
        String[] lines = getDescription().split("\n");
        int totalHeight = lines.length * g.getFontMetrics().getHeight();
        int startY = y - (totalHeight / 2);
        for (String line : lines) {
            int lineWidth = g.getFontMetrics().stringWidth(line);
            int x = (400 + (400 - lineWidth) / 2);

            g.drawString(line, x, startY);
            startY += g.getFontMetrics().getHeight();
        }


//        g.setFont(FONT_PERFORMANCE);
//        int performanceY = 1830;
//        String[] performance = getPerformance().split("\n");
//        int totalPerformanceHeight = performance.length * g.getFontMetrics().getHeight();
//        int startPerformanceY = performanceY - (totalPerformanceHeight / 2);
//        for (String line : performance) {
//            int lineWidth = g.getFontMetrics().stringWidth(line);
//            int x = (280 + (400 - lineWidth) / 2);
//
//            g.drawString(line, x, startPerformanceY);
//            startPerformanceY += g.getFontMetrics().getHeight();
//        }

        g.dispose();
        return label;
    }

    public BufferedImage generatePerformanceLabel() throws IOException {
        // Assuming 300 DPI for a 4x2 inches label
        int dpi = 300;
        int width = 4 * dpi; // 4 inches wide
        int height = 2 * dpi; // 2 inches tall

        BufferedImage template = ImageIO.read(new URL(NAFS_TEMPLATE_URL));
        BufferedImage label = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = label.createGraphics();
        g.drawImage(template, 0, 0, width, height, null);

        g.setFont(FONT_PERFORMANCE);
        g.setColor(Color.BLACK);

        // Draw the performance text
        int performanceY = 90; // Y-coordinate for the first line of text
        String[] performance = getPerformance().split("\n"); // getPerformance() should return the performance text

        for (int i = 0; i < performance.length; i++) {
            String line = performance[i];
            int lineWidth = g.getFontMetrics().stringWidth(line);
            int x;

            if (i < 3) {
                x = (width - lineWidth) / 2;
            } else if (i == 3) {
                x = (width - lineWidth) / 2 + 150;
                performanceY += 100;
            } else {
                x = (width - lineWidth) / 2 + 150;
            }

            g.drawString(line, x, performanceY);
            performanceY += g.getFontMetrics().getHeight();
        }

        g.dispose();
        return label;
    }
}
