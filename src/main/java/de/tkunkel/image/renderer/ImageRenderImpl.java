package de.tkunkel.image.renderer;

import de.tkunkel.image.types.ImageProcessingData;
import de.tkunkel.image.types.PixelProcessingData;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextMeasurer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

@Service
public class ImageRenderImpl {
    int sizeOfCell = 50;
    Color borderColor = Color.DARK_GRAY;

    public void renderImage(String filename, ImageProcessingData data) throws IOException {
        int width = data.pixels.length;
        int height = data.pixels[0].length;
        BufferedImage image = new BufferedImage(width * sizeOfCell, height * sizeOfCell, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        for (int x = 0; x < data.pixels.length; x++) {
            for (int y = 0; y < data.pixels[x].length; y++) {
                PixelProcessingData pixelProcessingData = data.pixels[x][y];
                Color targetColor = pixelProcessingData.targetColor;
                String text = pixelProcessingData.text;

                Font font = new Font("TimesRoman", Font.PLAIN, 12);
                g.setFont(font);

                g.setColor(targetColor);
                g.fillRect(x * sizeOfCell, y * sizeOfCell, sizeOfCell, sizeOfCell);

                g.setColor(borderColor);
                g.drawRect(x * sizeOfCell, y * sizeOfCell, sizeOfCell, sizeOfCell);

                drawTextCentered(g, x, y, text, targetColor);
            }
        }
        //g.drawString(message, (width - stringWidth) / 2, height / 2 + stringHeight / 4);

        ImageIO.write(image, "PNG", new File(filename));
    }

    private void drawTextCentered(Graphics2D g, int x, int y, String text, Color targetColor) {

        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int posX = x * sizeOfCell + sizeOfCell / 2 - textWidth/2;
        int posY = y * sizeOfCell + sizeOfCell / 2 + fontMetrics.getAscent() / 2;
        g.setColor(complementaryColor(targetColor));
        g.drawString(text, posX, posY);
    }

    Color complementaryColor(final Color bgColor) {

        Color complement = new Color(255 - bgColor.getRed(),
                255 - bgColor.getGreen(),
                255 - bgColor.getBlue());

        return complement;
    }
}
