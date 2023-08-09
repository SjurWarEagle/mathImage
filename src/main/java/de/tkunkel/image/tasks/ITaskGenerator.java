package de.tkunkel.image.tasks;

public interface ITaskGenerator {
    TaskType getType();
    String generate(int value);
}
