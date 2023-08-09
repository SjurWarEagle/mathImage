package de.tkunkel.image.converter;

import de.tkunkel.image.types.ImageProcessingData;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColorIndexer {

    public List<ColorGroup> indexColors(ImageProcessingData data) {
        List<ColorGroup> rc = new ArrayList<>();
        Set<Color> colors = new HashSet<>();

        for (int x = 0; x < data.pixels.length; x++) {
            for (int y = 0; y < data.pixels[x].length; y++) {
                colors.add(data.pixels[x][y].targetColor);
            }
        }


        int slice = (data.maxValue - data.minValue) / colors.size();
        int from = data.minValue;
        int to = from + slice;
        int cnt = 1;
        for (Color color : colors) {
            ColorGroup colorGroup = new ColorGroup();
            colorGroup.min = from;
            colorGroup.max = to-1;
            colorGroup.color = color;

            rc.add(colorGroup);

            from = to;
            to = from + cnt++ * to;
        }

        return rc;
    }
}
