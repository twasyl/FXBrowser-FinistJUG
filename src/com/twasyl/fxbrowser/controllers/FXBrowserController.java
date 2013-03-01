package com.twasyl.fxbrowser.controllers;

import com.twasyl.fxbrowser.app.FXBrowser;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author twasyl
 */
public class FXBrowserController implements Initializable {

    @FXML
    private TextField urlField;
    @FXML
    private WebView presentationView;


    @FXML
    public void openUrl(ActionEvent event) {
        System.out.println("URL entered: " + this.urlField.getText());
        try {
            URL url = new URL(this.urlField.getText());
            loadContent(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void openPresentation(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File presentationFile = fileChooser.showOpenDialog(null);

        if (presentationFile != null) {
            try {
                URL url = new URL("file", "", presentationFile.getAbsolutePath());
                loadContent(url);
            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void loadContent(URL url) {
        this.presentationView.getEngine().load(url.toString());
    }

    @FXML
    public void urlTextFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openUrl(null);
        }
    }

    @FXML
    public void webViewDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
    }

    @FXML
    public void webViewDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean dropCompleted = false;

        if (db.hasFiles()) {
            File draggedFile = db.getFiles().get(0);

            if (draggedFile.getName().toLowerCase().endsWith(".html")) {
                try {
                    URL url = new URL("file", "", draggedFile.getAbsolutePath());
                    loadContent(url);
                    dropCompleted = true;
                } catch (MalformedURLException ex) {
                }
            }
        }

        event.setDropCompleted(dropCompleted);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StringProperty appName = new SimpleStringProperty("FXBrowser");
        StringExpression appTitle = appName.concat(" - ");
        appTitle = appTitle.concat(this.presentationView.getEngine().titleProperty());

        FXBrowser.getCurrentStage().titleProperty().bind(appTitle);
    }
}
