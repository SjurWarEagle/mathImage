package de.tkunkel.image.tasks;

import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.TaskType;

public interface ITaskGenerator {
    TaskType getType();
    String generate(int min, int max);

    void generateForAll(ImageProcessingData data);

    int calc(int a, int b);

    int getMaxValue();

    int getMinValue();

    String getLegendText();
}
