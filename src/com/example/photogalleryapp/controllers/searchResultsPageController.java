package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.albumModel;
import com.example.photogalleryapp.models.photoModel;
import com.example.photogalleryapp.models.userModel;
import com.example.photogalleryapp.services.usersTextFileStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class searchResultsPageController {
	@FXML TilePane tile;
	@FXML TextField albumNameField;
	@FXML Label errorLabel;
	
	private userModel user;
	private Stage mainStage;
	private ArrayList<photoModel> photos;
	
	public void start(Stage mainStage, userModel user, ArrayList<photoModel> photos) throws FileNotFoundException{
		this.mainStage = mainStage;
		this.user = user;
		this.photos = photos;
		populatePhotoGallery();
	}	
	
	public void logout(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/loginPage.fxml"));
		GridPane root = (GridPane)loader.load();

		loginPageController loginPageController = loader.getController();
		loginPageController.start(mainStage);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void goBack(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/albumsPageView.fxml"));
		GridPane root = (GridPane)loader.load();
		
		albumsPageController albumsPageController = loader.getController();
		albumsPageController.start(mainStage, user);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void createAlbum(ActionEvent e) throws IOException{
		if(user.albumNameInUse(albumNameField.getText())) {
			errorLabel.setText("Album name has been used");
			return;
		}
		albumModel newAlbum = new albumModel(albumNameField.getText());
		newAlbum.setPhotos(photos);
		user.addAlbum(newAlbum);
		errorLabel.setText("Created Album " + albumNameField.getText());
	}
	
	private void populatePhotoGallery() throws FileNotFoundException {
		tile.setOrientation(Orientation.HORIZONTAL);
		for(photoModel photo : photos) {
			try {
				ImageView imageView = createPhotoImageView(photo);
				VBox vbox = new VBox();
				Label date = new Label(photo.getDate());	
				vbox.getChildren().addAll(imageView, date);
				tile.getChildren().addAll(vbox);
			}catch(FileNotFoundException e) {
				//System.out.println("FileNotFoundException");
			}
		}
	}
	
	private ImageView createPhotoImageView(photoModel photo) throws FileNotFoundException {
		final Image thumbNail = new Image(new FileInputStream(photo.getImageFile()), 200, 300, true, true);
		ImageView imageView = new ImageView(thumbNail);
		return imageView;
	}
}
