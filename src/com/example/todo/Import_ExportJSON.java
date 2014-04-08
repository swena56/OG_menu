package com.example.todo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

public class Import_ExportJSON {

	Activity activity;
	
	Import_ExportJSON(Activity activity)
	{
		this.activity = activity;
	}
	
	public void saveToJsonFile(String data, String FILENAME)
	{
		
		File myFile = new File(Environment.getExternalStorageDirectory()+FILENAME);
		try {
			
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = 
									new OutputStreamWriter(fOut);
			myOutWriter.append(data);
			myOutWriter.close();
			fOut.close();
			
		} catch (Exception e) {
			Toast error = Toast.makeText(activity,"Error Writing to "+myFile.toString(),Toast.LENGTH_LONG);
			error.show();
		}
	}
	
	public String readJsonFile(String FILENAME)
	{
		File sdcard = Environment.getExternalStorageDirectory();

		//Get the text file
		File file = new File(sdcard,FILENAME);

		//Read text from file
		StringBuilder text = new StringBuilder();

		try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;

		    while ((line = br.readLine()) != null) {
		        text.append(line);
		        text.append('\n');
		    }
		}
		catch (IOException e) {
		    //You'll need to add proper error handling here
		}

		    return text.toString();
		

	}
	
	public String loadDataBaseWithJson(String json)
	{
		DatabaseHelper db = new DatabaseHelper(activity, "Line - Window");
		//add tag
			//add instructions
		db.close();
		String test = "";
		try {
		      JSONArray jsonArray = new JSONArray(json);
		     
		      for (int i = 0; i < jsonArray.length(); i++) {
		        JSONObject jsonObject = jsonArray.getJSONObject(i);
		        	String name = jsonObject.getString("name");
		        	String cookTime = jsonObject.getString("cookTime");
		        	String plate = jsonObject.getString("plate");
		        	String notes = jsonObject.getString("notes");
		        	String instructions = "";
		        	JSONArray listOfinstructions = jsonObject.getJSONArray("instructions");
		        	
		        	JSONObject instruction = listOfinstructions.getJSONObject(0);
		        	String content = instruction.getString("content");
		        	String amount = instruction.getString("amount");
		        	String instructionData = instruction.getString("instruction");
		        	instructions = instructions + content + " " + amount + " " + instructionData;
		        	
			        	test = test+ "index"+i+": "
			        	+"\n"+name
			        	+"\n"+cookTime
			        	+"\n"+plate
			        	+"\n"+notes
			        	+"\n("+instructions+")"
			        	+"\n";
		        	//add to database
		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		return test;
	}
	
	public String exportToJson1()
	{
		List<String> stationList = new ArrayList<String>();
	    stationList.add("Alley");
        stationList.add("Line - Apitizer");
        stationList.add("Line - Grill");
        stationList.add("Line - Saute");
        stationList.add("Line - Window");
        stationList.add("Production - Pasta");
        stationList.add("Production - Protien");
        stationList.add("Production - Thaw Pull");
        stationList.add("Production - Sauce");
        stationList.add("Production - Veggie");
        stationList.add("Togo Specialist");
        stationList.add("Salad and Desert");
        stationList.add("Serving");
        
		
		
		 JSONArray completeListAllStations = new JSONArray();
		    for(int index = 0; index < stationList.size(); index++)
		    {
		    	//add station name to JSON
		    	JSONObject station = new JSONObject();
		    	try {
					station.put("StationData", stationList.get(index).toString());
					
					DatabaseHelper db = new DatabaseHelper(activity, stationList.get(index));
					List<Tag> listOfAllTags = db.getAllTags();
					db.close();
					
					for(int i = 0; i<listOfAllTags.size(); i++)
					{
						station.put("Tags", listOfAllTags.get(i).toString() );
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	
		    	
		    	completeListAllStations.put(station);
		    	
		    	
		    	
		    }
		
		return completeListAllStations.toString();
	}
	
	public String exportToJson()
	{
		List<String> stationList = new ArrayList<String>();
		    stationList.add("Alley");
	        stationList.add("Line - Apitizer");
	        stationList.add("Line - Grill");
	        stationList.add("Line - Saute");
	        stationList.add("Line - Window");
	        stationList.add("Production - Pasta");
	        stationList.add("Production - Protien");
	        stationList.add("Production - Thaw Pull");
	        stationList.add("Production - Sauce");
	        stationList.add("Production - Veggie");
	        stationList.add("Togo Specialist");
	        stationList.add("Salad and Desert");
	        stationList.add("Serving");
	        
	   JSONArray completeListAllStations = new JSONArray();
	    for(int index = 0; index < stationList.size(); index++)
	    {
	    	
	   
		DatabaseHelper db = new DatabaseHelper(activity, stationList.get(index));
		List<Tag> listOfAllTags = db.getAllTags();
		
		
		JSONArray completeList = new JSONArray();  	//complete list
		for(int i = 0; i < listOfAllTags.size(); i ++)
		{
			JSONObject object = new JSONObject();
			  try 
			  {
			    object.put("name", listOfAllTags.get(i).getTagName());
			    object.put("cookTime", listOfAllTags.get(i).getTagCookTime());
			    object.put("plate", listOfAllTags.get(i).getTagPlate());
			    object.put("notes", listOfAllTags.get(i).getTagNotes());
			    
			    //create jsonArray for holding instructions
			    JSONArray instructions = new JSONArray();
			    	
			    
			    List<Todo> listOfInstructions = db.getAllToDosByTag(listOfAllTags.get(i).getTagName()); //gather all instructions for item
			    
			    //loop through all instructions for selected Tag
			    for(int y = 0; y < listOfInstructions.size(); y++)
			    {
			    	JSONObject instructionData = new JSONObject();
			    	 instructionData.put("content", listOfInstructions.get(y).getNote());
					 instructionData.put("amount",  listOfInstructions.get(y).getAmount());
					 instructionData.put("instruction", listOfInstructions.get(y).getInstruction());
					 instructions.put(instructionData);
			    }
			    
			    object.put("instructions", instructions);
			    
			  } catch (JSONException e) { e.printStackTrace();  }
			  
			  completeList.put(object);
	  
		}
		db.close();
		 JSONObject oneStation = new JSONObject();
		 try {
			oneStation.put("station", completeList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		completeListAllStations.put(oneStation);
		
	    }
		
		
		
		return completeListAllStations.toString();
	}
	
	
	
}
