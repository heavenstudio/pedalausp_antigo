/*
 * TestPanel.fx
 *
 * Created on Feb 12, 2010, 4:20:37 PM
 */
package br.com.lab360.pedalusp.totem.hwcontrol;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import br.com.lab360.pedalusp.totem.gui.Controller;
import br.com.lab360.pedalusp.totem.gui.AbstractView;
import javafx.scene.control.TextBox;
import java.lang.System;
import javafx.scene.control.Label;
import br.com.lab360.pedalusp.totem.data.StationData;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.FontWeight;
import com.sun.javafx.scene.control.caspian.ButtonSkin;

/**
 * @author User
 */
public class TestPanel extends AbstractView {

    
    var statusLabelList: Label[];
    public var controller: Controller;
    var bksim = controller.bkSim;

    public var menu01: Button = Button {
                text: "Atualizar Status do HW"
                font: Font.font("Arial", 20)
                action: function () {
                    update();
                }
            }
    var backBtn: Button = Button {
                text: "Voltar"
                width: 100
                height: 40
                font: Font.font("Century Gothic", FontWeight.BOLD, 20)
                skin: ButtonSkin {
                    fill: LinearGradient {
                        startX: 0.0
                        startY: 0.0
                        endX: 0.0
                        endY: 1.0
                        proportional: true
                        stops: [
                            Stop {
                                offset: 0.0
                                color: Color.web("#fcfbb3")},
                            Stop {
                                offset: 1.0
                                color: Color.web("#faf86b")}
                        ]
                    }
                    borderFill: Color.web("#faf86b");
                }
                action: function () {
                    controller.showWelcomeView();
                    }
            }
    var lblLiberar = Label {
                text: "Botao para Destravar";
            }
    var txtBoxDestravar = TextBox {
                columns: 2
            }
    var botaoDestravar: Button = Button {
                text: "Destravar"
                font: Font.font("Arial", 20)
                action: function () {

                    bksim.testeApertarBotaoDestravar(Integer.parseInt(txtBoxDestravar.text));

                }
            }
    var lblDevolver = Label {
                text: "Devolver";
            }
    var txtBoxDevolver = TextBox {
                columns: 2
            }

   var txtBikeRFID = TextBox {
                columns: 12
            }
    var botaoDevolver: Button = Button {
                text: "Devolver"
                font: Font.font("Arial", 20)
                action: function () {
                        java.lang.System.out.println("DEVOLVER__________________________________");
                    bksim.testeDevolverBicicleta(Integer.parseInt(txtBoxDevolver.text), txtBikeRFID.text);

                }
            }
    var lblReservar = Label {
                text: "Reservar";
            }
    var txtBoxReservar = TextBox {
                columns: 2
            }
    var botaoReservar: Button = Button {
                text: "Reservar"
                font: Font.font("Arial", 20)
                action: function () {
                    bksim.testeReservarBicicleta(Integer.parseInt(txtBoxReservar.text));

                }
            }

    override protected function create(): Node {
        title.text = "Tela de teste";
        render();
        VBox {
            content: [background, title,
                VBox {translateX: 200
                    translateY: -500
                    content: bind [menu01,
                        lblReservar, txtBoxReservar, botaoReservar,
                        lblLiberar, txtBoxDestravar, botaoDestravar, statusLabelList,
                        lblDevolver, txtBoxDevolver, txtBikeRFID, botaoDevolver, backBtn
                    ]}]
        }
    }

    function render() {
        var stationData = StationData {};

        controller.estacao = stationData.getStation(Integer.parseInt(System.getProperty("station_id")));

        for (i in [0..(controller.estacao.slotList.size() - 1)]) {
            var newLbl = Label {
                        text: "HW {String.valueOf(i+1)}:{System.getProperty("HW".concat(String.valueOf(i+1)))}    -     SLOT{String.valueOf(i+1)}:{System.getProperty("SLOT".concat(String.valueOf(i+1)))}";
                    }

            insert newLbl into statusLabelList
        }
    }

    public function update() {
        statusLabelList = null;
        render();


    }
}
