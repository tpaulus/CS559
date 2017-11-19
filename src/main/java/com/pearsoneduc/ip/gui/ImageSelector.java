/***************************************************************************

  ImageSelector.java

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


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import com.pearsoneduc.ip.io.*;
import com.pearsoneduc.ip.op.OperationException;


/**
 * Provides a GUI framework for visualising a set of images derived
 * from a single source image.
 *
 * <p>The input image is read from a file, checked and then processed
 * to generate a set of output images, which are stored in a hashtable.
 * A frame is then created containing a view of the first image that was
 * generated, together with a combobox that can be used to switch the view
 * to other images.</p>
 *
 * <p>This is an abstract class, and must be extended to be used.  Derived
 * classes should supply definitions for the methods
 * {@link #imageOK() imageOK}, which checks whether the input image is
 * suitable for processing, and {@link #generateImages() generateImages},
 * which produces the images that are to be visualised.  The latter must
 * add each image to the hashtable as an ImageIcon, using the method
 * {@link addImage}.  It must also return a Vector containing all the keys
 * that are required to access images in the hashtable.</p>
 *
 * @author Nick Efford
 * @version 1.1 [1999/06/29]
 */

public abstract class ImageSelector
 extends JFrame implements ActionListener {


  /////////////////////////// INSTANCE VARIABLES ///////////////////////////


  /** Original image read from file. */
  private BufferedImage sourceImage;

  /** Images derived from the original. */
  private Hashtable images;

  /** Swing component used for image display. */
  private JLabel view;

  /** Swing component used to select an image. */
  private JComboBox selector;


  ///////////////////////////////// METHODS ////////////////////////////////


  /**
   * Constructs an ImageSelector using an image from a file.
   * @param imageFile name of the file containing the image
   * @exception IOException if the file cannot be accessed or there is
   *  some other problem with reading from it.
   * @exception ImageDecoderException if there is a problem with the
   *  format of the data in the file.
   * @exception OperationException if the image is otherwise unsuitable
   *  for processing.
   */

  public ImageSelector(String imageFile)
   throws IOException, ImageDecoderException, OperationException {

    super(imageFile);

    // Load image and test its suitability

    readImage(imageFile);
    if (!imageOK())
      throw new OperationException("invalid input image");

    // Create processed versions of the image

    images = new Hashtable();
    Vector choices = generateImages();

    // Get the first image and create a component to display it

    ImageIcon image = (ImageIcon) images.get((String) choices.firstElement());
    view = new JLabel(image);

    // Create a component to switch between the different images

    selector = new JComboBox(choices);
    selector.addActionListener(this);
    JPanel selectorPane = new JPanel();
    selectorPane.add(selector);

    // Add components to the frame

    Container pane = getContentPane();
    pane.add(view, BorderLayout.CENTER);
    pane.add(selectorPane, BorderLayout.SOUTH);
    addWindowListener(new WindowMonitor());

  }


  /**
   * Reads the source image from a file.
   * @param filename name of image file
   * @exception IOException if the file cannot be accessed or there
   *  is some other problem with reading from it.
   * @exception ImageDecoderException if there is a problem with
   *  the format of the data in the file.
   */

  public void readImage(String filename)
   throws IOException, ImageDecoderException {
    ImageDecoder input = ImageFile.createImageDecoder(filename);
    sourceImage = input.decodeAsBufferedImage();
  }


  /**
   * Checks that the source image is suitable for processing.
   * @return true if the image is suitable, false otherwise.
   */

  public abstract boolean imageOK();


  /**
   * Generates processed versions of the source image and stores them
   * in a hashtable.
   * @return a Vector containing strings that can be used as keys to
   *  access the images.
   */

  public abstract Vector generateImages();


  /**
   * @return a handle for the source image.
   */

  public BufferedImage getSourceImage() {
    return sourceImage;
  }


  /**
   * Adds an image (represented as an ImageIcon) to the hashtable.
   * @param key String used as a key to retrieve the image from the
   *  hashtable
   * @param image the ImageIcon to be added to the hashtable
   */

  public void addImage(String key, ImageIcon image) {
    images.put(key, image);
  }


  /**
   * Selects an image to display.
   * @param event ActionEvent generated by choosing an item from
   *  the combobox
   */

  public void actionPerformed(ActionEvent event) {
    Object key = ((JComboBox) event.getSource()).getSelectedItem();
    ImageIcon image = (ImageIcon) images.get(key);
    view.setIcon(image);
  }


}
