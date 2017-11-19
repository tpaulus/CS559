package edu.sdsu.cs;

import com.pearsoneduc.ip.op.MeanKernel;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * @author Tom Paulus
 * Created on 11/18/17.
 */
public class Spatial {
    public static BufferedImage meanFilter(final BufferedImage source, final int size) {
        Kernel kernel = new MeanKernel(size, size);
        ConvolveOp blurOp = new ConvolveOp(kernel);
        return blurOp.filter(source, null);
    }
}
