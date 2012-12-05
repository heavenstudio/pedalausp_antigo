/*
 * MainMenu.fx
 *
 * Created on Feb 8, 2010, 3:42:09 PM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import java.lang.System;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import br.com.lab360.pedalusp.totem.data.CyclistData;
import br.com.muggler.contactless.javafx.MessageDialog;


/**
 * @author User
 */
public class MainMenuView extends AbstractView {

    protected var controller: Controller;
    var cyclistData = CyclistData {};

            public var mainMessage = Label {
                text: "Digite a opção desejada:"
                translateY:100
                translateX:200
                    font: Font.font("Corbel",FontWeight.BOLD,30)
                    textFill: Color.web("#005AA9")
            };
            
	// Tela de erro
	var errorDialog: MessageDialog = MessageDialog{
		translateX:210;
		translateY:150;
		fadeDuration:3500ms;
	};
	function showMessageDialog(message :String){
	    errorDialog.message = message;
		errorDialog.visible=true;
		errorDialog.toFront();
	    errorDialog.fadeTransition.playFromStart();
	 }

    public var actionButton: Button = Button {
                width: 0;
                height: 0;
                onKeyReleased: function (ke: KeyEvent): Void {
                    timeoutView.stop();
                    if (ke.code == KeyCode.VK_1) {
                        //Solicita ao servidor a liberação
                        var resultado = cyclistData.verificaPermissaoCiclista();

                        //Caso alguem já tenha solicitado a bicicleta ao mesmo tempo
                        if(resultado.split(':')[0].equals('fail')){
                            timeoutView.stop();                                
                            showMessageDialog(resultado.split(':')[1]);
                        }else{
	                        controller.showSelectBikeView("");
                        }
                    }
                    if (ke.code == KeyCode.VK_2) {
                        controller.showMapView("mainmenu", Integer.valueOf(System.getProperty("station_number")));
                    }
                    if (ke.code == KeyCode.VK_3) {
                        controller.showCyclistData();
                    }
                    if (ke.code == KeyCode.VK_4) {
                        controller.showProblemView("mainmenu");
                    }
                    if (ke.code == KeyCode.VK_K) {
                        controller.showWelcomeView();
                    }
                }
            }


    var menu01 = ImageView {
                image: Image {url: "{__DIR__}img/bot_num1.jpg"}
                y: 170;
            };
    var menu01Text = Label {
                text: "Escolher uma bicicleta"
                translateY:180
                translateX:100
                    font: Font.font("Corbel",FontWeight.BOLD,30)
                    textFill: Color.web("#005AA9")
            };
    var menu02 = ImageView {
                image: Image {url: "{__DIR__}img/bot_num2.jpg"}
                y: 240;
            };
                var menu02Text = Label {
                text: "Ver mapa de estações"
                translateY:250
                translateX:100
                    font: Font.font("Corbel",FontWeight.BOLD,30)
                    textFill: Color.web("#005AA9")
            };
    var menu03 = ImageView {
                image: Image {url: "{__DIR__}img/bot_num3.jpg"}
                y: 310;
            };
                var menu03Text = Label {
                text: "Ver detalhes da minha conta"
                translateY:320
                translateX:100
                    font: Font.font("Corbel",FontWeight.BOLD,30)
                    textFill: Color.web("#005AA9")
            };
    var menu04 = ImageView {
                image: Image {url: "{__DIR__}img/bot_num4.jpg"}
                y: 380;
            };

                var menu04Text = Label {
                text: "Comunicar problema"
                translateY:390
                translateX:100
                    font: Font.font("Corbel",FontWeight.BOLD,30)
                    textFill: Color.web("#005AA9")
            };
   var sair = ImageView {
            image: Image {url: "{__DIR__}img/nav07.jpg"}
            y:512;
    };

    override protected function create(): Node {
        title.text = "Terceira Tela";
        errorDialog.visible=false;
        timeoutView.play();
        Group {
            content: [background, title, mainMessage, sair,
                Group {
                    translateX: 140
                    content: [actionButton, menu01, menu02, menu03, menu04,
                    menu01Text, menu02Text, menu03Text, menu04Text ]}, errorDialog]
        }

    }

    var timeoutView: Timeline = Timeline {
                repeatCount: 1
                keyFrames: [
                    KeyFrame {
                        time: 20s
                        action: function () {
                                controller.showWelcomeView();
                        }
                    }
                ]
            }
}
