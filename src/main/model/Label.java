package main.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Represents a label with all values and an image of it
public class Label {
    public static final String PATH_TEMPLATE = "./data/LabelTemplate.jpeg";
    public static final Font FONT_VALUES = new Font("Arial", Font.BOLD, 70);
    public static final Font FONT_DESCRIPTION = new Font("Arial", Font.PLAIN, 40);

    private final String description;
    private double uFactor;
    private final double shgc;
    private final double er;
    private final double vt;

    // EFFECTS: Constructs a label with the given details and assigns its image to it
    public Label(String description, double uFactor, double shgc, double er, double vt) {
        this.description = description;
        this.uFactor = uFactor;
        this.shgc = shgc;
        this.er = er;
        this.vt = vt;
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

    public double getEr() {
        return er;
    }

    public double getVt() {
        return vt;
    }

    public void setUFactor(double value) {
        uFactor = value;
    }

    // EFFECTS: generates an image of the label with its details and returns it;
    // throws IOException if error occurs while handling file operations
    public BufferedImage generateImage() throws IOException {
        BufferedImage template = ImageIO.read(new File(PATH_TEMPLATE));

        BufferedImage label = new BufferedImage(template.getWidth(), template.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = label.createGraphics();
        g.drawImage(template, null, 0, 0);

        g.setFont(FONT_VALUES);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(getUFactor()), 190, 870);
        g.drawString(String.valueOf(getShgc()), 650, 870);
        g.drawString(String.valueOf(getEr()), 190, 1020);
        g.drawString(String.valueOf(getVt()), 650, 1020);

        g.setFont(FONT_DESCRIPTION);
        int y = 1170;
        String[] lines = getDescription().split("\n");
        int totalHeight = lines.length * g.getFontMetrics().getHeight();
        int startY = y - (totalHeight / 2);
        for (String line : lines) {
            int lineWidth = g.getFontMetrics().stringWidth(line);
            int x = (280 + (400 - lineWidth) / 2);

            g.drawString(line, x, startY);
            startY += g.getFontMetrics().getHeight();
        }

        g.dispose();
        return label;
    }
}
