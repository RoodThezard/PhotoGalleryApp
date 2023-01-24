package com.example.photogalleryapp.controllers;

import com.example.photogalleryapp.models.albumModel;
import com.example.photogalleryapp.models.photoModel;
import com.example.photogalleryapp.models.userModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

public class searchPageController {
	@FXML TextField fromField;
	@FXML TextField toField;
	@FXML TextField tagsSearchField;
	@FXML Label errorLabel;
	
	private userModel user;
	private Stage mainStage;
	private Stage popupStage;
	
	public void start(Stage mainStage, Stage popupStage, userModel user) throws FileNotFoundException{
		this.popupStage = popupStage;
		this.mainStage = mainStage;
		this.user = user;
	}	
	
	public void search(ActionEvent e) throws IOException{
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = formatter.parse(fromField.getText());
			toDate =  formatter.parse(toField.getText());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			errorLabel.setText("Dates are formatted wrong");
			//e1.printStackTrace();
		}
		ArrayList<photoModel> photos = new ArrayList<photoModel>();
		for(albumModel album : user.getAlbums()) {
			for(photoModel photo : album.getPhotos()) {
				if(!photos.contains(photo)) {
					Date photoDate;
					try {
						photoDate = formatter.parse(photo.getDate());
						if(fromDate != null && toDate != null) {
							if(photoDate.after(fromDate) &&  photoDate.before(toDate) ) {
								if(tagsSearchField.getText().isEmpty() || checkTags(photo, tagsSearchField.getText())) {
									photos.add(photo);
								}			
							}
						}else if(!tagsSearchField.getText().isEmpty()) {
							if(checkTags(photo, tagsSearchField.getText())) {
								photos.add(photo);
							}
						}else {
							errorLabel.setText("No search parameters");
						}
						
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		searchResults(photos);
	}
	
	private void searchResults(ArrayList<photoModel> photos) {
		popupStage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/example/photogalleryapp/views/searchResultsPageView.fxml"));
		GridPane root;
		try {
			root = (GridPane)loader.load();
			searchResultsPageController searchResultsPageController = loader.getController();
			searchResultsPageController.start(mainStage, user, photos);

			Scene scene = new Scene(root);
			mainStage.setScene(scene);
			mainStage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private boolean checkTags(photoModel photo, String tags) {
		String[] tagsSearch = tags.split("\\s+");
		if(tagsSearch.length > 3 || tagsSearch.length == 2) {
			return false;
		}
		
		String[] tag1 = tagsSearch[0].split("=");
		if(tag1.length != 2) {
			return false;
		}
		Predicate<photoModel> pred1 = s -> s.containsTag(tag1[0], tag1[1]);
		
		if(tagsSearch.length == 3) {
			
			String[] tag2 = tagsSearch[2].split("=");
			if(tag2.length != 2) {
				return false;
			}
			Predicate<photoModel> pred2 = s -> s.containsTag(tag2[0], tag2[1]);
			if(tagsSearch[1].equals("OR")) {
				return pred1.or(pred2).test(photo);
			}else if(tagsSearch[1].equals("AND")) {
				return pred1.and(pred2).test(photo);
			}
		}else {
			return pred1.test(photo);
		}
		return false;
	}
}
