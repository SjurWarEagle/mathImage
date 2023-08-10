package de.tkunkel.image.converter;

import de.tkunkel.image.tasks.ITaskGenerator;
import de.tkunkel.image.types.ColorGroup;
import de.tkunkel.image.types.ImageProcessingData;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;
import java.util.List;

@Service
public class ColorIndexer {

    public List<ColorGroup> indexColors(ImageProcessingData data, ITaskGenerator generator) {
        List<ColorGroup> rc = new ArrayList<>();
        Set<Color> colors = new HashSet<>();

        for (int x = 0; x < data.pixels.length; x++) {
            for (int y = 0; y < data.pixels[x].length; y++) {
                Optional<Color> optionalMatch = getSimilarColor(colors, data.pixels[x][y].targetColor);
                if (optionalMatch.isPresent()) {
                    data.pixels[x][y].targetColor = optionalMatch.get();
                } else {
                    colors.add(data.pixels[x][y].targetColor);
                }
            }
        }

        int slice = (generator.getMaxValue() - generator.getMinValue()) / colors.size();
        int from = generator.getMinValue();
        int to = from + slice - 1;

        for (Color color : colors) {
            ColorGroup colorGroup = new ColorGroup();
            colorGroup.min = from;
            colorGroup.max = to;
            colorGroup.color = color;

            rc.add(colorGroup);

            from = to + 1;
            to = from + slice - 1;
        }

        return rc;
    }

    private Optional<Color> getSimilarColor(Set<Color> colors, Color targetColor) {
        int tolerance = 50;
        for (Color color : colors) {
            if (
                    (Math.abs(color.getRed() - targetColor.getRed()) < tolerance)
                            && (Math.abs(color.getGreen() - targetColor.getGreen()) < tolerance)
                            && (Math.abs(color.getBlue() - targetColor.getBlue()) < tolerance)
            ) {
                return Optional.of(color);
            }
        }
        return Optional.empty();
    }
}
