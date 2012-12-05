/*
 * CyclistDataView.fx
 *
 * Created on Feb 17, 2010, 11:06:41 AM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.layout.Tile;
import javafx.geometry.HPos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import br.com.lab360.pedalusp.totem.data.CyclistData;
import br.com.lab360.pedalusp.totem.data.PunishmentData;
import br.com.lab360.pedalusp.totem.model.Punishment;
import br.com.lab360.pedalusp.totem.data.AppliedPunishmentsData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import java.util.ArrayList;
import br.com.lab360.pedalusp.totem.data.RentsData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

/**
 * @author User
 */
public class CyclistDataView extends AbstractView {

    protected var controller: Controller;
    var data: Node[];
    var tituloTabela = Label {
                translateX: 300
                translateY: 300
                text: "ÚLTIMOS EMPRÉSTIMOS"
                font: Font.font("Corbel", FontWeight.BOLD, 20);
                textFill: Color.web("#005AA9")
            }
    var voltar = ImageView {
                image: Image {url: "{__DIR__}img/nav04.jpg"}
                y: 512;
            };
    public var backBtn: Button = Button {
                translateY: 600
                translateX: 800
                width: 0
                height: 0
                onKeyReleased: function (ke: KeyEvent) {

                    if (ke.code == KeyCode.VK_K or ke.code == KeyCode.VK_ENTER) {
                        timeoutView.stop();
                        controller.showMainMenuView();
                    }
                    }
            }

    override protected function create(): Node {
        callData();
        timeoutView.play();
        title.text = "Dados do ciclista";
        Group {
            content: bind [background, title, backBtn, data, tituloTabela, voltar
            ]
        };


    }

