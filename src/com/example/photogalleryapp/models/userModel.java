package com.example.photogalleryapp.models;

import java.io.*;
import java.util.ArrayList;

public class userModel {
	private String userName;
	private ArrayList<albumModel> albums;

	public userModel(String userName) {
		this.userName = userName;
		albums = new ArrayList<albumModel>();
	}

	public userModel(String userName, String file) throws IOException {
		this.userName = userName;
		albums = new ArrayList<albumModel>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		int albumNum = Integer.parseInt(br.readLine());
		String albumName;
		for(int i = 0; i < albumNum; i++) {		
			albumName = br.readLine();
			String albumFile = "src/model/data/" + userName + "/" + albumName + ".txt";
			try {
				albums.add(new albumModel(albumName, albumFile));
			}catch(IOException e) {
				System.out.println("album file doesn't exist");
			}
		}
		String next = br.readLine();
		if(next != null) {
			int singleTagNum = Integer.parseInt(next);
			for(int i = 0; i < singleTagNum; i++) {	
				String tag = br.readLine();
				if(!photoModel.singleValTagTypes.contains(tag)) {
					photoModel.singleValTagTypes.add(tag);
				}
			}
			int multiTagNum = Integer.parseInt(br.readLine());
			for(int i = 0; i < multiTagNum; i++) {	
				String tag = br.readLine();
				if(!photoModel.multiValTagTypes.contains(tag)) {
					photoModel.multiValTagTypes.add(tag);
				}
			}
		}		
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
	
	public void convert2TextFile() throws IOException {
		File userFile = new File("src/model/data/" + userName + "File.txt");
		userFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter("src/model/data/" + userName + "File.txt", false));
		writer.write(Integer.toString(albums.size()));
		writer.newLine();
		for(albumModel album : albums) {
			writer.write(album.getAlbumName());
			album.convert2TextFile(userName);
			writer.newLine();
		}
		writer.write(Integer.toString(photoModel.singleValTagTypes.size()));
		writer.newLine();
		for(String tag : photoModel.singleValTagTypes) {
			writer.write(tag);
			writer.newLine();
		}
		writer.write(Integer.toString(photoModel.multiValTagTypes.size()));
		writer.newLine();
		for(String tag : photoModel.multiValTagTypes) {
			writer.write(tag);
			writer.newLine();
		}
		writer.close();
	}

}
