package edu.sdsu.cs;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Add Salt and Pepper Noise to an Image
 *
 * @author Tom Paulus
 * Created on 10/23/17.
 */
public class SaltAndPepper {
    /**
     *
     * @param image {@link BufferedImage} Source Image
     * @param p Percentage of Seasoning to apply to the image
     * @return {@link BufferedImage} Seasoned Image
     */
    public static BufferedImage season(BufferedImage image, float p) {
        if (p > 1 || p < 0) throw new RuntimeException("Seasoning can only be applied between 0 and 1");

        Random r = new Random();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (r.nextFloat() <= p) {
                    // This pixel will become noise
                    if (r.nextBoolean()) {
                        image.setRGB(x,y, Color.WHITE.getRGB());
                    } else {
                        image.setRGB(x,y, Color.BLACK.getRGB());
                    }
                }
            }
        }

        return image;
    }
}
