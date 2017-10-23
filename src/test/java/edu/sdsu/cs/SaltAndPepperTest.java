package edu.sdsu.cs;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static edu.sdsu.cs.EdgeDetectTest.makeGrayScale;

/**
 * @author Tom Paulus
 * Created on 10/23/17.
 */
public class SaltAndPepperTest {
    private static final String EXAMPLE_FILE_NAME = "Peppers0.jpg";
    private static final String ALTERNATE_FILE_NAME = "Peppers1.jpg";

    private static final int EDGE_THRESHOLD = 25;
    private static final float SEASONING = .20f;

    @Test
    public void Control() throws Exception {
        BufferedImage result = SaltAndPepper.season(
                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                SEASONING);

        File outFile = File.createTempFile("seasoned", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeWithNoise() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();

        BufferedImage result = edgeDetect.detectEdge(
                SaltAndPepper.season(
                        makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                        SEASONING),
                EDGE_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_seasoned", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeWithFilter() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();

        BufferedImage result = edgeDetect.detectEdge(
                Filter.medianFilter(
                        SaltAndPepper.season(
                                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                                SEASONING)
                ),
                EDGE_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_filter", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void ControlAlt() throws Exception {
        BufferedImage result = SaltAndPepper.season(
                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(ALTERNATE_FILE_NAME))),
                SEASONING);

        File outFile = File.createTempFile("seasoned-2", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeWithNoiseAlt() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();

        BufferedImage result = edgeDetect.detectEdge(
                SaltAndPepper.season(
                        makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(ALTERNATE_FILE_NAME))),
                        SEASONING),
                EDGE_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_seasoned-2", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeWithFilterAlt() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();

        BufferedImage result = edgeDetect.detectEdge(
                Filter.medianFilter(
                        SaltAndPepper.season(
                                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(ALTERNATE_FILE_NAME))),
                                SEASONING)
                ),
                EDGE_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_filter-2", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }
}