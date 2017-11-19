/***************************************************************************

  JPEGDecoder.java

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
import java.awt.image.BufferedImage;
import com.sun.image.codec.jpeg.*;



/**
 * Reads image data in JPEG format from a stream or a file.
 * This class is simply a wrapper for the decoder provided in the
 * <kbd>com.sun.image.codec.jpeg</kbd> package.
 *
 * @author Nick Efford
 * @version 1.0 [1999/06/28]
 * @see JPEGDecoderException
 * @see JPEGEncoder
 * @see java.awt.image.BufferedImage
 * @see com.sun.image.codec.jpeg.JPEGImageDecoder
 */

public class JPEGDecoder implements ImageDecoder {


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  /** Decodes JPEG-compressed image data. */
  private JPEGImageDecoder decoder;


  //////////////////////////////// METHODS /////////////////////////////////


  /**
   * Constructs a JPEGDecoder that reads from standard input.
   */

  public JPEGDecoder() {
    this(System.in);
  }

  /**
   * Constructs a JPEGDecoder that reads from the specified InputStream.
   * @param in the InputStream from which data will be read
   */

  public JPEGDecoder(InputStream in) {
    decoder = JPEGCodec.createJPEGDecoder(in);
  }

  /**
   * Constructs a JPEGDecoder that reads from a named file.
   * @param imgfile name of the file containing the image data
   * @exception FileNotFoundException if the file cannot be accessed.
   */

  public JPEGDecoder(String imgfile) throws FileNotFoundException {
    this(new FileInputStream(imgfile));
  }


  /**
   * Decodes the input data and creates an image.
   * @return a BufferedImage containing the data
   * @exception IOException if there was a problem reading the datastream.
   * @exception JPEGDecoderException if there were irregularities in
   *  the datastream.
   */

  public BufferedImage decodeAsBufferedImage()
   throws IOException, JPEGDecoderException {
    try {
      return decoder.decodeAsBufferedImage();
    }
    catch (ImageFormatException e) {   // catch and convert exception
      throw new JPEGDecoderException(e.getMessage());
    }
  }


}
