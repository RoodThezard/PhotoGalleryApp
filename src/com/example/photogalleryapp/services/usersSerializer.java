package com.example.photogalleryapp.services;

import java.io.*;
import java.util.ArrayList;
import com.example.photogalleryapp.models.userModel;

public class usersSerializer {
    private static String dataDir = "./src/com/example/photogalleryapp/data/";

    public static void storeData(ArrayList<userModel> users) throws IOException{
        FileOutputStream fileOut = new FileOutputStream(dataDir + "users.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(users);
        out.close();
        fileOut.close();
    }

    public static ArrayList<userModel> getData() throws IOException {
        try{
            FileInputStream fileIn = new FileInputStream(dataDir + "users.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<userModel> users = (ArrayList<userModel>) in.readObject();
            in.close();
            fileIn.close();
            return users;
        }catch (ClassNotFoundException c){
            return null;
        }
    }

}
