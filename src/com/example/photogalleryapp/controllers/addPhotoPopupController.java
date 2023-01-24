package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.albumModel;
import com.example.photogalleryapp.models.photoModel;
import com.example.photogalleryapp.models.userModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class addPhotoPopupController {
	@FXML TextField captionTextField;
	@FXML TextField tagValueField;
	@FXML ComboBox<String> tagNameComboBox;
	@FXML Label tagsLabel;
	@FXML Button uploadPhotoButton;
	
	private userModel user;
	private albumModel album;
	private Stage popupStage;
	private photoModel photo;
	private albumPageController prevAlbumPageController = null;
	private createAlbumPageController prevCreateAlbumPageController = null;
	
	public void start(Stage popupStage, userModel user, albumModel album) throws FileNotFoundException{
		this.popupStage = popupStage;
		this.user = user;
		this.album = album;
		popupStage.setOnCloseRequest((WindowEvent event) -> {
			photo = null;
		});
		createComboBox();
	}
	
	public void start(Stage popupStage, userModel user, albumModel album, albumPageController prev) throws FileNotFoundException{
		start(popupStage, user, album);
		this.prevAlbumPageController = prev;
	}
	
	public void start(Stage popupStage, userModel user, albumModel album, createAlbumPageController prev) throws FileNotFoundException{
		start(popupStage, user, album);
		this.prevCreateAlbumPageController = prev;
	}
	
	public void addPhoto(ActionEvent e) throws IOException{
		if(photo != null) {
			photo.setCaption(captionTextField.getText());
			album.addPhoto(photo);
			popupStage.close();
			if(prevAlbumPageController == null) {
				prevCreateAlbumPageController.refresh();
			}else {
				prevAlbumPageController.refresh();
			}
		}else {
			uploadPhotoButton.setStyle("-fx-text-fill: red;");
		}
	}
	
	public void uploadPhoto(ActionEvent e) throws IOException{
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("Image Files", Arrays.asList("*.bmp", "*.gif", "*.jpeg", "*.jpg", "*.png")));
		File f = fc.showOpenDialog(popupStage);
		 if (f != null) {
			 photo = new photoModel(f.getAbsolutePath());
			 uploadPhotoButton.setStyle("-fx-text-fill: black;");
		 }
	}
	
	public void addTag(ActionEvent e) throws IOException{
		String tagName = tagNameComboBox.getValue();
		String tagVal = tagValueField.getText();
		if(tagName != null && !(tagName.equals("New Single Val Tag") || tagName.equals("New Multi Val Tag"))){
			if(!tagVal.isEmpty() && photo != null) {
				photo.addTag(tagName, tagVal);
				displayTagsLabel();
			}
		}
	}
	
	private void createComboBox() {
		ArrayList<String> tagTypes = new ArrayList<String>();
		tagTypes.addAll(photoModel.singleValTagTypes);
		tagTypes.addAll(photoModel.multiValTagTypes);
		tagTypes.add("New Single Val Tag");
		tagTypes.add("New Multi Val Tag");
		tagNameComboBox.setItems(FXCollections.observableArrayList(tagTypes));
		tagNameComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal.equals("New Single Val Tag")) {
				TextInputDialog dialog = new TextInputDialog(album.getAlbumName());
				dialog.initOwner(popupStage); 
				dialog.setTitle("New Single Val Tag");
				dialog.setHeaderText("New Single Value Tag");
				dialog.setContentText("Create new tag: ");
				Optional<String> result = dialog.showAndWait();
				final Button check = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
				check.addEventFilter(ActionEvent.ACTION, (ActionEvent ev) ->{
					if(tagTypes.contains(result.get())) {
						ev.consume();
					}
				});
				if (result.isPresent()) { 
					tagTypes.add(0, result.get());
					photoModel.singleValTagTypes.add(result.get());
					tagNameComboBox.setItems(FXCollections.observableArrayList(tagTypes));
				}
			}else if(newVal.equals("New Multi Val Tag")) {
				TextInputDialog dialog = new TextInputDialog(album.getAlbumName());
				dialog.initOwner(popupStage); 
				dialog.setTitle("New Multi Val Tag");
				dialog.setHeaderText("New Multi Value Tag");
				dialog.setContentText("Create new tag: ");
				Optional<String> result = dialog.showAndWait();
				final Button check = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
				check.addEventFilter(ActionEvent.ACTION, (ActionEvent ev) ->{
					if(tagTypes.contains(result.get())) {
						ev.consume();
					}
				});
				if (result.isPresent()) { 
					tagTypes.add(0, result.get());
					photoModel.multiValTagTypes.add(result.get());
					tagNameComboBox.setItems(FXCollections.observableArrayList(tagTypes));
				}
			}
		});
	}
	
	private void displayTagsLabel() {
		String res = "";
		for(HashMap.Entry<String, ArrayList<String>> entry : photo.getTags().entrySet()) {
			res = res + entry.getKey() + "=" + String.join(", ", entry.getValue());
			res = res + System.lineSeparator();
		}
		tagsLabel.setText(res);
	}
}
