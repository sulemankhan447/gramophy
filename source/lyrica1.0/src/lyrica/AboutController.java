/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lyrica;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author debayan
 */
public class AboutController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    public Label versionLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        versionLabel.setText("Version "+Lyrica.version);
    }    
    
    @FXML
    public void returnButtonClicked(ActionEvent event) throws Exception
    {
        new openFXML("dashboard.fxml",true,event);
    }
    
}
