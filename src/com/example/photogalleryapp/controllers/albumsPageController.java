package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.albumModel;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class albumsPageController {
	@FXML TilePane tile;
	
	private userModel user;
	private Stage mainStage;
	private HashMap<ImageView, albumModel> galleryMap;
	
	public void start(Stage mainStage, userModel user) throws FileNotFoundException{
		this.mainStage = mainStage;
		this.user = user;
		populateAlbumGallery();
	}	
	
	public void createAlbum(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/createAlbumPageView.fxml"));
		GridPane root = (GridPane)loader.load();

		photosFileIO.convertUserToTextFile(user);
		
		createAlbumPageController createAlbumPageController = loader.getController();
		createAlbumPageController.start(mainStage, user);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void logout(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/loginPageView.fxml"));
		GridPane root = (GridPane)loader.load();
		
		
		loginPageController loginPageController = loader.getController();
		loginPageController.start(mainStage);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void search(ActionEvent e) throws IOException{
		Stage popupStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/searchPageView.fxml"));
		GridPane root = (GridPane)loader.load();
		
		searchPageController searchPageController = loader.getController();
		searchPageController.start(mainStage, popupStage, user);

		Scene scene = new Scene(root);
		popupStage.setScene(scene);
		popupStage.show();
	}
	
	private void populateAlbumGallery() throws FileNotFoundException {
		tile.setOrientation(Orientation.HORIZONTAL);
		galleryMap = new HashMap<ImageView, albumModel>();
		for(albumModel album : user.getAlbums()) {
			ImageView imageView = createAlbumImageView(album);
			galleryMap.put(imageView, album);
			VBox vbox = new VBox();
			Label name = new Label(album.getAlbumName());
			Label photoNum = new Label(Integer.toString(album.getPhotos().size()) + " photos");
			Label dateRange = new Label(album.getDateRange());		
			vbox.getChildren().addAll(imageView, name, photoNum, dateRange);
			tile.getChildren().addAll(vbox);
		}
	}
	
	private ImageView createAlbumImageView(albumModel album) throws FileNotFoundException {
		final Image thumbNail;
		if(album.getPhotos().size() == 0) {
			thumbNail = new Image("/resources/stockPhotos/friends.jpg", 100, 150, true, true);

		}else {
			thumbNail = new Image(new FileInputStream(album.getPhotos().get(0).getImageFile()), 100, 150, true, true);

		}
		ImageView imageView = new ImageView(thumbNail);
		ContextMenu albumMenu = createAlbumContextMenu(album);

		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				albumModel selectedAlbum = galleryMap.get(e.getSource());
				if(e.getButton() == MouseButton.PRIMARY) {
					//Open album	
					try {
						openAlbum(selectedAlbum);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
					}
				}else if(e.getButton() == MouseButton.SECONDARY) {
					albumMenu.show(imageView, e.getScreenX(), e.getScreenY());
				}
			}
		});
		return imageView;
	}
	
	private ContextMenu createAlbumContextMenu(albumModel album) {
		ContextMenu albumMenu = new ContextMenu();
		MenuItem item1 = new MenuItem("Rename");
		MenuItem item2 = new MenuItem("Delete");
		albumMenu.getItems().addAll(item1, item2);
		
		item1.setOnAction((ActionEvent e) ->{
			TextInputDialog dialog = new TextInputDialog(album.getAlbumName());
			dialog.initOwner(mainStage); 
			dialog.setTitle("Rename " + album.getAlbumName());
			Optional<String> result = dialog.showAndWait();
			final Button check = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
			check.addEventFilter(ActionEvent.ACTION, (ActionEvent ev) ->{
				if(user.albumNameInUse(result.get())) {
					ev.consume();
				}
			});
			if (result.isPresent()) { 
				album.setAlbumName(result.get()); 
				try {
					tile.getChildren().clear();
					populateAlbumGallery();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		item2.setOnAction((ActionEvent e) ->{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete " + album.getAlbumName());
			alert.setHeaderText("Are you sure you want to delete " + album.getAlbumName() + "?");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK){
			    user.removeAlbum(album);
			    try {
					tile.getChildren().clear();
					populateAlbumGallery();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		return albumMenu;
	}
	
	
	private void openAlbum(albumModel album) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/albumPageView.fxml"));
		GridPane root = (GridPane)loader.load();

		albumPageController albumPageController = loader.getController();
		albumPageController.start(mainStage, user, album);

		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	private void openAlbumContextMenu() {
		
	}
}
