package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.albumModel;
import com.example.photogalleryapp.models.photoModel;
import com.example.photogalleryapp.models.userModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class slideShowPageController {
	@FXML ImageView imageView;
	
	private userModel user;
	private albumModel album;
	private Stage mainStage;
	private ArrayList<Image> images;
	private int index;
	
	public void start(Stage mainStage, userModel user, albumModel album) throws FileNotFoundException{
		this.mainStage = mainStage;
		this.user = user;
		this.album = album;
		index = 0;
		images = new ArrayList<Image>();
		for(photoModel photo : album.getPhotos()) {
			images.add(new Image(new FileInputStream(photo.getImageFile()), 200, 300, true, true));
		}
		imageView.setImage(images.get(0));
	}
	
	public void next(ActionEvent e) throws IOException{
		if(index + 1 < images.size()) {
			imageView.setImage(images.get(index + 1));
			index = index + 1;
		}
	}
	
	public void previous(ActionEvent e) throws IOException{
		if(index - 1 >= 0) {
			imageView.setImage(images.get(index - 1));
			index = index - 1;
		}
	}
	
	public void goBack(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/albumPageView.fxml"));
		GridPane root = (GridPane)loader.load();

		albumPageController albumPageController = loader.getController();
		albumPageController.start(mainStage, user, album);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void logout(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/loginPage.fxml"));
		GridPane root = (GridPane)loader.load();
		
		user.convert2TextFile();
		
		loginPageController loginPageController = loader.getController();
		loginPageController.start(mainStage);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
}
