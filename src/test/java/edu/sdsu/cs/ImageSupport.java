package edu.sdsu.cs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Tom Paulus
 * Created on 11/19/17.
 */
class ImageSupport {
    static void saveImage(final BufferedImage image, final String fileName) throws IOException {
        File outFile = File.createTempFile(fileName + "-", ".jpg");
        ImageIO.write(image, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    static BufferedImage getGrayScale(BufferedImage inputImage) {
        BufferedImage img = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = img.getGraphics();
        g.drawImage(inputImage, 0, 0, null);
        g.dispose();
        return img;
    }
}
