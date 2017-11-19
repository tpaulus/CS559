/***************************************************************************

  SIFDecoder.java

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
 * Reads image data in SIF format from a stream or a file.
 * Documentation for the {@link SIFEncoder} class gives further
 * details of this format.
 *
 * @author Nick Efford
 * @version 1.0 [1999/01/30]
 * @see SIFDecoderException
 * @see SIFEncoder
 * @see java.awt.image.BufferedImage
 */

public class SIFDecoder implements ImageDecoder, SIFConstants {


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  /** Stream used to read image data. */
  private DataInputStream input;

  /** Bytes identifying SIF image type. */
  private byte[] signature;

  /** Indicates whether data are compressed. */
  private boolean compression;

  /** Type code for BufferedImage. */
  private int type;

  /** Width of image, in pixels. */
  private int width;

  /** Height of image, in pixels. */
  private int height;


  ///////////////////////////// PUBLIC METHODS /////////////////////////////


  /**
   * Constructs a SIFDecoder associated with the standard input.
   * @exception IOException if bytes cannot be read from standard input.
   * @exception SIFDecoderException if the data available via standard
   *  input are not in SIF format.
   */

  public SIFDecoder() throws IOException, SIFDecoderException {
    this(System.in);
  }

  /**
   * Constructs a SIFDecoder from an existing InputStream.
   * @param in An InputStream object
   * @exception IOException if bytes cannot be read from the stream.
   * @exception SIFDecoderException if the data available on the stream
   *  are not in SIF format.
   */

  public SIFDecoder(InputStream in) throws IOException, SIFDecoderException {
    input = new DataInputStream(in);
    readHeader();
  }

  /**
   * Constructs a SIFDecoder that reads from a named file.
   * @param imgfile name of the file containing image data
   * @exception IOException if the file does not exist or data cannot
   *  be read from it.
   * @exception SIFDecoderException if the contents of the file are not
   *  in SIF format.
   */

  public SIFDecoder(String imgfile) throws IOException, SIFDecoderException {
    this(new FileInputStream(imgfile));
  }


  /**
   * Creates a String representing the signature (first four bytes)
   * of the data on the input stream.
   * @return String representing an image signature.
   */

  public String getSignatureString() {
    return new String(signature);
  }


  /**
   * @return width of the image to be decoded.
   */

  public int getWidth() {
    return width;
  }


  /**
   * @return height of the image to be decoded.
   */

  public int getHeight() {
    return height;
  }


  /**
   * @return number of pixels in the image to be decoded.
   */

  public int getNumPixels() {
    return width*height;
  }


  /**
   * @return code signifying image type - either
   *  <kbd>BufferedImage.TYPE_BYTE_GRAY</kbd>
   *  or <kbd>BufferedImage.TYPE_3BYTE_BGR</kbd>.
   */

  public int getType() {
    return type;
  }


  /**
   * Determines the number of bands in the image data available on
   * the input stream.  This will be 3 for colour images, 1 for
   * greyscale images or 0 otherwise.
   * @return number of bands.
   */

  public int getNumBands() {
    if (type == BufferedImage.TYPE_3BYTE_BGR)
      return 3;
    else if (type == BufferedImage.TYPE_BYTE_GRAY)
      return 1;
    else
      return 0;
  }


  /**
   * @return true if the image is an 8-bit greyscale image, false otherwise.
   */

  public boolean isGrey() {
    return type == BufferedImage.TYPE_BYTE_GRAY;
  }


  /**
   * @return true if the image is a 24-bit RGB image, false otherwise.
   */

  public boolean isRGB() {
    return type == BufferedImage.TYPE_3BYTE_BGR;
  }


  /**
   * @return true if image data are compressed, false otherwise.
   */

  public boolean isCompressed() {
    return compression;
  }


  /**
   * Reads image data from a stream.
   * @return a BufferedImage containing data read from the stream.
   * @exception IOException If there is an error while reading
   *  image data (e.g. premature end of file).
   * @see java.awt.image.BufferedImage
   */

  public BufferedImage decodeAsBufferedImage() throws IOException {
    BufferedImage img = new BufferedImage(width, height, type);
    DataBufferByte db = (DataBufferByte) img.getRaster().getDataBuffer();
    byte[] data = db.getData();
    if (compression) {
      InflaterInputStream inflater = new InflaterInputStream(input);
      int value, i = 0;
      while ((value = inflater.read()) != -1)
        data[i++] = (byte) value;
    }
    else 
      input.readFully(data);
    return img;
  }


  //////////////////////////// PRIVATE METHODS /////////////////////////////

  
  /**
   * Reads a SIF header consisting of image signature, width and height.
   * @exception IOException if data could not be read from the stream.
   * @exception SIFDecoderException if the header is invalid.
   */

  private void readHeader() throws IOException, SIFDecoderException {

    signature = new byte[4];
    input.readFully(signature);
    String sigString = getSignatureString();
    if (sigString.equals(GREY_SIG)
     || sigString.equals(GREY_COMPRESSED_SIG))
      type = BufferedImage.TYPE_BYTE_GRAY;
    else if (sigString.equals(RGB_SIG)
     || sigString.equals(RGB_COMPRESSED_SIG))
      type = BufferedImage.TYPE_3BYTE_BGR;
    else
      throw new SIFDecoderException("invalid data signature");

    if (Character.isLowerCase(sigString.charAt(0)))
      compression = true;
    else
      compression = false;

    width = input.readInt();
    height = input.readInt();

  }


}
