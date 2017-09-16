package edu.sdsu.cs;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Tom Paulus
 * Created on 9/15/17.
 */
@SuppressWarnings("Duplicates")
public class QuantizationTest {
    private static final String TEST_IMAGE_NAME = "Peak.jpg";

    @Test
    public void quantiseImageLow() throws Exception {
        final int NUM_BITS = 2;

        BufferedImage testImage = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_NAME));
        File outFile = File.createTempFile("Quantize_Low-", ".jpg");
        quantizeTest(testImage, NUM_BITS, outFile);

        System.out.println("outFile = " + outFile.getAbsolutePath());
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void quantiseImageMed() throws Exception {
        final int NUM_BITS = 4;

        BufferedImage testImage = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_NAME));
        File outFile = File.createTempFile("Quantize_Med-", ".jpg");
        quantizeTest(testImage, NUM_BITS, outFile);

        System.out.println("outFile = " + outFile.getAbsolutePath());
        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void quantiseImageHigh() throws Exception {
        final int NUM_BITS = 7;

        BufferedImage testImage = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_NAME));
        File outFile = File.createTempFile("Quantize_High-", ".jpg");
        quantizeTest(testImage, NUM_BITS, outFile);

        System.out.println("outFile = " + outFile.getAbsolutePath());
        Desktop.getDesktop().open(outFile);
    }

    private void quantizeTest(final BufferedImage image, final int numBits, final File outFile) throws IOException {
        Quantization q = new Quantization(image);
        BufferedImage quantized = q.quantiseImage(numBits);
        ImageIO.write(quantized, "JPG", outFile);
    }

}