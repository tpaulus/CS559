package edu.sdsu.cs;

import com.pearsoneduc.ip.op.FFTException;
import com.pearsoneduc.ip.op.ImageFFT;

import java.awt.image.BufferedImage;

/**
 * @author Tom Paulus
 * Created on 11/18/17.
 */
public class Butterworth {
    /**
     * Apply a Butterworth Low pass filter to an image
     *
     * @param input {@link BufferedImage} Source Image
     * @param order Order of filter
     * @param cutoff Filter radius
     * @return {@link BufferedImage} Filtered Image
     * @throws  FFTException if spectral data are not available or
     *  filter parameters are invalid.
     */
    public static BufferedImage applyFilter(final BufferedImage input,
                                     final int order,
                                     final float cutoff)
            throws FFTException {
        ImageFFT fft = new ImageFFT(input);
        fft.transform();
        fft.butterworthLowPassFilter(order, cutoff);
        fft.transform();

        return fft.toImage(null);
    }
}
