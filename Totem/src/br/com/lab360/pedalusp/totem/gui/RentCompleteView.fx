/*
 * RentComplete.fx
 *
 * Created on Feb 8, 2010, 3:47:10 PM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.text.FontWeight;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;

/**
 * @author User
 */
public class RentCompleteView extends AbstractView {

    protected var controller: Controller;
    protected var protocolo: String;
    public var actionButton: Button = Button {
                width:0
                height:0
                translateX:800
                translateY:600
                onKeyReleased: function (ke: KeyEvent) {
                    if (ke.code == KeyCode.VK_K) {
                        timeoutView.stop();
                        controller.showWelcomeView();
                    }
                }
            }

   var mainMessage = Label {
                text: "Por favor, dirija-se à bicicleta escolhida\ne pressione o botão preto para retirá-la. \n\n Protocolo:{protocolo}"
                translateY: 80
                translateX: 130
                textAlignment:TextAlignment.CENTER;
                font: Font.font("Corbel", FontWeight.BOLD, 30)
                textFill: Color.web("#005AA9")
            };

    var timeIcon = ImageView {
            image: Image {url: "{__DIR__}img/picpeq_tempo.jpg"}
            x:375
            y:435;
    };
    var umMinuto = Label {
                translateX:155
                translateY:380
                text: "Você tem 1 minuto para retirar a bicicleta";
                font: Font.font("Corbel", FontWeight.BOLD, 25)
            }
    var timeoutView: Timeline = Timeline {
                repeatCount: 1
                keyFrames: [
                    KeyFrame {
                        time: 10s
                        action: function () {
                            controller.showWelcomeView();
                        }
                    }
                ]
            }
   var sair = ImageView {
            image: Image {url: "{__DIR__}img/nav07.jpg"}
            y:512;
    };
    override protected function create(): Node {
        title.text = "Aluguel completo";
        timeoutView.play();
        Group{
            content: [background, title, mainMessage, umMinuto, actionButton, timeIcon, sair]
        }
    }
}
