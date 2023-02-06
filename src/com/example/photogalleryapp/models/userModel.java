package com.example.photogalleryapp.models;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class userModel implements java.io.Serializable{
	private String userName;
	private ArrayList<albumModel> albums;

	public userModel(String userName) {
		this.userName = userName;
		albums = new ArrayList<albumModel>();
	}

	public userModel(String userName, ArrayList<albumModel> albums) throws IOException {
		this.userName = userName;
		this.albums = albums;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String newUserName) {
		this.userName = newUserName;
	}
	
	public void addAlbum(albumModel album) {
		albums.add(album);
	}
	
	public void removeAlbum(albumModel album) {
		albums.remove(album);
	}
	
	public boolean albumNameInUse(String albumName) {
		for(albumModel album : albums) {
			if(albumName.equals(album.getAlbumName())) {
				return true;
			}
		}
		return false;	
	}
	
	public ArrayList<albumModel> getAlbums(){
		return albums;
	}
	
	public String toString() {
		return userName;
	}
}
