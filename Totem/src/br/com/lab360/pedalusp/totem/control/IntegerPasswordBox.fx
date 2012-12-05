/*
 * IntegerPasswordBox.fx
 *
 * Created on Feb 8, 2010, 4:11:43 PM
 */
package br.com.lab360.pedalusp.totem.control;

import javafx.scene.control.TextBox;
import javafx.util.Math;
import javafx.animation.Timeline;

/**
 * @author User
 */
public class IntegerPasswordBox extends TextBox {

    public-read var password = "";
    def pValidate = java.util.regex.Pattern.compile('^\\d*$');

    override function replaceSelection(arg) {

        def m = pValidate.matcher(arg);
        if (m.find()) {
            var pos1 = Math.min(dot, mark);
            var pos2 = Math.max(dot, mark);
            password = "{password.substring(0, pos1)}{arg}{password.substring(pos2)}";
            super.replaceSelection(getStars(arg.length()));
            //super.replaceSelection(arg);
        } else {
            Timeline {
                keyFrames: [
                    at(0s) { super.opacity => 1.0 }
                    at(200ms) { super.opacity => 0.0 }
                    at(500ms) { super.opacity => 1.0 }
                ]
            }.play();
        }

    }

    override function deleteNextChar() {
        if ((mark == dot) and (dot < password.length())) {
            password = "{password.substring(0, dot)}{password.substring(dot + 1)}";
        }
        super.deleteNextChar();
    }

    override function deletePreviousChar() {
        if ((mark == dot) and (dot > 0)) {
            password = "{password.substring(0, dot - 1)}{password.substring(dot)}";
        }
        super.deletePreviousChar();
    }

    function getStars(len: Integer): String {
        var result: String = "";
        for (i in [1..len]) {
            result = "{result}*";
        }
        result;
    }

    public function clear() {
        password = "";
    }
}
