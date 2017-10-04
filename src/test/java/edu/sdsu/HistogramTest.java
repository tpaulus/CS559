package edu.sdsu;

import org.junit.Test;

import java.awt.*;
import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Tom Paulus
 * Created on 10/3/17.
 */
public class HistogramTest {
    private static final String EXAMPLE_FILE_NAME = "histogram.jpg";

    @Test
    public void makeHistogram() throws Exception {
        Histogram histogram = new Histogram(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME));
        File outFile = File.createTempFile("hist", ".jpg");
        histogram.makeHistogram(outFile);

        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void equalize() throws Exception {
        Histogram histogram = new Histogram(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME));
        File outFile = File.createTempFile("equalized", ".jpg");
        histogram.equalize(outFile);

        Desktop.getDesktop().open(outFile);
    }

    @Test
    public void composite() throws Exception {
        Histogram histogram = new Histogram(this.getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE_NAME));
        File outFile = File.createTempFile("equalized", ".jpg");
        histogram.equalize(outFile);

        histogram = new Histogram(outFile);
        File newHist = File.createTempFile("equalizedHist", ".jpg");
        histogram.makeHistogram(newHist);

        Desktop.getDesktop().open(newHist);
    }
}