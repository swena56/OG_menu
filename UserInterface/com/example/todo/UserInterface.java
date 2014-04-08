package com.example.todo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class UserInterface{

	 
	private View v;
	private Context context;
	private ArrayAdapter<String> plateSpinnerArrayAdapter;
	
	//xml
	Spinner plateSpinner;
	EditText nameEditText;
	
	//constructors
	public UserInterface(View view, Context context)
	{
		this.v = view;
		this.context = context;
		
		
	}
	
	public List<String> getListOfPlates()
	{
		List<String >list = new ArrayList<String>();
        list.add("Can't Leak");
        list.add("Tour Plate");
        list.add("Dinner Plate");
        list.add("Lunch Plate");
        list.add("Soup Bowl");
        list.add("Bowl");
        list.add("Flatbread Plate");
        list.add("Brodetto Bowl");
        list.add("Boat");
        list.add("Ramakin");
        return list;
	}
	
	public void setUpAdapters(ArrayAdapter<String> plateSpinnerArrayAdapter)
	{
		this.plateSpinnerArrayAdapter = plateSpinnerArrayAdapter;
	}
	
	public Tag get()
	{
		Tag tag = new Tag("");
		tag.setTagPlate(plateSpinner.getSelectedItem().toString());
		
		return tag;
	}
	
	public void initalize(Tag tag)
	{
		Log.d("initalize","name");
		//set default form data
		//initalize components
	    nameEditText = (EditText) v.findViewById(R.id.editTextExport);
	    nameEditText.setText(tag.getTagName().toString());
	    //platespinner
	    plateSpinner = (Spinner) v.findViewById(R.id.spinner);
	    plateSpinner.setAdapter(plateSpinnerArrayAdapter);
	    
       
        //String desiredPlateSelection = tag.getTagPlate().toString();
       // int numPlates = plateSpinner.getAdapter().getCount();
       // Log.d("initalize","plates: "+desiredPlateSelection);
        /*
        for(int i = 0; i<=numPlates; i++)
        {
        	if(desiredPlateSelection.equals(plateSpinner.getAdapter().getItem(i).toString()))
        	{
        		plateSpinner.setSelection(i);
        		break;
        	}
        }
        
		    */
		
	}
	
}
