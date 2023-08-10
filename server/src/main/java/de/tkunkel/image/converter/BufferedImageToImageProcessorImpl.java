package de.tkunkel.image.converter;

import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.PixelProcessingData;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class BufferedImageToImageProcessorImpl implements IImageProcessor {


    public ImageProcessingData read(BufferedImage bufferedImage) throws IOException {

        ImageProcessingData rc = new ImageProcessingData();

        rc.pixels = new PixelProcessingData[bufferedImage.getWidth()][bufferedImage.getHeight()];

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int rgb = bufferedImage.getRGB(x, y);
                rc.pixels[x][y] = new PixelProcessingData(x, y);
                rc.pixels[x][y].originalColor = new Color(rgb);
                //todo do we need some mapping to avoid slice color fluctuation?
                rc.pixels[x][y].targetColor = new Color(rgb);
            }
        }

        return rc;
    }

    @Override
    public ImageProcessingData generateProcessingData() {
        ImageProcessingData rc = new ImageProcessingData();
        rc.pixels = new PixelProcessingData[10][10];

        return rc;
    }
}
