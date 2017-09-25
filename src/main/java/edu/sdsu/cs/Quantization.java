package edu.sdsu.cs;

import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;

/**
 * @author Tom Paulus
 * Created on 9/15/17.
 */
public class Quantization {
    private BufferedImage sourceImage;

    public Quantization(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    /**
     * Reduce the Color Depth of an Image by reducing the number of bits per channel.
     * The fewer number of bits, the fewer colors can be represented in the image.
     *
     * @param numBits Number of Bits Per Channel to Reduce the Image to
     * @return {@link BufferedImage} Quantized Image
     */
    public BufferedImage quantiseImage(int numBits) {
        int n = 8 - numBits;

        // Generate the Lookup table,
        // this will do all the hard work reducing the number of bits per channel
        float scale = 255.0f / (255 >> n);
        byte[] tableData = new byte[256];
        for (int i = 0; i < 256; ++i)
            tableData[i] = (byte) Math.round(scale * (i >> n));
        LookupOp lookup =
                new LookupOp(new ByteLookupTable(0, tableData), null);

        // Use the Lookup table to process the source image and return the result
        return lookup.filter(sourceImage, null);
    }
}
