package edu.sdsu;

import org.junit.Test;

import javax.imageio.ImageIO;

import static org.junit.Assert.*;

/**
 * @author Tom Paulus
 * Created on 9/30/17.
 */
public class SpeedTest {
    private static final String TEST_IMAGE_1 = "car1.png";
    private static final String TEST_IMAGE_2 = "car2.png";
    private static final double TEST_IMAGE_TIME = 2500; // Time between tow images in ms
    private static final double TEST_IMAGE_SCALE = 17.0/110;  // Feet per Pixel

    @Test
    public void calculateSpeed() throws Exception {
        Speed s = new Speed(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_1)),
                ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_2)),
                TEST_IMAGE_TIME);

        double pps = s.calculateSpeed();  // Pixels Per Second
        assertTrue(pps > 0);

        double fps = pps * TEST_IMAGE_SCALE; // Feet Per Second

        System.out.println(String.format("The car was traveling %.3f fps", fps));
        System.out.println(String.format("That's %.0f mph!", fps * 0.681818));
    }
}