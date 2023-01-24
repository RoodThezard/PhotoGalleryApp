package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.albumModel;
import com.example.photogalleryapp.models.photoModel;
import com.example.photogalleryapp.models.userModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class copyToAlbumPopupController {
	@FXML ListView<albumModel> albumListView;
	@FXML Label errorLabel;
	
	private userModel user;
	private albumModel album;
	private Stage popupStage;
	private photoModel photo;
	private albumPageController prevAlbumPageController = null;
	private ObservableList<albumModel> albumList;
	
	public void start(Stage popupStage, userModel user, albumModel album, photoModel photo, albumPageController prev) throws FileNotFoundException{
		this.popupStage = popupStage;
		this.user = user;
		this.album = album;
		this.photo = photo;
		this.prevAlbumPageController = prev;
		
		albumList = FXCollections.observableArrayList(user.getAlbums());
		albumList.remove(album);
		albumListView.setItems(albumList);
		albumListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		albumListView.setCellFactory(param -> new ListCell<albumModel>() {
		    @Override
		    protected void updateItem(albumModel item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null || item.getAlbumName() == null) {
		            setText(null);
		        } else {
		            setText(item.getAlbumName());
		        }
		    }
		});
		
	}
	
	public void done(ActionEvent e) throws IOException{
		ObservableList<albumModel> selected = albumListView.getSelectionModel().getSelectedItems();
		if(selected.size() == 0) {
			errorLabel.setText("Please select an album");
			return;
		}
		for(albumModel album : selected) {
			album.addPhoto(photo);
		}
		popupStage.close();
		prevAlbumPageController.refresh();
	}
	
	public void cancel(ActionEvent e) throws IOException{
		popupStage.close();
	}
	
}
