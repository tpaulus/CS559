package edu.sdsu.cs;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Tom Paulus
 * Created on 9/16/17.
 */
public class Enlarging {
    private BufferedImage image;

    public Enlarging(BufferedImage image) {
        this.image = image;
    }

    /**
     * Scales the Source image by the given scale factor via Area Based Interpolation
     *
     * @param scaleFactor Scale Factor
     * @return {@link BufferedImage} Scaled Image
     */
    public BufferedImage scale(int scaleFactor) {
        BufferedImage destImage = new BufferedImage(
                image.getWidth() * scaleFactor - (scaleFactor - 1),
                image.getHeight() * scaleFactor - (scaleFactor - 1),
                image.getType());

        for (int y = 0; y < destImage.getHeight(); y++) {
            for (int x = 0; x < destImage.getWidth(); x++) {
                if (y % scaleFactor == 0 || x % scaleFactor == 0) {
                    // Original Value
                    destImage.setRGB(x, y, image.getRGB(x / scaleFactor, y / scaleFactor));
                } else {
                    // Calculated Value
                    int n_source_y = y / scaleFactor;
                    int s_source_y = y / scaleFactor + 1;
                    int e_source_x = x / scaleFactor;
                    int w_source_x = x / scaleFactor + 1;

                    Color ne = new Color(image.getRGB(e_source_x, n_source_y));
                    Color nw = new Color(image.getRGB(w_source_x, n_source_y));
                    Color se = new Color(image.getRGB(e_source_x, s_source_y));
                    Color sw = new Color(image.getRGB(w_source_x, s_source_y));

                    Color interp = new Color(
                            getInterpolatedChannel(y, x, n_source_y, s_source_y, e_source_x, w_source_x, ne.getRed(), nw.getRed(), se.getRed(), sw.getRed(), scaleFactor),
                            getInterpolatedChannel(y, x, n_source_y, s_source_y, e_source_x, w_source_x, ne.getGreen(), nw.getGreen(), se.getGreen(), sw.getGreen(), scaleFactor),
                            getInterpolatedChannel(y, x, n_source_y, s_source_y, e_source_x, w_source_x, ne.getBlue(), nw.getBlue(), se.getBlue(), sw.getBlue(), scaleFactor)
                    );
                    destImage.setRGB(x, y, interp.getRGB());
                }
            }
        }

        return destImage;
    }

    private static int getInterpolatedChannel(int y, int x, int n_source_y, int s_source_y, int e_source_x, int w_source_x, int ne, int nw, int se, int sw, int k) {
        int A1 = area(w_source_x * k, x, n_source_y * k, y);
        int A2 = area(e_source_x * k, x, n_source_y * k, y);
        int A3 = area(w_source_x * k, x, s_source_y * k, y);
        int A4 = area(e_source_x * k, x, s_source_y * k, y);

        return (A4 * nw + A3 * ne + A2 * sw + A1 * se) / (A1 + A2 + A3 + A4);
    }

    private static int area(int x1, int x, int y1, int y) {
        return Math.abs(x1 - x) * Math.abs(y1 - y);
    }
}
