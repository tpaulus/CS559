/***************************************************************************

  PPMEncoder.java

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
import java.util.Date;



/**
 * Writes image data to a stream or a file encoded in PBM, PGM or PPM
 * format, as appropriate.
 *
 * <p>The PBM, PGM and PPM formats are simple, popular formats for binary,
 * greyscale and colour images on Unix systems.  This class supports only
 * the ASCII variants of these formats; the more compact raw binary
 * variants are not currently supported.</p>
 *
 * <p>A PBM, PGM or PPM header contains a two-character signature (see
 * table below), the image width, the image height and (unless it is a PBM
 * file) the maximum pixel value.  Each of these elements are separated by
 * whitespace.  The header may also contain comment lines, each of which
 * begin with the '#' character.  By default, an encoder adds a comment
 * giving the date and time of image creation; to prevent this, invoke the
 * encoder's {@link #disableComments() disableComments} method.</p>
 *
 * <center>
 * <table border=1>
 * <tr> <th>Signature</th> <th>Image type</th> </tr>
 * <tr> <td align="center">P1</td> <td>binary (1 bit per pixel)</td> </tr>
 * <tr> <td align="center">P2</td> <td>greyscale (8 or 16 bits)</td> </tr>
 * <tr> <td align="center">P3</td> <td>RGB colour (24 bits)</td> </tr>
 * </table>
 * </center>
 *
 * <p>Image data are written using ASCII numeric characters.  Line breaks
 * are inserted after each row of data, and also wherever necessary to
 * ensure that no line in the file is more than 70 characters in length.</p>
 *
 * <p>PBM data are written as a stream of the characters '1' or '0'.
 * By default, the inverted meaning of these values is used, as required
 * by the official definition of the PBM format - with '1' signifying black
 * and '0' signifying white.  This can be reversed by invoking the
 * {@link #disableBitmapInversion() disableBitmapInversion} method.</p>
 *
 * <p>PGM data are written as a series of ASCII decimal values,
 * right-justified in a field four characters wide (or six characters wide
 * in the case of 16-bit images).  PPM data are written in a
 * band-interleaved fashion - i.e., the R, G and B values are written for
 * each pixel in turn - with each colour component's value written as for
 * greyscale images.  An extra space is inserted between each RGB
 * triplet.</p>
 *
 * <p>Example of use:</p>
 * <pre>
 *     BufferedImage image =
 *      new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_GRAY);
 *     ...
 *     PPMEncoder pgm = new PPMEncoder("test.pgm");
 *     pgm.disableComments();
 *     pgm.encode(image);
 * </pre>
 *
 * @author Nick Efford
 * @version 1.1 [1999/06/27]
 * @see PPMEncoderException
 * @see PPMDecoder
 * @see java.awt.image.BufferedImage
 */

public class PPMEncoder implements ImageEncoder, PPMConstants {


  //////////////////////////// CLASS CONSTANTS /////////////////////////////


  /** Maximum value of pixels in 16-bit image */
  private static final int MAX_USHORT_VALUE = 65535;

  /** Maximum number of pixel values per line in a PBM image */
  private static final int MAX_BITS_PER_LINE = 70;

  /** Maximum number of pixel values per line in an 8-bit PGM image */
  private static final int MAX_BYTES_PER_LINE = 17;

  /** Maximum number of pixel values per line in a 16-bit PGM image */
  private static final int MAX_SHORTS_PER_LINE = 11;

  /** Maximum number of pixel values per line in a PPM image */
  private static final int MAX_RGB_PER_LINE = 5;


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  /** Writes the datastream. */
  private PrintWriter writer;

  /** Specifies what type of datastream to write (PBM, PGM or PPM). */
  private int type = TYPE_UNKNOWN;

  /**
   * Maximum pixel value required in PGM and PPM headers.
   * Default value is 255.
   */
  private int maxValue = 255;

  /**
   * Flag to indicate whether comments are added to header.
   * Default value is true; a comment line <em>will</em> be added.
   */
  private boolean comments = true;

  /**
   * Value written to a PBM datastream when a pixel value is 1.
   * Default value = 0, i.e. bitmaps are inverted.
   */
  private int one = 0;


