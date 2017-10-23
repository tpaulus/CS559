package edu.sdsu.cs;

import java.awt.*;
import java.awt.image.*;

/**
 * @author Tom Paulus
 * Created on 10/20/17.
 */
public class EdgeDetect {
    /**
     * Detect Edges using only a single channel of the image (ie. Red, Green or Blue)
     *
     * @param image {@link BufferedImage} Source Image
     * @param threshold Sobel Detection Threshold
     * @return {@link BufferedImage} Sobel Result Gradient
     */
    public BufferedImage detectEdge(final BufferedImage image, int threshold) {
        SobelEdge edge = new SobelEdge(threshold);
        return edge.filter(image);
    }

    /**
     * Detect Edges using only a single channel of the image (ie. Red, Green or Blue)
     *
     * @param image {@link BufferedImage} Source Image
     * @param threshold Sobel Detection Threshold
     * @param channel Color Channel (0=Red, 1=Green, 2=Blue)
     * @return {@link BufferedImage} Sobel Result Gradient
     */
    public BufferedImage detectEdge(final BufferedImage image, int threshold, int channel) {
        BufferedImage singleChannel = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int color;

                switch (channel) {
                    case 0:
                        // Red
                        color = new Color(image.getRGB(x,y)).getRed();
                        break;

                    case 1:
                        // Green
                        color = new Color(image.getRGB(x,y)).getGreen();
                        break;

                    case 2:
                        // Blue
                        color = new Color(image.getRGB(x,y)).getBlue();
                        break;

                    default:
                        throw new RuntimeException("Invalid Channel");
                }

                singleChannel.setRGB(x,y, new Color(color, color, color).getRGB());
            }
        }

        return detectEdge(singleChannel, threshold);
    }


    private class SobelEdge {
        private int gradientThreshold = -1;
        private int magnitudeCalculation = 1;

        public SobelEdge() {
        }

        public SobelEdge(int gradientThreshold) {
            this.gradientThreshold = gradientThreshold;
        }

        public SobelEdge(int gradientThreshold, int magnitudeCalculation) {
            this.gradientThreshold = gradientThreshold;
            this.magnitudeCalculation = magnitudeCalculation;
        }

        void checkImage(BufferedImage bufferedImage) {
            if (bufferedImage.getType() != 10) {
                throw new ImagingOpException("Operation requires an 8-bit grey image");
            }
        }

        @SuppressWarnings("SameParameterValue")
        BufferedImage createCompatibleDestImage(BufferedImage bufferedImage, ColorModel colorModel) {
            if (colorModel == null) colorModel = bufferedImage.getColorModel();

            return new BufferedImage(colorModel,
                    colorModel.createCompatibleWritableRaster(bufferedImage.getWidth(), bufferedImage.getHeight()),
                    colorModel.isAlphaPremultiplied(),
                    null);
        }

        public BufferedImage filter(BufferedImage inputImage) {
            this.checkImage(inputImage);
            BufferedImage outputImage = this.createCompatibleDestImage(inputImage, null);

            WritableRaster writableRaster = outputImage.getRaster();
            float[] gradientMagnitude = this.gradientMagnitude(inputImage);
            float max = gradientMagnitude[0];

            for (float aGradientMagnitude : gradientMagnitude) {
                if (aGradientMagnitude > max) {
                    max = aGradientMagnitude;
                }
            }

            int width = inputImage.getWidth();
            int height = inputImage.getHeight();
            float f2 = 255.0f / max;
            if (this.gradientThreshold >= 0) {
                for (int x = 1; x < width - 1; x++) {
                    for (int y = 1; y < height - 1; y++) {
                        if (Math.round(f2 * gradientMagnitude[y * width + x]) >= this.gradientThreshold) {
                            writableRaster.setSample(x, y, 0, 255);
                        }
                    }
                }
            } else {
                for (int x = 1; x < width - 1; x++) {
                    for (int y = 1; y < height - 1; y++) {
                        writableRaster.setSample(x, y, 0, Math.round(f2 * gradientMagnitude[y * width + x]));
                    }
                }
            }
            return outputImage;
        }

        protected float[] gradientMagnitude(BufferedImage bufferedImage) {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            float[] gradientArr = new float[width * height];

            WritableRaster writableRaster = bufferedImage.getRaster();

            if (this.magnitudeCalculation == 2) {
                for (int x = 1; x < width - 1; x++) {
                    for (int y = 1; y < height - 1; y++) {
                        int xGradient = this.xGradient(writableRaster, x, y);
                        int yGradient = this.yGradient(writableRaster, x, y);
                        gradientArr[y * width + x] = Math.abs(xGradient) + Math.abs(yGradient);
                    }
                }
            } else {
                for (int x = 1; x < width - 1; x++) {
                    for (int y = 1; y < height - 1; y++) {
                        int xGradient = this.xGradient(writableRaster, x, y);
                        int yGradient = this.yGradient(writableRaster, x, y);
                        gradientArr[y * width + x] = (float) Math.sqrt(xGradient * xGradient + yGradient * yGradient);
                    }
                }
            }
            return gradientArr;
        }

        private int xGradient(Raster raster, int x, int y) {
            return raster.getSample(x - 1, y - 1, 0) +
                    2 * raster.getSample(x - 1, y, 0) +
                    raster.getSample(x - 1, y + 1, 0) -
                    raster.getSample(x + 1, y - 1, 0) -
                    2 * raster.getSample(x + 1, y, 0) -
                    raster.getSample(x + 1, y + 1, 0);
        }

        private int yGradient(Raster raster, int x, int y) {
            return raster.getSample(x - 1, y - 1, 0) +
                    2 * raster.getSample(x, y - 1, 0) +
                    raster.getSample(x + 1, y - 1, 0) -
                    raster.getSample(x - 1, y + 1, 0) -
                    2 * raster.getSample(x, y + 1, 0) -
                    raster.getSample(x + 1, y + 1, 0);
        }
    }
}