package pjatu;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameController {
    @FXML private GridPane gameField;
    @FXML private Button menuButton;
    @FXML private Text timer;
    
    protected boolean gameStatus;
    private String[] images = {"pjatu/res/img_easy.png","pjatu/res/img_medium.png","pjatu/res/img_hard.png"};
    private int xBlank = 0;
    private int yBlank = 0;
    
    private Thread timerThread;
    private StringProperty timeText = new SimpleStringProperty();
    private long startTime;
    
    protected int difficultyLevel;
    
    public GameController(int difficultyLevel){
        this.difficultyLevel=difficultyLevel;
        gameStatus = true;
    }
    
    @FXML void onMenuButton() throws Exception{
        Parent menu = FXMLLoader.load(getClass().getResource("MenuWindow.fxml"));
        Scene scene = new Scene(menu);
        Stage stage = (Stage) menuButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        gameStatus = false;
    }
    
    public void startGame(){
        createGamefield();
        loadImages();
        randomMovements();
        
        gameField.getScene().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.LEFT)
                makeMove(0);
            else if(e.getCode() == KeyCode.UP)
                makeMove(1);
            else if(e.getCode() == KeyCode.RIGHT)
                makeMove(2);
            else if(e.getCode() == KeyCode.DOWN)
                makeMove(3);
            
            if(checkField())
                endGame();
        });
        
        //timer.textProperty().bind(timeText);
        //timeText.setValue("(00:00:000)");
        startTime = System.nanoTime();
        
        timerThread = new Thread(() -> {
            while(gameStatus){
                long delta=System.nanoTime()-startTime;
                long mili = (delta / 1000000) % 1000;
                long sec = (delta / 1000000) / 1000 % 60;
                long min = (delta / 1000000) / 1000 / 60;
                String timeValue = "("+
                    (min%90 < 10 ? "0"+(min%90) : (min%90))+":"+
                    (sec < 10 ? "0"+sec : sec)+":"+
                    (mili > 100 ? mili :
                            (mili >= 10 ? "0"+mili : "00"+mili))
                    +")";
                //timer.setText(timeValue);
                timer.textProperty().unbind();
                timeText.setValue(timeValue);
                timer.textProperty().bind(timeText);
                try{
                    Thread.sleep(50);
                } catch (Exception e){
                    
                }
            }
        });
        timerThread.start();
    }
    
    private void createGamefield(){
        gameField.getColumnConstraints().clear();
        gameField.getRowConstraints().clear();
        double width = gameField.getWidth();
        double height = gameField.getHeight();
        int coro = difficultyLevel*3;
        for(int i = 1; i < coro; i++){
            gameField.getColumnConstraints().add(new ColumnConstraints(width/coro));
            gameField.getRowConstraints().add(new RowConstraints(height/coro));
        }
    }
    
    private void loadImages(){
        Image img = new Image(images[difficultyLevel-1], true);
        int coro = difficultyLevel*3;
        double width = 924.0/coro,
            height = 600.0/coro;
        for(int i = 0; i < difficultyLevel*3; i++){
                for(int j = 0; j < difficultyLevel*3; j++){
                    ImageView iv = imageField(i,j,width, height, img);
                    gameField.add(iv, i, j);
                }
            }
    }
    
    private ImageView imageField(int x, int y, double width, double height, Image img){
        ImageView iv = new ImageView();
        Rectangle2D rect = new Rectangle2D(x * width,
                                           y * height,
                                           width,
                                           height);
        iv.setImage(img);
        iv.setViewport(rect);
        iv.setId((""+x)+y);
        if(x==0 && y== 0)
            blackoutImage(iv);
        return iv;
    }
    
    private void blackoutImage(ImageView iv){
        ColorAdjust ca = new ColorAdjust();
        ca.setBrightness(-1.0);
        iv.setEffect(ca);
    }
    
    private void fillLastImage(){
        ImageView iv = (ImageView)getNode(0,0,gameField);
        iv.setEffect(new ColorAdjust());
    }
    
    private void makeMove(int dir){
        if(dir%2==0)
            makeXAxisMove(dir);
        else
            makeYAxisMove(dir);
    }
    
    private void makeXAxisMove(int dir){
        if(dir == 0 && xBlank > 0){
            ImageView first = (ImageView)getNode(xBlank,yBlank, gameField);
            ImageView second = (ImageView)getNode(xBlank-1,yBlank, gameField);
            swapViews(first,second);
            xBlank--;
        } else if (dir == 2 && xBlank < difficultyLevel*3-1){
            ImageView first = (ImageView)getNode(xBlank,yBlank, gameField);
            ImageView second = (ImageView)getNode(xBlank+1,yBlank, gameField);
            swapViews(first,second);
            xBlank++;
        }
    }
    
    private void makeYAxisMove(int dir){
        if(dir == 1 && yBlank > 0){
            ImageView first = (ImageView)getNode(xBlank,yBlank, gameField);
            ImageView second = (ImageView)getNode(xBlank,yBlank-1, gameField);
            swapViews(first,second);
            yBlank--;
        } else if (dir == 3 && yBlank < difficultyLevel*3-1){
            ImageView first = (ImageView)getNode(xBlank,yBlank, gameField);
            ImageView second = (ImageView)getNode(xBlank,yBlank+1, gameField);
            swapViews(first,second);
            yBlank++;
        }
    }
    
    private void swapViews(ImageView first, ImageView second){
        Rectangle2D tmpViewpoint = first.getViewport();
        first.setViewport(second.getViewport());
        second.setViewport(tmpViewpoint);
        
        Effect tmpCA = first.getEffect();
        first.setEffect(second.getEffect());
        second.setEffect(tmpCA);
        
        String tmpId = first.getId();
        first.setId(second.getId());
        second.setId(tmpId);
    }
    
    private Node getNode (int column, int row, GridPane gridPane) {
    Node result = null;
    ObservableList<Node> childrens = gridPane.getChildren();
    for (Node node : childrens) {
        if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
            result = node;
            break;
        }
    }

    return result;
    }
    
    private boolean checkField(){
        ImageView prev = (ImageView)getNode(0,0,gameField);
        for(int i = 0; i < difficultyLevel*3; i++){
            for(int j = 0; j < difficultyLevel*3; j++){
                if(i==0 & j == 0)
                    continue;
                ImageView tmp = (ImageView)getNode(i,j,gameField);
                if(prev.getId().compareTo(tmp.getId())>0)
                    return false;
                prev = tmp;
            }
        }
        return true;
    }
    
    private void randomMovements(){
        for(int i = 0; i < difficultyLevel*1; i++){
            makeMove((int)(Math.random()*4));
        }
    }
    
    private void endGame(){
        //TODO: timer stop
        gameStatus = false;
        fillLastImage();
        gameField.getScene().setOnKeyPressed(null);
        saveResult();
    }
    
    private void saveResult(){
        ScoreLoader sl = new ScoreLoader();
        try{
        sl.loadTable(difficultyLevel-1);
        sl.addNewScore(timeText.getValue());
        sl.reloadTable();}
        catch(Exception e){
            
        }
    }
    
}
