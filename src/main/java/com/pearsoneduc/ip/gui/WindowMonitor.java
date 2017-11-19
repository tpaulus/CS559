/***************************************************************************

  WindowMonitor.java

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


import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * A class providing basic window event handling.  Typically it is
 * used as follows:
 *
 * <pre>
 *   public class MyApp extends JFrame {
 *     public MyApp() {
 *       ...
 *       addWindowListener(new WindowMonitor());
 *       ...
 *     }
 *     ...
 *   }
 * </pre>
 *
 * This code is adapted from an example given in <i>Java Swing</i>,
 * by Eckstein, Loy &amp; Wood (O'Reilly &amp; Associates, 1998).
 *
 * @author Nick Efford
 * @version 1.0 [1999/01/20]
 * @see java.awt.Window
 * @see java.awt.event.WindowEvent
 * @see java.awt.event.WindowAdapter
 */

public class WindowMonitor extends WindowAdapter {

  /**
   * Specifies behaviour when a window is closed.
   * The window is hidden and disposed of, then the program is terminated.
   * @param event a WindowEvent triggered when a window is closed
   */

  public void windowClosing(WindowEvent event) {
    Window window = event.getWindow();
    window.setVisible(false);
    window.dispose();
    System.exit(0);
  }

}
