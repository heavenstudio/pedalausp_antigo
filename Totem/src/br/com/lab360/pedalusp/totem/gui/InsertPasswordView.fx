/*
 * InsertPassword.fx
 *
 * Created on Feb 8, 2010, 3:41:00 PM
 */
package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import br.com.lab360.pedalusp.totem.control.IntegerPasswordBox;
import br.com.lab360.pedalusp.totem.data.LoginData;
import java.lang.NumberFormatException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.HPos;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Button;
import br.com.lab360.pedalusp.totem.control.IntegerBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;


// Alguns comportamentos desta classe foram alterados para efetivar o caso de uso de acesso
// via cartão RFID. Basicamente, foram introduzidas as mudanças necessárias para que o input
// do nUsp seja bloqueado, e ao errar a senha o valor não seja apagado.

/**
 * @author User
 */
public class InsertPasswordView extends AbstractView {

    protected var controller: Controller;

    var countPassChars = 0; //Contador para os caracteres da senha.
    var countNuspChars = 0;

    public var nuspMessage = Label {
                text: "Confira o seu numero USP:"
                translateY:50
                translateX:230
                graphicHPos:HPos.CENTER
                    font: Font.font("Corbel",FontWeight.BOLD,30)
                    textFill: Color.web("#005AA9")
            };

        public var mainMessage = Label {
                text: "Por favor, digite sua senha:"
                translateY:200
                translateX:240
                graphicHPos:HPos.CENTER
                    font: Font.font("Corbel",FontWeight.BOLD,30)
                    textFill: Color.web("#005AA9")
            };

    public var voltar = ImageView {
            image: Image {url: "{__DIR__}img/nav04.jpg"}
            y:512;
    };

    public var confirmar = ImageView {
            image: Image {url: "{__DIR__}img/nav06.jpg"}
            x:500;
            y:512;
    };
 public var nuspTextBox: IntegerBox =
            IntegerBox {
                columns: 7
                width: 250
                height: 50
                translateX: 275
                translateY: 120
                selectOnFocus: false
                font: Font.font("Courier New", FontWeight.BOLD, 50)
                onKeyReleased: function (ke: KeyEvent) {
                    if (ke.code == KeyCode.VK_0 or ke.code == KeyCode.VK_1
                            or ke.code == KeyCode.VK_2 or ke.code == KeyCode.VK_3
                            or ke.code == KeyCode.VK_4 or ke.code == KeyCode.VK_5
                            or ke.code == KeyCode.VK_6 or ke.code == KeyCode.VK_7
                            or ke.code == KeyCode.VK_8 or ke.code == KeyCode.VK_9) {
                        countNuspChars++;
                        if (countNuspChars == 7) {
                            passwordBox.requestFocus();
                        }
                    }

                    if (ke.code == KeyCode.VK_ESCAPE) {
                        clearBtn.requestFocus();
                        clearBtn.action();
                    }
                    if (ke.code == KeyCode.VK_ENTER) {
                        passwordBox.requestFocus();
                    }
                    if (ke.code == KeyCode.VK_K) {
                            timeoutView.stop();
                        controller.showWelcomeView();
                    }

                };
            }
    public var passwordBox: IntegerPasswordBox = IntegerPasswordBox {
                columns: 4
                width: 140
                height: 50
                translateX: 335
                translateY: 250
                selectOnFocus: false
                font: Font.font("Courier New",FontWeight.BOLD, 50)
                onKeyReleased: function (ke: KeyEvent) {
                    if (ke.code == KeyCode.VK_0 or ke.code == KeyCode.VK_1
                            or ke.code == KeyCode.VK_2 or ke.code == KeyCode.VK_3
                            or ke.code == KeyCode.VK_4 or ke.code == KeyCode.VK_5
                            or ke.code == KeyCode.VK_6 or ke.code == KeyCode.VK_7
                            or ke.code == KeyCode.VK_8 or ke.code == KeyCode.VK_9) {
                        countPassChars++;
                        if (countPassChars == 4) {
                            validatePass();
                        }
                    }

                    if (ke.code == KeyCode.VK_ESCAPE) {
                        clearBtn.requestFocus();
                        clearBtn.action();
                    }
                    if (ke.code == KeyCode.VK_ENTER) {
                        validatePass();
                    }
                    if (ke.code == KeyCode.VK_K) {
                            timeoutView.stop();
                        controller.showWelcomeView();
                    }

                };
            }


var messageAlert = Text {
                translateX: 340
                translateY: 350
                wrappingWidth: 300
                font: Font.font("Corbel",FontWeight.BOLD,20)
                fill: Color.RED
            };



var possuiSenha = Text {
                x: 235
                y: 400
               font: Font.font("Corbel",FontWeight.BOLD,20)
                fill: Color.web("#005AA9")
                wrappingWidth: 400
                textAlignment: TextAlignment.CENTER;
                content: "Não possui senha?\nAcesse www.usp.br/pedalusp e cadastre-se!"
            }
var clearBtn:Button = Button {
                translateX: 800
                translateY: 600
                width: 0
                height: 0
                action: function () {
                    resetTextBox();
                    //nuspTextBox.requestFocus();
                    passwordBox.requestFocus();
                }

            }

    override protected function create(): Node {
timeoutView.play();
        title.text = "Segunda Tela";

        Group {
            content: bind [background, title, nuspMessage, mainMessage, possuiSenha, nuspTextBox, passwordBox, clearBtn,
            messageAlert, voltar, confirmar]
        }
    }



    //Limpa os paramentros da tela
    public function resetTextBox() {           
        passwordBox.text = "";
        passwordBox.clear();
        //nuspTextBox.text = "";
        countPassChars = 0;
        //countNuspChars = 0;

    }

    //Funcao para validar o Password no Banco da dados
    public function validatePass() {

        var lData = LoginData {};
        //var cyclist_NUSP = "3732221";
        var cyclist_NUSP = nuspTextBox.text;

        var result: String = lData.verificaLogin(cyclist_NUSP, passwordBox.password);
        if (result.split(":")[0].equals("fail")) {
            messageAlert.content = result.split(":")[1];
			clearBtn.requestFocus();
            clearBtn.action();
        }else {
               try
               {
                 //verifica se o dado retornado é um inteiro
                 var cyclist_id = Integer.parseInt (result);
                 resetTextBox();
                 java.lang.System.setProperty("cyclist_id", result);
                 java.lang.System.setProperty("cyclist_password", passwordBox.password);
                 java.lang.System.setProperty("cyclist_nusp", cyclist_NUSP);
				 timeoutView.stop();
                 controller.showMainMenuView();
               }
               catch (e: NumberFormatException)
               {
                 messageAlert.content = "Usuário ou Senha inválidos";
                 resetTextBox();
                 passwordBox.requestFocus();
               }

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
