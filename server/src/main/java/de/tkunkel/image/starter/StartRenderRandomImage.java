package de.tkunkel.image.starter;

import de.tkunkel.image.converter.ColorIndexer;
import de.tkunkel.image.converter.RandomImageProcessorImpl;
import de.tkunkel.image.renderer.ImageRenderImpl;
import de.tkunkel.image.tasks.ITaskGenerator;
import de.tkunkel.image.tasks.SmallMultiplyTaskGeneratorImpl;
import de.tkunkel.image.types.ImageProcessingData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@SpringBootApplication
@ComponentScan(basePackages = {"de.tkunkel.image"})
public class StartRenderRandomImage {
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

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(StartRenderRandomImage.class, args);
        context.getBean(StartRenderRandomImage.class).startup();
    }

    public void startup() throws Exception {
        ImageProcessingData imageProcessingData = randomImageProcessor.generateProcessingData();
        generator=new SmallMultiplyTaskGeneratorImpl();

        imageProcessingData.colorGroups = colorIndexer.indexColors(imageProcessingData, generator);

        generator.generateForAll(imageProcessingData);

        BufferedImage image = imageRender.renderImage(imageProcessingData, generator, false);
        ImageIO.write(image, "PNG", new File("output/tmp.png"));

        image = imageRender.renderImage(imageProcessingData, generator, true);
        ImageIO.write(image, "PNG", new File("output/tmp_solved.png"));


        System.exit(0);
    }
}
