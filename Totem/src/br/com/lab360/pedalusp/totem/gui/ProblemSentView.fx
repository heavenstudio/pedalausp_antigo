/*
 * ProblemSentView.fx
 *
 * Created on Feb 28, 2010, 10:17:19 AM
 */

package br.com.lab360.pedalusp.totem.gui;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author User
 */

public class ProblemSentView extends AbstractView{
protected var controller: Controller;
public var backToView;
    public var actionButton: Button = Button {
                onKeyReleased: function (ke: KeyEvent) {
                    if (ke.code == KeyCode.VK_ENTER or ke.code == KeyCode.VK_M) {
                            timeoutView.stop();
                        controller.showWelcomeView();
                    }
                }
                }



                var voltar = ImageView {
                image: Image {url: "{__DIR__}img/nav04.jpg"}
                y: 512;
            };
                     var corretoIcon = ImageView {
                    image: Image {url: "{__DIR__}img/picgrd_certo.jpg"}
                    x:330
                    y:150;
            };
    var messageLabel = Label {
                width: 700
                height: 500
                translateX:50
                translateY:100
                textWrap: true
                text: "O problema foi comunicado com sucesso!";
                font: Font.font("Corbel",FontWeight.BOLD,30)
                textFill: Color.web("#005AA9")
                hpos: HPos.CENTER;
            }

    var messageLabel2 = Label {
                width: 700
                height: 500
                translateX:50
                translateY:150
                textWrap: true
                text: "Agradecemos sua ajuda"
                font: Font.font("Corbel",FontWeight.BOLD,30)
                textFill: Color.web("#005AA9")
                hpos: HPos.CENTER;
            }
      
    var timeoutView: Timeline = Timeline {
                repeatCount: 1
                keyFrames: [
                    KeyFrame {
                        time: 10s
                        action: function () {
                                if(backToView == "welcome"){
                                controller.showWelcomeView();
                            }
                            else if(backToView == "mainmenu"){
                                controller.showMainMenuView();
                            }

                        }
                    }
                ]
            }


    override protected function create(): Node {
        title.text = "Problema enviado";
        timeoutView.play();
        Group {
            content: [background, title, messageLabel, messageLabel2, corretoIcon, voltar]
        }
    }
}
