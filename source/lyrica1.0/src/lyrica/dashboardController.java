/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lyrica;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSlider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.stage.Window;

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
    
    
    static int currentSongIndex = 0;
    
    MediaPlayer player = null;
    @FXML
    private JFXButton pauseStartButton;
    
    @FXML
    private JFXProgressBar pBarLoading;
    
    @FXML
    private JFXSlider songSeek;
    
    @FXML
    private JFXButton previousButton;

    @FXML
    private JFXButton nextButton;
    
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
    private JFXButton stopButton;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            searchForSongs();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        
        
    
    Task task = new Task<Void>() {
            @Override public Void call() {
                while(true)
            {
                
                
            
                if(songSeek.isValueChanging())
            {
                double newSongTime = ((songSeek.getValue()/100)*currentSongDuration)*60000;
                player.seek(Duration.millis(newSongTime));
            }
                else
            {
                if(isSongAsigned)
            {
                double currentSongCurrentSeek = player.getCurrentTime().toMinutes();
                
                double seekProgress = (currentSongCurrentSeek/currentSongDuration)*100;
                
                songSeek.setValue(seekProgress);
            }
            }
                
                
                
                try
                {
                    Thread.sleep(150);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                
            }
                
                  
            }
        };
        new Thread(task).start();
        
        
        
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
            console.pln(Integer.toString(Lyrica.noOfSongs));
            console.pln("Song Names : ");
            int j;
            for(j=0; j<Lyrica.songList.size(); j++)
            {
                console.pln(Lyrica.songList.get(j).toString());
            }
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
        pauseStartButton.setText("Pause");
        songSeek.setOpacity(1);
        previousButton.setOpacity(1);
        nextButton.setOpacity(1);
        pauseStartButton.setOpacity(1);
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
         
        console.pln("asdasd");
        String fileNameAttr = "file://";
        if(System.getProperty("os.name").equals("Linux"))
        {
            fileNameAttr = "file://";
        }
        else
        {
            fileNameAttr = "file:///";
        }
        Media pick = new Media("file://"+fname);
        
        String tmpcurrentSongName = fileNameWithoutLocation.replace(".mp3","");
        currentSongName = tmpcurrentSongName.replace(".MP3","");
        if(isSongAlreadyPlaying)
        {
            player.stop();
        }
        player = new MediaPlayer(pick);
        
        
        player.setOnEndOfMedia(new Runnable() {
    @Override
    public void run() {
        nextButtonClicked();
        console.pln("asd");
    }
});
        
        player.setOnReady(new Runnable() {

        @Override
        public void run() {
            String tmpTitle = tmpcurrentSongName;
            String tmpAlbum = "Unknown";
            
            currentSongDuration = pick.getDuration().toMinutes();
            
            
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
            pauseStartButton.setText("Resume");
            isSongAlreadyPlaying = false;
            player.pause();
        }
        else
        {
            if(isSongAsigned)
            {
                pauseStartButton.setText("Pause");
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
            stopButton.setOpacity(0);
            pauseStartButton.setText("Start");
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
    public void aboutButtonClicked(ActionEvent event) throws Exception
    {
        if(player != null)
        {
            player.stop();
            isSongAsigned = false;
            isSongAlreadyPlaying = false;
        }
        new openFXML("about.fxml",true,event);
    }

}
