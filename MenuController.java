package pjatu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {
    
    @FXML private Button exitButton;
    
    @FXML private void onEasyClicked() throws Exception{
        openGameWindow(1);
    }
    @FXML private void onMediumClicked() throws Exception{
        openGameWindow(2);
    }
    @FXML private void onHardClicked() throws Exception{
        openGameWindow(3);
    }
    private void openGameWindow(int level) throws Exception{
        Stage stage = (Stage) exitButton.getScene().getWindow();
        
        FXMLLoader load = new FXMLLoader(getClass().getResource("GameWindow.fxml"));
        GameController gc = new GameController(level);
        load.setController(gc);
        Scene s = new Scene(load.load());
        //Stage gameStage = new Stage();
        //gameStage.setScene(s);
        //gameStage.show();
        stage.setScene(s);
        stage.show();
        gc.startGame();
    }
    @FXML
    private void onHighscoreClicked() throws Exception{
        Parent menu = FXMLLoader.load(getClass().getResource("HighscoreWindow.fxml"));
        Scene scene = new Scene(menu);
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void onExitClicked(){
        Stage s = (Stage) exitButton.getScene().getWindow();
        s.close();
    }
}
