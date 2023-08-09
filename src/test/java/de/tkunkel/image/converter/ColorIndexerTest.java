package de.tkunkel.image.converter;

import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.PixelProcessingData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColorIndexerTest {

    @Test
    public void indexColors() {
        ColorIndexer colorIndexer = new ColorIndexer();
        ImageProcessingData data = generateTestData();

        List<ColorGroup> colorGroups = colorIndexer.indexColors(data);
        Assertions.assertNotNull(colorGroups);
        Assertions.assertEquals(2, colorGroups.size());
        Assertions.assertEquals(0, colorGroups.get(0).min);
        Assertions.assertEquals(49, colorGroups.get(0).max);
        Assertions.assertEquals(Color.WHITE, colorGroups.get(0).color);

        Assertions.assertEquals(50, colorGroups.get(1).min);
        Assertions.assertEquals(99, colorGroups.get(1).max);
        Assertions.assertEquals(Color.BLUE, colorGroups.get(1).color);
    }

    private static ImageProcessingData generateTestData() {
        ImageProcessingData data = new ImageProcessingData();

        data.pixels = new PixelProcessingData[10][10];

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                data.pixels[x][y] = new PixelProcessingData(x, y);
                data.pixels[x][y].text = x + " / " + y;
            }
        }
        data.pixels[2][2].originalColor = Color.BLUE;
        data.pixels[2][2].targetColor = Color.BLUE;

        return data;
    }

}