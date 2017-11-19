package edu.sdsu.cs;

import com.pearsoneduc.ip.op.FFTException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static edu.sdsu.cs.ImageSupport.getGrayScale;
import static edu.sdsu.cs.ImageSupport.saveImage;

/**
 * @author Tom Paulus
 * Created on 11/18/17.
 */
@SuppressWarnings("FieldCanBeLocal")
public class LowPassTest {
    private final String[] TEST_IMAGES = {"Blonde1.jpg", "Blonde2.jpg", "Zebra.jpg"};
    private int TEST_ORDER = 3;
    private float TEST_RADIUS = 0.4f;
    private int TEST_MEAN_SIZE = 3;

    @Test
    public void butterworth() throws Exception {
        for (String testImage : TEST_IMAGES) {
            BufferedImage image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(testImage));
            applyButterworth(getGrayScale(image), testImage.split("\\.")[0].toUpperCase());
        }
    }

    @Test
    public void spatial() throws Exception {
        for (String testImage : TEST_IMAGES) {
            BufferedImage image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(testImage));
            applySpatialFilter(image, testImage.split("\\.")[0].toUpperCase());
        }
    }


    private void applyButterworth(final BufferedImage image,
                                  final String nameStem)
            throws FFTException, IOException {
        BufferedImage filteredImage = Butterworth.applyFilter(image, TEST_ORDER, TEST_RADIUS);
        BufferedImage spectrumImageBefore = Spectrum.draw(image, 0);
        BufferedImage spectrumImageAfter = Spectrum.draw(filteredImage, 0);

        saveImage(filteredImage, nameStem + "-butterworth");
        saveImage(spectrumImageBefore, nameStem + "-spectrum-before");
        saveImage(spectrumImageAfter, nameStem + "-spectrum-after");
    }

    private void applySpatialFilter(BufferedImage image, final String nameStem) throws IOException {
        BufferedImage filteredImage = Spatial.meanFilter(image, TEST_MEAN_SIZE);
        saveImage(filteredImage, nameStem + "-mean");
    }
}
