/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lyrica;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.util.Duration;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author debayan
 */
public class dashboardController implements Initializable {
    
    static boolean isSongAlreadyPlaying = false;
    static boolean isSongAsigned = false;
    static boolean isSeekBeingChanged = false;
    static String currentSongName = "";
    static String currentAlbumName = "";
    static String currentArtistName = "";
    static Double currentSongDuration = 0.00;
    
    boolean isFirstTimeUse = true;
    int c;
    boolean didPointCome = false;
    char currentChar;
    char currentChar2;
    char currentChar3;
    
    String tmpSongSeek = "";
    StringBuilder toBeShown = new StringBuilder();
    
    static int currentSongIndex = 0;
    
    MediaPlayer player = null;
    
    
    @FXML
    private AnchorPane mainPane;
    
    @FXML
    private AnchorPane aboutPane;
    
    @FXML
    private AnchorPane settingsPane;
    
    @FXML
    private JFXButton aboutButton;
    
    @FXML
    private JFXButton applyChangesButton;
    
    @FXML
    private Label artistLabel;
    
    @FXML
    private JFXButton pauseStartButton;
   
    @FXML
    private ImageView playPauseButtonImage;
    
    @FXML
    private JFXProgressBar pBarLoading;
    
    @FXML
    private Slider songSeek;
    
    @FXML
    private JFXButton previousButton;

    @FXML
    private JFXButton nextButton;
    
    @FXML
    private Label cL;
     
    @FXML
    private Label albumLabel;
    
    @FXML
    private Label loadingMusicFilesLabel;
    
    @FXML
    private Label itsLonelyHereLabel;
    
    @FXML
    private JFXButton checkForSongsButton;
    
    @FXML
    private Label itsLonelyHere2Label;
    
    @FXML
    private JFXListView songListview;
    
    @FXML
    private Label songNameLabel;
    
    @FXML
    private JFXButton settingsButton;
    
    @FXML
    private JFXButton stopButton;
    
    @FXML
    private JFXButton resetToDefaultButton;
    
    @FXML
    private JFXColorPicker colorChooser;
    
    @FXML
    private AnchorPane headerPane;
    
    @FXML
    private AnchorPane footerPane;
    
    @FXML
    private Label versionLabel;
    
    @FXML
    private Label totalMusicInfoLabel;
     String fileNameAttr = "file://";
     
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        versionLabel.setText("Version "+Lyrica.version);
        if(Lyrica.themeColor.equals("#7c43bd"))
        {
            resetToDefaultButton.setDisable(true);
        }
        else
        {
            resetToDefaultButton.setDisable(false);
        }
        colorChooser.setValue(Color.web(Lyrica.themeColor));
        headerPane.setStyle("-fx-background-color : "+Lyrica.themeColor);
        footerPane.setStyle("-fx-background-color : "+Lyrica.themeColor);
        if(System.getProperty("os.name").equals("Linux"))
        {
            fileNameAttr = "file://";
        }
        else
        {
            fileNameAttr = "file:///";
        }
        try
        {
            searchForSongs();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    refreshSeek();
    refreshSeek2(); 
       
        mainPane.toFront();
        
    }    
    
