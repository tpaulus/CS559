CS-559 Homework 3
=================


## Programming Task A
We can apply a Sobel Edge Detection Method with varying thresholds to the provided images. This method yields good results for simple images (those with clear borders between areas), but doesn't work as well with overlapping colors.

![](output/edge_detect_A.png)

![](output/edge_detect_B.png)

We can also apply this method to each of the individual channels of the image, and then merge the results together. This works better in the case of `peppers1`, since the border between the areas is not white, rather different colors.

![](output/edge_detect_C.png)

## Programming Task B

Adding Salt and Pepper noise can wreak havoc on edge detection if the image is not filtered first. Using a simple median filter, we can reduce the number of false edges almost completely.

![](output/salt_and_pepper_A.png)

![](output/salt_and_pepper_B.png)

```java
public static BufferedImage medianFilter(BufferedImage image) {
    Color[] pixel = new Color[9];
    int[] R = new int[9];
    int[] B = new int[9];
    int[] G = new int[9];

    for (int i = 1; i < image.getWidth() - 1; i++)
        for (int j = 1; j < image.getHeight() - 1; j++) {
            pixel[0] = new Color(image.getRGB(i - 1, j - 1));
            pixel[1] = new Color(image.getRGB(i - 1, j));
            pixel[2] = new Color(image.getRGB(i - 1, j + 1));
            pixel[3] = new Color(image.getRGB(i, j + 1));
            pixel[4] = new Color(image.getRGB(i + 1, j + 1));
            pixel[5] = new Color(image.getRGB(i + 1, j));
            pixel[6] = new Color(image.getRGB(i + 1, j - 1));
            pixel[7] = new Color(image.getRGB(i, j - 1));
            pixel[8] = new Color(image.getRGB(i, j));
            for (int k = 0; k < 9; k++) {
                R[k] = pixel[k].getRed();
                B[k] = pixel[k].getBlue();
                G[k] = pixel[k].getGreen();
            }
            Arrays.sort(R);
            Arrays.sort(G);
            Arrays.sort(B);
            image.setRGB(i, j, new Color(R[4], B[4], G[4]).getRGB());
        }

    return image;
}
```