    function callData() {
        var cData: CyclistData = CyclistData {};
        var cyclist = cData.getCyclistData();

        var punData = PunishmentData {};
        var apsData = AppliedPunishmentsData {};
        var aps = apsData.getAppliedPunishments(Integer.parseInt(java.lang.System.getProperty("cyclist_id")));


        var rentData = RentsData {};
        var rents = rentData.getRentsByCyclistId(Integer.parseInt(java.lang.System.getProperty("cyclist_id")));

        var punicoes  ;
        var tabelaPunicao  ;
        var cdataPunish01: ArrayList = new ArrayList();
        cdataPunish01.add("Penalidade");
        var cdataPunish02: ArrayList = new ArrayList();
        cdataPunish02.add("Aplicada em");
        var cdataPunish03: ArrayList = new ArrayList();
        cdataPunish03.add("Qtde.");

        //Possui punições
        if (aps.appliedPunishments.size() > 0) {

            var count = 0;

            while (count < 3 and aps.appliedPunishments.size() > count) {
                //lblTxt = "{lblTxt}\n{}:{ps.punishments.get(count).startDate.substring(10)} {ps.punishments.get(count).endDate.substring(10)}";
                var pun_id = aps.appliedPunishments.get(count).punishmentId;
                var pun = punData.getPunishment(Integer.parseInt(pun_id));
                cdataPunish01.add(pun.appliedType);
                if (aps.appliedPunishments.get(count).appliedAt.length() > 0) {
                    cdataPunish02.add("{getDate(aps.appliedPunishments.get(count).appliedAt)}, {getTime(aps.appliedPunishments.get(count).appliedAt)}");
                }
                cdataPunish03.add("{getAppliedAmount(pun)}");
                count += 1;
                }

            var column01b = for (text in cdataPunish01)
                        Label {
                            text: String.valueOf(text)
                            font: Font.font("Arial", FontWeight.BOLD, 12);
                            textFill: Color.web("#005AA9")
                            hpos: HPos.CENTER
                        }
            var column02b = for (text in cdataPunish02)
                        Label {
                            text: String.valueOf(text)
                            font: Font.font("Arial", FontWeight.BOLD, 12);
                            textFill: Color.web("#005AA9")
                            hpos: HPos.CENTER
                        }
            var column03b = for (text in cdataPunish03)
                        Label {
                            text: String.valueOf(text)
                            font: Font.font("Arial", FontWeight.BOLD, 12);
                            textFill: Color.web("#005AA9")
                            hpos: HPos.CENTER
                        }
            tabelaPunicao = Tile {
                translateX: 400
                translateY: 130
                hgap: 5
                columns: 3
                rows: 6
                vgap: 3
                content: for (i in [0..5])
                    [column01b[i], column02b[i], column03b[i]]
            }//Tile
            var punishmentIcon = ImageView {
                        image: Image {url: "{__DIR__}img/picpeq_exclama.jpg"}
                        x: 700
                        y: 150;
                    };
            insert punishmentIcon into data;


            } //Não possui punições
        else {
            punicoes = Label {
                translateX: 410;
                translateY: 135;
                font: Font.font("Corbel", FontWeight.BOLD, 16);
                text: "Você não possui\npenalidades registradas.";
            }

            var punishmentOKIcon = ImageView {
                        image: Image {url: "{__DIR__}img/picgrd_certo.jpg"}
                        x: 630
                        y: 110;
                    };
            insert punishmentOKIcon into data;
            }

        var line01 = Line {
                    startX: 50, startY: 110
                    endX: 350, endY: 110
                    strokeWidth: 3
                    stroke: Color.web("#005AA9")
                }
        var line02 = Line {
                    startX: 50, startY: 220
                    endX: 350, endY: 220
                    strokeWidth: 3
                    stroke: Color.web("#005AA9")
                }

        var cyclistNamelbl = Label {
                    translateX: 50;
                    translateY: 60;
                    text: cyclist.name;
                    font: Font.font("Corbel", FontWeight.BOLD, 30);
                    textFill: Color.web("#005AA9")
                }
        var cyclistDataDesc = "No USP:\nE-mail:\nCelular:\nStatus:";
        var cyclistDataDesc2 = "{cyclist.nusp}\n{cyclist.email}\n{cyclist.ddd_mobile} {cyclist.mobile_number}\n{cyclist.status}";


        var cyclistIcon = ImageView {
                    image: Image {url: "{__DIR__}img/picpeq_feliz.jpg"}
                    x: 50
                    y: 150;
                };


        var cyclistDescriptionlbl = Label {
                    translateX: 100;
                    translateY: 130;
                    text: cyclistDataDesc;
                    font: Font.font("Arial", FontWeight.BOLD, 15);
                    textFill: Color.web("#005AA9")
                }
        var cyclistDescription2lbl = Label {
                    translateX: 170;
                    translateY: 130;
                    text: cyclistDataDesc2;
                    font: Font.font("Arial", FontWeight.BOLD, 15);
                    textFill: Color.web("#005AA9")
                }

        //RENTS

        var tabelaRents  ;
        var cdataRent01: ArrayList = new ArrayList();
        cdataRent01.add("Protocolo");
        var cdataRent02: ArrayList = new ArrayList();
        cdataRent02.add("Retirada");
        var cdataRent03: ArrayList = new ArrayList();
        cdataRent03.add("Origem");
        var cdataRent04: ArrayList = new ArrayList();
        cdataRent04.add("Devolução");
        var cdataRent05: ArrayList = new ArrayList();
        cdataRent05.add("Destino");

        var count2 = 0;
        while (count2 < 5 and rents.rents.size() > count2) {
            //lblTxt = "{lblTxt}\n{}:{ps.punishments.get(count).startDate.substring(10)} {ps.punishments.get(count).endDate.substring(10)}";
            cdataRent01.add(rents.rents.get(count2).Id);
            cdataRent02.add("{getDate(rents.rents.get(count2).originDate)} - {getTime(rents.rents.get(count2).originDate)}");
            cdataRent03.add("{rents.rents.get(count2).originStationName}");
            cdataRent04.add("{getDate(rents.rents.get(count2).returnDate)} - {getTime(rents.rents.get(count2).returnDate)}");
            cdataRent05.add("{rents.rents.get(count2).returnStationName}");

            count2 += 1;
        }

        var column01a = for (text in cdataRent01)
                    Label {
                        text: String.valueOf(text)
                        font: Font.font("Arial", FontWeight.BOLD, 12);
                        textFill: Color.web("#005AA9")
                        hpos: HPos.CENTER
                    }
        var column02a = for (text in cdataRent02)
                    Label {
                        text: String.valueOf(text)
                        font: Font.font("Arial", FontWeight.BOLD, 12);
                        textFill: Color.web("#005AA9")
                        hpos: HPos.CENTER
                    }
        var column03a = for (text in cdataRent03)
                    Label {
                        text: String.valueOf(text)
                        font: Font.font("Arial", FontWeight.BOLD, 12);
                        textFill: Color.web("#005AA9")
                        hpos: HPos.CENTER
                    }

        var column04a = for (text in cdataRent04)
                    Label {
                        text: String.valueOf(text)
                        font: Font.font("Arial", FontWeight.BOLD, 12);
                        textFill: Color.web("#005AA9")
                        hpos: HPos.CENTER
                    }
        var column05a = for (text in cdataRent05)
                    Label {
                        text: String.valueOf(text)
                        font: Font.font("Arial", FontWeight.BOLD, 12);
                        textFill: Color.web("#005AA9")
                        hpos: HPos.CENTER
                    }

        tabelaRents = Tile {
            translateX: 90
            translateY: 340
            tileWidth: 110
            hgap: 0
            columns: 5
            rows: 6
            vgap: 3
            content: for (i in [0..5])
                [column01a[i], column02a[i], column03a[i], column04a[i], column05a[i]]
        }//Tile

        var rentIcon = ImageView {
                    image: Image {url: "{__DIR__}img/picpeq_bike.jpg"}
                    x: 50
                    y: 350;
                };

        var line03 = Line {
                    startX: 400, startY: 110
                    endX: 750, endY: 110
                    strokeWidth: 3
                    stroke: Color.web("#005AA9")
                }
        var line04 = Line {
                    startX: 400, startY: 220
                    endX: 750, endY: 220
                    strokeWidth: 3
                    stroke: Color.web("#005AA9")
                }

        insert line03 into data;
        insert line04 into data;
        insert rentIcon into data;
        insert tabelaRents into data;
        insert tabelaPunicao into data;
        insert line01 into data;
        insert line02 into data;
        insert punicoes into data;
        insert cyclistNamelbl into data;
        insert cyclistDescriptionlbl into data;
        insert cyclistDescription2lbl into data;
        insert cyclistIcon into data;


        }

    function getDate(dtime: String):String {
        if (dtime.length() > 8) {
            var y = dtime.substring(0, 4);
            var m = dtime.substring(5, 7);
            var d = dtime.substring(8, 10);
            return "{d}/{m}/{y}"
            } else {
            return " "
            }
        }
        
        function getAppliedAmount(pun: Punishment):String {
            if (pun.appliedType.equals('Multa')){
            	return 'R$ {pun.appliedAmount},00';
            }
            if (pun.appliedType.equals('Suspensão')){
                return '{pun.appliedAmount} horas';
            }
            return ''
        }

        function getTime(dtime: String):String {
        if (dtime.length() > 8) {
            var t = dtime.substring(11, 16);
            return "{t}"
            } else {
            return " "
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
