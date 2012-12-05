package br.com.muggler.contactless.javafx;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.HPos;
import javafx.animation.transition.FadeTransition;
import javafx.animation.Interpolator;










/**
 * A dialog whose default behavior is to transition in, display 
 * a message, wait for a set amount of time, and then transition out.
 * 
 * @author muggler
 */
 public class SmartcardErrorDialog extends CustomNode {
     
     public var fadeDuration = 5s;
     
 	 def windowTitle = ##"SmartCardErrorDialog.title";
 	
 	 def imgBackground = Image {url: "{__DIR__}fundo.jpg"}
 	 
     def background = ImageView {
     	image: imgBackground
     	fitWidth: 400;	
     	fitHeight: 200;
     };
     
     def imgError = Image {url: "{__DIR__}icon_error_red_64x64.png"}
      	 
     def errorView = ImageView {
     	image: imgError;
     	translateX:55
     	translateY:75
     	fitWidth: 32;	
     	fitHeight: 32;
     };
     
     def title = Label {
                 text: windowTitle;
                 translateX: 5
                 width:0
                 height:0
                 translateY: 5
                 font: Font.font("Arial", 10)
             };
                 
	 def mainMessage = Label {
	                //text: ##"SmartcardErrorDialog.readError";
	                text: "Erro de Leitura!";
	                translateX:105
	                translateY:75
	                graphicHPos:HPos.CENTER
	                    font: Font.font("Corbel",FontWeight.BOLD,30)
	                    textFill: Color.web("#EA1111")
	            };

	            public var fadeTransition = FadeTransition {
	                    duration: bind fadeDuration node: this 
	                    fromValue: 1.0 toValue: 0.0
	                    repeatCount:1 autoReverse: false
	                    interpolator: Interpolator.DISCRETE;
	                }
	            
    override function create(): Node {
            title.text = windowTitle;
             Group {
                content: bind [background, errorView, mainMessage, title]
            };
            
    };
    
    
    
}