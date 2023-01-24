package com.example.photogalleryapp.services;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.example.photogalleryapp.models.albumModel;
import com.example.photogalleryapp.models.photoModel;
import com.example.photogalleryapp.models.userModel;

public class photosFileIO {
	private static String dataDir = "./src/com/example/photogalleryapp/data/";

	public static ArrayList<userModel> getData() throws IOException {
		ArrayList<userModel> users = new ArrayList<userModel>();
		BufferedReader br = new BufferedReader(new FileReader(dataDir + "usersFile.txt"));
		String userName;
		while ((userName = br.readLine()) != null) {
			String userFile = dataDir + userName + "File.txt";
			try {
				users.add(createUserFromFile(userName, userFile));
			}catch(IOException e) {
				System.out.println("User file doesn't exist");
			}
			
		}
		return users;
	}
	
	public static void storeData(ArrayList<userModel> users) throws IOException {
		File[] files = new File(dataDir).listFiles();
		if(files != null) {
			for(File f : files) {
	            f.delete();
			}
		}
		File usersFile = new File(dataDir + "usersFile.txt");
		usersFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataDir + "usersFile.txt", false));
		for(userModel user : users) {
			writer.write(user.getUserName());
			writer.newLine();
			try {
				convertUserToTextFile(user);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("exception");
				e.printStackTrace();
			}
		}
		writer.close();
	}

	public static void convertUserToTextFile(userModel user) throws IOException {
		ArrayList<albumModel> albums = user.getAlbums();
		String userName = user.getUserName();

		File userFile = new File(dataDir + user.getUserName() + "File.txt");
		userFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataDir + userName + "File.txt", false));
		writer.write(Integer.toString(albums.size()));
		writer.newLine();
		for(albumModel album : albums) {
			writer.write(album.getAlbumName());
			convertAlbumToTextFile(album, userName);
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

	public static void convertAlbumToTextFile(albumModel album, String userName) throws IOException {
		ArrayList<photoModel> photos = album.getPhotos();
		String albumName = album.getAlbumName();
		File dir = new File(dataDir + userName + "/");
		dir.mkdirs();
		File albumFile = new File(dir,albumName + ".txt");
		albumFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataDir + userName + "/"  +  albumName + ".txt", false));
		for(photoModel photo : photos) {
			writer.write(photo.toString());
			writer.newLine();
		}
		writer.close();

	}
	private static userModel createUserFromFile(String userName, String file) throws IOException{
		ArrayList<albumModel> albums = new ArrayList<albumModel>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		int albumNum = Integer.parseInt(br.readLine());
		String albumName;
		for(int i = 0; i < albumNum; i++) {
			albumName = br.readLine();
			String albumFile = dataDir + userName + "/" + albumName + ".txt";
			try {
				albums.add(createAlbumFromTextFile(albumName, albumFile));
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
		userModel user = new userModel(userName, albums);
		return user;
	}

	private static albumModel createAlbumFromTextFile(String albumName, String file) throws IOException {
		ArrayList<photoModel> photos = new ArrayList<photoModel>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		while ((st = br.readLine()) != null) {
			String url = st;
			String caption = br.readLine();
			int tagNum = Integer.parseInt(br.readLine());
			HashMap<String, ArrayList<String>> tags = new HashMap<String, ArrayList<String>>();
			for(int i = 0; i < tagNum; i++) {
				String[] tagEntry = br.readLine().split("=", 2);
				tags.put(tagEntry[0], new ArrayList<String>(Arrays.asList(tagEntry[1].split("\\s*,\\s*"))));
			}
			try {
				photos.add(new photoModel(url, caption, tags));

			}catch(IllegalArgumentException e) {
				continue;
			}
		}
		return new albumModel(albumName, photos);
	}
}
