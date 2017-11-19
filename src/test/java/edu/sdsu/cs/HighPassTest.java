package edu.sdsu.cs;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static edu.sdsu.cs.ImageSupport.getGrayScale;
import static edu.sdsu.cs.ImageSupport.saveImage;

/**
 * @author Tom Paulus
 * Created on 11/19/17.
 */
public class HighPassTest {
    private static final String TEST_IMAGE = "Flowers.jpg";
    private static final float[] TEST_CUTOFFS = {.1f, .25f, .35f, .5f, .75f};
    private static final String[] TEST_CUTOFF_NAMES = {"VLOW", "LOW", "MED", "HIGH", "XHIGH"};
    private static final int TEST_BIAS = 0;

    @Test
    public void applyFilter() throws Exception {
        BufferedImage image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(TEST_IMAGE));

        for (int i = 0; i < Math.min(TEST_CUTOFFS.length, TEST_CUTOFF_NAMES.length); i++) {
            BufferedImage filteredImage = HighPass.applyFilter(getGrayScale(image), TEST_CUTOFFS[i], TEST_BIAS);
            saveImage(filteredImage, "FLOWERS-gaussian-" + TEST_CUTOFF_NAMES[i]);
        }
    }
}