/***************************************************************************

  ImageFile.java

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



import java.io.IOException;



/**
 * Defines static factory methods that create image encoders and
 * decoders for files.
 *
 * <p>Each encoder implements the {@link ImageEncoder} interface and
 * therefore provides a method named
 * {@link ImageEncoder#encode(BufferedImage) encode} which can be used
 * to write the image to a file.  Similarly, each decoder implements
 * the {@link ImageDecoder} interface and therefore provides a method
 * {@link ImageDecoder#decodeAsBufferedImage() decodeAsBufferedImage}
 * which can be used to read data from a file.</p>
 *
 * <p>Appropriate encoders / decoders are created by examining the suffix
 * of the filename supplied to the factory methods.  For example, if the
 * filename ends with <kbd>.jpg</kbd>, a {@link JPEGEncoder} will be
 * created to write the file, or a {@link JPEGDecoder} to read it.</p>
 *
 * @author Nick Efford
 * @version 1.0 [1999/06/28]
 * @see ImageEncoder
 * @see ImageDecoder
 * @see ImageEncoderException
 * @see ImageDecoderException
 */

public class ImageFile {


  /**
   * Creates an ImageEncoder suitable for use with the specified filename.
   * @param filename name of the image file
   * @return new instance of a suitable ImageEncoder.
   * @exception IOException if there was some problem with output
   * @exception ImageEncoderException if the image type is unsupported by
   *  the format or could not be encoded in that format.
   */

  public static ImageEncoder createImageEncoder(String filename)
   throws IOException, ImageEncoderException {

    if (filename.endsWith(".pbm")
     || filename.endsWith(".pgm")
     || filename.endsWith(".ppm"))
      return new PPMEncoder(filename);
    else if (filename.endsWith(".sif"))
      return new SIFEncoder(filename);
    else if (filename.endsWith(".png"))
      return new PNGEncoder(filename);
    else if (filename.endsWith(".jpg")
     || filename.endsWith(".jpeg"))
      return new JPEGEncoder(filename);
    else
      throw new ImageEncoderException("cannot determine file format");

  }


  /**
   * Creates an ImageDecoder suitable for use with the specified filename.
   * @param filename name of the image file
   * @return new instance of a suitable ImageDecoder.
   * @exception IOException if there was some problem with reading data
   * @exception ImageDecoderException if the file does not appear to
   *  follow the expected format.
   */

  public static ImageDecoder createImageDecoder(String filename)
   throws IOException, ImageDecoderException {

    if (filename.endsWith(".pbm")
     || filename.endsWith(".pgm")
     || filename.endsWith(".ppm"))
      return new PPMDecoder(filename);
    else if (filename.endsWith(".sif"))
      return new SIFDecoder(filename);
    else if (filename.endsWith(".png"))
      return new PNGDecoder(filename);
    else if (filename.endsWith(".jpg")
     || filename.endsWith(".jpeg"))
      return new JPEGDecoder(filename);
    else
      throw new ImageDecoderException("cannot determine file format");

  }


}
