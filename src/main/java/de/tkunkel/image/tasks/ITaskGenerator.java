package de.tkunkel.image.tasks;

import de.tkunkel.image.types.TaskType;

public interface ITaskGenerator {
    TaskType getType();
    String generate(int min, int max);
}
