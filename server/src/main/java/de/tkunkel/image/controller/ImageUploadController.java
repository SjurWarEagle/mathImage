package de.tkunkel.image.controller;

import de.tkunkel.image.converter.BufferedImageToImageProcessorImpl;
import de.tkunkel.image.converter.ColorIndexer;
import de.tkunkel.image.renderer.ImageRenderImpl;
import de.tkunkel.image.tasks.ITaskGenerator;
import de.tkunkel.image.tasks.SmallAdditionTaskGeneratorImpl;
import de.tkunkel.image.tasks.SmallMultiplyTaskGeneratorImpl;
import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.TaskType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@RestController()
public class ImageUploadController {

    private final ImageRenderImpl imageRender;
    private ITaskGenerator generator;
    private final ColorIndexer colorIndexer;

    public ImageUploadController(ImageRenderImpl imageRender,
                                 ColorIndexer colorIndexer) {
        this.imageRender = imageRender;
        this.colorIndexer = colorIndexer;
    }


    @PostMapping(
            value = "/api/image-upload",
            produces = "application/json"
    )
    public GeneratedImagesResponse uploadImage(@RequestParam MultipartFile sourceImage, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (sourceImage.getBytes().length > 100_000) {
                throw new RuntimeException("Your image is too big");
            }
            String selectedMath = String.valueOf(request.getParameter("selectedMath"));
            TaskType taskType = TaskType.valueOf(selectedMath);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(sourceImage.getBytes()));
            GeneratedImagesResponse responseData = processImage(bufferedImage, taskType);
            return responseData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GeneratedImagesResponse processImage(BufferedImage bufferedImage, TaskType taskType) throws IOException {
        GeneratedImagesResponse rc = new GeneratedImagesResponse();

        BufferedImageToImageProcessorImpl processor = new BufferedImageToImageProcessorImpl();
        ImageProcessingData imageProcessingData = processor.read(bufferedImage);
        ITaskGenerator generator = new SmallMultiplyTaskGeneratorImpl();

        generator = determinMathByType(taskType, generator);

        imageProcessingData.colorGroups = colorIndexer.indexColors(imageProcessingData, generator);

        generator.generateForAll(imageProcessingData);

        BufferedImage image = imageRender.renderImage(imageProcessingData, generator, false);
        BufferedImage imageWithSolution = imageRender.renderImage(imageProcessingData, generator, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", bos);

        byte[] bytesOfImage = bos.toByteArray();

        //reset
        bos = new ByteArrayOutputStream();
        ImageIO.write(imageWithSolution, "PNG", bos);
        byte[] bytesOfImageWithSolution = bos.toByteArray();

        bos.close();

        rc.image = bytesOfImage;
        rc.imageWithSolution = bytesOfImageWithSolution;

        return rc;
    }

    private static ITaskGenerator determinMathByType(TaskType taskType, ITaskGenerator generator) {
        if (Objects.isNull(taskType)){
            return new SmallAdditionTaskGeneratorImpl();
        }
        switch (taskType) {
            case SMALL_ADDITION -> {
                generator = new SmallAdditionTaskGeneratorImpl();
            }
            case SMALL_MULTIPLY -> {
                generator = new SmallMultiplyTaskGeneratorImpl();
            }
            case BIG_ADDITION, BIG_MULTIPLY -> {
                throw new UnsupportedOperationException("not implemented");
            }
        }
        return generator;
    }
}
