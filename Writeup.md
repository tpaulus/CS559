CS-559 Homework 1
=================

## 1. Similarity Between Pixels
In order to compare two square regions between two images, I would first find the average RBG Color of the regions. Second, convert the computed average to HSL Saturation and Lightness values which can then be compared. Refer to `CompareRegions.java` and `CompareRegionstest.java` for the Implementation and Testing of the aforementioned comparison method.

This method emphasizes differences in color, rather than saturation and brightness. As a result, colors that vary only by shade will have a much higher comparison score, where as colors that are radically different, like Black and White will yield a much lower comparison score.

## 2. PCB Visual Inspection

## 3. Quantization
Please refer to `Quantization.java` and `QuantizationTest.java` for the Implementation and Testing of the Quantization algorithm.

Quantization it the process by which the number of bits used to represent each of the images constituent color channels (R, G, B) is reduced to reduce the overall size of the image. As a result, the number of color options (aka. the color depth) is reduced as is evident in the two images below. 

![Quantized Image with Low Color Depth](/output/quantize_low.jpg)
The Image above has a color depth of 3-bits per channel. While the image still is in 24-bit color, which is due to the color profile used by the Buffered Image and `java.awt.*` package, the first 5 bits of each channel are 0 for the image above.

![Quantized Image with Higher Color Depth](/output/quantize_high.jpg)
The image above has a color depth of 7-bits per channel, and we can see how many more color options there are in this photo. There aer still quite a few colors missing when compared to the source image, but the difference in quality and color variation is evident.

## 4. Enlarging



## Image Credit
Elemaki (Own work) [[GFDL](http://www.gnu.org/copyleft/fdl.html) or [CC BY-SA 3.0](http://creativecommons.org/licenses/by-sa/3.0)], [via Wikimedia Commons](https://commons.wikimedia.org/wiki/File%3A02.Trinidad_(59).JPG)  

Ralf Roletschek [[GFDL](http://www.gnu.org/copyleft/fdl.html) or [CC BY-SA 3.0](http://creativecommons.org/licenses/by-sa/3.0)], [via Wikimedia Commons](https://commons.wikimedia.org/wiki/File%3A13-08-09-peak-by-RalfR-01.jpg)