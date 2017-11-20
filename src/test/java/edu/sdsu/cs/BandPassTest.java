package edu.sdsu.cs;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static edu.sdsu.cs.ImageSupport.getGrayScale;
import static edu.sdsu.cs.ImageSupport.saveImage;
import static org.junit.Assert.*;

/**
 * @author Tom Paulus
 * Created on 11/19/17.
 */
public class BandPassTest {
    private static final String TEST_IMAGE = "Blonde2.jpg";
    private static final double TEST_BANDWIDTH = 1.75;
    private static final double TEST_RADIUS = .25;
    private static final int TEST_BIAS = 50;

    @Test
    public void applyFilter() throws Exception {
        BufferedImage image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(TEST_IMAGE));
        BufferedImage filteredImage = BandPass.applyFilter(getGrayScale(image), TEST_BANDWIDTH, TEST_RADIUS, TEST_BIAS);
        saveImage(filteredImage, "BLONDE2-GBPF");
    }
}