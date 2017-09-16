package edu.sdsu.cs;

import java.awt.*;

/**
 * @author Tom Paulus
 * Created on 9/15/17.
 */
public class CompareRegions {
    private static final float H_WEIGHT = .8f;
    private static final float S_WEIGHT = .1f;
    private static final float B_WEIGHT = .1f;

    /**
     * Compare two color regions and calculate their difference in color.
     * Results will be between 0 and 1, with a higher score representing a
     * greater similarity between the two regions.
     *
     * @param region1 {@link Color}
     * @param region2 {@link Color}
     * @return Result Score, between 0 and 1; where 1 = same color
     */
    public static double compare(Color[][] region1, Color[][] region2) {
        Color region1Ave = calculateRegionAverage(region1);
        float[] region1HSB = Color.RGBtoHSB(
                region1Ave.getRed(),
                region1Ave.getGreen(),
                region1Ave.getBlue(),
                null);

        Color region2Ave = calculateRegionAverage(region2);
        float[] region2HSB = Color.RGBtoHSB(
                region2Ave.getRed(),
                region2Ave.getGreen(),
                region2Ave.getBlue(),
                null);

        return compareHSBRegions(region1HSB, region2HSB);
    }

    private static Color calculateRegionAverage(Color[][] sector) {
        final int pixels = (int) Math.pow(sector.length, 2);
        int r = 0;
        int g = 0;
        int b = 0;

        for (int i = 0; i < sector.length; i++) {
            for (int j = 0; j < sector[i].length; j++) {
                Color p = sector[i][j];
                r += p.getRed();
                g += p.getGreen();
                b += p.getBlue();
            }
        }

        return new Color(r / pixels, g / pixels, b / pixels);
    }


    private static double compareHSBRegions(float[] region1, float[] region2) {
        return 1 - (weightedCompare(region1[0], region2[0], H_WEIGHT) +
                weightedCompare(region1[1], region2[1], S_WEIGHT) +
                weightedCompare(region1[2], region2[2], B_WEIGHT));
    }

    private static float weightedCompare(float f1, float f2, float weight) {
        return Math.abs(f1 - f2) * weight;
    }
}
