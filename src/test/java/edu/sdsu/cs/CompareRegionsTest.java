package edu.sdsu.cs;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertNotEquals;

/**
 * @author Tom Paulus
 * Created on 9/15/17.
 */
public class CompareRegionsTest {
    private static final int M = 250;

    @Test
    public void compare() throws Exception {
        Color[][] region1 = makeColoredRegion(M, M, Color.ORANGE);
        Color[][] region2 = makeColoredRegion(M, M, Color.GREEN);

        double result = CompareRegions.compare(region1, region2);
        System.out.println("result = " + result);
        assertNotEquals(result, 0, 0);
    }

    private Color[][] makeColoredRegion(int m, int n, Color color) {
        Color[][] region = new Color[m][n];
        for (int i = 0; i < region.length; i++) {
            for (int j = 0; j < region[i].length; j++) {
                region[i][j] = color;
            }
        }

        return region;
    }
}