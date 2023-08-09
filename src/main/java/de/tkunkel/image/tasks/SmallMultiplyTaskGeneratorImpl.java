package de.tkunkel.image.tasks;

import de.tkunkel.image.types.ColorGroup;
import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.TaskType;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class SmallMultiplyTaskGeneratorImpl implements ITaskGenerator {
    private static final Random rnd = new Random();

    @Override
    public TaskType getType() {
        return TaskType.SMALL_MULTIPLY;
    }

    @Override
    public String generate(int min, int max) {
        int gap = 1;
        int a = 0;
        int b = 0;
        int loops = 0;

        do {
            a = rnd.nextInt(2, max - gap);
            b = rnd.nextInt(2, max - gap);
            if (loops++ > 100_000_000) {
                throw new RuntimeException("Too many loops");
            }
        } while (((a * b) < min) || ((a * b) > max) || a > 10 || b > 10);

        return a + " x " + b;
    }

    public void generateForAll(ImageProcessingData data) {
        // System.out.println(data.colorGroups);
        for (int x = 0; x < data.pixels.length; x++) {
            for (int y = 0; y < data.pixels[x].length; y++) {
                int tmpX = x;
                int tmpY = y;
                Optional<ColorGroup> optionalColorGroup = data.colorGroups.stream().filter(colorGroup -> colorGroup.color.equals(data.pixels[tmpX][tmpY].targetColor)).findFirst();
                if (optionalColorGroup.isEmpty()) {
                    throw new RuntimeException("No legend for color " + data.pixels[x][y].targetColor + " at x=" + x + ", y=" + y);
                }
                ColorGroup colorGroup = optionalColorGroup.get();
                data.pixels[x][y].targetMin = colorGroup.min;
                data.pixels[x][y].targetMax = colorGroup.max;
            }
        }

        for (int x = 0; x < data.pixels.length; x++) {
            for (int y = 0; y < data.pixels[x].length; y++) {
                data.pixels[x][y].text = generate(data.pixels[x][y].targetMin, data.pixels[x][y].targetMax);
            }
        }
    }

    @Override
    public int calc(int a, int b) {
        return a*b;
    }

    @Override
    public int getMaxValue() {
        return 90;
    }

    @Override
    public int getMinValue() {
        return 0;
    }

    @Override
    public String getLegendText() {
        return "1x1";
    }
}
