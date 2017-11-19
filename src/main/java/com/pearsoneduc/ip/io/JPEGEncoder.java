/***************************************************************************

  JPEGEncoder.java

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
 * Writes image data to a stream or file encoded in the JPEG format.
 * This class is merely a wrapper for the JPEGImageEncoder class
 * in the <kbd>com.sun.image.codec.jpeg</kbd> package.
 *
 * @author Nick Efford
 * @version 1.0 [1999/06/28]
 * @see JPEGEncoderException
 * @see JPEGDecoder
 * @see java.awt.image.BufferedImage
 * @see com.sun.image.codec.jpeg.JPEGImageEncoder
 */

public class JPEGEncoder implements ImageEncoder {


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  private JPEGImageEncoder encoder;


  //////////////////////////////// METHODS /////////////////////////////////


  /**
   * Constructs a JPEGEncoder that writes to standard output.
   */

  public JPEGEncoder() {
    this(System.out);
  }

  /**
   * Constructors a JPEGEncoder that writes to the specified OutputStream.
   * @param out stream to which JPEG-compressed data will be written
   */

  public JPEGEncoder(OutputStream out) {
    encoder = JPEGCodec.createJPEGEncoder(out);
  }

  /**
   * Constructs a JPEGEncoder that writes to a named file.
   * @param imgfile name of the image file
   * @exception FileNotFoundException if the file cannot be accessed.
   */

  public JPEGEncoder(String imgfile) throws FileNotFoundException {
    this(new FileOutputStream(imgfile));
  }


  /**
   * Encodes the specified image in JPEG File Interchange Format.
   * @param image the image to be encoded
   * @exception IOException if data could not be written to the stream.
   * @exception JPEGEncoderException if the image cannot be written in
   *  this format.
   */

  public void encode(BufferedImage image)
   throws IOException, JPEGEncoderException {
    try {
      encoder.encode(image);
    }
    catch (ImageFormatException e) {
      throw new JPEGEncoderException(e.getMessage());
    }
  }


}
