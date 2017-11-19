/***************************************************************************

  SIFEncoder.java

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
import java.util.zip.*;
import java.awt.image.*;



/**
 * Writes image data to a stream or file encoded in the SIF format.
 *
 * <p>SIF files have a very simple binary format.  The first four bytes
 * constitute a <em>signature</em> for the file.  Valid signatures are
 * listed below:</p>
 *
 * <center>
 * <table border=1>
 * <tr> <th>Signature</th> <th>Image type</th> </tr>
 * <tr> <td align="center"><kbd>BIMG</kbd></td>
 * <td>8-bit greyscale image</td> </tr>
 * <tr> <td align="center"><kbd>bIMG</kbd></td>
 * <td>8-bit greyscale image, compressed</td> </tr>
 * <tr> <td align="center"><kbd>CIMG</kbd></td>
 * <td>24-bit colour image</td> </tr>
 * <tr> <td align="center"><kbd>cIMG</kbd></td>
 * <td>24-bit colour image, compressed</td> </tr>
 * </table>
 * </center>
 *
 * <p>Following the signature are two binary-formatted integers,
 * representing the width and height of the image.  The remainder of
 * a SIF file consists of image data.  Colour images are stored in
 * band-interleaved format, i.e. blue, green and red values for each
 * pixel in turn.</p>
 *
 * @author Nick Efford
 * @version 1.0 [1999/01/30]
 * @see SIFEncoderException
 * @see SIFDecoder
 * @see java.awt.image.BufferedImage
 */

public class SIFEncoder implements ImageEncoder, SIFConstants {


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  /** Stream used to write SIF data. */
  private DataOutputStream output;

  /** Indicates whether datastream will be compressed. */
  private boolean compression = true;


  ///////////////////////////// PUBLIC METHODS ///////////////////////////// 


  /**
   * Constructs a SIFEncoder associated with the standard output.
   */

  public SIFEncoder() {
    this(System.out);
  }

  /**
   * Constructs a SIFEncoder associated with a specified OutputStream.
   * @param stream the stream to which image data will be written
   */

  public SIFEncoder(OutputStream stream) {
    output = new DataOutputStream(stream);
  }

  /**
   * Constructs a SIFEncoder that writes to a named file.
   * @param imgfile name of the file to which image data will be written
   * @exception FileNotFoundException if the file could not be accessed.
   */

  public SIFEncoder(String imgfile) throws FileNotFoundException {
    this(new FileOutputStream(imgfile));
  }


  /**
   * @return true if compression will be carried out, false otherwise.
   */

  public boolean compressionEnabled() {
    return compression;
  }


  /**
   * Turns image compression on.
   */

  public void enableCompression() {
    compression = true;
  }


  /**
   * Turns image compression off.
   */

  public void disableCompression() {
    compression = false;
  }


  /**
   * Writes a BufferedImage to a stream in SIF format.
   * @param img The BufferedImage to be written
   * @exception IOException if bytes could not be written to the stream.
   * @exception SIFEncoderException if the BufferedImage type is not
   *  supported by the SIF format.
   */

  public void encode(BufferedImage img)
   throws IOException, SIFEncoderException {
    writeHeader(img);
    if (img.getType() == BufferedImage.TYPE_BYTE_GRAY
     || img.getType() == BufferedImage.TYPE_3BYTE_BGR) {
      DataBufferByte db = (DataBufferByte) img.getRaster().getDataBuffer();
      byte[] data = db.getData();
      if (compression) {
        DeflaterOutputStream deflater = new DeflaterOutputStream(output);
        deflater.write(data, 0, data.length);
        deflater.finish();
      }
      else {
        output.write(data);
        output.flush();
      }
    }
    else {
      Raster raster = img.getRaster();
      if (compression) {
        DeflaterOutputStream deflater = new DeflaterOutputStream(output);
        for (int y = 0; y < img.getHeight(); ++y)
          for (int x = 0; x < img.getWidth(); ++x)
            for (int i = 2; i >= 0; --i)
              deflater.write(raster.getSample(x, y, i));
        deflater.finish();
      }
      else {
        for (int y = 0; y < img.getHeight(); ++y)
          for (int x = 0; x < img.getWidth(); ++x)
            for (int i = 2; i >= 0; --i)
              output.write(raster.getSample(x, y, i));
        output.flush();
      }
    }
  }


  //////////////////////////// PRIVATE METHODS /////////////////////////////


  /**
   * Writes a SIF header to the output stream.
   * @param img image for which header data must be written
   * @exception IOException if data could not be written to the stream.
   * @exception SIFEncoderException if the image type is not supported by
   *  the SIF format.
   */

  private void writeHeader(BufferedImage img)
   throws IOException, SIFEncoderException {

    if (img.getType() == BufferedImage.TYPE_BYTE_BINARY
     || img.getType() == BufferedImage.TYPE_USHORT_GRAY
     || img.getType() == BufferedImage.TYPE_USHORT_555_RGB
     || img.getType() == BufferedImage.TYPE_USHORT_565_RGB
     || img.getType() == BufferedImage.TYPE_BYTE_INDEXED)
      throw new SIFEncoderException("unsupported image type");

    if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
      if (compression)
        output.write(GREY_COMPRESSED_SIG.getBytes());
      else
        output.write(GREY_SIG.getBytes());
    }
    else {
      if (compression)
        output.write(RGB_COMPRESSED_SIG.getBytes());
      else
        output.write(RGB_SIG.getBytes());
    }

    output.writeInt(img.getWidth());
    output.writeInt(img.getHeight());
    output.flush();

  }


}
