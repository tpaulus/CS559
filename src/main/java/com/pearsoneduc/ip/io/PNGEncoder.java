/***************************************************************************

  PNGEncoder.java

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
import com.visualtek.PNG.PNGData;
import com.visualtek.PNG.PNGDataEncoder;
import com.visualtek.PNG.PNGException;
import com.visualtek.PNG.PNGInfo;



/**
 * Writes image data to a stream or a file encoded in the PNG format.
 * This class is merely a wrapper for PNGDataEncoder from the
 * <code>com.visualtek.PNG</code> package.
 *
 * <p>Example of use:</p>
 * <pre>
 *     BufferedImage image =
 *      new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_GRAY);
 *     ...
 *     PNGEncoder png = new PNGEncoder("test.png");
 *     png.encode(image);
 * </pre>
 *
 * @author Nick Efford
 * @version 1.1 [1999/06/27]
 * @see java.awt.image.BufferedImage
 */

public class PNGEncoder implements ImageEncoder {


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  /** Writes PNG data to a stream. */
  private PNGDataEncoder encoder;

  /** PNG header information. */
  private PNGInfo header = new PNGInfo();


  ///////////////////////////// PUBLIC METHODS /////////////////////////////


  /**
   * Constructs a PNGEncoder associated with standard output.
   */

  public PNGEncoder() {
    this(System.out);
  }

  /**
   * Constructs a PNGEncoder that writes to an existing OutputStream object.
   * @param out the destination for the image data
   */

  public PNGEncoder(OutputStream out) {
    encoder = new PNGDataEncoder(out);
  }

  /**
   * Constructs a PPMEncoder that writes to a named file.
   * @param filename Name of the file to which image data will be written
   * @exception IOException if the file could not be accessed.
   */

  public PNGEncoder(String filename) throws IOException {
    this(new FileOutputStream(filename));
  }


  /**
   * Encodes the specified image in the PNG image format.
   * @param image the image to be encoded
   * @exception IOException if data could not be written to the stream.
   * @exception PNGEncoderException if the image could not be written
   *  in PNG format.
   * @see java.awt.image.BufferedImage
   */

  public void encode(BufferedImage image)
   throws IOException, PNGEncoderException {
    try {
      buildHeader(image);
      encoder.writeInfo(header);
      Raster raster = image.getRaster();
      if (image.getType() == BufferedImage.TYPE_BYTE_GRAY)
        writeGreyscaleData(raster);
      else
        writeRGBData(raster);
      encoder.writeEnd(null);
    }
    catch (PNGException e) {   // catch and convert exception
      throw new PNGEncoderException(e.getMessage());
    }
  }


  //////////////////////////// PRIVATE METHODS /////////////////////////////


  /**
   * Constructs a PNG header for the specified image.
   * @param image the image to be encoded
   * @exception PNGEncoderException if the image type is not supported.
   */

  private void buildHeader(BufferedImage image) throws PNGEncoderException {

    if (image.getType() == BufferedImage.TYPE_BYTE_BINARY
     || image.getType() == BufferedImage.TYPE_USHORT_GRAY
     || image.getType() == BufferedImage.TYPE_USHORT_555_RGB
     || image.getType() == BufferedImage.TYPE_USHORT_565_RGB
     || image.getType() == BufferedImage.TYPE_BYTE_INDEXED)
      throw new PNGEncoderException("unsupported image type");

    header.width = image.getWidth();
    header.height = image.getHeight();
    header.bit_depth = 8;
    if (image.getType() == BufferedImage.TYPE_BYTE_GRAY)
      header.color_type = PNGData.PNG_COLOR_TYPE_GRAY;
    else
      header.color_type = PNGData.PNG_COLOR_TYPE_RGB;

  }


  /**
   * Writes 8-bit greyscale image data in PNG format.
   * @param raster Raster containing the data to be encoded
   * @exception IOException if data could not be written to the stream.
   * @exception PNGException if image data could not be written in PNG form.
   */

  private void writeGreyscaleData(Raster raster)
   throws IOException, PNGException {
    byte[] row = new byte[header.width];
    for (int y = 0; y < header.height; ++y) {
      for (int x = 0; x < header.width; ++x)
        row[x] = (byte) raster.getSample(x, y, 0);
      encoder.writeRow(row);
    }
  }


  /**
   * Writes RGB colour image data in PNG format.
   * @param raster Raster containing the data to be encoded
   * @exception IOException if data could not be written to the stream.
   * @exception PNGException if image data could not be written in PNG form.
   */

  private void writeRGBData(Raster raster)
   throws IOException, PNGException {
    byte[] row = new byte[3*header.width];
    for (int y = 0; y < header.height; ++y) {
      int i = 0;
      for (int x = 0; x < header.width; ++x, i += 3) {
        row[i] = (byte) raster.getSample(x, y, 0);
        row[i+1] = (byte) raster.getSample(x, y, 1);
        row[i+2] = (byte) raster.getSample(x, y, 2);
      }
      encoder.writeRow(row);
    }
  }


}
