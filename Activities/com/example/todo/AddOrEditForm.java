package com.example.todo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;


public class AddOrEditForm extends Activity {

	List<Todo> allTodos = null;
	List<Tag> allTags = null;
	String uptodateName = "";
	
	String nameLoadedInForm;
	String stationSelected;

	ListView listInstruction;		//inside list of instructions
	List<String >plates;
	List<String >cookTimeList;
	List<String >stationList;
	Tag tagLoadedInForm;
	
	ArrayAdapter<String> cookTimeSpinnerAdapter;
	ArrayAdapter<String> stationSpinnerAdapter;
	String imagePath = "drawable/unknown";
	//xml components
	ImageView image;
	public Bitmap bitmap;
	Spinner plateSpinner;
	Spinner cookTimeSpinner;
	Spinner stationSpinner;
	RadioGroup lunchRadioGroup;

    //load from web
    ProgressBar progressbar;


	@Override
	public void onBackPressed() {
		//close activity
		 finish();
		 //go to list activity
		 Log.d("AddOrEditForm","Back was Pressed");
		 Intent intent = new Intent(AddOrEditForm.this, MenuList.class);
		 intent.putExtra("Station", stationSelected);
		 startActivity(intent);
		super.onBackPressed();
	}

    public class loadImageTask extends AsyncTask<String, Void, Void>
    {
        Drawable imgLoad;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            imgLoad = LoadImageFromWeb(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if(progressbar.isShown())
            {
                progressbar.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                image.setBackgroundDrawable(imgLoad);
            }
        }
    }


    @Override
	protected void onResume() {
		
		Log.d("AddOrEditForm","onResume");
		//load items into list
		DatabaseHelper db = new DatabaseHelper(getBaseContext(),stationSelected);
		//allTags = db.getAllTags();
		allTodos = db.getAllToDosByTag(tagLoadedInForm.getTagName()); 
		
		if(allTodos.size() == 0)
		{
			allTodos.add(new Todo("Instruction list is empty.",0));
		}
		db.close();
		
		//update item list
		 listInstruction.setAdapter(new myTodoAdapter());
		 
		super.onResume();
	}
	
	/*
	@Override
	protected void onPause() {
		//close activity
		 finish();
		 //go to list activity
		 Intent intent = new Intent(AddOrEditForm.this, MenuList.class);
		 startActivity(intent);
		super.onPause();
	}
	*/

    public static Drawable LoadImageFromWeb(String url)
    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