    public void refreshSeek2()
    {
        Task t2 = new Task<Void>() {
        @Override public Void call() {
            while(true)
            {
                if(songSeek.isValueChanging()== false && isSongAlreadyPlaying && isSongAsigned)
                {
                    double currentSongMinRaw = player.getCurrentTime().toMinutes();
                    String currentSongMinString = Double.toString(currentSongMinRaw);
                    StringBuilder toBeChecked  = new StringBuilder();
                    toBeChecked.append("0.");
                    int pointIndex = currentSongMinString.indexOf(".");
                    toBeChecked.append(currentSongMinString.charAt(pointIndex+1));
                    toBeChecked.append(currentSongMinString.charAt(pointIndex+2));
                    double secs = Double.parseDouble(toBeChecked.toString())*60.0;
                    
                    StringBuilder secString = new StringBuilder();
                    if (secs<10)
                    {
                        secString.append("0");
                        secString.append(Character.toString(Double.toString(secs).charAt(0)));
                    }
                    else if (secs>10)
                    {
                        secString.append(Character.toString(Double.toString(secs).charAt(0)));
                        secString.append(Character.toString(Double.toString(secs).charAt(1)));
                    }
                    int min = (int) (currentSongMinRaw + (currentSongMinRaw - Double.parseDouble(toBeChecked.toString())));
                    
                    
                    toBeShown = new StringBuilder();
                    toBeShown.append(min);
                    toBeShown.append(":");
                    toBeShown.append(secString.toString());
                       
                        
                        /*tmpSongSeek = Double.toString(player.getCurrentTime().toMinutes());
                        toBeShown = new StringBuilder();
                        boolean didPointCome = false;
                        for(int cx = 0;cx< tmpSongSeek.length(); cx++)
                        {
                            currentChar = tmpSongSeek.charAt(cx);
                            if(cx != tmpSongSeek.length()-1 && cx != tmpSongSeek.length()-2)
                            {
                                currentChar2 = tmpSongSeek.charAt(cx+1);
                                currentChar3 = tmpSongSeek.charAt(cx+2);
                            }
                            
                            if(tmpSongSeek.charAt(cx) == '.')
                            {
                    
                                didPointCome = true;
                                toBeShown.append(":");
                                toBeShown.append(tmpSongSeek.charAt(cx+1));
                                toBeShown.append(tmpSongSeek.charAt(cx+2));
                                break;
                            }
            
                if(didPointCome == false)
                {
                    toBeShown.append(tmpSongSeek.charAt(cx));
                }
                            
                                                        
                        }
                        
                        
                        didPointCome = false;*/
                        
                        Platform.runLater(new Runnable() {
    public void run() {
        cL.setText(toBeShown.toString());
    }
});
                    
                }
              
                
               
                
                
                try
                {
                    Thread.sleep(700);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
               
                }
                
                  
            }
        };
    Thread xx = new Thread(t2);
    xx.setDaemon(true);
    xx.start();
    }
    
    public void refreshSeek()
    {
            
        
    
    Task t2 = new Task<Void>() {
        @Override public Void call() {
            while(true)
            {
                if(songSeek.isValueChanging()== false && player!=null && isSongAsigned)
                {
                    
                        
                        double c2 = player.getCurrentTime().toMinutes();
               
                        double s2 = (c2/currentSongDuration)*100;
                        
                        songSeek.setValue(s2);
                        

                    
                }
              
                
               
                
                
                try
                {
                    Thread.sleep(300);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
               
                }
                
                  
            }
        };
    Thread xx = new Thread(t2);
    xx.setDaemon(true);
    xx.start();
     
    }
    
    @FXML
    public void searchForSongs() throws IOException
    {
        pBarLoading.setOpacity(1);
        loadingMusicFilesLabel.setOpacity(1);
        
        try 
        {
            File folder = new File(Lyrica.songDirectory);
            File[] listOfFiles = folder.listFiles();
            int i;
            int tmpNoOfSongs = 0;
            for(i = 0; i<listOfFiles.length ; i++)
            {
                if(listOfFiles[i].isFile())
                {
                    String tmpFileNameExtension = FilenameUtils.getExtension(listOfFiles[i].getName());
                    if (tmpFileNameExtension.equalsIgnoreCase("mp3"))
                    {
                        
                        Lyrica.songList.add(listOfFiles[i].getName());
                        tmpNoOfSongs++;
                    }
                }
            }
            
            Lyrica.noOfSongs = tmpNoOfSongs;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        pBarLoading.setOpacity(0);
        loadingMusicFilesLabel.setOpacity(0);
        
        if(Lyrica.noOfSongs == 0)
        {
            itsLonelyHereLabel.setOpacity(1);
            itsLonelyHere2Label.setOpacity(1);
            checkForSongsButton.setOpacity(1);
            itsLonelyHere2Label.setText("We were not able to search for songs in '"+Lyrica.songDirectory+"' ...\nPlease copy some songs over there");
        }
        else
        {
            
            checkForSongsButton.setDisable(true);
            songListview.getItems().clear();
            songListview.getItems().add("Song Name");
            int k;
            for(k=0; k<Lyrica.songList.size(); k++)
            {
                String nameToBeAdded = Lyrica.songList.get(k).toString().replace(".mp3","");
                nameToBeAdded = nameToBeAdded.replace(".MP3", "");
                songListview.getItems().add(nameToBeAdded);
            }
            songListview.setOpacity(1);
        }
    }
    
    @FXML
    public void playSelectedSong()
    {
        int selected = (songListview.getSelectionModel().getSelectedIndex() - 1);
        if(selected != -1)
        {
            
            console.pln("SELECTED : "+Lyrica.songList.get(selected));
            playSong(Lyrica.songDirectory+"/"+Lyrica.songList.get(selected), Lyrica.songList.get(selected).toString(), selected);
        }
        
        
    }
    
    public void playSong(String fileName,String fileNameWithoutLocation, int songIndex)
    {
        
        currentSongIndex = songIndex;
        playPauseButtonImage.setImage(new Image(dashboardController.class.getResourceAsStream("/resources/img/pause.png")));
        songSeek.setOpacity(1);
        previousButton.setOpacity(1);
        nextButton.setOpacity(1);
        cL.setOpacity(1);
        pauseStartButton.setOpacity(1);
        artistLabel.setOpacity(1);
        totalMusicInfoLabel.setOpacity(1);
        stopButton.setOpacity(1);
        String fname = "asds";
        
        try
        {
            String songName = fileName;
            String songNameToBeProcessed = "";
            int i;
            for(i = 0; i<songName.length(); i++)
            {
                if(songName.charAt(i) == '/' )
                {
                    songNameToBeProcessed += "/";
                }
                else if(Character.toString(songName.charAt(i)).equals("\\"))
                {
                    songNameToBeProcessed += Character.toString(songName.charAt(i));
                }
                else
                {
                    if(songName.charAt(i) == ' ')
                    {
                        songNameToBeProcessed += "%20";
                    }
                    else
                    {
                        songNameToBeProcessed+=URLEncoder.encode(Character.toString(songName.charAt(i)), "UTF-8");
                    }
                   
                }
            }
            fname = songNameToBeProcessed;
            console.pln(fname);
        }
            
        catch(Exception e)
        {
            e.printStackTrace();
        }
         

       
        Media pick = new Media(fileNameAttr+fname);
        
        String tmpcurrentSongName = fileNameWithoutLocation.replace(".mp3","");
        currentSongName = tmpcurrentSongName.replace(".MP3","");
        
        
        if(isSongAlreadyPlaying)
        {
            player.stop();
        }
        player = null;
        boolean wasAvailable = false;
        try
        {
            player = new MediaPlayer(pick);
            wasAvailable = true;
        }
        catch(Exception e)
        {
            console.pln("Unable to play music file...");
            wasAvailable = false;
        }
        
        
        
        player.setOnEndOfMedia(
                            new Runnable() {

        @Override
        public void run() {
            nextButtonClicked();
            }
                            }
                    );
        player.setOnReady(new Runnable() {

        @Override
        public void run() {
            String tmpTitle = tmpcurrentSongName;
            String tmpAlbum = "Unknown";
            String tmpArtist = "Unknown Artist";
            
            
            currentSongDuration = pick.getDuration().toMinutes();
            String currentSongMinString = Double.toString(currentSongDuration);
            StringBuilder toBeChecked  = new StringBuilder();
            toBeChecked.append("0.");
            int pointIndex = currentSongMinString.indexOf(".");
            toBeChecked.append(currentSongMinString.charAt(pointIndex+1));
            toBeChecked.append(currentSongMinString.charAt(pointIndex+2));
            double secs = Double.parseDouble(toBeChecked.toString())*60.0;
                    
            StringBuilder secString = new StringBuilder();
            if (secs<10)
            {
                secString.append("0");
                secString.append(Character.toString(Double.toString(secs).charAt(0)));
            }
            else if (secs>10)
            {
                secString.append(Character.toString(Double.toString(secs).charAt(0)));
                secString.append(Character.toString(Double.toString(secs).charAt(1)));
            }
            int min = (int) (currentSongDuration + (currentSongDuration - Double.parseDouble(toBeChecked.toString())));
           
            
            toBeShown = new StringBuilder();
            toBeShown.append(min);
            toBeShown.append(":");
            toBeShown.append(secString.toString());
                    
            totalMusicInfoLabel.setText(toBeShown.toString());
        
                console.pln("Song Duration :"+currentSongDuration);
                try
                {
                    tmpTitle = pick.getMetadata().get("title").toString();
                }
                catch(Exception e)
                {
                    tmpTitle = tmpcurrentSongName;
                }
            
                try
                {
                    tmpAlbum = pick.getMetadata().get("album").toString();
                }
                catch(Exception e)
                {
                    tmpAlbum = "Unknown";
                }
                
                try
                {
                    tmpArtist = pick.getMetadata().get("artist").toString();
                }
                catch(Exception e)
                {
                    tmpArtist = "Unknown Artist";
                }
                
                artistLabel.setText(tmpArtist);
                refreshSongInfo(tmpTitle, tmpAlbum);
                console.pln(pick.getMetadata().toString());
            }
        });
        player.play();
        isSongAlreadyPlaying = true;
        isSongAsigned = true;
    }
    
    @FXML
    public void pauseStartButtonClicked(ActionEvent event)
    {
        if(isSongAlreadyPlaying)
        {
            playPauseButtonImage.setImage(new Image(dashboardController.class.getResourceAsStream("/resources/img/play.png")));
            isSongAlreadyPlaying = false;
            player.pause();
        }
        else
        {
            if(isSongAsigned)
            {
                playPauseButtonImage.setImage(new Image(dashboardController.class.getResourceAsStream("/resources/img/pause.png")));
                player.play();
                isSongAlreadyPlaying = true;
            }
        }
    }
    
    public void refreshSongInfo(String titlePassed, String albumPassed)
    {
        songNameLabel.setText(titlePassed);
        albumLabel.setText(albumPassed);
    }
    
    @FXML
    public void stopButtonClicked(ActionEvent event)
    {
        if(isSongAsigned)
        {
            player.stop();
            player = null;
            isSongAlreadyPlaying = false;
            isSongAsigned = false;
            
            songSeek.setOpacity(0);
            previousButton.setOpacity(0);
            nextButton.setOpacity(0);
            pauseStartButton.setOpacity(0);
            cL.setOpacity(0);
            totalMusicInfoLabel.setOpacity(0);
            stopButton.setOpacity(0);
            playPauseButtonImage.setImage(new Image(dashboardController.class.getResourceAsStream("/resources/img/play.png")));
            refreshSongInfo("Select A Song","To Be Played");
        }
    }
    
    @FXML
    public void quitButtonClicked(ActionEvent event)
    {
        console.pln("Quitting...");
        System.exit(0);
    }
    
    @FXML
    public void selectAnotherSongDirectory(ActionEvent event)
    {
        new mentionSongDirectory().setVisible(true);
        Node node = (Node) event.getSource();
        Stage s= (Stage) node.getScene().getWindow();
        s.close();
    }
    
    @FXML
    public void nextButtonClicked()
    {
        int newSongIndex;
        String newSong;
        String songDirectory;
        if(currentSongIndex == Lyrica.songList.size() - 1)
        {
            // index 1
            newSongIndex = 0;
            newSong = Lyrica.songList.get(newSongIndex).toString();
            songDirectory = Lyrica.songDirectory;
        }
        else
        {
            newSongIndex = currentSongIndex +1;
            newSong = Lyrica.songList.get(newSongIndex).toString();
            songDirectory = Lyrica.songDirectory;
        }
        console.pln("NEXT BUTTON CLICKED");
        playSong(songDirectory+"/"+newSong,newSong,newSongIndex);
        songListview.getSelectionModel().select(newSongIndex+1);
        songListview.getFocusModel().focus(newSongIndex+1);
        songListview.scrollTo(newSongIndex+1);
    }
    
    
    @FXML
    public void previousButtonClicked()
    {
        int newSongIndex;
        String newSong;
        String songDirectory;
        if(currentSongIndex == 0)
        {
            // index 1
            newSongIndex = Lyrica.songList.size() - 1;
            newSong = Lyrica.songList.get(newSongIndex).toString();
            songDirectory = Lyrica.songDirectory;
        }
        else
        {
            newSongIndex = currentSongIndex -1;
            newSong = Lyrica.songList.get(newSongIndex).toString();
            songDirectory = Lyrica.songDirectory;
        }
        console.pln("PREVIOUS BUTTON CLICKED");
        playSong(songDirectory+"/"+newSong,newSong,newSongIndex);
        songListview.getSelectionModel().select(newSongIndex+1);
        songListview.getFocusModel().focus(newSongIndex+1);
        songListview.scrollTo(newSongIndex+1);
    }
    
    
    @FXML
    public void aboutButtonClicked(ActionEvent event)
    {
        mainPane.setStyle("-fx-opacity:0;");
        aboutPane.setStyle("-fx-opacity : 1;");
        settingsPane.toBack();
        aboutPane.toFront();
        aboutButton.setDisable(true);
        settingsButton.setDisable(true);
    }
    
    @FXML
    public void aboutReturnButtonClicked(ActionEvent event)
    {
        aboutPane.setStyle("-fx-opacity:0;");
        mainPane.setStyle("-fx-opacity:1;");
        mainPane.toFront();
        aboutButton.setDisable(false);
        settingsButton.setDisable(false);
    }
    
    @FXML
    public void newSongSeek()
    {
        double newSongTime = ((songSeek.getValue()/100)*currentSongDuration)*60000;
        player.seek(Duration.millis(newSongTime));
    }
    
    @FXML
    public void settingsButtonClicked(ActionEvent event)
    {
        mainPane.setStyle("-fx-opacity:0;");
        settingsPane.setStyle("-fx-opacity:1;");
        aboutButton.setDisable(true);
        settingsButton.setDisable(true);
        settingsPane.toFront();
    }
    
    @FXML
    public void settingsGoBackButtonClicked(ActionEvent event)
    {
        mainPane.setStyle("-fx-opacity:1;");
        settingsPane.setStyle("-fx-opacity:0;");
        aboutButton.setDisable(false);
        settingsButton.setDisable(false);
        mainPane.toFront();
    }
    
    
    @FXML
    private void colorChooserClicked(ActionEvent event)
    {
        applyChangesButton.setDisable(false);
    }
    
    @FXML
    private void changeThemeColorButtonClicked(ActionEvent event) throws Exception
    {
        String newThemeColor = colorChooser.getValue().toString();
        newThemeColor = newThemeColor.replace("0x","#");
        newThemeColor = newThemeColor.replace("ff","");
        
        console.pln("Writing to colorConfig.dx ...");
        
        filer.writeToFile("files/colorConfig.dx", newThemeColor);
        
        console.pln("...Done!");
        
        console.pln("Changing local Theme Color...");
        
        Lyrica.themeColor = newThemeColor;
        
        console.pln("...Done!");
        colorChooser.setValue(Color.web(Lyrica.themeColor));
        headerPane.setStyle("-fx-background-color : "+Lyrica.themeColor);
        footerPane.setStyle("-fx-background-color : "+Lyrica.themeColor);   
        resetToDefaultButton.setDisable(false);
        applyChangesButton.setDisable(true);
    }
    
    @FXML
    public void resetToDefaultButtonClicked(ActionEvent event) throws Exception
    {
        String newThemeColor = "#7c43bd";
        
        console.pln("Writing to colorConfig.dx ...");
        
        filer.writeToFile("files/colorConfig.dx", newThemeColor);
        
        console.pln("...Done!");
        
        console.pln("Changing local Theme Color...");
        
        Lyrica.themeColor = newThemeColor;
        
        console.pln("...Done!");
        
        colorChooser.setValue(Color.web(Lyrica.themeColor));
        headerPane.setStyle("-fx-background-color : "+Lyrica.themeColor);
        footerPane.setStyle("-fx-background-color : "+Lyrica.themeColor);  
        resetToDefaultButton.setDisable(true);
        applyChangesButton.setDisable(true);
    }

}
