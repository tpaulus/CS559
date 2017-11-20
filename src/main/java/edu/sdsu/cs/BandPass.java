package edu.sdsu.cs;

import com.pearsoneduc.ip.op.FFTException;

import java.awt.image.BufferedImage;

/**
 * @author Tom Paulus
 * Created on 11/19/17.
 */
public class BandPass {
    public static BufferedImage applyFilter(final BufferedImage source,
                                            final double bandwidth,
                                            final double radius,
                                            final int bias)
            throws FFTException {
        ImageFFT fft = new ImageFFT(source);
        fft.transform();
        fft.gaussianBandPassFilter(radius, bandwidth);
        fft.transform();

        return fft.toImage(null, bias);
    }

    private static class ImageFFT extends com.pearsoneduc.ip.op.ImageFFT {
        public ImageFFT(BufferedImage image) throws FFTException {
            super(image);
        }

        public void gaussianBandPassFilter(double radius, double delta)
                throws FFTException {

            if (!spectral)
                throw new FFTException(NO_DATA);

            int u2 = width / 2;
            int v2 = height / 2;
            int su, sv, i = 0;
            double mag, r, rmax = Math.min(u2, v2);

            for (int v = 0; v < height; ++v) {
                sv = shift(v, v2) - v2;
                for (int u = 0; u < width; ++u, ++i) {
                    su = shift(u, u2) - u2;
                    r = Math.sqrt(su * su + sv * sv) / rmax;
                    double filter = 1 - Math.exp((r * r - radius * radius) / 2 * delta * delta);
                    mag = filter
                            * data[i].getMagnitude();
                    data[i].setPolar(mag, data[i].getPhase());
                }
            }

        }
    }
}