	public void updateDatabase(int tagId)
	{
		//initialize xml
		EditText editTextName = (EditText) findViewById(R.id.editTextExport);
		String editTextString = editTextName.getText().toString();

        EditText editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        String editTextNotesString = editTextNotes.getText().toString();
		
		  DatabaseHelper db = new DatabaseHelper(getApplicationContext(),stationSelected); 
		  Tag tagToUpdateOrginal = new Tag();
		  tagToUpdateOrginal.setId(tagId);
		  tagToUpdateOrginal.setTagName(editTextString);
		  tagToUpdateOrginal.setTagPlate(plates.get((int) plateSpinner.getSelectedItemId()));
		  tagToUpdateOrginal.setTagCookTime(cookTimeList.get((int) cookTimeSpinner.getSelectedItemId()));
		  tagToUpdateOrginal.setTagStation(stationSelected);
          tagToUpdateOrginal.setTagNotes(editTextNotesString);
          tagToUpdateOrginal.imagePath = imagePath;
          String imageRawData = "No image loaded";
          if(bitmap != null)
          {
            tagToUpdateOrginal.setImage(bitmap);
              //imageRawData = tagToUpdateOrginal.getRawImageData().toString();
              imageRawData = tagToUpdateOrginal.getRawImageDataByteArray().toString();
          }else
          {
              tagToUpdateOrginal.setImage(BitmapFactory.decodeResource(null,R.drawable.boat));
          }
		  //set dinner or lunch radio button
		
		
			//log update data  	
			Log.d("AddOrEditForm", "Save was clicked, (updateDatabase) item to update:"+tagToUpdateOrginal.getTagName()+
					 "\n\t	station: "+tagToUpdateOrginal.getTagStation()+
					  "\n\t	plateSelectionOf: "+tagToUpdateOrginal.getTagPlate()+
					  "\n\t	CookTime: "+tagToUpdateOrginal.getTagCookTime()+
                    "\n\t	ImageData: "+imageRawData);
			  
		    //update Tag
			 db.updateTag(tagToUpdateOrginal);

        Toast imageTest = Toast.makeText(getApplicationContext(), "Image update: "+imageRawData ,Toast.LENGTH_SHORT);
        imageTest.show();

			//Message to User
			Toast updateMessage = Toast.makeText(getApplicationContext(), "Contents was updated." ,Toast.LENGTH_SHORT);
			updateMessage.show();
			    
			db.close();
			
			
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        // Make sure the request was successful
	       Log.d("AddOrEditForm", "-----------onActivityResult----------"+
           "\n requestCode: "+requestCode +
           "\n resultCode: "+resultCode +
           "\n data: "+data.toString());

	        if (resultCode == RESULT_OK)
	        {
	        	if (requestCode == 2 ) //camera
	        	{

                    //gather picture
                    String stringFormOfImageRawDataLoadedInForm = "no picture loaded";
		        	Bitmap bm = (Bitmap) data.getExtras().get("data");
				    bitmap = Bitmap.createScaledBitmap(bm, 160, 160, true);
					image.setImageBitmap(bitmap);

                    //create folder




                    //save image to app directory
                    try {
                        FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory()+"/OliveGarden/"+tagLoadedInForm.getId()+".jpg");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }





                    imagePath =  Environment.getExternalStorageDirectory()+"/OliveGarden/"+tagLoadedInForm.getId()+".jpg";
                    tagLoadedInForm.imagePath = Environment.getExternalStorageDirectory()+"/OliveGarden/"+tagLoadedInForm.getId()+".jpg";
                    //log data
                    Log.d("AddOrEditForm","Picture Saved: "+Environment.getExternalStorageDirectory()+"/OliveGarden/"+tagLoadedInForm.getId()+".jpg");
                    Toast imageTest = Toast.makeText(getApplicationContext(), "Image Loaded: "+stringFormOfImageRawDataLoadedInForm ,Toast.LENGTH_SHORT);
                    imageTest.show();

	        	} else
	        	if (requestCode == 0 ) //file
	        	{
	            // The user picked a contact.
	            // The Intent's data Uri identifies which contact was selected.
	        	  Uri uri = data.getData();
	        	  String path = uri.getPath();

	        	  //perform check to make sure the picture selected is an image
                    Log.d("AddOrEditForm","File Path Selected: " +path);
	        	   File file = new File(path);


	              String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};



	              boolean fileAcceptableYesOrNo = false;
	        	  for (String extension : okFileExtensions)
                  {
                    if (file.getName().toLowerCase().endsWith(extension))
                    {               
                    	BitmapFactory.Options options = new BitmapFactory.Options();
                    	options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmapDefault = BitmapFactory.decodeFile(path, options);
                        bitmap = Bitmap.createScaledBitmap(bitmapDefault, 160, 160, true);

                        //save image to app directory
                        try {
                            FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory()+"/OliveGarden/"+tagLoadedInForm.getId()+".jpg");
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //set image on form
                    	image.setImageBitmap(bitmap);
                    	fileAcceptableYesOrNo = true;
                        imagePath = Environment.getExternalStorageDirectory()+"/OliveGarden/"+tagLoadedInForm.getId()+".jpg";
                        tagLoadedInForm.imagePath = imagePath;
                      break;
                    }
                  }
	        	  
	        	  if(fileAcceptableYesOrNo)
	        	  {
	        		  Toast tagIDs = Toast.makeText(getApplicationContext(),"Item Clicked:"+path+", image("+image.getMeasuredWidth()+")",Toast.LENGTH_SHORT);
	 	 			 tagIDs.show();
	        	  } else
	        	  {
	        		  Toast tagIDs = Toast.makeText(getApplicationContext(),"File selected is not a picture.",Toast.LENGTH_SHORT);
		 	 			 tagIDs.show();
	        	  }
	        	 
	        	}
	            // Do something with the contact here (bigger example below)
	        
	        }
	    
	}

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object fetch(String address) throws MalformedURLException,IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_or_edit_form);
        Log.d("AddOrEditForm","Activity was created!");
		//get passed intent data
		Intent intent = getIntent();
	    String tagText = intent.getStringExtra("Tag");
	    stationSelected = intent.getStringExtra("Station");
	    long tagId = intent.getLongExtra("TagId",-1);


        progressbar = (ProgressBar) findViewById(R.id.loadingBar);

	    //opens database and loads tag list
	    DatabaseHelper db = new DatabaseHelper(getApplicationContext(),stationSelected);
	    allTags = db.getAllTags();
	   // long tagId = db.getTagId(tagText);

	    //checks tag list and if its empty put empty list message
	    if(allTags.size() <= 0)
	    	allTags.add(new Tag("The List is Empty, Try adding a new item."));

		
		//loads our form tag with correct tag
	    tagLoadedInForm = new Tag();
		for (int i = 0; i < allTags.size(); i++) 
		{
			
            if(allTags.get(i).tag_name.equals(tagText))
            {
            	tagLoadedInForm = allTags.get(i);
            	break;
            }
        }


        Log.d("AddOrEditForm","Package:"+getPackageName());
        image = (ImageView) findViewById(R.id.imageView1);
        int indentifierNumber = getResources().getIdentifier(tagLoadedInForm.imagePath, null, getPackageName());
        if(indentifierNumber != 0)
        {
            //picture not stored as resource
            Log.d("AddOrEditForm", "Default Resource Image Detected: " + imagePath + ", with and IndetifierNumber: " + indentifierNumber);
            Drawable drawableBasedOnStringIdentifier = getResources().getDrawable(indentifierNumber);
            image.setImageDrawable(drawableBasedOnStringIdentifier);
        }else
        {
            //load user image from harddrive
            Log.d("AddOrEditForm", "Custom User Picture");
            imagePath = tagLoadedInForm.imagePath;
            File imgFile = new File(tagLoadedInForm.imagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = Bitmap.createScaledBitmap(myBitmap, 160, 160, true);
                image.setImageBitmap(myBitmap);
            }
        }







        //When the picture is clicked create popup message
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view1;
                final PopupWindow mpopup;
                Log.d("AddOrEditForm", "Image was clicked");

                view1 = getLayoutInflater().inflate(R.layout.select_image, null);
                view1.setBackgroundColor(Color.RED);

                mpopup = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mpopup.setFocusable(true);

                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.showAtLocation(view1, 0, image.getBottom(), image.getWidth());

                Button cancel = (Button) view1.findViewById(R.id.button1);
                cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mpopup.dismiss();

                    }
                });

                Button camera = (Button) view1.findViewById(R.id.button2);
                camera.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mpopup.dismiss();
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                        startActivityForResult(cameraIntent, 2);
                    }
                });

                Button fileExplorer = (Button) view1.findViewById(R.id.button3);
                fileExplorer.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mpopup.dismiss();
                        //select image
                        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        fileIntent.setType("file/*"); // intent type to filter application based on your requirement
                        startActivityForResult(fileIntent, 0);
                    }
                });


            }
        });
		//Extract data from xml
		 EditText nameEditText = (EditText) findViewById(R.id.editTextExport);
		 nameEditText.setText(tagLoadedInForm.getTagName());


		 EditText notes = (EditText) findViewById(R.id.editTextNotes);
         notes.setText(tagLoadedInForm.getTagNotes());  //set notes form field

		    //plate list to be loaded in spinner
		    plates = new ArrayList<String>();
		    plates.add("Select A Plate");
		    plates.add("Brodetto Bowl");
		    plates.add("Boat");
		    plates.add("Bowl");
		    plates.add("Can't Leak");
		    plates.add("Dinner Plate");
		    plates.add("Flatbread Plate");
		    plates.add("Lunch Plate");
		    plates.add("Infinity");
		    plates.add("Mini Infinity & Underliner");
		    plates.add("Soup Bowl");
		    plates.add("Tour Plate");
		    plates.add("Underliner");
		    plates.add("Ramakin");
		    //array adapter for plate spinner
			ArrayAdapter<String> plateSpinnerAdapter = new ArrayAdapter<String>( getApplicationContext(),
	                android.R.layout.simple_spinner_item,
	                plates);
		    plateSpinner = (Spinner) findViewById(R.id.spinner);
		    plateSpinner.setAdapter(plateSpinnerAdapter); 
		    
		    Log.d("AddOrEditForm","Plate Spinner Loaded with: "+ plates.toString());
		    //sets the xml to the database selection  
		    
	        for(int i = 0; i<plates.size(); i++)
	        {
	        	if(tagLoadedInForm.getTagPlate().equals(plates.get(i)))
	        	{
	        		plateSpinner.setSelection(i);
	        		break;
	        	}
	        }
	        
	        //cook time spinner
	        cookTimeList = new ArrayList<String>();
	        
	        cookTimeList.add("Select A Cook Time");
	        cookTimeList.add("NOW");
	        cookTimeList.add("0 to 1 minuts");
	        cookTimeList.add("2 to 3 minuts");
	        cookTimeList.add("3 to 4 minuts");
	        cookTimeList.add("7 to 8 minuts");
	        cookTimeList.add("9 to 10 minuts");
	        cookTimeList.add("10 minuts plus");
	        
	        cookTimeSpinnerAdapter = new ArrayAdapter<String>( getApplicationContext(),
	                android.R.layout.simple_spinner_item,
	                cookTimeList);
		    cookTimeSpinner = (Spinner) findViewById(R.id.spinnerCookTime);
		    cookTimeSpinner.setAdapter(cookTimeSpinnerAdapter);
		   
		    for(int i = 0; i<cookTimeList.size(); i++)
	        {
	        	if(tagLoadedInForm.getTagCookTime().equals(cookTimeList.get(i)))
	        	{
	        		cookTimeSpinner.setSelection(i);
	        		break;
	        	}
	        }
		    
		
		    
		   
		   //Log all the data that is loaded in the form
		   Log.d("AddOrEditForm","Tag Loaded:"
				   +tagLoadedInForm.getId()+" "+tagLoadedInForm.getTagName()
				   +",plateSelecction: "+tagLoadedInForm.getTagPlate()
				   );
		 
	      allTodos = db.getAllToDosByTag(tagLoadedInForm.getTagName()); 
	     
		    
		    Toast tagIDs = Toast.makeText(getApplicationContext(),"TagID of item clickd"+tagLoadedInForm.getId(),Toast.LENGTH_SHORT);
			 tagIDs.show();
	    
	        
		    
	    if(allTodos.size()==0)
	    {
	    	 allTodos.add(new Todo("Instruction List is Empty",0));
	    }
	    db.close();
	   
	    String listOfTodos = "";
	    for(Todo todo : allTodos)
	    {
	    	listOfTodos = listOfTodos + todo.getNote() + ", ";
	    }
	    
	    //fill instruction list
	    listInstruction = (ListView) findViewById(R.id.listViewContents);
	    listInstruction.setAdapter(new myTodoAdapter());
	    
	    //log all information about addOrEditForm
	    Log.d("AddOrEditForm","Load form loaded with: "+tagLoadedInForm.getTagName()+" - id("+tagLoadedInForm.getId()+")"+
	    		"\n		plate: "+tagLoadedInForm.getTagPlate() +
	    		"\n		cookTIme: "+tagLoadedInForm.getTagCookTime() +
	    		"\n		InstructionCount: "+listInstruction.getCount()+
	    		"\n		ContentsOfInstructionList:( "+listOfTodos+")"+
                "\n     ImagePathLoaded: "+ tagLoadedInForm.imagePath
	    		);
	    
	    
	    //what happens if an instruction is clicked on
	    listInstruction.setOnItemClickListener(new OnItemClickListener() {
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	    	{
	    		Log.d("AddOrEditForm","Instruction was clicked for edit: "+allTodos.get(position).getNote()+", InstructionCount: "+allTodos.size()+", ID:"+allTodos.get(position).getId());
	    		Intent intent = new Intent(AddOrEditForm.this,AddIntruction.class);
	    		intent.putExtra("Station", stationSelected);
	    		intent.putExtra("todoId", allTodos.get(position).getId());
	    		intent.putExtra("todoIndex",position);
	    		intent.putExtra("Tag", tagLoadedInForm.getTagName());
	    		intent.putExtra("Content", allTodos.get(position).getNote());
	    		intent.putExtra("Amount", allTodos.get(position).getAmount());
	    		intent.putExtra("Instruction",allTodos.get(position).getInstruction());
	    		Log.d("AddOrEditForm", "data send in intent: todoId: "+allTodos.get(position).getId()+
	    				"\n,todoIndex: " +position +
	    				"\nTag: " + tagLoadedInForm.getTagName() +
	    				"\nContent: " +allTodos.get(position).getNote() +
	    				"\nAmount: " + allTodos.get(position).getAmount() +
	    				"\nInstruction: " + allTodos.get(position).getInstruction()
	    				);
	    		startActivity(intent);
	    		
	    	}
		});
	   
	    
	    //if add instruction button is pressed go to add instruction activity
	    Button addInstructionButton = (Button) findViewById(R.id.buttonAddInstruction);
	    addInstructionButton.setOnClickListener(new OnClickListener() {
			@Override
			
			public void onClick(View v) {
				Intent intentA = getIntent();
			    long tagId = intentA.getLongExtra("TagId", -1);
				String tagText = intentA.getStringExtra("Tag");
				
				Intent intent = new Intent(AddOrEditForm.this,AddIntruction.class);
				intent.putExtra("TagId", tagId);
				intent.putExtra("Tag", tagText);
				intent.putExtra("todoIndex", -1);
				intent.putExtra("Content", "");
				intent.putExtra("Amount", "");
				intent.putExtra("Instruction", "");
				intent.putExtra("Station", stationSelected);
				
				Log.d("form,","TagId put in extras: "+ tagId);
				 
				//update list
				listInstruction.setAdapter(new myTodoAdapter());
				
				startActivity(intent);
				Log.d("AddOrEditForm,","addInstruction was clicked for item: "+tagText);
			}
		});
	    //strictly edit the update database fuction
	    
	    // save button
	    Button save = (Button) findViewById(R.id.buttonSave);
	    save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 //update database
				 updateDatabase(tagLoadedInForm.getId());  //sets name data
					//close activity
					  finish();
					 //go to list activity
					  
					  DatabaseHelper db = new DatabaseHelper(getApplicationContext(),stationSelected);
					  allTags = db.getAllTags();
					  listInstruction.setAdapter(new myTodoAdapter());
					  db.close();
					  
					 Intent intentList = new Intent(AddOrEditForm.this, MenuList.class);
					 intentList.putExtra("Station", stationSelected);
					 startActivity(intentList);
			}
		}); //end of save button
	    
	    //cancel button
	    Button cancel = (Button) findViewById(R.id.buttonCancel);
	    cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Message to User
				 Toast cancelMessage = Toast.makeText(getApplicationContext(),"Canceled Nothing Was Saved" ,Toast.LENGTH_SHORT);
				 cancelMessage.show();
				 
				//close activity
				 finish();
				 
				 //go to list activity
				 Intent intent = new Intent(AddOrEditForm.this, MenuList.class);
				 intent.putExtra("Station", stationSelected);
				 
				 startActivity(intent);
				
			}
		});//end of cancel button
	    
	    //delete button
	    Button delete =  (Button) findViewById(R.id.buttonDelete);
	    delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 //determine from form which data is going to be deleted
				Intent intentA = getIntent();
			    long tagId = intentA.getLongExtra("TagId", -1);
				String tagText = intentA.getStringExtra("Tag");
				 
				//access database
				 DatabaseHelper db = new DatabaseHelper(getApplicationContext(),stationSelected);
				 for(int i = 0; i <  db.getAllTags().size(); i ++)
				 {
					 if( db.getAllTags().get(i).getTagName().equals(tagText))
					 {
						 Log.d("add","delete, checking tags match was found for: "+ tagText);
						 db.deleteTag( db.getAllTags().get(i), true);
						 //db.deleteTag(db.getAllTags().get(i), false);
						 break;
					 }
				 }
				
				 db.close(); //close database

                //delete picture
                new File(tagLoadedInForm.imagePath).delete();

				 Log.d("add",tagText+" and its corresponding data were deleted.");
				 //message to user
				 Toast deleteMessage = Toast.makeText(getApplicationContext(), tagText+" and its corresponding data were deleted." ,Toast.LENGTH_SHORT);
				 deleteMessage.show();
				 
				 finish();
				 
				 //go to list activity
				 Intent intentList = new Intent(AddOrEditForm.this, MenuList.class);
				 intentList.putExtra("Station", stationSelected);
				 startActivity(intentList);
			}
		});//end of delete button

        db.close(); //close database
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_or_edit_form, menu);
		return true;
	}

	class myTodoAdapter extends ArrayAdapter<Todo>
	{
	    myTodoAdapter() {
	        super(getApplicationContext(), R.layout.list_item, allTodos);
	    }

	    public View getView(int position, View convertView, ViewGroup parent)
	    {
	        View row = convertView;
	        listViewItemHolder holder = null;

	        if(row == null)
	        {
	            LayoutInflater inflater = getLayoutInflater();
	            row = inflater.inflate(R.layout.list_item,null);
	            holder = new listViewItemHolder(row);

	                row.setTag(holder);

	        }else
	        {
	            holder = (listViewItemHolder)row.getTag();
	        }

	     holder.populateForm(allTodos.get(position),position);

	     return row;
	    }
	}  //end of class
	
	static class listViewItemHolder
    {
        private TextView name = null;
        private TextView station = null;
        private ImageView icon = null;

        listViewItemHolder(View row)
        {
            name = (TextView) row.findViewById(R.id.ListItemName);
            station = (TextView) row.findViewById(R.id.ListItemStation);
            icon = (ImageView) row.findViewById(R.id.imageView);
        }

        void populateForm(Todo todo, int position)
        {
        	if(todo.note != null)
        	{
        		name.setText((position+1)+")"+todo.getAmount()+ "-"+todo.getNote()+"");
        		station.setText(todo.getInstruction());

        	}

        }
    }



}
