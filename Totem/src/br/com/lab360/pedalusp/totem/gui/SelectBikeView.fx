/*
 * SelectBike.fx
 *
 * Created on Feb 8, 2010, 3:43:20 PM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import br.com.lab360.pedalusp.totem.control.IntegerBox;
import br.com.lab360.pedalusp.totem.model.Station;
import br.com.lab360.pedalusp.totem.data.StationData;
import java.lang.System;
import br.com.lab360.pedalusp.totem.data.SlotData;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.FontWeight;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import com.sun.javafx.scene.control.caspian.ButtonSkin;

/**
 * @author User
 */
public class SelectBikeView extends AbstractView {

    protected var controller: Controller;
    protected var mensagem: String;
    var localStation: Station = controller.estacao;
    var stationData = StationData {};
    var slotData = SlotData {};
    var bikesListImage: ImageView[];
    var bikesListNumber: Label[];
    var control: Integer = 0;
    var numBlock = 1; //Bloco ativo na tela na posição esquerda (cada bloco é composto de 2 bikes)
    var justOne = "none"; //Variavel que evita que executa dois movimentos (direita/esquerda) ao mesmo tempo
    var bikeListBox: HBox = HBox {translateY: 130; translateX: 120 content: bind [bikesListImage]};
    var bikeListNumberBox: Group = Group {translateY: 262; translateX: 57 content: bind [bikesListNumber]
            };
    var mainMessage = Label {
                text: "Digite o número da bicicleta que deseja liberar\n e pressione ENTRA:"
                translateY: 50
                translateX: 80
                graphicHPos: HPos.CENTER
                font: Font.font("Corbel", FontWeight.BOLD, 24)
                textFill: Color.web("#005AA9")
            };
    var secondMessage = Label {
                text: mensagem
                translateY: 90
                translateX: 80
                graphicHPos: HPos.CENTER
                font: Font.font("Corbel", FontWeight.BOLD, 20)
                textFill: Color.RED
            };
    var legenda = ImageView {
                image: Image {url: "{__DIR__}img/leg_bike.jpg"}
                y: 130;
                x: 450;
            };
    var esquerda = ImageView {
                image: Image {url: "{__DIR__}img/bot_esq.jpg"}
                y: 330;
                x: 5;
            };
    var direita = ImageView {
                image: Image {url: "{__DIR__}img/bot_dir.jpg"}
                y: 330;
                x: 700
            };
    var leftBack = Rectangle {
                width: 120
                height: 360
                fill: Color.WHITE;
                y: 120
                x: 0;
            };
    var rightBack = Rectangle {
                width: 120
                height: 360
                fill: Color.WHITE;
                x: 700
                y: 120
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
    var messageAlert = Text {
                translateX: 100
                translateY: 100
                wrappingWidth: 100
            };
    //Move para esquerda (avança a lista de bikes)
    var timeline = Timeline {
                repeatCount: 22
                keyFrames: [
                    KeyFrame {
                        time: 30ms
                        action: function () {
                            //verifica se não chegou ao tamanho máximo
                            //if (bikeListBox.translateX >= ((localStation.numberSlots / 4) - 2) * (-400)) {
                            if (bikeListBox.translateX >= (((localStation.numberSlots / 2) - 1) * -220)) {
                                //verifica se não chegou ao fim da movimentação

                                bikeListBox.translateX = bikeListBox.translateX - 10;
                                bikeListNumberBox.translateX = bikeListNumberBox.translateX -10;
                                if (bikeListBox.translateX == numBlock * (-220) + 120) {
                                    justOne = "none";
                                    numBlock++;
                                    }
                                }
                        }
                    }
                ]
            }
    //move para direita (volta para o inicio da lista de bikes)
    var backTimeline = Timeline {
                repeatCount: 22
                keyFrames: [
                    KeyFrame {
                        time: 30ms
                        action: function () {

                            if (bikeListBox.translateX < 120) {
                                bikeListBox.translateX = bikeListBox.translateX + 10;
                                bikeListNumberBox.translateX = bikeListNumberBox.translateX +10;
                                if (bikeListBox.translateX == (numBlock - 2) * (-220) + 120) {
                                    justOne = "none";
                                    numBlock--;
                                }

                            }
                        }
                    }
                ]
            }
    var count: Integer;
    public var selectedBike: IntegerBox =
            IntegerBox {
                columns: 2
                width: 100
                height: 50
                translateX: 345
                translateY: 140
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

                    //Selecionar bicicleta
                    if (ke.code == KeyCode.VK_ENTER) {
                        sendBtn.requestFocus();
                        sendBtn.fire();
                    }

                    //Avancar lista
                    if (ke.code == KeyCode.VK_N) {
                        if (justOne.equals("none")) {
                            //120 margem a direita
                            if (bikeListBox.translateX > (((localStation.numberSlots / 2) - 1) * -220 + 120)) {
                                justOne = "front";
                                timeline.play();
                            }
                        }
                    }
                    //Voltar lista de bicicletas
                    if (ke.code == KeyCode.VK_M) {

                        if (justOne.equals("none")) {
                            if (bikeListBox.translateX < 120) {
                                justOne = "back";
                                backTimeline.play();
                            }
                        }
                    }
                    //Voltar para tela anterior
                    if (ke.code == KeyCode.VK_K) {
                        clear();
                        timeoutView.stop();
                        controller.showMainMenuView();
                    }

                };
            }
    var sendBtn: Button = Button {
                translateX: 800
                translateY: 600
                action: function () {
                    //Verifica se possui bicicleta com os dados do quiosque
                    if (System.getProperty("SLOT".concat(selectedBike.text)).equals("1") or System.getProperty("SLOT".concat(selectedBike.text)).equals("2")) {

                        var slotOld = localStation.slotList.get(Integer.parseInt(selectedBike.text) - 1);

                        //Verifica o status no servidor e
                        //TODO: e realiza uma reserva interna da bicicleta no servidor
                        var slotUpdated = slotData.getSlotById(Integer.parseInt(slotOld.Id));
                        //if (slotUpdated.status.equals("Ocupada") or slotUpdated.status.equals("Reservada")) {
                            //Solicita ao servidor a liberação
                            var resultado = slotData.solicitaLiberacaoServer(slotUpdated);

                            //Caso alguem já tenha solicitado a bicicleta ao mesmo tempo
                            if(resultado.split(':')[0].equals('fail')){
                                timeoutView.stop();                                
                                controller.showSelectBikeView(resultado.split(':')[1]);
                            }else{
                                //Libera bicicleta - HW
                                controller.HWControl.destravamento(Integer.parseInt(selectedBike.text));
                                //Passa para a proxima tela
                                selectedBike.text = "";
                                timeoutView.stop();
                                controller.showRentCompleteView(resultado);
                            }
                        //}
                        //else{
                         //       timeoutView.stop();
                         //       controller.showSelectBikeView("Bicicleta já selecionada. Escolha outra bicicleta.");
                         //   }
                    } else {
                        clearBtn.fire();
                    }
                }
            }
    var clearBtn = Button {
                translateX: 800
                translateY: 600
                action: function () {
                    clear();
                }
            }

