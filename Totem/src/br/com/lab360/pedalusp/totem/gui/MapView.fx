/*
 * MapView.fx
 *
 * Created on Feb 12, 2010, 9:55:29 AM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import br.com.lab360.pedalusp.totem.gui.img.mapa_completoUI;
import javafx.scene.image.ImageView;
import javafx.fxd.FXDNode;
import br.com.lab360.pedalusp.totem.data.StationsData;
import javafx.scene.text.TextAlignment;
import br.com.lab360.pedalusp.totem.gui.img.mapa_estacaolotadaUI;
import br.com.lab360.pedalusp.totem.gui.img.mapa_estacaonormalUI;
import br.com.lab360.pedalusp.totem.gui.img.mapa_estacaovaziaUI;
import java.lang.System;

/**
 * @author User
 */
public class MapView extends AbstractView {

    protected var controller: Controller;
    public var backToView;
    public var stationNumber: Integer;
    public var usageList: FXDNode[];
    public var usageListLbl: Label[];
    var numberOfStations: Integer;
    var data: Node[];
    var mapsList: Node[];
    var marginX = 150;
    var marginY = 50;
    var esquerda = ImageView {
                image: Image {url: "{__DIR__}img/bot_esq.jpg"}
                y: 50;
                x: 10;
            };
    var direita = ImageView {
                image: Image {url: "{__DIR__}img/bot_dir.jpg"}
                y: 50;
                x: 430
            };
    var bicicletaIcon = ImageView {
                image: Image {url: "{__DIR__}img/picpeq_bike.jpg"}
                x: 585
                y: 80;
            };
    var bicicletasDisponiveis = Label {
                text: "Bicicletas Disponíveis"
                translateY: 95
                translateX: 635
                textAlignment: TextAlignment.CENTER
                font: Font.font("Corbel", FontWeight.BOLD, 15)
                textFill: Color.web("#005AA9")
            };
    var baiaIcon = ImageView {
                image: Image {url: "{__DIR__}img/picpeq_baia.jpg"}
                x: 585
                y: 30;
            };
    var baiasVazias = Label {
                text: "Baias Vazias"
                translateY: 40
                translateX: 635
                textAlignment: TextAlignment.CENTER
                font: Font.font("Corbel", FontWeight.BOLD, 15)
                textFill: Color.web("#005AA9")
            };
    var voltar = ImageView {
                image: Image {url: "{__DIR__}img/nav04.jpg"}
                y: 512;
            };
    var mapaCompleto = ImageView {
                image: Image {url: "{__DIR__}img/nav08.jpg"}
                x: 500;
                y: 512;
            };
    public var backBtn: Button = Button {
                width: 0
                height: 0
                translateX: 800
                translateY: 600
                onKeyReleased: function (ke: KeyEvent) {
                    if (ke.code == KeyCode.VK_K) {
                        if (backToView == "welcome") {
                            controller.showWelcomeView();
                            }
                        if (backToView == "mainmenu") {
                            controller.showMainMenuView();
                            }
                    }
                    if (ke.code == KeyCode.VK_L) {
                        controller.showMapCompleteView(String.valueOf(backToView));
                    }
                    if (ke.code == KeyCode.VK_N) {
                        if (stationNumber < numberOfStations) {
                            controller.showMapView(String.valueOf(backToView), stationNumber + 1);
                        }
                    }
                    if (ke.code == KeyCode.VK_M) {
                        if (stationNumber > 1) {
                            controller.showMapView(String.valueOf(backToView), stationNumber - 1);
                        }
                    }


                };
            }
    var mapa: Node[];
    var backgroundRet = ImageView {image: Image {url: "{__DIR__}img/fundo.png"}};

    override protected function create(): Node {
        title.text = "Mapas";
        renderStations();
        Group {
            content: bind [mapa, usageList, usageListLbl, backgroundRet, title, backBtn, data,
                esquerda, direita, bicicletaIcon, bicicletasDisponiveis,
                baiaIcon, baiasVazias, voltar, mapaCompleto]
        }

    }

