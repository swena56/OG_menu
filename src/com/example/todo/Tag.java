package com.example.todo;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.apache.http.util.ByteArrayBuffer;


public class Tag {
 
    int id;
    String tag_name = "tag Not Named";
    String plateSelection = "Select A Plate";
    String cookTimeSelection = "Select A Cook Time";
   
    String stationSelection = "Select A Station";
    String notes = "";
    Bitmap image = null;//BitmapFactory.decodeResource(null,R.drawable.boat);
    String imagePath = "drawable/unknown";
    int resourceIdentifier = 0;
        // constructors
    public Tag() {
 
    }
 
    public Tag(String tag_name) {
        this.tag_name = tag_name;
        //this.image = BitmapFactory.decodeResource(null,R.drawable.boat);
    }

    public Tag(String tag_name, String resourceURL) {
        this.tag_name = tag_name;
        this.imagePath = resourceURL;
    }
 
    public Tag(int id, String tag_name, String plateSelection) {
        this.id = id;
        this.tag_name = tag_name;
        
        if(plateSelection.equals(""))
        {
        	this.plateSelection = "Lunch Plate"; //default data
        }else
        {
        	this.plateSelection = plateSelection;
        }
    }
 
    // setter
    public void setId(int id) {
        this.id = id;
    }
 
    public void setImage(Bitmap image)
    {
    	this.image = image;
    }


   public void setImageData(ByteBuffer buffer)
   {
       byte[] bufferArray = buffer.array();
       Bitmap bitmap = BitmapFactory.decodeByteArray(bufferArray , 0, bufferArray .length);
       this.image = bitmap;
   }

    public void setTagName(String tag_name) {
        this.tag_name = tag_name;
    }
    
    public void setTagPlate(String plateSelection) {
        this.plateSelection = plateSelection;
    }
    
   
    public void setTagCookTime(String cookTimeSelection) {
        this.cookTimeSelection = cookTimeSelection;
    }
    
    public void setTagStation(String stationSelection) {
        this.stationSelection = stationSelection;
    }
    
    public void setTagNotes(String notes) {
        this.notes = notes;
    }

    public void setTagImagePath(String path) {
        this.imagePath = path;
    }
 
    // getter
    public int getId() {
        return this.id;
    }
 
    public Bitmap getImage()
    {
    	return image;
    }
    
    public ByteBuffer getRawImageData()
    {

        if(image != null)
        {
    	//int bytes = image.getByteCount();
    	//or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
    	int bytes = image.getWidth()*image.getHeight()*4;

    	ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
    	image.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

    	//byte[] array = buffer.array();
    	return buffer;
        } else
        {

            return null;
        }
    }

    public byte[] getRawImageDataByteArray()
    {
        int bytes = image.getByteCount();
        //or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
        //int bytes = image.getWidth()*image.getHeight()*4;

        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
        image.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

        byte[] array = buffer.array();
        return array;
    }
    public String getTagName() {
        return this.tag_name;
    }
    
    public String getTagPlate() {
        return this.plateSelection;
    }
    
    public String getTagCookTime() {
        return this.cookTimeSelection;
    }
    
    public String getTagStation() {
        return this.stationSelection;
    }
    
    
    
    public String getTagNotes() {
        return this.notes;
    }
}
 
