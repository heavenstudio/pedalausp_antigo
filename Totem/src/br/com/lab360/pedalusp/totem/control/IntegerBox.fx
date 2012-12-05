/*
 * IntegerBox.fx
 *
 * Created on Feb 4, 2010, 7:18:09 PM
 */

package br.com.lab360.pedalusp.totem.control;

import javafx.scene.control.TextBox;
import javafx.animation.Timeline;

public class IntegerBox extends TextBox
{
  def pValidate = java.util.regex.Pattern.compile('^\\d*$');

  override function replaceSelection(repl)
  {
    def m = pValidate.matcher(repl);
    if (m.find())
    {
      super.replaceSelection(repl);
    }
    else
    {
      Timeline
      {
        keyFrames:
        [
          at(0s) { super.opacity => 1.0 }
          at(200ms) { super.opacity => 0.0 }
          at(500ms) { super.opacity => 1.0 }
        ]
      }.play();
    }
  }
}
