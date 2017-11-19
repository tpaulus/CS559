/***************************************************************************

  PNGDecoder.java

  Written by Nick Efford.

  Copyright (c) 2000, Pearson Education Ltd.  All rights reserved.

  THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR
  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
  ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE
  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
  BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

***************************************************************************/


package com.pearsoneduc.ip.io;



import java.io.*;
import java.awt.image.*;
import com.visualtek.PNG.PNGException;
import com.visualtek.PNG.PNGInfo;
import com.visualtek.PNG.PNGData;
import com.visualtek.PNG.PNGDataDecoder;



/**
 * Reads image data in the PNG format from a stream or a file.
 * This class is merely a wrapper for PNGDataDecoder from
 * the <code>com.visualtek.PNG</code> package.
 *
 * @author Nick Efford
 * @version 1.1 [1999/06/27]
 * @see PNGDecoderException
 * @see java.awt.image.BufferedImage
 */

public class PNGDecoder implements ImageDecoder {


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  /** Reads PNG data from an input stream. */
  private PNGDataDecoder decoder;

  /** Information stored at start of PNG datastream. */
  private PNGInfo startInfo = new PNGInfo();

  /** Information stored at end of PNG datastream. */
  private PNGInfo endInfo = new PNGInfo();


  ///////////////////////////// PUBLIC METHODS /////////////////////////////


  /**
   * Constructs a PNGDecoder associated with standard input.
   * @exception IOException if there was a problem reading data
   *  from standard input.
   * @exception PNGDecoderException if the data do not conform to
   *  the PNG format.
   */

  public PNGDecoder() throws IOException, PNGDecoderException {
    this(System.in);
  }

  /**
   * Constructs a PNGDecoder that reads from an existing InputStream.
   * @param in InputStream representing the source of image data
   * @exception IOException if there was a problem reading data
   *  from the stream.
   * @exception PNGDecoderException if the data do not conform to the
   *  PNG format.
   */

  public PNGDecoder(InputStream in) throws IOException, PNGDecoderException {
    try {
      decoder = new PNGDataDecoder(in);
      decoder.readInfo(startInfo);
    }
    catch (PNGException e) {   // catch and convert exception
      throw new PNGDecoderException(e.getMessage());
    }
  }

  /**
   * Constructs a PNGDecoder that reads from a named file.
   * @param imgfile name of the file containing the image data
   * @exception IOException if the file does not exist or there was
   *  a problem reading data from it.
   * @exception PNGDecoderException if the data do not conform
   *  to the PNG format.
   */

  public PNGDecoder(String imgfile) throws IOException, PNGDecoderException {
    this(new FileInputStream(imgfile));
  }


  /**
   * Indicates what type of image is available for decoding.
   * @return one of the constants TYPE_UNKNOWN, TYPE_PBM, TYPE_PGM
   *  and TYPE_PPM, defined in the PPMConstants interface.
   * @see PPMConstants
   */

  public int getType() {
    return startInfo.color_type;
  }


  /**
   * Indicates whether binary image data are available for decoding.
   * @return true if a binary image is available, false otherwise.
   */

  public boolean isBinary() {
    return getType() == PNGData.PNG_COLOR_TYPE_GRAY && getBitDepth() == 1;
  }


  /**
   * Indicates whether greyscale image data are available for decoding.
   * @return true if a greyscale image is available, false otherwise.
   */

  public boolean isGrey() {
    return getType() == PNGData.PNG_COLOR_TYPE_GRAY
        || getType() == PNGData.PNG_COLOR_TYPE_GRAY_ALPHA;
  }


  /**
   * Indicates whether indexed colour image data are available for decoding.
   * @return true if indexed colour data are available, false otherwise.
   */

  public boolean hasPalette() {
    return getType() == PNGData.PNG_COLOR_TYPE_PALETTE;
  }


  /**
   * Indicates whether RGB image data are available for decoding.
   * @return true if a colour image is available, false otherwise.
   */

  public boolean isRGB() {
    return getType() == PNGData.PNG_COLOR_TYPE_RGB
        || getType() == PNGData.PNG_COLOR_TYPE_RGB_ALPHA;
  }


  /**
   * @return width of image to be decoded.
   */

  public int getWidth() {
    return startInfo.width;
  }


  /**
   * @return height of image to be decoded.
   */

  public int getHeight() {
    return startInfo.height;
  }


  /**
   * @return number of pixels in image to be decoded.
   */

  public int getNumPixels() {
    return startInfo.width*startInfo.height;
  }


  /**
   * @return bit depth (number of bits per sample) for image to be decoded.
   */

  public int getBitDepth() {
    return startInfo.bit_depth;
  }


  /**
   * @return number of bytes required to store one row of image data.
   */

  public int getRowSize() {
    return startInfo.rowbytes;
  }