  ///////////////////////////// PUBLIC METHODS /////////////////////////////


  /**
   * Constructs a PPMEncoder associated with standard output.
   */

  public PPMEncoder() {
    this(System.out);
  }

  /**
   * Constructs a PPMEncoder that writes to an existing OutputStream object.
   * @param out the destination for the image data
   */

  public PPMEncoder(OutputStream out) {
    writer = new PrintWriter(
              new BufferedWriter(
               new OutputStreamWriter(out)));
  }

  /**
   * Constructs a PPMEncoder that writes to a named file.
   * @param filename Name of the file to which image data will be written
   * @exception IOException if the file could not be accessed.
   */

  public PPMEncoder(String filename) throws IOException {
    writer = new PrintWriter(
              new BufferedWriter(
               new FileWriter(filename)));
  }


  /**
   * Indicates whether comments may be added to a PBM, PGM or PPM header.
   * @return true if comments may be added, false otherwise.
   */

  public boolean commentsEnabled() {
    return comments;
  }


  /**
   * Disables commenting of the PBM, PGM or PPM header with creation date.
   */

  public void disableComments() {
    comments = false;
  }


  /**
   * Enables commenting of the PBM, PGM or PPM header.
   */

  public void enableComments() {
    comments = true;
  }


  /**
   * Indicates whether a binary image will be inverted when written to a PBM
   * file, such that black and white are written as 1 and 0, respectively.
   * @return true if a bitmap would be inverted, false otherwise.
   */

  public boolean bitmapInversionEnabled() {
    return one == 0;
  }


  /**
   * Disables bitmap inversion when writing data in the PBM format.
   */

  public void disableBitmapInversion() {
    one = 1;
  }


  /**
   * Enables bitmap inversion when writing data in the PBM format.
   */

  public void enableBitmapInversion() {
    one = 0;
  }


  /**
   * Encodes the specified image using the most appropriate image format.
   * Images of type <kbd>BufferedImage.TYPE_BYTE_BINARY</kbd> are
   * encoded using the PBM format.
   * Images of type <kbd>BufferedImage.TYPE_BYTE_GRAY</kbd> or
   * <kbd>BufferedImage.TYPE_USHORT_GRAY</kbd> are encoded using the
   * PGM format. Other image types are encoded using the PPM format.
   *
   * @param image the image to be encoded
   * @exception PPMEncoderException if the image could not be written
   *  successfully.
   * @see java.awt.image.BufferedImage
   */

  public void encode(BufferedImage image) throws PPMEncoderException {
    checkImageType(image);
    writeHeader(image);
    if (type == TYPE_PBM)
      writePBM(image);
    else if (type == TYPE_PGM)
      writePGM(image);
    else
      writePPM(image);
  }


  //////////////////////////// PRIVATE METHODS /////////////////////////////


  /**
   * Checks image to figure out what type of file should be written.
   * @param image the image to be encoded
   */

  private void checkImageType(BufferedImage image) {

    if (image.getType() == BufferedImage.TYPE_BYTE_BINARY)
      type = TYPE_PBM;
    else if (image.getType() == BufferedImage.TYPE_BYTE_GRAY
     || image.getType() == BufferedImage.TYPE_USHORT_GRAY)
      type = TYPE_PGM;
    else
      type = TYPE_PPM;

    if (image.getType() == BufferedImage.TYPE_USHORT_GRAY)
      maxValue = MAX_USHORT_VALUE;

  }


  /**
   * Writes a PBM, PGM or PPM image header.
   * @param image the image to be encoded
   * @exception PPMEncoderException if the header could not be written.
   */

  private void writeHeader(BufferedImage image) throws PPMEncoderException {

    // write signature

    if (type == TYPE_PBM)
      writer.println(PBM_SIG);
    else if (type == TYPE_PGM)
      writer.println(PGM_SIG);
    else
      writer.println(PPM_SIG);

    // write comment

    if (comments)
      writer.println("# Created " + new Date());

    // write image dimensions and maximum value

    writer.println(image.getWidth() + " " + image.getHeight());
    if (type != TYPE_PBM)
      writer.println(maxValue);

    if (writer.checkError())
      throw new PPMEncoderException("error writing PBM/PGM/PPM header");

  }


