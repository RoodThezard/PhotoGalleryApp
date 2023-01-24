package com.example.photogalleryapp.models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class photoModel {
	public static ArrayList<String> singleValTagTypes = new ArrayList<String>(Arrays.asList("Location"));
	public static ArrayList<String> multiValTagTypes = new ArrayList<String>(Arrays.asList("Person"));
	public String url;
	private String caption;
	private HashMap<String, ArrayList<String>> tags;
	private File file;

	public photoModel(String url, String caption, HashMap<String, ArrayList<String>> tags) throws IllegalArgumentException{
		this.url = url;
		this.caption = caption;
		this.tags = tags;
		try{
			this.file = new File(url);
		}catch(Exception e) {
			throw new IllegalArgumentException("File does not exist");
		}
	}

	public photoModel(String url) {
		this(url, "", new HashMap<String, ArrayList<String>>());
	}
	
	public String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
	    String date = formatter.format(file.lastModified()); 
		return date;
	}
	
	public File getImageFile() {
		return file;
	}
	
	public void addTag(String tagName, String tagValue) {
		if(tags.containsKey(tagName) && multiValTagTypes.contains(tagName)) {
			tags.get(tagName).add(tagValue);
		}else if(!tagName.contains("=")){
			tags.put(tagName, new ArrayList<String>(Arrays.asList(tagValue)));
		}
	}
	
	public void deleteTag(String tagName, String tagValue){
		if(tags.containsKey(tagName)) {
			if(tags.get(tagName).contains(tagValue)) {
				tags.get(tagName).remove(tagValue);
			}
			if(tags.get(tagName).size() == 0) {
				tags.remove(tagName);
			}
		}
	}
	
	public boolean containsTag(String tagName, String tagValue) {
		if(tags.containsKey(tagName)) {
			if(tags.get(tagName).contains(tagValue)) {
				return true;
			}
		}
		return false;
	}
	
	public void setTags(HashMap<String, ArrayList<String>> tags) {
		this.tags = tags;
	}
	
	public HashMap<String, ArrayList<String>> getTags(){
		return tags;
	}
	
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public String toString() {
		String res = url + System.lineSeparator() + caption + System.lineSeparator() + tags.size();
		for(HashMap.Entry<String, ArrayList<String>> entry : tags.entrySet()) {
			res = res + System.lineSeparator() + entry.getKey() + "=" + String.join(", ", entry.getValue());
		}
		return res;
	}
}