    override protected function create(): Node {
        render();
        title.text = "Seleciona Bicicleta";
        Group {
            content: bind [background, title, mainMessage,secondMessage, messageAlert, voltar, confirmar,
                bikeListBox, legenda, bikeListNumberBox,
                selectedBike, sendBtn, clearBtn, leftBack, esquerda, rightBack, direita
            ]
        };
    }

    public function render() {
        //Busca os dados da estação
        localStation = stationData.getStation(Integer.parseInt(System.getProperty("station_id")));

        //Busca o numero de slots
        localStation.numberSlots = localStation.slotList.size();

        //Quantidade de slots a esquerda do Totem
        var left = localStation.slot_left;
        var slotCounter = 0;
        var diff = 0;
        var marginLeft = 0;
        if(localStation.slot_left>0){
            var leftImg = ImageView {
                        image: Image {url: "{__DIR__}img/est_barradir.jpg"}
                        translateY: 100;
                    }
            insert leftImg into bikesListImage;
        marginLeft = 67;
        }

        for (i in [0..localStation.numberSlots]) {

            var oneImg   ;
            var numberLabel   ;

            //Se for um totem
            if (i == left) {
                    var imgStr = "";
                    if(left ==0){
                       imgStr = "est_totemdir.jpg"
                    }else
                    if(left == localStation.numberSlots){
                        imgStr = "est_totemesq.jpg"
                    }else{
                        imgStr = "est_totemamb.jpg"
                    }
                oneImg = ImageView {
                    image: Image {url: "{__DIR__}img/{imgStr}"}
                    translateY: 100;
                };

                numberLabel = Label {
                    translateX: 110 * (control + 1) + diff
                };
                diff = 40;


            } //caso contrario é um slot
            else {
                //Se ele estiver ocupado
                if (localStation.slotList.get(slotCounter).status.equals("Ocupada")) {
                    oneImg = ImageView {
                        image: Image {url: "{__DIR__}img/est_bikedisponivel.jpg"}
                        translateY: 100;
                    }
                    numberLabel = Label {
                        text: String.valueOf(slotCounter + 1)
                        translateX: 110 * (control + 1) + diff + marginLeft
                        font: Font.font("Courier New", FontWeight.BOLD, 25)
                        textFill: Color.WHITE
                    };

                    } //Verifica se está reservado
                else if (localStation.slotList.get(slotCounter).status.equals("Reservada")) {
                    oneImg = ImageView {
                        image: Image {url: "{__DIR__}img/est_bikelembrete.jpg"}
                        translateY: 100;
                    }
                    numberLabel = Label {
                        text: String.valueOf(slotCounter + 1)
                        translateX: 110 * (control + 1) + diff + marginLeft
                        font: Font.font("Courier New", FontWeight.BOLD, 25)
                        textFill: Color.WHITE
                    };
                    } else if (localStation.slotList.get(slotCounter).status.equals("Bloqueada")) {
                    oneImg = ImageView {
                        image: Image {url: "{__DIR__}img/est_bikeindisponivel.jpg"}
                        translateY: 100;
                    }
                    numberLabel = Label {
                        text: String.valueOf(slotCounter + 1)
                        translateX: 110 * (control + 1) + diff + marginLeft
                        font: Font.font("Courier New", FontWeight.BOLD, 25)
                        textFill: Color.WHITE
                    };

                    //Caso contrario ele está livre
                } else {
                    oneImg = ImageView {
                        image: Image {url: "{__DIR__}img/est_bikesem.jpg"}
                        translateY: 100;
                    }
                    numberLabel = Label {
                        translateX: 110 * (control + 1) + diff+ marginLeft
                    };
                    }
                slotCounter++;
                }

            insert oneImg into bikesListImage;
            insert numberLabel into bikesListNumber;
            control++;
        }

          if(localStation.slot_right>0){
            var rightImg = ImageView {
                        image: Image {url: "{__DIR__}img/est_barraesq.jpg"}
                        translateY: 100;
                    }
            insert rightImg into bikesListImage;
        }
    }

    public function clear() {
        count = 0;
        selectedBike.text = "";
        selectedBike.requestFocus();
        mensagem = "";
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
