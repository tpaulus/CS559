package edu.sdsu.cs;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Tom Paulus
 * Created on 9/16/17.
 */
public class EnlargingTest {
    private static final String TEST_IMAGE_NAME = "City.jpg";

    @Test
    public void scale2() throws Exception {
        BufferedImage testImage = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_NAME));
        File outFile = File.createTempFile("Scale_2x-", ".png");

        Enlarging e = new Enlarging(testImage);
        BufferedImage scaledImage = e.scale(2);
        ImageIO.write(scaledImage, "PNG", outFile);

        System.out.println("outFile = " + outFile.getAbsolutePath());
        Desktop.getDesktop().open(outFile);

    }

    @Test
    public void scale3() throws Exception {
        BufferedImage testImage = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_NAME));
        File outFile = File.createTempFile("Scale_3x-", ".png");

        Enlarging e = new Enlarging(testImage);
        BufferedImage scaledImage = e.scale(3);
        ImageIO.write(scaledImage, "PNG", outFile);

        System.out.println("outFile = " + outFile.getAbsolutePath());
        Desktop.getDesktop().open(outFile);
    }
}