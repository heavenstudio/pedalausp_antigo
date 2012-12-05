/*
 * ProblemsView.fx
 *
 * Created on Feb 23, 2010, 7:22:46 PM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.Group;
import javafx.scene.Node;
import br.com.lab360.pedalusp.totem.data.ProblemData;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import br.com.lab360.pedalusp.totem.control.IntegerBox;
import javafx.scene.paint.Color;
import br.com.lab360.pedalusp.totem.model.Problems;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author User
 */
public class ProblemsView extends AbstractView {

    protected var controller: Controller;
    public var backToView;
    var gList = Group {
                content: bind [pList]
            }
    var gListB = Group {
                content: bind [pListB]
                visible: false;
            }
    var pList: Node[];
    var pListB: Node[];
    var problemsInstance: Problems;
    var numbernode: Node;
    var esquerda = ImageView {
                image: Image {url: "{__DIR__}img/bot_esq.jpg"}
                y: 330;
                x: 10;
            };
    var direita = ImageView {
                image: Image {url: "{__DIR__}img/bot_dir.jpg"}
                y: 330;
                x: 680
            };
    var countIntegerBox = 0;
    var countIntegerBoxB = 0;
    public var mainMessage = Label {
                text: "Digite o número do problema que deseja relatar:"
                translateY: 80
                translateX: 80
                graphicHPos: HPos.CENTER
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
    public var selectedProblem: IntegerBox =
            IntegerBox {
                columns: 2
                width: 100
                height: 50
                translateX: 350
                translateY: 120
                selectOnFocus: false
                font: Font.font("Corbel", FontWeight.BOLD, 50)
                onKeyReleased: function (ke: KeyEvent) {
                    if (ke.code == KeyCode.VK_0 or ke.code == KeyCode.VK_1
                            or ke.code == KeyCode.VK_2 or ke.code == KeyCode.VK_3
                            or ke.code == KeyCode.VK_4 or ke.code == KeyCode.VK_5
                            or ke.code == KeyCode.VK_6 or ke.code == KeyCode.VK_7
                            or ke.code == KeyCode.VK_8 or ke.code == KeyCode.VK_9) {
                        countIntegerBox++;
                        if (countIntegerBox == 1) {
                            sendBtn.requestFocus();
                            sendBtn.fire();
                        }
                    }
                    if (ke.code == KeyCode.VK_ESCAPE) {
                        clearBtn.requestFocus();
                        clearBtn.fire();
                    }
                    if (ke.code == KeyCode.VK_ENTER) {
                        sendBtn.requestFocus();
                        sendBtn.fire();
                    }
                    if (ke.code == KeyCode.VK_K) {
                        clear();
                        if (backToView == "mainmenu") {
                            controller.showMainMenuView();
                        } else if (backToView == "welcome") {
                            controller.showWelcomeView();
                        }
                    }
                    if (ke.code == KeyCode.VK_N) {
                        gList.visible = false;
                        gListB.visible = true;
                    }
                    if (ke.code == KeyCode.VK_M) {
                        gList.visible = true;
                        gListB.visible = false;
                    }
                }
            }


   public var numberMessage = Label {
            text: "Nº"
            translateY: 135
            translateX: 500
            graphicHPos: HPos.CENTER
            visible: false;
            font: Font.font("Corbel", FontWeight.BOLD, 30)
            textFill: Color.web("#005AA9")
            };
    public var selectedProblemNumber: IntegerBox =
            IntegerBox {
                columns: 2
                width: 100
                height: 50
                translateX: 550
                translateY: 120
                visible: false;
                selectOnFocus: false
                font: Font.font("Corbel", FontWeight.BOLD, 50)
                onKeyReleased: function (ke: KeyEvent) {
                    if (ke.code == KeyCode.VK_0 or ke.code == KeyCode.VK_1
                            or ke.code == KeyCode.VK_2 or ke.code == KeyCode.VK_3
                            or ke.code == KeyCode.VK_4 or ke.code == KeyCode.VK_5
                            or ke.code == KeyCode.VK_6 or ke.code == KeyCode.VK_7
                            or ke.code == KeyCode.VK_8 or ke.code == KeyCode.VK_9) {
                        countIntegerBoxB++;
                        if (countIntegerBox == 1) {
                            sendBtn.requestFocus();
                            sendBtn.fire();
                        }
                    }
                    if (ke.code == KeyCode.VK_ESCAPE) {
                        clearBtn.requestFocus();
                        clearBtn.fire();
                    }
                    if (ke.code == KeyCode.VK_ENTER) {
                        sendBtn.requestFocus();
                        sendBtn.fire();
                    }
                    if (ke.code == KeyCode.VK_K) {

                        clear();
                        if (backToView == "mainmenu") {
                            controller.showMainMenuView();
                        } else if (backToView == "welcome") {
                            controller.showWelcomeView();
                        }

                    }
                }
            }
    var sendBtn: Button = Button {
                translateX: 800
                translateY: 600
                width: 0
                height: 0
                action: function () {
                    //TODO - Enviar para o servidor o problema selecionado
                    java.lang.System.out.println("SELECTED PROBLEM");
                    java.lang.System.out.println(selectedProblem.text);
                    java.lang.System.out.println("SELECTED PROBLEM");
                    java.lang.System.out.println(selectedProblemNumber.text);
                    var selectNumber: Integer;
                    var selectPNumber: Integer;
                    
                    try{
                   		selectNumber = java.lang.Integer.parseInt(selectedProblem.text);
                    }
                    catch(ex: java.lang.NumberFormatException){
                        selectedProblem.requestFocus();
                        throw ex;
                    }
                    
                    
                    //Verifica se é um problema valido

                    if (selectNumber < (problemsInstance.problems.size()+1) and selectNumber > 0) {
                        if ((problemsInstance.problems.get(selectNumber - 1)).withNumber) {
                            if (selectedProblemNumber.visible == false) {
                                selectedProblemNumber.visible = true;
                                selectedProblemNumber.requestFocus();
                                numberMessage.visible = true;
                            } else {
			                    try{
			                        selectPNumber = java.lang.Integer.parseInt(selectedProblemNumber.text);
								}
			                    catch(ex: java.lang.NumberFormatException){
			                    	selectedProblemNumber.requestFocus();
			                    	throw ex;
			                    }
                                var pData = ProblemData {};
                                pData.reportProblem(
                                (problemsInstance.problems.get(
                                selectNumber - 1)).Id,
                                java.lang.Integer.parseInt(java.lang.System.getProperty("cyclist_id")),
                                selectPNumber);
                                selectedProblemNumber.visible = false;
                                numberMessage.visible = false;
                                clear();
                                controller.showProblemSentView(String.valueOf(backToView));
                            }
                        } else {
                            var pData = ProblemData {};
                            pData.reportProblem(
                            (problemsInstance.problems.get(
                            java.lang.Integer.parseInt(selectedProblem.text) - 1)).Id,
                            java.lang.Integer.parseInt(java.lang.System.getProperty("cyclist_id")),
                            0);
                            selectedProblemNumber.visible = false;
                            numberMessage.visible = false;
                            clear();
                            controller.showProblemSentView(String.valueOf(backToView));
                        }
                    } else {
                        clear();
                    }

                }
            }
    var clearBtn = Button {
                translateX: 800
                translateY: 600
                width: 0
                height: 0
                action: function () {
                    clear();
                }
            }

    override protected function create(): Node {
        title.text = "Problemas";
        renderProblems();
        Group {
            content: bind [background, mainMessage, numberMessage,
                title, gList, gListB, selectedProblem, selectedProblemNumber, sendBtn, clearBtn,
                esquerda, direita, voltar, confirmar]
        }


    }

    function renderProblems() {
        var pData = ProblemData {};
        problemsInstance = pData.getProblemsList();
        for (i in [0..3]) {
            var num = Label {
                        translateX: 170;
                        width: 100;
                        height: 40;
                        translateY: 150 + 65 * (i + 1);
                        text: problemsInstance.problems.get(i).problemNumber;
                        font: Font.font("Corbel", FontWeight.BOLD, 25)
                        textFill: Color.web("#FFFFFF")
                    }
                    var imgNumBack = ImageView {
                        image: Image {url: "{__DIR__}img/bola_azul.jpg"}
                        translateX: 152;
                        translateY: 147 + 65 * (i + 1);
                       }
            var bt = Label {
                        width: 480;
                        height: 40;
                        translateX: 200;
                        translateY: 150 + 65 * (i + 1)
                        text: problemsInstance.problems.get(i).title;
                        font: Font.font("Corbel", FontWeight.BOLD, 25)
                        textFill: Color.web("#005AA9")
                    }
                    insert imgNumBack into pList;
            insert num into pList;
            insert bt into pList;
            }
        for (i in [4..problemsInstance.problems.size() - 1]) {
            var num = Label {
                        translateX: 170;
                        width: 100;
                        height: 40;
                        translateY: 150 + 65 * (i - 3);
                        text: problemsInstance.problems.get(i).problemNumber;
                        font: Font.font("Corbel", FontWeight.BOLD, 25)
                        textFill: Color.web("#FFFFFF")
                    }
            var imgNumBack = ImageView {
                        image: Image {url: "{__DIR__}img/bola_azul.jpg"}
                        translateX: 152;
                        translateY: 147 + 65 * (i - 3);
                       }
            var bt = Label {
                        width: 480;
                        height: 40;
                        translateX: 200;
                        translateY: 150 + 65 * (i - 3)
                        text: problemsInstance.problems.get(i).title;
                        font: Font.font("Corbel", FontWeight.BOLD, 25)
                        textFill: Color.web("#005AA9")
                    }
            insert imgNumBack into pListB;
            insert num into pListB;
            insert bt into pListB;


            }
        var bt2 = Label {
                    width: 480;
                    height: 40;
                    translateX: 200;
                    translateY: 150 + 65 * (4)
                    text: "Para outros problemas, ligue para 3091-3222/3091-4222 ou\nescreva para pedalusp@usp.br"
                    font: Font.font("Corbel", FontWeight.BOLD, 25)
                    textFill: Color.web("#005AA9")
                }
        insert bt2 into pListB;

    }

    public function clear() {
        countIntegerBox = 0;
        countIntegerBoxB = 0;
        selectedProblem.text = "";
        selectedProblemNumber.text = "";
        selectedProblem.requestFocus();
    }
}
