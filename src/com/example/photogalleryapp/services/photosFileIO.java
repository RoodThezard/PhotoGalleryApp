package com.example.photogalleryapp.services;

import java.io.*;
import java.util.ArrayList;

import com.example.photogalleryapp.models.userModel;

public class photosFileIO {
	public static ArrayList<userModel> getData() throws IOException {
		ArrayList<userModel> users = new ArrayList<userModel>();
		BufferedReader br = new BufferedReader(new FileReader("src/model/data/usersFile.txt"));
		String userName;
		while ((userName = br.readLine()) != null) {
			String userFile = "src/model/data/" + userName + "File.txt";
			try {
				users.add(new userModel(userName, userFile));
			}catch(IOException e) {
				System.out.println("User file doesn't exist");
			}
			
		}
		return users;
	}
	
	public static void storeData(ArrayList<userModel> users) throws IOException {
		File[] files = new File("src/model/data").listFiles();
		if(files != null) {
			for(File f : files) {
	            f.delete();
			}
		}
		File usersFile = new File("src/model/data/usersFile.txt");
		usersFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter("src/model/data/usersFile.txt", false));
		for(userModel user : users) {
			writer.write(user.getUserName());
			writer.newLine();
			try {
				user.convert2TextFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("exception");
				e.printStackTrace();
			}
		}
		writer.close();
	}
}
