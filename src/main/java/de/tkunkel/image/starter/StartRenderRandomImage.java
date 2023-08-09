package de.tkunkel.image.starter;

import de.tkunkel.image.converter.RandomImageProcessorImpl;
import de.tkunkel.image.renderer.ImageRenderImpl;
import de.tkunkel.image.tasks.SmallAdditionTaskGeneratorImpl;
import de.tkunkel.image.types.ImageProcessingData;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"de.tkunkel.image"})
public class StartRenderRandomImage implements ApplicationRunner {
    private final RandomImageProcessorImpl randomImageProcessor;
    private final ImageRenderImpl imageRender;
    private final SmallAdditionTaskGeneratorImpl smallAdditionTaskGenerator;

    public StartRenderRandomImage(RandomImageProcessorImpl randomImageProcessor, ImageRenderImpl imageRender, SmallAdditionTaskGeneratorImpl smallAdditionTaskGenerator) {
        this.randomImageProcessor = randomImageProcessor;
        this.imageRender = imageRender;
        this.smallAdditionTaskGenerator = smallAdditionTaskGenerator;
    }

    public static void main(String[] args) {
        SpringApplication.run(StartRenderRandomImage.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ImageProcessingData imageProcessingData = randomImageProcessor.generateProcessingData();
        smallAdditionTaskGenerator.generateForAll(imageProcessingData);

        imageRender.renderImage("tmp.png", imageProcessingData);

        System.exit(0);
    }
}