package com.example.photogalleryapp.models;

import java.io.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class albumModel implements java.io.Serializable{
	private String albumName;
	private ArrayList<photoModel> photos;
	private long beginDate = 0;
	private long endDate = Long.MAX_VALUE;
	public albumModel(String albumName) {
		this.albumName = albumName;
		photos = new ArrayList<photoModel>();
	}

	public albumModel(String albumName, ArrayList<photoModel> photos) throws IOException {
		this.albumName = albumName;
		this.photos = photos;
	}
	
	public void addPhoto(photoModel photo) {
		photos.add(photo);
		long lastModified = photo.getImageFile().lastModified();
		if(lastModified >= beginDate){
			beginDate = lastModified;
		}
		if(lastModified <= endDate) {
			endDate = lastModified;
		}
	}
	
	public void removePhoto(photoModel photo) {
		photos.remove(photo);
	}
	
	public ArrayList<photoModel> getPhotos() {
		return photos;
	}
	
	public void setPhotos(ArrayList<photoModel> photos) {
		for(photoModel photo : photos) {
			addPhoto(photo);
		}
	}
	
	public String getAlbumName() {
		return albumName;
	}
	
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	
	public String getDateRange() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
	    String beginDateStr = formatter.format(beginDate); 
	    String endDateStr = formatter.format(endDate);
	    if(endDate == Long.MAX_VALUE) {
	    	endDateStr = "--/--/--";
	    }
	    if(beginDate == 0) {
	    	beginDateStr = "--/--/--";
	    }
	    return beginDateStr + " - " + endDateStr;
	}
}

