package pjatu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class HighscoreController {
    
    private ScoreLoader sl = new ScoreLoader();
    
    @FXML private Button easyButton;
    @FXML private Button mediumButton;
    @FXML private Button hardButton;
    
    @FXML private Text firstPlace;
    @FXML private Text secondPlace;
    @FXML private Text thirdPlace;
    @FXML private VBox places;
    
    @FXML private void onExitButton() throws Exception{
        Parent menu = FXMLLoader.load(getClass().getResource("MenuWindow.fxml"));
        Scene scene = new Scene(menu);
        Stage stage = (Stage) easyButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        
    }
    @FXML private void onEasyButton() throws Exception{
        sl.loadTable(0);
        setButtonsStatus("clicked", "notClicked", "notClicked");
        reloadScores();
    }
    @FXML private void onMediumButton() throws Exception{
        sl.loadTable(1);
        setButtonsStatus("notClicked", "clicked", "notClicked");
        reloadScores();
    }
    @FXML private void onHardButton() throws Exception{
        sl.loadTable(2);
        setButtonsStatus("notClicked", "notClicked", "clicked");
        reloadScores();
    }
    private void setButtonsStatus(String easy, String medium, String hard){
        easyButton.getStyleClass().remove(1);
        easyButton.getStyleClass().add(easy);
        mediumButton.getStyleClass().remove(1);
        mediumButton.getStyleClass().add(medium);
        hardButton.getStyleClass().remove(1);
        hardButton.getStyleClass().add(hard);
    }
    private void reloadScores(){
        firstPlace.setText(sl.getScore(0));
        secondPlace.setText(sl.getScore(1));
        thirdPlace.setText(sl.getScore(2));
        places.getChildren().clear();
        for(int i = 3; i < sl.scoresNumber; i++){
            Text text = new Text();
            text.setText(sl.getScore(i));
            text.setFont(Font.font("Franklin Gothic Demi", 18));
            text.setTextAlignment(TextAlignment.CENTER);
            text.setWrappingWidth(400.0);
            places.getChildren().add(text);
        }
    }
    
}
