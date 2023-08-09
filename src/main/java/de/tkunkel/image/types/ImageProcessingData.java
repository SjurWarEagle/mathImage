package de.tkunkel.image.types;

import java.util.List;

public class ImageProcessingData {
    public List<ColorGroup> colorGroups;
    public PixelProcessingData[][] pixels;
    public int minValue = 0;
    public int maxValue = 100;
}
