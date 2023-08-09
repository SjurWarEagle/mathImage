package de.tkunkel.image.converter;

import de.tkunkel.image.types.ColorGroup;
import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.PixelProcessingData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

class ColorIndexerTest {

    @Test
    public void indexColors() {
        ColorIndexer colorIndexer = new ColorIndexer();
        ImageProcessingData data = generateTestData(10);

        List<ColorGroup> colorGroups = colorIndexer.indexColors(data);
        Assertions.assertNotNull(colorGroups);
        Assertions.assertEquals(3, colorGroups.size());
        Assertions.assertEquals(0, colorGroups.get(0).min);
        Assertions.assertEquals(32, colorGroups.get(0).max);
        Assertions.assertEquals(Color.WHITE, colorGroups.get(0).color);

        Assertions.assertEquals(33, colorGroups.get(1).min);
        Assertions.assertEquals(65, colorGroups.get(1).max);
        Assertions.assertEquals(Color.GREEN, colorGroups.get(1).color);

        Assertions.assertEquals(66, colorGroups.get(2).min);
        Assertions.assertEquals(98, colorGroups.get(2).max);
        Assertions.assertEquals(Color.BLUE, colorGroups.get(2).color);
    }

    private static ImageProcessingData generateTestData(int size) {
        ImageProcessingData data = new ImageProcessingData();

        data.pixels = new PixelProcessingData[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                data.pixels[x][y] = new PixelProcessingData(x, y);
                data.pixels[x][y].text = x + " / " + y;
            }
        }
        data.pixels[2][2].originalColor = Color.BLUE;
        data.pixels[2][2].targetColor = Color.BLUE;

        data.pixels[0][2].originalColor = Color.GREEN;
        data.pixels[0][2].targetColor = Color.GREEN;

        return data;
    }


}