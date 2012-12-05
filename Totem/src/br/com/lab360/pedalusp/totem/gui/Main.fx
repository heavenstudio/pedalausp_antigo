/*
 * Main.fx
 *
 * Created on Feb 8, 2010, 3:20:07 PM
 */

package br.com.lab360.pedalusp.totem.gui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.StageStyle;
import br.com.lab360.pedalusp.totem.StartTotem;
import javafx.async.JavaTaskBase;
import javafx.async.RunnableFuture;
import br.com.lab360.pedalusp.totem.hwcontrol.HWController;

// imports do modulo contactless
// O serviço que irá publicar os eventos de leitura de cartões
import br.com.muggler.contactless.javafx.FXSmartcardService;
// Um ajudante estático para injetar as dependências
import br.com.muggler.contactless.ServiceAssemblyHelper;

/**
 * @author User
 */

var control = new Controller();

// Delegando a criação do serviço de leitura para o código java
def smartcardService: FXSmartcardService = ServiceAssemblyHelper.getFxSmartCardService();

Stage {
    scene: Scene {
        fill: Color.WHITE
        content: bind [control.contents]
    }
    resizable: false
    width: 800
    height: 600
    visible: true
    style: StageStyle.TRANSPARENT //Retira a barra superior do aplicativo

}
var start:StartTotem = StartTotem{};
start.startTheTotem(control.HWControl);

//Classe para executar threads em background no quiosque (JavaFX)
class BackgroundTask extends JavaTaskBase{

    override function create():RunnableFuture{
        control.HWControl;
    }

}

BackgroundTask{}.start();


// A tarefa de background do serviço de leitura
class SmartcardInterface extends JavaTaskBase{
    override function create():RunnableFuture{smartcardService;}
}
SmartcardInterface{}.start();

control.showWelcomeView();





