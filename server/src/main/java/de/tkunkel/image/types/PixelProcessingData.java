package de.tkunkel.image.types;

import java.awt.*;

public class PixelProcessingData {
    public int x;
    public int y;
    public String text;
    public int targetMin;
    public int targetMax;

    public Color originalColor;
    public Color targetColor;

    public PixelProcessingData(int x, int y) {
        this.x = x;
        this.y = y;

        this.originalColor = Color.WHITE;
        this.targetColor = Color.WHITE;

    }
}
