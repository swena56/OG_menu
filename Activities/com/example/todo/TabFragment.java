package com.example.todo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TabFragment extends Fragment {
	private int index;
	View v;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		index = data.getInt("idx");
		 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		  if(index == 0)
	        {
			  //inflate view with contents of list fragment xml
			    v = inflater.inflate(R.layout.activity_main, null);
			    
			    final Spinner stationSpinner = (Spinner) v.findViewById(R.id.spinner1);
                //station spinner
    	        final List<String>stationList = new ArrayList<String>();
    	        
    	        stationList.add("Alley");
    	        stationList.add("Line - Appetizer");
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
    	        
    	        //stationList.add("Appitizer");
    	        ArrayAdapter<String> stationSpinnerAdapter = new ArrayAdapter<String>(v.getContext(),
    	                android.R.layout.simple_spinner_item,
    	                stationList);
    		    stationSpinner.setAdapter(stationSpinnerAdapter);
    		   
    		    //listener for cooking instruction button
			    Button menu = (Button) v.findViewById(R.id.buttonMenu);
			    menu.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						//create database if it does not exist
						DatabaseHelper db = new DatabaseHelper(getActivity(), stationList.get(stationSpinner.getSelectedItemPosition()));
						db.close();
						
						Intent intent = new Intent(getActivity(), MenuList.class);
						//provide next window with station selection
						intent.putExtra("Station", stationList.get(stationSpinner.getSelectedItemPosition()));
						startActivity(intent);
					}
				}); //end of menu on click listener
			    
			    Button deleteAll = (Button) v.findViewById(R.id.buttonDeleteAll);
			    deleteAll.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						for(int i = 0; i<stationList.size(); i++)
						{
							//cycle through all stations and delete the corresponding databases
						}
						DatabaseHelper db = new DatabaseHelper(getActivity(),stationList.get(stationSpinner.getSelectedItemPosition()));
						db.deleteAll();
						db.close();
						Toast message = Toast.makeText(getActivity(),"Everything was deleted.",Toast.LENGTH_SHORT);
						message.show();
						
					}
				}); //end of deleteALl button
			    
			    Button exit  = (Button) v.findViewById(R.id.buttonExit);
			    exit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast message = Toast.makeText(getActivity(),"Bye",Toast.LENGTH_SHORT);
						 message.show();
						System.exit(0);
						
					}
				});
			    
	        }else
	        	 if(index == 1)
	 	        {
	        		 //inflate view with contents of list fragment xml
	 			    v = inflater.inflate(R.layout.settings_and_about, null);
	 			    Button loadDefault = (Button) v.findViewById(R.id.loadDefaultData);
	 			    loadDefault.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							LoadDefaultData defaultData = new LoadDefaultData(getActivity());
						}
					});

                    Toast load = Toast.makeText(getActivity(), "Default Data was loaded." ,Toast.LENGTH_SHORT);
                    load.show();
	 			    /*
	 			    Button importJson = (Button) v.findViewById(R.id.buttonImport);
	 			    importJson.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Import_ExportJSON json = new Import_ExportJSON(getActivity());
							//String jsonImportResults = json.readJsonFile();
							

							Toast jsonImport = Toast.makeText(getActivity(),"Import Results: ",Toast.LENGTH_LONG);
							jsonImport.show();
							
						}
					});
	 			    
	 			    */
	 			    Button exportToJson = (Button) v.findViewById(R.id.buttonExportToJson);
	 			    exportToJson.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Import_ExportJSON json = new Import_ExportJSON(getActivity());
							String jsonExportResults = json.exportToJson1();
							
							//xml of file location input fields
							//EditText editTextExport = (EditText) v.findViewById(R.id.editTextExport);
							//json.saveToJsonFile(jsonExportResults, editTextExport.getText().toString());
							
							
							Toast json1 = Toast.makeText(getActivity(),jsonExportResults,Toast.LENGTH_LONG);
							json1.show();
							 
							
							
							
						}
					});
	 	        }
		return v;
	}
	

}
