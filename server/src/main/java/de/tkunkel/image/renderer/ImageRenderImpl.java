package de.tkunkel.image.renderer;

import de.tkunkel.image.tasks.ITaskGenerator;
import de.tkunkel.image.types.ColorGroup;
import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.PixelProcessingData;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ImageRenderImpl {
    final int sizeOfCell = 50;
    Color borderColor = Color.DARK_GRAY;

    public BufferedImage renderImage(ImageProcessingData data, ITaskGenerator generator, boolean withSolution) throws IOException {
        int width = data.pixels.length;
        int height = data.pixels[0].length;

        return renderImage(withSolution, data, width, height, generator);
    }

    private BufferedImage renderImage(boolean withSolution, ImageProcessingData data, int width, int height, ITaskGenerator generator) throws IOException {
        BufferedImage image = new BufferedImage(width * sizeOfCell, height * sizeOfCell, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        for (int x = 0; x < data.pixels.length; x++) {
            for (int y = 0; y < data.pixels[x].length; y++) {
                PixelProcessingData pixelProcessingData = data.pixels[x][y];
                Color targetColor = pixelProcessingData.targetColor;
                String text = pixelProcessingData.text;

                Font font = new Font("TimesRoman", Font.PLAIN, 12);
                g.setFont(font);

                if (withSolution) {
                    g.setColor(targetColor);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x * sizeOfCell, y * sizeOfCell, sizeOfCell, sizeOfCell);

                g.setColor(borderColor);
                g.drawRect(x * sizeOfCell, y * sizeOfCell, sizeOfCell, sizeOfCell);

                drawTextCentered(g, x, y, text, targetColor, withSolution, data.pixels[x][y].targetMin, data.pixels[x][y].targetMax, generator);
            }
        }

        image = addLegend(image, data.colorGroups);
        addTypeOfCalc(image, generator);

        return image;
    }

    private BufferedImage addTypeOfCalc(BufferedImage imageWithLegend, ITaskGenerator generator) {
        int gap = 20;
        Graphics2D g = imageWithLegend.createGraphics();
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(generator.getLegendText());

        int x = imageWithLegend.getWidth() - gap / 2 - textWidth;
        int y = imageWithLegend.getHeight() - gap / 2;
        g.setColor(Color.DARK_GRAY);
        g.drawString(generator.getLegendText(), x, y);

        return imageWithLegend;
    }

    private BufferedImage addLegend(BufferedImage image, List<ColorGroup> colorGroups) {
        int heightOfLegend = colorGroups.size() * 50;
        int gap = 20;
        int legendColorSize = 20;

        BufferedImage imageWithLegend = new BufferedImage(image.getWidth(), image.getHeight() + heightOfLegend, image.getType());
        Graphics2D g = imageWithLegend.createGraphics();
        FontMetrics fontMetrics = g.getFontMetrics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, imageWithLegend.getWidth(), imageWithLegend.getHeight());

        g.drawImage(image, null, 0, 0);

        int cnt = 0;
        for (ColorGroup colorGroup : colorGroups) {
            int x = gap;
            int y = image.getHeight() + gap + cnt * gap + cnt * legendColorSize + fontMetrics.getAscent();

            g.setColor(Color.DARK_GRAY);
            g.drawString(colorGroup.min + "-" + colorGroup.max, x, y);

            x += legendColorSize + 5 + gap;
            y -= gap-fontMetrics.getAscent()/2;
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, legendColorSize, legendColorSize);
            g.setColor(colorGroup.color);
            g.fillRect(x, y, legendColorSize, legendColorSize);

            cnt++;
        }

        return imageWithLegend;
    }

    private void drawTextCentered(Graphics2D g, int x, int y, String text, Color targetColor, boolean withSolution, int min, int max, ITaskGenerator generator) {

        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int posX = x * sizeOfCell + sizeOfCell / 2 - textWidth / 2;
        int posY = y * sizeOfCell + sizeOfCell / 2 + fontMetrics.getAscent() / 2;
        if (withSolution) {
            g.setColor(complementaryColor(targetColor));
        } else {
            g.setColor(borderColor);
        }
        // task
        g.drawString(text, posX, posY);

        if (withSolution) {
            // range
            g.drawString("[" + min + ":" + max + "]", x * sizeOfCell + 10, (y + 1) * sizeOfCell - 5);

            // solution
            int result = calc(text, generator);
            g.drawString("" + result, x * sizeOfCell + 20, y * sizeOfCell + 15);
        }
    }

    private int calc(String text, ITaskGenerator generator) {
        String[] split = text.split(" ");
        return generator.calc(Integer.parseInt(split[0]), Integer.parseInt(split[2]));
    }

    Color complementaryColor(final Color bgColor) {

        return new Color(255 - bgColor.getRed(),
                255 - bgColor.getGreen(),
                255 - bgColor.getBlue());
    }
}
