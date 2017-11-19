/***************************************************************************

  OperationViewer.java

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


package com.pearsoneduc.ip.gui;


import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import com.pearsoneduc.ip.io.*;
import com.pearsoneduc.ip.op.OperationException;


/**
 * Provides a GUI framework for simple, non-interactive image
 * processing operations in which one output image is generated
 * from a single input image.
 *
 * <p>The input image is read from a file, checked and processed to
 * produce the output image.  A frame is then created, containing
 * a tabbed pane which displays the input and output images.</p>
 *
 * <p>This is an abstract class, and must be extended to be used.
 * The input and output images can be accessed within derived classes
 * as {@link #inputImage <kbd>inputImage</kbd>} and
 * {@link #outputImage <kbd>outputImage</kbd>}, respectively.
 * Derived classes must supply definitions for the method
 * {@link #imageOK() imageOK}, which checks whether the input image is
 * suitable for processing, and the method
 * {@link #processImage() processImage}, which carries out
 * the processing.</p>
 *
 * @author Nick Efford
 * @version 1.0 [1999/06/21]
 * @see ImageView
 * @see java.awt.image.BufferedImage
 */

public abstract class OperationViewer extends JFrame {


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  protected BufferedImage inputImage;

  protected BufferedImage outputImage;

  /** Component displaying the input image. */
  private ImageView inputView;

  /** Component displaying the output image. */
  private ImageView outputView;


  //////////////////////////////// METHODS /////////////////////////////////


  /**
   * Constructs an OperationViewer using the image in the named file.
   * @param imageFile name of file containing the input image
   * @exception IOException if the file cannot be accessed or data
   *  cannot not be read from it.
   * @exception ImageDecoderException if there is a problem with
   *  the format of the input data.
   * @exception OperationException if the image is unsuitable for
   *  processing.
   */

  public OperationViewer(String imageFile)
   throws IOException, ImageDecoderException, OperationException {
    readImage(imageFile);
    if (!imageOK())
      throw new OperationException("image is unsuitable for operation");
    processImage();
    inputView = new ImageView(inputImage);
    outputView = new ImageView(outputImage);
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.add(new JScrollPane(inputView), "input");
    tabbedPane.add(new JScrollPane(outputView), "output");
    getContentPane().add(tabbedPane);
    addWindowListener(new WindowMonitor());
  }


  /**
   * Reads an image from a named file.
   * @param filename name of the image file
   * @exception IOException if there was a problem accessing or reading
   *  from the file.
   * @exception ImageDecoderException if there was a problem with the
   *  format of the input data.
   */

  public void readImage(String filename)
   throws IOException, ImageDecoderException {
    ImageDecoder input = ImageFile.createImageDecoder(filename);
    inputImage = input.decodeAsBufferedImage();
  }


  /**
   * Checks whether the input image is suitable for processing.
   * @return true if the input image is suitable, false otherwise.
   */

  public abstract boolean imageOK();


  /**
   * Processes the input image and generates the output image.
   */

  public abstract void processImage();


}
