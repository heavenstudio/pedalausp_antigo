package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.geometry.HPos;
import javafx.scene.text.TextAlignment;
import java.lang.System;

// imports do modulo contactless
// Interface do receptor de mensagens, implementada por esta classe para
// receber eventos de leitura do cartão
import br.com.muggler.contactless.smartcard.SmartcardPublisherClient;
// O tipo do objeto que publica as mensagens
import br.com.muggler.contactless.smartcard.SmartcardPublisher;
// Tela de erro
import br.com.muggler.contactless.javafx.SmartcardErrorDialog;

/**
 * Referente a tela inicial.
 * @author User
 */
public class WelcomeView extends AbstractView, SmartcardPublisherClient {

    protected var controller: Controller;

    public var mainMessage = Label {
                text: "Aproxime o cartão USP do leitor abaixo"
                translateY:100
                translateX:130
                graphicHPos:HPos.CENTER
                    font: Font.font("Corbel",FontWeight.BOLD,30)
                    textFill: Color.web("#005AA9")
            };

    public var cartaoUsp = ImageView {
            image: Image {url: "{__DIR__}img/ilus_cartaousp.jpg"}
            x:200
            y:130;
            fitHeight:220;

    };

    var duvidas = Text {
                x: 265
                y: 380
                font: Font.font("Corbel",FontWeight.BOLD,20)
                fill: Color.web("#005AA9")
                wrappingWidth: 350
                textAlignment: TextAlignment.CENTER;
                content: "Em caso de dúvidas,\nacesse www.usp.br/pedalusp\nou ligue para 3091-3222/3091-4222"
            }

    var comoFunciona = ImageView {
            image: Image {url: "{__DIR__}img/nav01.jpg"}
            y:512;
    };

    var verEstacoes = ImageView {
            image: Image {url: "{__DIR__}img/nav02.jpg"}
            x:300;
            y:512;
    };

    var comunicarProblema = ImageView {
            image: Image {url: "{__DIR__}img/nav03.jpg"}
            x:500;
            y:512;
    };

  

    public var actionButton: Button = Button {
                width: 0
                height: 0
                translateX: 800
                translateY: 600
                font: Font.font("Century Gothic", FontWeight.BOLD, 20)//Centric Gothic

                onKeyReleased: function (ke: KeyEvent) {
                    
                    if (ke.code == KeyCode.VK_M) {
                        controller.showInstructionVideoView();
                    }
                    if (ke.code == KeyCode.VK_L) {
                        controller.showMapView("welcome", Integer.valueOf(System.getProperty("station_number")));
                    }
                    if (ke.code == KeyCode.VK_N) {
                        controller.showProblemView("welcome");
                    }
                   //TODO: Temporario para simular a passagem da carteirinha
                    if (ke.code == KeyCode.VK_E) {
                        controller.showInsertPasswordView();
                    }
                    //TODO: Temporario para entrar no ambiente de testes
                    if (ke.code == KeyCode.VK_H) {
                        controller.showTestPanel();
                    }
                
                }
            };


        override protected function create(): Node {
        title.text = "Primeira tela";
        errorDialog.visible=false;
        Group {
            content: bind [background, comoFunciona, verEstacoes, comunicarProblema,
            mainMessage, title, cartaoUsp, duvidas, actionButton, errorDialog
            ]
        }       

    }
	// servico de eventos de leitura    
    	public var publisher : SmartcardPublisher;
    // Metodo para injecao do servico
    	override function setSmartcardPublisher(pPublisher:SmartcardPublisher){
    	    publisher = pPublisher;
    	}
    	
    	// Tela de erro
    	var errorDialog: SmartcardErrorDialog = SmartcardErrorDialog{
    		translateX:210;
    		translateY:150;
    		fadeDuration:3500ms;
    	};
    	function showErrorDialog(){
    		errorDialog.visible=true;
    		errorDialog.toFront();
    	    errorDialog.fadeTransition.playFromStart();
    	 }
    	
    	// Tratamento de leitura bem sucedida
    	override function nUspRead(nUsp:String){
    	    FX.deferAction(
    	    	function(): Void {
		    	    if(controller.readerActive==false) then{
		    	        return;
		    	    }
    	    		controller.showInsertPasswordView(nUsp);
    	    	});
    	}
    	
    	// Tratamento de erro de leitura
    	override function readError(){
    	    FX.deferAction(
    	    	function(): Void {
		    	    if(controller.readerActive==false) then{
						return;
		       	    }
    	    		showErrorDialog();
    	    	});
    	}
    
}
