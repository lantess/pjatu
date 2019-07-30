package pjatu;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoreLoader {
    final public int scoresNumber = 20;
    final private String[] filename;
    private ArrayList<String> scores;
    private File inputFile;
    private FileWriter outputFile;
    private int currTable;
    
    public ScoreLoader() {
        filename = new String[]{"easy.scr", "medium.scr", "hard.scr"};
        scores = new ArrayList<>();
    }
    
    public void loadTable(int i) throws Exception{
        if(scores.size()!=0)
            exportTable();
        scores.clear();
        importTable(i);
        currTable = i;
    }
    public void reloadTable() throws Exception{
        loadTable(currTable);
    }
    
    public String getScore(int i){
        return scores.get(i);
    }
    public void addNewScore(String result){
        for(int i = 0; i < scoresNumber; i++){
            if(result.compareTo(scores.get(i))<0){
                String tmp = scores.get(i);
                scores.set(i, result);
                result = tmp;
            }
        }
        try{
            reloadTable();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void importTable(int i) throws Exception{
            inputFile = new File(filename[i]);
            Scanner sc = new Scanner(inputFile);
            while(sc.hasNext()){
                scores.add(sc.nextLine());
            }
    }
    
    private void exportTable() throws Exception{
        outputFile = new FileWriter(filename[currTable]);
        for(int i = 0; i< scoresNumber; i++)
            outputFile.append(scores.get(i)+(
                    i==scoresNumber-1 ? "" : '\n'));
        outputFile.flush();
        outputFile.close();
    }
}
