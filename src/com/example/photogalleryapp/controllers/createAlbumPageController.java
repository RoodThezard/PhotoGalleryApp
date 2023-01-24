package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.albumModel;
import com.example.photogalleryapp.models.photoModel;
import com.example.photogalleryapp.models.userModel;
import com.example.photogalleryapp.services.photosFileIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class createAlbumPageController {
	@FXML TextField albumNameField;
	@FXML TilePane tile;
	@FXML Button addPhotoButton;
	@FXML Button cancelButton;
	@FXML Button createAlbumButton;
	@FXML Button logoutButton;
	@FXML Label errorLabel;
	
	private userModel user;
	private Stage mainStage; 
	private albumModel newAlbum;
	
	public void start(Stage mainStage, userModel user) throws FileNotFoundException{
		this.mainStage = mainStage;
		this.user = user;
		newAlbum = new albumModel("default");
		populatePhotoGallery();
	}
	
	public void cancel(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/albumsPageView.fxml"));
		GridPane root = (GridPane)loader.load();
		
		albumsPageController albumsPageController = loader.getController();
		albumsPageController.start(mainStage, user);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void logout(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/loginPage.fxml"));
		GridPane root = (GridPane)loader.load();

		photosFileIO.convertUserToTextFile(user);
		
		loginPageController loginPageController = loader.getController();
		loginPageController.start(mainStage);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void submit(ActionEvent e) throws IOException{
		if(user.albumNameInUse(albumNameField.getText())) {
			errorLabel.setText("Album name has been used");
			newAlbum = null;
			return;
		}

		newAlbum.setAlbumName(albumNameField.getText());
		user.addAlbum(newAlbum);
		photosFileIO.convertUserToTextFile(user);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/albumsPageView.fxml"));
		GridPane root = (GridPane)loader.load();
		
		albumsPageController albumsPageController = loader.getController();
		albumsPageController.start(mainStage, user);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void addPhoto(ActionEvent e) throws IOException{
		Stage popupStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/addPhotoPopupView.fxml"));
		Pane root = (Pane)loader.load();
		
		addPhotoPopupController addPhotoPopupController = loader.getController();
		addPhotoPopupController.start(popupStage, user, newAlbum, this);
		
		Scene scene = new Scene(root);
		popupStage.setScene(scene);
		popupStage.show();
	}
	
	public void refresh() {
		tile.getChildren().clear();
		try {
			populatePhotoGallery();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void populatePhotoGallery() throws FileNotFoundException {
		tile.setOrientation(Orientation.HORIZONTAL);
		for(photoModel photo : newAlbum.getPhotos()) {
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
		final Image thumbNail = new Image(new FileInputStream(photo.getImageFile()), 100, 150, true, true);
		ImageView imageView = new ImageView(thumbNail);
		return imageView;
	}
}
