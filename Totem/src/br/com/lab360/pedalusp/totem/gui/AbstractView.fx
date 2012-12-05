/*
 * AbstractView.fx
 *
 * Created on Feb 8, 2010, 3:37:49 PM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.CustomNode;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

/**
 * @author User
 */
public abstract class AbstractView extends CustomNode {

    /*
    def imgLogo = Image {url: "{__DIR__}img/LogoLab360.png"}
    public var logo = ImageView {
    smooth: true
    effect: Reflection {fraction: 0.3}
    image: imgLogo
    translateX: bind (scene.width - imgLogo.width) / 2
    translateY: 40
    };
     */
    def imgBackground = Image {url: "{__DIR__}img/fundo.jpg"}
    public var background = ImageView {image: imgBackground};
    public var title = Label {
                text: "Segunda Tela"
                translateX: 5
                width:0
                height:0
                translateY: 5
                font: Font.font("Arial", 10)
            };
}
