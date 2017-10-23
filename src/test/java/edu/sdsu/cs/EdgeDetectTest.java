package edu.sdsu.cs;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Tom Paulus
 * Created on 10/21/17.
 */
@SuppressWarnings("Duplicates")
public class EdgeDetectTest {
    private static final String EXAMPLE_FILE_NAME = "Peppers0.jpg";
    private static final String ALTERNATE_FILE_NAME = "Peppers1.jpg";

    private static final int EDGE_DETECT_LOW_THRESHOLD = 10;
    private static final int EDGE_DETECT_MED_THRESHOLD = 25;
    private static final int EDGE_DETECT_HIGH_THRESHOLD = 50;

    static BufferedImage makeGrayScale(final BufferedImage source) {
        BufferedImage image = new BufferedImage(source.getWidth(), source.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = image.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();

        return image;
    }

    private static BufferedImage averageImages(final BufferedImage[] images) {
        int width = images[0].getWidth();
        int height = images[0].getHeight();

        for (BufferedImage image : images)
            if (image.getHeight() != height || image.getWidth() != width)
                throw new RuntimeException("Image Dimension Mismatch!");

        BufferedImage result = new BufferedImage(width, height, images[0].getType());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int sum = 0;

                for (BufferedImage image : images)
                    sum += image.getRGB(x, y);

                result.setRGB(x, y, sum / images.length);
            }
        }

        return result;
    }

    @Test
    public void detectEdgeLowGrayScale() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();

        BufferedImage result = edgeDetect.detectEdge(
                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                EDGE_DETECT_LOW_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_low", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeMedGrayScale() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();
        BufferedImage result = edgeDetect.detectEdge(
                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                EDGE_DETECT_MED_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_med", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeMedGrayScaleAlt() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();
        BufferedImage result = edgeDetect.detectEdge(
                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(ALTERNATE_FILE_NAME))),
                EDGE_DETECT_MED_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_med-2", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeHighGrayScale() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();
        BufferedImage result = edgeDetect.detectEdge(
                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                EDGE_DETECT_HIGH_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_high", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeColors() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();

        BufferedImage[] edgeChannels = {
                edgeDetect.detectEdge(
                        makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                        EDGE_DETECT_MED_THRESHOLD,
                        0),
                edgeDetect.detectEdge(
                        makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                        EDGE_DETECT_MED_THRESHOLD,
                        1),
                edgeDetect.detectEdge(
                        makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                        EDGE_DETECT_MED_THRESHOLD,
                        2)
        };

        BufferedImage result = averageImages(edgeChannels);
        File outFile = File.createTempFile("edge_detect_color", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeColorsAlt() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();

        BufferedImage[] edgeChannels = {
                edgeDetect.detectEdge(
                        makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(ALTERNATE_FILE_NAME))),
                        EDGE_DETECT_HIGH_THRESHOLD,
                        0),
                edgeDetect.detectEdge(
                        makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(ALTERNATE_FILE_NAME))),
                        EDGE_DETECT_HIGH_THRESHOLD,
                        1),
                edgeDetect.detectEdge(
                        makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(ALTERNATE_FILE_NAME))),
                        EDGE_DETECT_HIGH_THRESHOLD,
                        2)
        };

        BufferedImage result = averageImages(edgeChannels);
        File outFile = File.createTempFile("edge_detect_color-2", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }
}