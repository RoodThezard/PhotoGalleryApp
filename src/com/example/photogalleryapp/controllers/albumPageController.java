package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.albumModel;
import com.example.photogalleryapp.models.photoModel;
import com.example.photogalleryapp.models.userModel;
import com.example.photogalleryapp.services.photosFileIO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class albumPageController {
	@FXML TilePane tile;
	@FXML Label albumNameLabel;
	
	private userModel user;
	private albumModel album;
	private Stage mainStage;
	private HashMap<ImageView, photoModel> galleryMap;
	
	public void start(Stage mainStage, userModel user, albumModel album) throws FileNotFoundException{
		this.mainStage = mainStage;
		this.user = user;
		this.album = album;
		albumNameLabel.setText(album.getAlbumName());
		populatePhotoGallery();
	}	
	
	public void logout(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/loginPageView.fxml"));
		GridPane root = loader.load();

		photosFileIO.convertUserToTextFile(user);
		
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
	
	public void slideShow(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/slideShowPageView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		slideShowPageController slideShowPageController = loader.getController();
		slideShowPageController.start(mainStage, user, album);
		
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
		addPhotoPopupController.start(popupStage, user, album, this);

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
		galleryMap = new HashMap<ImageView, photoModel>();
		for(photoModel photo : album.getPhotos()) {
			try {
				ImageView imageView = createPhotoImageView(photo);
				galleryMap.put(imageView, photo);
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
		ContextMenu photoMenu = createPhotoContextMenu(photo);

		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(e.getButton() == MouseButton.PRIMARY) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/photoPageView.fxml"));
					GridPane root;
					try {
						root = (GridPane)loader.load();
						photoPageController photoPageController = loader.getController();
						photoPageController.start(mainStage, user, album, photo);
						
						Scene scene = new Scene(root);
						mainStage.setScene(scene);
						mainStage.show();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else if(e.getButton() == MouseButton.SECONDARY) {
					photoMenu.show(imageView, e.getScreenX(), e.getScreenY());
				}
			}
		});
		return imageView;
	}
	
	private ContextMenu createPhotoContextMenu(photoModel photo) {
		ContextMenu photoMenu = new ContextMenu();
		MenuItem item1 = new MenuItem("Move to other album");
		MenuItem item2 = new MenuItem("Copy to other album");
		MenuItem item3 = new MenuItem("Delete");
		photoMenu.getItems().addAll(item1, item2, item3);
		
		item1.setOnAction((ActionEvent e) ->{
			Stage popupStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/moveToAlbumPopupView.fxml"));
			Pane root;
			try {
				root = (Pane)loader.load();
				moveToAlbumPopupController moveToAlbumPopupController = loader.getController();
				moveToAlbumPopupController.start(popupStage, user, album, photo, this);

				Scene scene = new Scene(root);
				popupStage.setScene(scene);
				popupStage.show();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			});
		
		item2.setOnAction((ActionEvent e) ->{
			Stage popupStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/copyToAlbumPopupView.fxml"));
			Pane root;
			try {
				root = (Pane)loader.load();
				copyToAlbumPopupController copyToAlbumPopupController = loader.getController();
				copyToAlbumPopupController.start(popupStage, user, album, photo, this);

				Scene scene = new Scene(root);
				popupStage.setScene(scene);
				popupStage.show();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		item3.setOnAction((ActionEvent e) ->{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Photo");
			alert.setHeaderText("Are you sure you want to delete this photo?");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK){
			    album.removePhoto(photo);
			    refresh();
			}
		});
		
		return photoMenu;
	}
}
