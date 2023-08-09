package de.tkunkel.image.types;

import java.awt.*;

public class ColorGroup {
    public int min;
    public int max;
    public Color color;

    @Override
    public String toString() {
        return "ColorGroup{" +
                "min=" + min +
                ", max=" + max +
                ", color=" + color +
                '}';
    }
}
