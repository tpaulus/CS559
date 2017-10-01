package edu.sdsu;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Tom Paulus
 * Created on 9/30/17.
 */
public class Speed {
    private BufferedImage startImg;
    private BufferedImage endImg;
    private double duration;

    public Speed(BufferedImage startImg, BufferedImage endImg, double duration) {
        this.startImg = startImg;
        this.endImg = endImg;
        this.duration = duration;

        if ((startImg.getWidth() != endImg.getWidth()) || (startImg.getHeight() != endImg.getHeight()))
            throw new RuntimeException("Image dimension mismatch!");

        if (duration <= 0)
            throw new RuntimeException("Teleportation or Time Bending is not allowed!");
    }

    public double calculateSpeed() {
        // AND The Images
        BufferedImage diff = difference(startImg, endImg);

        // Add up all the columns to make an int[]
        int[] cols = scan(diff);

        // Find the 2 humps via the max values
        int left = search(cols);
        int right = rsearch(cols);

        return right - left;
    }

    private BufferedImage difference(final BufferedImage image1, final BufferedImage image2) {
        BufferedImage output = new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());

        for (int x = 0; x < image1.getWidth(); x++) {
            for (int y = 0; y < image1.getHeight(); y++) {
                int p1 = image1.getRGB(x, y);
                int p2 = image2.getRGB(x, y);

                output.setRGB(x, y, pixelDifference(p1, p2).getRGB());
            }
        }

        return output;
    }

    private Color pixelDifference(int p1, int p2) {
        long difference = 0;

        Color c1 = new Color(p1);
        Color c2 = new Color(p2);

        difference += Math.abs(c1.getRed() - c2.getRed());
        difference += Math.abs(c1.getGreen() - c2.getGreen());
        difference += Math.abs(c1.getBlue() - c2.getBlue());

        final int r = (int) (difference / 3);
        try {
            return new Color(r, r, r);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return new Color(0);
    }

    private int[] scan(final BufferedImage image) {
        int[] output = new int[image.getWidth()];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                output[x] += new Color(image.getRGB(x, y)).getRed();
            }
        }

        return output;
    }


    private int search(int[] vals) {
        int max = 0;
        int pos = 0;

        for (int x = 0; x < vals.length / 2; x++) {
            int val = vals[x];
            if (val > max) {
                max = val;
                pos = x;
            }
        }

        return pos;
    }

    private int rsearch(int[] vals) {
        int max = 0;
        int pos = vals.length;

        for (int x = vals.length - 1; x >= vals.length / 2; x--) {
            int val = vals[x];
            if (val > max) {
                max = val;
                pos = x;
            }
        }

        return pos;
    }
}
