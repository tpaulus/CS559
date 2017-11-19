CS-559 Homework 4
=================

## 1. FFT
### A - Image Size
### B - Bit Reversal
### C - Ringing
### D - Spectrum

## 2. Low Pass Filtering
- [ Discuss findings between Butterworth and Mean ]

Original Image | Butterworth Image | Mean Filtered Image
:------------- | :---------------- | :------------------
![](output/BLONDE1-before.jpg) | ![](output/BLONDE1-butterworth.jpg) | ![](output/BLONDE1-mean.jpg)
![](output/BLONDE2-before.jpg) | ![](output/BLONDE2-butterworth.jpg) | ![](output/BLONDE2-mean.jpg)
![](output/ZEBRA-before.jpg) | ![](output/ZEBRA-butterworth.jpg) | ![](output/ZEBRA-mean.jpg)

Source File    | Spectrum Before | Spectrum After
:------------- | :-------------- | :-------------
![](output/BLONDE1-before.jpg) | ![](output/BLONDE1-spectrum-before.jpg) | ![](output/BLONDE1-spectrum-after.jpg)
![](output/BLONDE2-before.jpg) | ![](output/BLONDE2-spectrum-before.jpg) | ![](output/BLONDE2-spectrum-after.jpg)
![](output/ZEBRA-before.jpg) | ![](output/ZEBRA-spectrum-before.jpg) | ![](output/ZEBRA-spectrum-after.jpg)

## 3. High Pass Filters

We find that the higher the cutoff, the sharper the image is. With the input image being blurry to begin with, we see little difference with a low cutoff (like .1) and see a much sharper image with the pedals of the flower being much more defined with cutoffs > .5.

.1 | .25 | .35 | .5 | .75
:- | :-- | :-- | :- | :--
![](output/FLOWERS-gaussian-VLOW.jpg) | ![](output/FLOWERS-gaussian-LOW.jpg) | ![](output/FLOWERS-gaussian-MED.jpg) | ![](output/FLOWERS-gaussian-HIGH.jpg) | ![](output/FLOWERS-gaussian-XHIGH.jpg)

## 4. More Butterworth