  /**
   * Reads a row of image data.
   * The data will be read into the supplied array or, if this is null,
   * returned as a new array of bytes.  The array size required to store
   * a single row is obtained using the {@link #getRowSize() getRowSize}
   * method.
   * @param row byte array in which image data will be placed, or null
   * @return a new array containing row data, if null was passed as
   *  a parameter; or null if an array was passed.
   * @exception IOException if there was a problem reading data.
   * @exception PNGDecoderException if the data do not conform to
   *  the PNG format.
   */

  public byte[] readRow(byte[] row) throws IOException, PNGDecoderException {
    try {
      if (row == null) {
        byte[] newRow = new byte[getRowSize()];
        decoder.readRow(newRow);
        return newRow;
      }
      else {
        decoder.readRow(row);
        return null;
      }
    }
    catch (PNGException e) {   // catch and convert exception
      throw new PNGDecoderException(e.getMessage());
    }
  }


  /**
   * Decodes the input data and creates an image.
   * Currently, this method supports only 8 bits per sample and it ignores
   * alpha channels.  It also converts paletted images into 24-bit colour
   * images.  For different behaviour, you can read an image one row at a
   * time using {@link #readRow(byte[]) readRow} and manipulate the
   * returned bytes in an appropriate manner.
   * @return a BufferedImage containing the data.
   * @exception IOException if there was a problem reading the data.
   * @exception PNGDecoderException if the data are not formatted correctly.
   */

  public BufferedImage decodeAsBufferedImage()
   throws IOException, PNGDecoderException {
    try {
      if (getBitDepth() != 8)
        throw new PNGDecoderException("unsupported bit depth");
      BufferedImage image;
      if (isGrey())
        image = readGreyscaleImage();
      else if (hasPalette())
        image = readPalettedImage();
      else if (isRGB())
        image = readRGBImage();
      else
        throw new PNGDecoderException("unsupported PNG color type");
      decoder.readEnd(endInfo);
      return image;
    }
    catch (PNGException e) {   // catch and convert exception
      throw new PNGDecoderException(e.getMessage());
    }
  }


  //////////////////////////// PRIVATE METHODS /////////////////////////////


  /**
   * Reads a greyscale image from the datastream.
   * @return a greyscale image.
   * @exception IOException if data cannot not be read.
   * @exception PNGException if the data are not in PNG format.
   */

  private BufferedImage readGreyscaleImage()
   throws IOException, PNGException {
    BufferedImage img =
     new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    WritableRaster raster = img.getRaster();
    byte[] row = new byte[getRowSize()];
    int channels = (getType() == PNGData.PNG_COLOR_TYPE_GRAY_ALPHA ? 2 : 1);
    for (int y = 0; y < getHeight(); ++y) {
      decoder.readRow(row);
      int i = 0;
      for (int x = 0; x < getWidth(); ++x, i += channels)
        raster.setSample(x, y, 0, PNGData.ubyte(row[i]));
    }
    return img;
  }


  /**
   * Reads a paletted image from the datastream.
   * The palette is used to create a 24-bit colour image and is then
   * discarded.
   * @returns a colour image.
   * @exception IOException if data cannot not be read.
   * @exception PNGException if the data are not in PNG format.
   */

  private BufferedImage readPalettedImage()
   throws IOException, PNGException {
    BufferedImage img =
     new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
    WritableRaster raster = img.getRaster();
    byte[] row = new byte[getRowSize()];
    for (int y = 0; y < getHeight(); ++y) {
      decoder.readRow(row);
      for (int x = 0; x < getWidth(); ++x) {
        int index = PNGData.ubyte(row[x]);
        raster.setSample(x, y, 0, startInfo.palette[index].getRed());
        raster.setSample(x, y, 1, startInfo.palette[index].getGreen());
        raster.setSample(x, y, 2, startInfo.palette[index].getBlue());
      }
    }
    return img;
  }


  /**
   * Reads an RGB image from the datastream.
   * @returns a colour image.
   * @exception IOException if data cannot not be read.
   * @exception PNGException if the data are not in PNG format.
   */

  private BufferedImage readRGBImage() throws IOException, PNGException {
    BufferedImage img =
     new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
    WritableRaster raster = img.getRaster();
    byte[] row = new byte[getRowSize()];
    int channels = (getType() == PNGData.PNG_COLOR_TYPE_RGB_ALPHA ? 4 : 3);
    for (int y = 0; y < getHeight(); ++y) {
      decoder.readRow(row);
      int i = 0;
      for (int x = 0; x < getWidth(); ++x, i += channels) {
        raster.setSample(x, y, 0, PNGData.ubyte(row[i]));
        raster.setSample(x, y, 1, PNGData.ubyte(row[i+1]));
        raster.setSample(x, y, 2, PNGData.ubyte(row[i+2]));
      }
    }
    return img;
  }


}
