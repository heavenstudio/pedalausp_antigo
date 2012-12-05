/*
 * MapCompleteView.fx
 *
 * Created on Mar 16, 2010, 7:14:13 PM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import br.com.lab360.pedalusp.totem.gui.img.mapa_estacaolotada_peqUI;
import br.com.lab360.pedalusp.totem.data.StationsData;
import br.com.lab360.pedalusp.totem.gui.img.mapa_estacaonormal_peqUI;
import br.com.lab360.pedalusp.totem.gui.img.mapa_estacaovazia_peqUI;
import javafx.fxd.FXDNode;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import br.com.lab360.pedalusp.totem.control.IntegerBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

/**
 * @author User
 */
public class MapCompleteView extends AbstractView {

    protected var controller: Controller;
    public var backToView;
    public var usageList: FXDNode[];
    public var usageListLbl: Label[];
    public var oneImg: FXDNode;
    public var oneImg2: FXDNode;
    public var oneImg3: FXDNode;
    var marginX = 150;
    var marginY = 50;
    var numberOfStations = 0;
    var count = 0;
    public var selectedStation: IntegerBox =
            IntegerBox {
                columns: 2
                width: 100
                height: 50
                translateX: 30
                translateY: 100
                selectOnFocus: false
                font: Font.font("Courier New", FontWeight.BOLD, 50)
                onKeyReleased: function (ke: KeyEvent) {
                    if (ke.code == KeyCode.VK_0 or ke.code == KeyCode.VK_1
                            or ke.code == KeyCode.VK_2 or ke.code == KeyCode.VK_3
                            or ke.code == KeyCode.VK_4 or ke.code == KeyCode.VK_5
                            or ke.code == KeyCode.VK_6 or ke.code == KeyCode.VK_7
                            or ke.code == KeyCode.VK_8 or ke.code == KeyCode.VK_9) {
                        count++;
                        if (count == 2) {
                            sendBtn.requestFocus();
                            sendBtn.fire();
                        }
                    }

                    //Limpar textbox
                    if (ke.code == KeyCode.VK_ESCAPE) {
                        clearBtn.requestFocus();
                        clearBtn.fire();
                    }

                    //Selecionar mapa
                    if (ke.code == KeyCode.VK_ENTER) {
                        sendBtn.requestFocus();
                        sendBtn.fire();
                    }
                    //Voltar para tela anterior
                    if (ke.code == KeyCode.VK_K) {
                        clear();
                        timeoutView.stop();
                        if (backToView == "welcome") {
                            controller.showWelcomeView();
                            }
                        if (backToView == "mainmenu") {
                            controller.showMainMenuView();
                            }

                    }

                };
            }
    var sendBtn: Button = Button {
                translateX: 800
                translateY: 600
                action: function () {
                    //Verifica se a estação existe
                    if (Integer.parseInt(selectedStation.rawText) <= numberOfStations) {
                        timeoutView.stop();
                        controller.showMapView(String.valueOf(backToView), Integer.parseInt(selectedStation.rawText));
                    } else {
                        
                        clear();
                    }
                }
            }

    public function clear() {
        count = 0;
        selectedStation.text = "";
        selectedStation.requestFocus();
    }
    var clearBtn = Button {
                translateX: 800
                translateY: 600
                action: function () {
                    clear();
                }
            }
    //  var map = mapa_completoUI {
    //            translateX: marginX
    //          translateY: marginY};
    var map = ImageView {
                image: Image {url: "{__DIR__}img/mapa_peq.jpg"}
                translateX: marginX
              translateY: marginY
            };
    public var mainMessage = Label {
                text: "Digite o número da estação:"
                translateY: 50
                translateX: 25
                font: Font.font("Corbel", FontWeight.BOLD, 30)
                textFill: Color.web("#005AA9")
            };
    var voltar = ImageView {
                image: Image {url: "{__DIR__}img/nav04.jpg"}
                y: 512;
            };
    var confirmar = ImageView {
                image: Image {url: "{__DIR__}img/nav06.jpg"}
                x: 500;
                y: 512;
            };
    var legenda = ImageView {
                image: Image {url: "{__DIR__}img/leg_estacao.jpg"}
                x: 140;
                y: 95;
            };

    override protected function create(): Node {
        timeoutView.play();
        render();
        title.text = "Mapas";
        Group {
            content: bind [background, selectedStation, map, usageList, usageListLbl, mainMessage,
                voltar, confirmar, legenda, clearBtn, sendBtn]
        }
    }

    public function render() {
        var sd = StationsData {};
        var stations = sd.getStations();
        java.lang.System.out.println("TAMANHO");
        numberOfStations = stations.stations.size();
        for (i in [0..stations.stations.size() - 1]) {
            //imprime na tela os pon
            var pointImg: FXDNode;
            var stationNum: Label;
            if (stations.stations.get(i).usage.equals("lotada")) {
                pointImg = mapa_estacaolotada_peqUI {
                    translateX: stations.stations.get(i).mapX + marginX;
                    translateY: stations.stations.get(i).mapY + marginY;
                };
                java.lang.System.out.println("tamanho da bolinha");
                java.lang.System.out.println(pointImg.boundsInLocal.width);
                java.lang.System.out.println(pointImg.boundsInLocal.height);

            }
            if (stations.stations.get(i).usage.equals("vazia")) {
                pointImg = mapa_estacaovazia_peqUI {
                    translateX: stations.stations.get(i).mapX + marginX;
                    translateY: stations.stations.get(i).mapY + marginY;
                };
            }
            if (stations.stations.get(i).usage.equals("normal")) {
                pointImg = mapa_estacaonormal_peqUI {
                    translateX: stations.stations.get(i).mapX + marginX;
                    translateY: stations.stations.get(i).mapY + marginY;
                };
            }
            insert pointImg into usageList;

            stationNum = Label {
                text: stations.stations.get(i).stationNumber.toString();
                translateX: stations.stations.get(i).mapX + marginX + 10;
                translateY: stations.stations.get(i).mapY + marginY + 5;
                font: Font.font("Courier New", FontWeight.BOLD, 20)
                textFill: Color.WHITE
            }
            insert stationNum into usageListLbl;

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