  /**
   * Writes a binary image in PBM format.
   * @param image the image to be encoded
   * @exception PPMEncoderException if the image data could not be written.
   */

  private void writePBM(BufferedImage image) throws PPMEncoderException {
    Raster raster = image.getRaster();
    for (int y = 0; y < image.getHeight(); ++y) {
      for (int x = 0; x < image.getWidth(); ++x) {
        if (raster.getSample(x, y, 0) == one)
          writer.print('1');
        else
          writer.print('0');
        if ((x+1) % MAX_BITS_PER_LINE == 0)  // max line length is 70 chars
          writer.println();
      }
      if (image.getWidth() != MAX_BITS_PER_LINE)
        writer.println();
    }
    if (writer.checkError())
      throw new PPMEncoderException("error writing PBM data");
  }


  /**
   * Writes an 8-bit or 16-bit greyscale image in PGM format.
   * @param image the image to be encoded
   * @exception PPMEncoderException if the image data could not be written.
   */

  private void writePGM(BufferedImage image) throws PPMEncoderException {

    Raster raster = image.getRaster();

    if (image.getType() == BufferedImage.TYPE_USHORT_GRAY) {
      for (int y = 0; y < image.getHeight(); ++y) {
        for (int x = 0; x < image.getWidth(); ++x) {
          writeAsShort(raster.getSample(x, y, 0));
          if ((x+1) % MAX_SHORTS_PER_LINE == 0)
            writer.println();
        }
        if (image.getWidth() != MAX_SHORTS_PER_LINE)
          writer.println();
      }
    }
    else {
      for (int y = 0; y < image.getHeight(); ++y) {
        for (int x = 0; x < image.getWidth(); ++x) {
          writeAsByte(raster.getSample(x, y, 0));
          if ((x+1) % MAX_BYTES_PER_LINE == 0)
            writer.println();
        }
        if (image.getWidth() != MAX_BYTES_PER_LINE)
          writer.println();
      }
    }

    if (writer.checkError())
      throw new PPMEncoderException("error writing PGM data");

  }


  /**
   * Writes a colour image in PPM format.
   * @param image the image to be encoded
   * @exception PPMEncoderException if the image data could not be written.
   */

  private void writePPM(BufferedImage image) throws PPMEncoderException {
    for (int y = 0; y < image.getHeight(); ++y) {
      for (int x = 0; x < image.getWidth(); ++x) {
        writer.print(" ");
	writeAsBytes(image.getRGB(x, y));
        if ((x+1) % MAX_RGB_PER_LINE == 0)
          writer.println();
      }
      if (image.getWidth() != MAX_RGB_PER_LINE)
        writer.println();
    }
    if (writer.checkError())
      throw new PPMEncoderException("error writing PPM data");
  }


  /**
   * Writes a byte value in a right-justified field four characters wide.
   * @param value the value to be written, as an integer
   */

  private void writeAsByte(int value) {
    if (value < 10)
      writer.print("   " + value);
    else if (value < 100)
      writer.print("  " + value);
    else
      writer.print(" " + value);
  }


  /**
   * Writes an RGB colour as three integer values, each right-justified
   * in a field four characters wide. The R, G and B components of the
   * colour are packed into a single integer.
   * @param colour RGB colour as a single integer
   */

  private void writeAsBytes(int value) {
    writeAsByte((value & 0x00ff0000) >> 16);
    writeAsByte((value & 0x0000ff00) >> 8);
    writeAsByte( value & 0x000000ff);
  }


  /**
   * Writes a 16-bit value in a right-justified field six characters wide.
   * @param value the value to be written, as an integer
   */

  private void writeAsShort(int value) {
    if (value < 10)
      writer.print("     " + value);
    else if (value < 100)
      writer.print("    " + value);
    else if (value < 1000)
      writer.print("   " + value);
    else if (value < 10000)
      writer.print("  " + value);
    else
      writer.print(" " + value);
  }


}
