package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.userModel;
import com.example.photogalleryapp.services.photosFileIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

public class loginPageController {
	@FXML Label errorLabel;
	@FXML TextField usernameTextField;
	@FXML Button loginButton;
	
	ArrayList<userModel> users;
	
	private Stage mainStage;
	
	public void start(Stage mainStage) throws IOException {
		this.mainStage = mainStage;
		populateUsersList();
		mainStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
	}	
	
	public void login(ActionEvent e) throws IOException{
		if(!usernameTextField.getText().trim().isEmpty()) {
			String username = usernameTextField.getText();
			Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			if(username.equals("admin")) {
				switch2AdminSubSystem();
				return;
			}
			for(int i = 0; i < users.size(); i++) {
				if(username.equals(users.get(i).getUserName())) {
					switch2UserSubSystem(users.get(i));
					return;
				}
			}
			errorLabel.setText("Incorrect Username");
		}
	}
	
	public void storeData() {
		try {
			photosFileIO.storeData(users);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void switch2UserSubSystem(userModel user) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/albumsPageView.fxml"));
		GridPane root = (GridPane)loader.load();
		
		albumsPageController albumsPageController = loader.getController();
		albumsPageController.start(mainStage, user);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	private void switch2AdminSubSystem() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/adminPageView.fxml"));
		GridPane root = (GridPane)loader.load();
		
		adminPageController adminPageController = loader.getController();
		adminPageController.start(mainStage, users);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	private void populateUsersList(){
		try {
			users = photosFileIO.getData();
		}catch(IOException e) {
			users = new ArrayList<userModel>();
		}
		
	}
	
	private void closeWindowEvent(WindowEvent event) {
		/*TODO call SongLibraryFileIO.songListToFile() with songList as argument
		 * 
		 */
		try {
			photosFileIO.storeData(users);
        }
        catch(Exception c) {
            return;
        }
	}
}
