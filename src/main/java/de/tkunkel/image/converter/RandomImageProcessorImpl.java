package de.tkunkel.image.converter;

import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.PixelProcessingData;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class RandomImageProcessorImpl implements IImageProcessor {
    @Override
    public ImageProcessingData generateProcessingData() {
        ImageProcessingData rc = new ImageProcessingData();
        rc.pixels = new PixelProcessingData[10][10];

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                rc.pixels[x][y] = new PixelProcessingData(x, y);
                rc.pixels[x][y].text = x + " / " + y;
            }
        }
        rc.pixels[2][2].originalColor = Color.BLUE;
        rc.pixels[2][2].targetColor = Color.BLUE;

        rc.pixels[7][7].originalColor = Color.BLUE;
        rc.pixels[7][7].targetColor = Color.BLUE;

        rc.pixels[7][2].originalColor = Color.BLUE;
        rc.pixels[7][2].targetColor = Color.BLUE;

        rc.pixels[2][7].originalColor = Color.BLUE;
        rc.pixels[2][7].targetColor = Color.BLUE;

        return rc;
    }
}
