package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.albumModel;
import com.example.photogalleryapp.models.photoModel;
import com.example.photogalleryapp.models.userModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class photoPageController {
	@FXML TextField captionTextField;
	@FXML TextField tagValueField;
	@FXML ListView<String> listView;
	@FXML ComboBox<String> tagNameComboBox;
	@FXML ImageView imageView;
	@FXML Label dateLabel;

	private userModel user;
	private albumModel album;
	private photoModel photo;
	private Stage mainStage;
	private ArrayList<String> tagList;
	
	public void start(Stage mainStage, userModel user, albumModel album, photoModel photo) throws FileNotFoundException{
		this.mainStage = mainStage;
		this.user = user;
		this.album = album;
		this.photo = photo;
		final Image image = new Image(new FileInputStream(photo.getImageFile()), 200, 300, true, true);
		imageView.setImage(image);
		tagList = new ArrayList<String>();
		captionTextField.setText(photo.getCaption());
		dateLabel.setText(photo.getDate());
		populateTagList();
		createComboBox();
		captionTextField.textProperty().addListener((obs, oldVal, newVal) -> 
			photo.setCaption(newVal)
        );
	}	
	
	public void openTagContextMenu(MouseEvent e) throws IOException{
		if(e.getButton() == MouseButton.SECONDARY) {
			ContextMenu tagMenu = new ContextMenu();
			MenuItem item1 = new MenuItem("Delete");
			tagMenu.getItems().addAll(item1);
			item1.setOnAction((ActionEvent e1) ->{
				ObservableList<String> selected = listView.getSelectionModel().getSelectedItems();
				for(String str : selected) {
					String[] tag = str.split("=", 2);
					photo.deleteTag(tag[0], tag[1]);
					populateTagList();
				}
			});
		}
	}
	
	public void addTag(ActionEvent e) throws IOException{
		String tagName = tagNameComboBox.getValue();
		String tagVal = tagValueField.getText();
		if(tagName != null && !(tagName.equals("New Single Val Tag") || tagName.equals("New Multi Val Tag"))){
			if(!tagVal.isEmpty() && photo != null) {
				photo.addTag(tagName, tagVal);
				populateTagList();
			}
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
	
	private void createComboBox() {
		ArrayList<String> tagTypes = new ArrayList<String>();
		tagTypes.addAll(photoModel.singleValTagTypes);
		tagTypes.addAll(photoModel.multiValTagTypes);
		tagTypes.add("New Single Val Tag");
		tagTypes.add("New Multi Val Tag");
		tagNameComboBox.setItems(FXCollections.observableArrayList(tagTypes));
		tagNameComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal.equals("New Single Val Tag")) {
				TextInputDialog dialog = new TextInputDialog();
				dialog.initOwner(mainStage); 
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
				TextInputDialog dialog = new TextInputDialog();
				dialog.initOwner(mainStage); 
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
	
	private void populateTagList() {
		for(HashMap.Entry<String, ArrayList<String>> entry : photo.getTags().entrySet()) {
			for(String val : entry.getValue()) {
				tagList.add(entry.getKey() + "=" + val);
			}
		}
		listView.setItems(FXCollections.observableArrayList(tagList));
	}
}
