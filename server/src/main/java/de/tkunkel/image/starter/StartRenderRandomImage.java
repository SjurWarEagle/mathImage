package de.tkunkel.image.starter;

import de.tkunkel.image.converter.ColorIndexer;
import de.tkunkel.image.converter.RandomImageProcessorImpl;
import de.tkunkel.image.renderer.ImageRenderImpl;
import de.tkunkel.image.tasks.ITaskGenerator;
import de.tkunkel.image.tasks.SmallMultiplyTaskGeneratorImpl;
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
    private ITaskGenerator generator;
    private final ColorIndexer colorIndexer;

    public StartRenderRandomImage(RandomImageProcessorImpl randomImageProcessor,
                                  ImageRenderImpl imageRender,
                                  ColorIndexer colorIndexer) {
        this.randomImageProcessor = randomImageProcessor;
        this.imageRender = imageRender;
        this.colorIndexer = colorIndexer;
    }

    public static void main(String[] args) {
        SpringApplication.run(StartRenderRandomImage.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ImageProcessingData imageProcessingData = randomImageProcessor.generateProcessingData();
        generator=new SmallMultiplyTaskGeneratorImpl();

        imageProcessingData.colorGroups = colorIndexer.indexColors(imageProcessingData, generator);

        generator.generateForAll(imageProcessingData);

        imageRender.renderImage("output/tmp.png", "output/tmp_solved.png", imageProcessingData, generator);

        System.exit(0);
    }
}
