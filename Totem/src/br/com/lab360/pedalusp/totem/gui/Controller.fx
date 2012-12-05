/*
 * Controller.fx
 *
 * Created on Feb 8, 2010, 3:31:28 PM
 */

package br.com.lab360.pedalusp.totem.gui;

import javafx.scene.Node;
import br.com.lab360.pedalusp.totem.model.Station;
import br.com.lab360.pedalusp.totem.hwcontrol.TestPanel;
import br.com.lab360.pedalusp.totem.hwcontrol.HWController;
import br.com.lab360.pedalusp.totem.hwcontrol.BikeHWSimulator;
import br.com.lab360.pedalusp.totem.serial.SerialConnection;

// Imports do modulo contactless
// Um ajudante estatico para injetar as dependencias
import br.com.muggler.contactless.ServiceAssemblyHelper;
// A caixa de dialogo de erro na leitura RFID
import br.com.muggler.contactless.javafx.SmartcardErrorDialog;

/**
 * @author User
 */

public class Controller {
    // Indica se o leitor está ativo, isto é, devemos responder ao evento de leitura
    public var readerActive = false;
    // Aqui fazemos um binding para verificar quando estamos na tela de boas-vindas,
    // quando o leitor deverá ser ativado.
    public var contents: Node on replace oldValue {
    	if (contents instanceof WelcomeView) then{
    		readerActive = true;
    	}else{
	    	readerActive=false;
    	}
    };
    public var welcomeView: WelcomeView;
    public var insertPasswordView: InsertPasswordView;
    public var mainMenuView: MainMenuView;
    public var selectBikeView: SelectBikeView;
    public var rentCompleteView: RentCompleteView;
    public var instructionVideoView: InstructionVideoView;
    public var mapView: MapView;
    public var mapCompleteView: MapCompleteView;

    public var problemView: ProblemsView;
    public var cyclistData: CyclistDataView;
    public var problemSentView: ProblemSentView;

    public var HWControl: HWController = HWController {};
    public var bkSim = new BikeHWSimulator();

    public var conn = new SerialConnection(HWControl, bkSim);
    public var testPanel: TestPanel;

    public var estacao: Station = new Station();    

    

    public function showWelcomeView():Void {
        HWControl.conn = conn;
        bkSim.sConn = conn;

        java.lang.System.setProperty("cyclist_id","0");
        welcomeView = WelcomeView {
            controller: this
        };
        // Injetando a publicação de mensagens dos eventos de leitura
        
        // Primeiro removo quaisquer instâncias de WelcomeView existentes
        // para que as mesmas sejam coletadas
        br.com.muggler.contactless.ServiceAssemblyHelper.clearSubscribers();
        // Agora injeto um publicador de mensagens na nova instância
        br.com.muggler.contactless.ServiceAssemblyHelper.injectPublisher(welcomeView);
        contents = welcomeView;
        welcomeView.actionButton.requestFocus();
    }

    public function showInsertPasswordView() {

        insertPasswordView = InsertPasswordView {
            controller: this
        };
        contents = insertPasswordView;
        insertPasswordView.nuspTextBox.requestFocus();

    }
    
// Novo metodo criado para a chamada da tela de senha ja com o nUsp preenchido,
// é invocado pela WelcomeView quando há uma leitura bem sucedida    
        public function showInsertPasswordView(nUsp:String) {
    
            insertPasswordView = InsertPasswordView {
                controller: this
            };
            contents = insertPasswordView;
            insertPasswordView.nuspTextBox.text = nUsp;
            insertPasswordView.nuspTextBox.editable = false;
            insertPasswordView.nuspTextBox.disable = true;
            insertPasswordView.passwordBox.requestFocus();
    
        }
        
    public function showMainMenuView():Void {

        mainMenuView = MainMenuView {
            controller: this
        };
        contents = mainMenuView;
        mainMenuView.actionButton.requestFocus();
    }

    public function showSelectBikeView(msg :String):Void{
        selectBikeView = SelectBikeView {
            controller: this
            mensagem: msg
        };
        contents = selectBikeView;
        selectBikeView.selectedBike.requestFocus();
        selectBikeView.clear();
    }

    public function showRentCompleteView(resultado :String) {

        rentCompleteView = RentCompleteView {
            controller: this
            protocolo: resultado
        };

        contents = rentCompleteView;
        rentCompleteView.actionButton.requestFocus();

    }
        public function showProblemSentView(back :String) {

        problemSentView = ProblemSentView {
            controller: this
            backToView: back;
        };

        contents = problemSentView;
        problemSentView.actionButton.requestFocus();

    }
     public function showMapView(back :String, stationNumber:Integer):Void {

        mapView = MapView {
            controller: this
            backToView: back;
            stationNumber:stationNumber
        };
        java.lang.System.out.println("st");
        java.lang.System.out.println(stationNumber );
        contents = mapView;

        mapView.backBtn.requestFocus();
    
    }

    public function showMapCompleteView(back :String):Void {

        mapCompleteView = MapCompleteView {
            controller: this
            backToView: back;
        };
        contents = mapCompleteView;
        mapCompleteView.selectedStation.requestFocus();

    }

     public function showProblemView(back :String) {

        problemView = ProblemsView{
            controller: this
            backToView: back;
        };
        problemView.selectedProblem.requestFocus();
        contents = problemView;
        
    }

 public function showTestPanel() {

        testPanel = TestPanel{
            controller: this
        };

        contents = testPanel;
        
    }

     public function showCyclistData() {

        cyclistData = CyclistDataView{
            controller: this
        };
        cyclistData.backBtn.requestFocus();
        contents = cyclistData;

    }

    public function finaliza() {

        //welcomeView = Welcome {
        //    controller: this
        //};

        //contents = welcomeView;
        //welcomeView.moreInfoBtn.requestFocus();
       
        //welcomeView.moreInfoBtn.requestFocus();
        }

        public function showInstructionVideoView() {
         instructionVideoView = InstructionVideoView {
            controller: this
        };

        contents = instructionVideoView;
        instructionVideoView.actionButton.requestFocus();
        instructionVideoView.mediaP.play();
    }
}
