package edu.sdsu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tom Paulus
 * Created on 10/3/17.
 */
public class Histogram {
    private static final int COLOR_DEPTH = 255;

    private static final int HIST_COL_WIDTH = 25;
    private static final int HIST_COL_PADDING = 5;

    private static final int EQUALIZE_L = 255;

    private BufferedImage inputImage;

    public Histogram(File file) throws IOException {
        this.inputImage = ImageIO.read(file);
    }

    public Histogram(InputStream stream) throws IOException {
        this.inputImage = ImageIO.read(stream);
    }

    public static void main(String[] args) {
        assert args.length > 0;
        try {
            Histogram histogram = new Histogram(new File(args[0]));
            File histFile = new File("HistImg.jpg");
            histogram.makeHistogram(histFile);
            Desktop.getDesktop().open(histFile);


            File equFile = new File("EqualizedImg.jpg");
            histogram.equalize(equFile);
            Desktop.getDesktop().open(histFile);

            Histogram equalized = new Histogram(equFile);
            File histEquFile = new File("EqualizedHistImg.jpg");
            histogram.makeHistogram(histEquFile);
            Desktop.getDesktop().open(histEquFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getMax(int[] values) {
        int max = 0;
        for (int value : values) {
            if (max < value) max = value;
        }

        return max;
    }

    /**
     * Make a Histogram for the Image
     *
     * @param output {@link File} Histogram
     * @throws IOException File could not be written to
     */
    @SuppressWarnings("WeakerAccess")
    public void makeHistogram(File output) throws IOException {
        makeGraph(getHistogramValues(), output);
    }

    private int[] getHistogramValues() {
        int[] values = new int[COLOR_DEPTH + 1];
        for (int x = 0; x < inputImage.getWidth(); x++) {
            for (int y = 0; y < inputImage.getHeight(); y++) {
                values[new Color(inputImage.getRGB(x, y)).getRed()] += 1;
            }
        }
        return values;
    }

    /**
     * Perform a Histogram Normalization over the Image
     *
     * @param output {@link File} Equalized Image
     * @throws IOException File could not be written to
     */
    @SuppressWarnings("WeakerAccess")
    public void equalize(File output) throws IOException {
        final double a = ((double) EQUALIZE_L) / (inputImage.getWidth() * inputImage.getHeight()); // Normalization Factor
        int[] histogram = getHistogramValues();

        histogram[0] = ((int) (a * histogram[0]));
        for (int l = 1; l < EQUALIZE_L; l++) {
            histogram[l] = ((int) (histogram[l - 1] + a * histogram[l]));
        }

        BufferedImage image = new BufferedImage(inputImage.getWidth(),
                inputImage.getHeight(),
                inputImage.getType());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int norm = histogram[new Color(inputImage.getRGB(x, y)).getRed()];
                image.setRGB(x, y, new Color(norm, norm, norm).getRGB());
            }
        }

        ImageIO.write(image, "JPG", output);
    }

    private void makeGraph(int[] values, File output) throws IOException {
        BufferedImage graph = new BufferedImage((COLOR_DEPTH + 1) * (HIST_COL_PADDING + HIST_COL_WIDTH),
                getMax(values) + HIST_COL_PADDING * 2,
                inputImage.getType());

        for (int i = 0; i < values.length; i++) {
            int sp = i * (HIST_COL_PADDING + HIST_COL_WIDTH);
            for (int x = sp; x < sp + HIST_COL_WIDTH; x++) {
                for (int y = 0; y < values[i]; y++) {
                    graph.setRGB(graph.getWidth() - x - 1,
                            graph.getHeight() - y - 1,
                            Color.WHITE.getRGB());
                }
            }
        }

        ImageIO.write(graph, "JPG", output);
    }
}
