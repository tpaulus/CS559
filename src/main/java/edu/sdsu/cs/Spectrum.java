package edu.sdsu.cs;

import com.pearsoneduc.ip.op.FFTException;
import com.pearsoneduc.ip.op.ImageFFT;

import java.awt.image.BufferedImage;

/**
 * @author Tom Paulus
 * Created on 11/18/17.
 */
public class Spectrum {
    /**
     * Computes the Fourier spectrum of an image, in the form of another image.
     * The window used when computing the spectrum can be specified using the format:
     * 1 = no window
     * 2 = Bartlett window
     * 3 = Hamming window
     * 4 = Hanning window
     *
     * @param image {@link BufferedImage} Source Image
     * @param window Windowing function
     * @return {@link BufferedImage} Computed Log-Based Fourier Spectrum
     * @throws FFTException if the image is not 8-bit greyscale.
     */
    public static BufferedImage draw(final BufferedImage image,
                              final int window)
            throws FFTException {
        ImageFFT fft = new ImageFFT(image, window);
        fft.transform();
        return fft.getSpectrum();
    }
}
