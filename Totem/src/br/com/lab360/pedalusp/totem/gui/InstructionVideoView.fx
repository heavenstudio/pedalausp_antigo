/*
 * Video.fx
 *
 * Created on Feb 8, 2010, 4:26:26 PM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.media.Media;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.lang.System;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

/**
 * @author User
 */
public class InstructionVideoView extends AbstractView {

    protected var controller: Controller;

    public var voltar = ImageView {
            image: Image {url: "{__DIR__}img/nav04.jpg"}
            y:512;
    };

    public var verNovamente = ImageView {
            image: Image {url: "{__DIR__}img/nav05.jpg"}
            x:500;
            y:512;
    };

    public var actionButton: Button = Button {
                width: 0
                height: 90
                translateX: 800
                translateY: 600
                onKeyReleased: function (ke: KeyEvent) {
                    if (ke.code == KeyCode.VK_K) {
                        mediaP.media = null;
                        mediaP.stop();
                        timeoutView.stop();
                        controller.showWelcomeView();
                    }else{
                        if (ke.code == KeyCode.VK_N) {
                            mediaP.stop();
                            mediaP.play();
                        }
                    }
                   
                }
            };
    def media = Media {source: System.getProperty("video_path")};
    public var mediaP = MediaPlayer {
                media: media

            };

    override protected function create(): Node {
            timeoutView.play();
        title.text = "Instrucao Video";
        Group {
            content: [background, title, actionButton, voltar, verNovamente,
                VBox {translateX: 140
                    translateY: 60
                    width:520
                    height:390
                    content: [MediaView {
                            mediaPlayer: mediaP
                        }]}]
        }
    }
     var timeoutView: Timeline = Timeline {
                repeatCount: 1
                keyFrames: [
                    KeyFrame {
                        time: 60s
                        action: function () {
                                controller.showWelcomeView();
                        }
                    }
                ]
            }
}
