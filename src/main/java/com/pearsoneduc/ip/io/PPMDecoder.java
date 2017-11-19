/***************************************************************************

  PPMDecoder.java

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



/**
 * Reads image data in the PBM, PGM or PPM format from a stream or a file.
 * Documentation for the {@link PPMEncoder} class contains further
 * information on these formats.  Note that only the ASCII variants can
 * be decoded.
 *
 * <p>Example of use:</p>
 * <pre>
 *     PPMDecoder ppm = new PPMDecoder(inputFile);
 *     BufferedImage image = ppm.decodeAsBufferedImage();
 * </pre>
 *
 * @author Nick Efford
 * @version 1.1 [1999/06/27]
 * @see PPMDecoderException
 * @see PPMEncoder
 * @see java.awt.image.BufferedImage
 */

public class PPMDecoder implements ImageDecoder, PPMConstants {


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  /** Reads characters from the datastream. */
  private Reader reader;

  /** Parses the datastream for header elements and pixel values. */
  private StreamTokenizer parser;

  /** Type of datastream: PBM, PGM or PPM (or unknown). */
  private int type = TYPE_UNKNOWN;

  /** Width of image, as specified in header read from datastream. */
  private int width;

  /** Height of image, as specified in header read from datastream. */
  private int height;

  /** Size of datastream (number of pixels). */
  private int size;

  /** Maximum pixel value specified in PGM and PPM headers. */
  private int maxValue;

  /**
   * Value stored in a binary image when a 1 is read from a PBM file.
   * Default value is 0 - i.e., bitmaps are inverted when read.
   */
  private int one = 0;

  /**
   * Value stored in a binary image when a 0 is read from a PBM file.
   * Default value is 1 - i.e., bitmaps are inverted when read.
   */
  private int zero = 1;


  ///////////////////////////// PUBLIC METHODS /////////////////////////////


  /**
   * Constructs a PPMDecoder associated with standard input.
   * @exception IOException if there was a problem reading data
   *  from standard input.
   * @exception PPMDecoderException if the data are not in a valid
   *  PBM, PGM or PPM format.
   */

  public PPMDecoder() throws IOException, PPMDecoderException {
    this(System.in);
  }

  /**
   * Constructs a PPMDecoder that reads from an existing InputStream.
   * @param in InputStream representing the source of image data
   * @exception IOException if there was a problem reading data
   *  from the stream.
   * @exception PPMDecoderException if the data are not in a valid
   *  PBM, PGM or PPM format.
   */

  public PPMDecoder(InputStream in) throws IOException, PPMDecoderException {
    reader = new BufferedReader(new InputStreamReader(in));
    parser = new StreamTokenizer(reader);
    parser.commentChar('#');
    readHeader();
  }

  /**
   * Constructs a PPMDecoder that reads from a named file.
   * @param imgfile name of the file containing the image data
   * @exception IOException if the file does not exist or there was
   *  a problem reading data from it.
   * @exception PPMDecoderException if the data are not in a valid
   *  PBM, PGM or PPM format.
   */

  public PPMDecoder(String imgfile) throws IOException, PPMDecoderException {
    this(new FileInputStream(imgfile));
  }


  /**
   * Indicates what type of image is available for decoding.
   * @return one of the constants TYPE_UNKNOWN, TYPE_PBM, TYPE_PGM
   *  and TYPE_PPM, defined in the PPMConstants interface.
   * @see PPMConstants
   */

  public int getType() {
    return type;
  }


  /**
   * Indicates whether binary image data are available for decoding.
   * @return true if a binary image is available, false otherwise.
   */

  public boolean isBinary() {
    return type == TYPE_PBM;
  }


  /**
   * Indicates whether greyscale image data are available for decoding.
   * @return true if a greyscale image is available, false otherwise.
   */

  public boolean isGrey() {
    return type == TYPE_PGM;
  }


  /**
   * Indicates whether RGB image data are available for decoding.
   * @return true if a colour image is available, false otherwise.
   */

  public boolean isRGB() {
    return type == TYPE_PPM;
  }


  /**
   * @return width of image to be decoded.
   */

  public int getWidth() {
    return width;
  }


  /**
   * @return height of image to be decoded.
   */

  public int getHeight() {
    return height;
  }


  /**
   * @return number of pixels in image to be decoded.
   */

  public int getNumPixels() {
    return size;
  }


  /**
   * @return maximum pixel value possible in image to be decoded.
   */

  public int getMaxValue() {
    return maxValue;
  }


  /**
   * Indicates whether a binary image will be inverted on input, such
   * that 1 becomes black and 0 becomes white.
   * @return true if the data will be inverted, false otherwise.
   */

  public boolean bitmapInversionEnabled() {
    return one == 0 && zero == 1;
  }


  /**
   * Disables bitmap inversion when reading data in the PBM format.
   */

  public void disableBitmapInversion() {
    one = 1;
    zero = 0;
  }


  /**
   * Enables bitmap inversion when reading data in the PBM format.
   */

  public void enableBitmapInversion() {
    one = 0;
    zero = 1;
  }


