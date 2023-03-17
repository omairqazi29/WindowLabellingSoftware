package main.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Represents a label with all values and an image of it
public class Label {
    public static final String PATH_TEMPLATE = "./data/LabelTemplate.jpg";

    public static final Font FONT_VALUES = new Font("Arial", Font.BOLD, 70);
    public static final Font FONT_MANUFACTURER = new Font("Arial", Font.BOLD, 50);
    public static final Font FONT_DESCRIPTION = new Font("Arial", Font.PLAIN, 40);

    private final String manufacturer;
    private final String description;
    private final double uFactor;
    private final double shgc;
    private final double er;
    private final double vt;
    private final BufferedImage image;

    // EFFECTS: Constructs a label with the given details and assigns its image to it
    public Label(String manufacturer, String description, double uFactor, double shgc, double er, double vt)
            throws IOException {
        this.manufacturer = manufacturer;
        this.description = description;
        this.uFactor = uFactor;
        this.shgc = shgc;
        this.er = er;
        this.vt = vt;
        image = generateImage();
    }

    public String getManufacturer() {
        return manufacturer;
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

    public BufferedImage getImage() {
        return image;
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
        g.drawString(String.valueOf(getUFactor()), 220, 610);
        g.drawString(String.valueOf(getShgc()), 770, 610);
        g.drawString(String.valueOf(getEr()), 220, 770);
        g.drawString(String.valueOf(getVt()), 770, 770);

        g.setFont(FONT_MANUFACTURER);
        g.drawString(getManufacturer(), 230, 860);

        g.setFont(FONT_DESCRIPTION);
        int y = 930;
        String[] lines = getDescription().split("\n");
        g.drawString(lines[0], 280, y);
        g.drawString(lines[1], 290, y += g.getFontMetrics().getHeight());
        g.drawString(lines[2], 200, y += g.getFontMetrics().getHeight());

        g.dispose();
        return label;
    }
}
