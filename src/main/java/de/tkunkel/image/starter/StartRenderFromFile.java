package de.tkunkel.image.starter;

import de.tkunkel.image.converter.ColorIndexer;
import de.tkunkel.image.converter.FileImageProcessorImpl;
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
public class StartRenderFromFile implements ApplicationRunner {
    private final ImageRenderImpl imageRender;
    private final SmallAdditionTaskGeneratorImpl smallAdditionTaskGenerator;
    private final ColorIndexer colorIndexer;

    public StartRenderFromFile(ImageRenderImpl imageRender,
                               SmallAdditionTaskGeneratorImpl smallAdditionTaskGenerator,
                               ColorIndexer colorIndexer) {
        this.imageRender = imageRender;
        this.smallAdditionTaskGenerator = smallAdditionTaskGenerator;
        this.colorIndexer = colorIndexer;
    }

    public static void main(String[] args) {
        SpringApplication.run(StartRenderFromFile.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        FileImageProcessorImpl fileImageProcessor = new FileImageProcessorImpl();
        ImageProcessingData imageProcessingData = fileImageProcessor.readFile("D:\\IdeaProjects\\mathImage\\src\\main\\resources\\images\\mc_diamont_sword.png");
//        ImageProcessingData imageProcessingData = fileImageProcessor.readFile("D:\\IdeaProjects\\mathImage\\src\\main\\resources\\images\\mc_diamont_pick.png");
        imageProcessingData.colorGroups = colorIndexer.indexColors(imageProcessingData);

        smallAdditionTaskGenerator.generateForAll(imageProcessingData);

        imageRender.renderImage("output/tmp.png", "output/tmp_solved.png", imageProcessingData);

        System.exit(0);
    }
}
