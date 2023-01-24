package com.example.photogalleryapp.models;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class albumModel {
	private String albumName;
	private ArrayList<photoModel> photos;
	private long beginDate = 0;
	private long endDate = Long.MAX_VALUE;
	public albumModel(String albumName) {
		this.albumName = albumName;
		photos = new ArrayList<photoModel>();
	}

	public albumModel(String albumName, String file) throws IOException {
		this.albumName = albumName;
		photos = new ArrayList<photoModel>();
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
				photoModel photo = new photoModel(url, caption, tags);
				addPhoto(photo);
				
			}catch(IllegalArgumentException e) {
				continue;
			}	
		}
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
	
	public void convert2TextFile(String userName) throws IOException {
		File albumFile = new File("src/model/data/" + userName + "/"  + albumName + ".txt");
		albumFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter("src/model/data/" + userName + "/"  +  albumName + ".txt", false));
		for(photoModel photo : photos) {
			writer.write(photo.toString());
			writer.newLine();
		}
		writer.close();
		
	}
}

