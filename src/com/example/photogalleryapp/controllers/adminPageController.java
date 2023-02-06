package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.userModel;
import com.example.photogalleryapp.services.usersSerializer;
import com.example.photogalleryapp.services.usersTextFileStream;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class adminPageController {
	@FXML ListView<userModel> userListView;
	@FXML TextField usernameTextField;
	
	private ArrayList<userModel> users;
	private Stage mainStage;
	
	public void start(Stage mainStage, ArrayList<userModel> users) throws FileNotFoundException{
		this.mainStage = mainStage;
		this.users = users;
		userListView.setItems(FXCollections.observableArrayList(users));
	}	
	
	public void logout(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/loginPageView.fxml"));
		GridPane root = (GridPane)loader.load();

		try {
			usersSerializer.storeData(users);
		}
		catch(Exception c) {
			usersTextFileStream.storeData(users);
		}
		
		loginPageController loginPageController = loader.getController();
		loginPageController.start(mainStage);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void delete (ActionEvent event) throws IOException {
		userModel selectedUser = userListView.getSelectionModel().getSelectedItem();
		if(selectedUser != null) {
			users.remove(selectedUser);
			userListView.setItems(FXCollections.observableArrayList(users));
		}
	}
	
	
	public void register (ActionEvent event) throws IOException {
		for(userModel user : users) {
			if(usernameTextField.getText().equals(user.getUserName())) {
				return;
			}
		}
		users.add(new userModel(usernameTextField.getText()));
		userListView.setItems(FXCollections.observableArrayList(users));
	}
	
	
}
	