  /**
   * Decodes the input data and creates an image.
   * @return a BufferedImage containing the data.
   * @exception IOException if there was a problem reading the data.
   * @exception PPMDecoderException if the data were not formatted correctly.
   */

  public BufferedImage decodeAsBufferedImage()
   throws IOException, PPMDecoderException {
    if (type == TYPE_PBM)
      return parsePBM();
    else if (type == TYPE_PGM)
      return parsePGM();
    else
      return parsePPM();
  }


  //////////////////////////// PRIVATE METHODS /////////////////////////////


  /**
   * Reads header information from a PBM, PGM or PPM datastream.
   * @exception IOException if data could not be read.
   * @exception PPMDecoderException if header does not conform to
   *  PBM, PGM or PPM standards.
   */

  private void readHeader() throws IOException, PPMDecoderException {

    // read and check magic number

    parser.nextToken();
    if (parser.ttype != StreamTokenizer.TT_WORD)
      throw new PPMDecoderException("invalid magic number");
    if (parser.sval.equals(PBM_SIG))
      type = TYPE_PBM;
    else if (parser.sval.equals(PGM_SIG))
      type = TYPE_PGM;
    else if (parser.sval.equals(PPM_SIG))
      type = TYPE_PPM;
    else
      throw new PPMDecoderException("invalid magic number");

    // parse image dimensions

    if ((width = nextNumber()) < 1
     || (height = nextNumber()) < 1)
      throw new PPMDecoderException("invalid image dimensions");
    size = width*height;

    // parse maximum value

    if (type != TYPE_PBM)
      maxValue = nextNumber();

    if ((type == TYPE_PGM && maxValue < 0)
     || (type == TYPE_PPM && (maxValue < 0 || maxValue > 255)))
      throw new PPMDecoderException("invalid maximum pixel value");

  }


  /**
   * Parses the datastream until a numeric token has been found.
   * @return the parsed number, as an integer.
   * @exception IOException if data could not be read.
   */

  private int nextNumber() throws IOException {

    do {
      parser.nextToken();
      if (parser.ttype == StreamTokenizer.TT_EOF)
        throw new EOFException();
    }
    while (parser.ttype != StreamTokenizer.TT_NUMBER);

    return (int) parser.nval;

  }


  /**
   * Reads a single character.
   * @exception EOFException if the end of the datastream has been reached.
   */

  private char readChar() throws IOException {
    int value;
    if ((value = reader.read()) == -1)
      throw new EOFException();
    return (char) value;
  }


  /**
   * Parses data in the PBM format.
   * @returns an image containing the parsed data.
   * @exception IOException if data cannot not be read.
   * @exception PPMDecoderException if the data are not in PBM format.
   */

  private BufferedImage parsePBM() throws IOException, PPMDecoderException {
    BufferedImage img =
     new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
    WritableRaster raster = img.getRaster();
    char c;
    for (int y = 0; y < height; ++y)
      for (int x = 0; x < width; ++x) {
        do { c = readChar(); }
         while (Character.isWhitespace(c));
        if (c == '1')
          raster.setSample(x, y, 0, one);
        else if (c == '0')
          raster.setSample(x, y, 0, zero);
        else
          throw new PPMDecoderException("invalid character in bitmap");
      }
    return img;
  }


  /**
   * Parses data in the PGM format.
   * @returns an image containing the parsed data.
   * @exception IOException if data cannot not be read.
   * @exception PPMDecoderException if the data are not in PGM format.
   */

  private BufferedImage parsePGM() throws IOException, PPMDecoderException {

    int imgType;
    if (maxValue > 255)
      imgType = BufferedImage.TYPE_USHORT_GRAY;
    else
      imgType = BufferedImage.TYPE_BYTE_GRAY;

    BufferedImage img = new BufferedImage(width, height, imgType);
    WritableRaster raster = img.getRaster();

    for (int y = 0; y < height; ++y)
      for (int x = 0; x < width; ++x) {
        parser.nextToken();
        if (parser.ttype == StreamTokenizer.TT_EOF)
          throw new EOFException("image appears to be truncated");
        if (parser.ttype != StreamTokenizer.TT_NUMBER)
          throw new PPMDecoderException("non-numeric value for pixel at ("
           + x + "," + y + ")");
        raster.setSample(x, y, 0, (int) parser.nval);
      }

    return img;

  }


  /**
   * Parses data in the PPM format.
   * @returns an image containing the parsed data.
   * @exception IOException if data cannot not be read.
   * @exception PPMDecoderException if the data are not in PPM format.
   */

  private BufferedImage parsePPM() throws IOException, PPMDecoderException {
    BufferedImage img =
     new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    WritableRaster raster = img.getRaster();
    for (int y = 0; y < height; ++y)
      for (int x = 0; x < width; ++x)
        for (int i = 0; i < 3; ++i) {
          parser.nextToken();
          if (parser.ttype == StreamTokenizer.TT_EOF)
            throw new EOFException("image appears to be truncated");
          if (parser.ttype != StreamTokenizer.TT_NUMBER)
            throw new PPMDecoderException("non-numeric value for sample "
             + i + " of pixel at (" + x + "," + y + ")");
          raster.setSample(x, y, i, (int) parser.nval);
        }
    return img;
  }


}
