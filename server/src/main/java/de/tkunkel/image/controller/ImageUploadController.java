package de.tkunkel.image.controller;

import de.tkunkel.image.converter.BufferedImageToImageProcessorImpl;
import de.tkunkel.image.converter.ColorIndexer;
import de.tkunkel.image.renderer.ImageRenderImpl;
import de.tkunkel.image.tasks.ITaskGenerator;
import de.tkunkel.image.tasks.SmallMultiplyTaskGeneratorImpl;
import de.tkunkel.image.types.ImageProcessingData;
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
            if (sourceImage.getBytes().length>100_000){
                throw new RuntimeException("Your image is too big");
            }
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(sourceImage.getBytes()));
            GeneratedImagesResponse responseData = processImage(bufferedImage);
            return responseData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GeneratedImagesResponse processImage(BufferedImage bufferedImage) throws IOException {
        GeneratedImagesResponse rc = new GeneratedImagesResponse();

        BufferedImageToImageProcessorImpl processor = new BufferedImageToImageProcessorImpl();
        ImageProcessingData imageProcessingData = processor.read(bufferedImage);
        ITaskGenerator generator = new SmallMultiplyTaskGeneratorImpl();

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
}