    public function renderStations() {
        java.lang.System.out.println("StationNumber");
        java.lang.System.out.println(stationNumber);
        var sd = StationsData {};
        var stations = sd.getStations();
        var tamBolX = 15 * 4 - 71;
        var tamBolY = 35.3 * 4 - 40.89;

        var posicaoXCentro = (-315) * 3 + (stations.stations.get(stationNumber - 1).mapX) * 4 + tamBolX + marginX;
        var posicaoYCentro = (-217.5) * 3 + (stations.stations.get(stationNumber - 1).mapY) * 4 + tamBolY + marginY;

        var centerTransX = 315 - posicaoXCentro;
        var centerTransY = 217.5 - posicaoYCentro + 70;
        var map = ImageView {
                image: Image {url: "{__DIR__}img/mapa.jpg"}
                    translateX: marginX + centerTransX -948
                    translateY: marginY + centerTransY - 645
                };
        //java.lang.System.out.println(posicaoXCentro);
        //java.lang.System.out.println(posicaoYCentro);
        //var centerTransX =(315*4)-posicaoXCentro;
        //var centerTransY =(217.5*4)- posicaoYCentro;


        insert map into mapa;
        var nomeEstacao = Label {
                    text: "{String.valueOf(stations.stations.get(stationNumber-1).stationNumber)} - {stations.stations.get(stationNumber-1).name}"
                    translateY: 60
                    translateX: 120
                    width: 400
                    textAlignment: TextAlignment.CENTER
                    font: Font.font("Corbel", FontWeight.BOLD, 25)
                    textFill: Color.web("#005AA9")
                };

        insert nomeEstacao into data;

        var stationBikes = Label {
                    text: stations.stations.get(stationNumber - 1).numBikes.toString();
                    translateX: 545;
                    translateY: 85;
                    font: Font.font("Courier New", FontWeight.BOLD, 30)
                    textFill: Color.web("#005AA9")
                }
        var stationFreeSlots = Label {
                    text: stations.stations.get(stationNumber - 1).numFreeSlot.toString();
                    translateX: 545
                    translateY: 40;
                    font: Font.font("Courier New", FontWeight.BOLD, 30)
                    textFill: Color.web("#005AA9")
                }
        insert stationFreeSlots into data;
        insert stationBikes into data;
        numberOfStations = stations.stations.size();

        for (i in [0..stations.stations.size() - 1]) {
            //imprime na tela os pon
            var pointImg: FXDNode;
            var stationNum: Label;
            var stationBikesNum: Label;
            var stationFreeSlotsNum: Label;

            var deslocX = (-315) * 3 + (stations.stations.get(i).mapX) * 4 + tamBolX + marginX + centerTransX;
            var deslocY = (-217.5) * 3 + (stations.stations.get(i).mapY) * 4 + tamBolY + marginY + centerTransY;

            if (stations.stations.get(i).usage.equals("lotada")) {
                pointImg = mapa_estacaolotadaUI {
                    translateX: deslocX;
                    translateY: deslocY;
                };
                java.lang.System.out.println(pointImg.boundsInLocal.width);
                java.lang.System.out.println(pointImg.boundsInLocal.height);

            }
            if (stations.stations.get(i).usage.equals("vazia")) {
                pointImg = mapa_estacaovaziaUI {
                    translateX: deslocX;
                    translateY: deslocY;
                };
            }
            if (stations.stations.get(i).usage.equals("normal")) {
                pointImg = mapa_estacaonormalUI {
                    translateX: deslocX;
                    translateY: deslocY;
                };
            }
            insert pointImg into usageList;

            stationNum = Label {
                text: stations.stations.get(i).stationNumber.toString();
                translateX: deslocX + 56 + 10;
                translateY: deslocY + 5;
                font: Font.font("Courier New", FontWeight.BOLD, 20)
                textFill: Color.WHITE
            }
            stationBikesNum = Label {
                text: stations.stations.get(i).numBikes.toString();
                translateX: deslocX + 117;
                translateY: deslocY + 5;
                font: Font.font("Courier New", FontWeight.BOLD, 20)
                textFill: Color.BLACK
            }
            stationFreeSlotsNum = Label {
                text: stations.stations.get(i).numFreeSlot.toString();
                translateX: deslocX + 5;
                translateY: deslocY + 5;
                font: Font.font("Courier New", FontWeight.BOLD, 20)
                textFill: Color.BLACK
            }

            insert stationNum into usageListLbl;
            insert stationBikesNum into usageListLbl;
            insert stationFreeSlotsNum into usageListLbl;


        }
    }
}
