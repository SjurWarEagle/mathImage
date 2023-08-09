package de.tkunkel.image.tasks;

import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.TaskType;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Random;

@Service
public class SmallAdditionTaskGeneratorImpl implements ITaskGenerator {
    private static final Random rnd = new Random();

    @Override
    public TaskType getType() {
        return TaskType.SMALL_ADDITION;
    }

    @Override
    public String generate(int value) {
        int a = rnd.nextInt(4, value - 5);
        int b = value - a;
        return a + " + " + b;
    }

    public void generateForAll(ImageProcessingData data) {
        for (int x = 0; x < data.pixels.length; x++) {
            for (int y = 0; y < data.pixels[x].length; y++) {
                if (data.pixels[x][y].targetColor.equals(Color.WHITE)) {
                    data.pixels[x][y].targetMin = 1;
                    data.pixels[x][y].targetMax = 19;
                } else if (data.pixels[x][y].targetColor.equals(Color.BLUE)) {
                    data.pixels[x][y].targetMin = 20;
                    data.pixels[x][y].targetMax = 99;
                } else {
                    throw new RuntimeException("Unknown Color " + data.pixels[x][y].targetColor + " at x=" + x + ", y=" + y);
                }
            }
        }

        for (int x = 0; x < data.pixels.length; x++) {
            for (int y = 0; y < data.pixels[x].length; y++) {
                data.pixels[x][y].text = generate(10 + rnd.nextInt(90));
            }
        }
    }
}
