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
public class EdgeDetectTest {
    private static final String EXAMPLE_FILE_NAME = "Sorry.jpg";
    private static final int EDGE_DETECT_LOW_THRESHOLD = 10;
    private static final int EDGE_DETECT_MED_THRESHOLD = 25;
    private static final int EDGE_DETECT_HIGH_THRESHOLD = 50;

    private static BufferedImage makeGrayScale(final BufferedImage source) {
        BufferedImage image = new BufferedImage(source.getWidth(), source.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = image.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();

        return image;
    }

    @Test
    public void detectEdgeLow() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();

        BufferedImage result = edgeDetect.detectEdge(
                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                EDGE_DETECT_LOW_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_low", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeMed() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();
        BufferedImage result = edgeDetect.detectEdge(
                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                EDGE_DETECT_MED_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_med", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void detectEdgeHigh() throws Exception {
        EdgeDetect edgeDetect = new EdgeDetect();
        BufferedImage result = edgeDetect.detectEdge(
                makeGrayScale(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME))),
                EDGE_DETECT_HIGH_THRESHOLD);

        File outFile = File.createTempFile("edge_detect_high", ".jpg");
        ImageIO.write(result, "JPEG", outFile);
        Desktop.getDesktop().open(outFile);
    }
}