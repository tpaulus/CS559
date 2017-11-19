package edu.sdsu.cs;

import com.pearsoneduc.ip.op.FFTException;

import java.awt.image.BufferedImage;

/**
 * @author Tom Paulus
 * Created on 11/18/17.
 */
@SuppressWarnings("WeakerAccess")
public class HighPass {
    /**
     * Apply a Gaussian High Pass filter to a given source image
     *
     * @param source {@link BufferedImage} Source Image
     * @param cutoff filter radius
     * @param bias   constant value added to data
     * @return {@link BufferedImage} Filtered Image
     * @throws FFTException if the data are in spectral form; an
     *                      image can be created only from data in the spatial domain.
     */
    public static BufferedImage applyFilter(final BufferedImage source,
                                            final float cutoff,
                                            final int bias)
            throws FFTException {
        ImageFFT fft = new ImageFFT(source);
        fft.transform();
        fft.gaussianHighPassFilter(cutoff);
        fft.transform();

        return fft.toImage(null, bias);
    }

    @SuppressWarnings("WeakerAccess")
    private static class ImageFFT extends com.pearsoneduc.ip.op.ImageFFT {
        public ImageFFT(BufferedImage image) throws FFTException {
            super(image);
        }

        public void gaussianHighPassFilter(float threshold) throws FFTException {
            int u2 = width / 2;
            int v2 = height / 2;
            int su, sv, i = 0;
            double mag, r, rmax = Math.min(u2, v2);

            for (int v = 0; v < height; ++v) {
                sv = shift(v, v2) - v2;
                for (int u = 0; u < width; ++u, ++i) {
                    su = shift(u, u2) - u2;
                    r = Math.sqrt(su * su + sv * sv) / rmax;
                    mag = Math.exp((-1 * r * r) / (1.442695 * threshold * threshold)) * data[i].getMagnitude();
                    data[i].setPolar(mag, data[i].getPhase());
                }
            }
        }
    }

}
