package com.example.todo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.TemplatesHandler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MenuList extends Activity {

	List<Tag> allTags = new ArrayList<Tag>();
	List<String> listOfTodosToAdd;
	ListView todoList;
	ArrayAdapter<String> listAdapter;
	 String stationSelected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_list);
		Intent intent = getIntent();
	    stationSelected = intent.getStringExtra("Station").toString();

		//set title text
		TextView title = (TextView) findViewById(R.id.textViewTitleStation);
		title.setText(stationSelected);
		
		 DatabaseHelper db = new DatabaseHelper(getApplication(),stationSelected);
		 
		  //collect items
	        allTags = db.getAllTags();
	        if(allTags.size() == 0)
	        {
	        	allTags.add(new Tag("Database is emptry, try adding an item."));
	        }
	        
	        String listOfTagsStringFormat = "";
	        for(Tag tag: allTags)
	        {
	        	listOfTagsStringFormat = listOfTagsStringFormat + tag.getTagName() + ", ";
	        }
	        
	       List<Todo> tempListOfTodos =  db.getAllToDos();
	       String listOfTodosStringFormat = "";
	       for(Todo todo : tempListOfTodos)
	       {
	    	   listOfTodosStringFormat = listOfTodosStringFormat + todo.getNote() + ", ";
	       }
	       
	        db.closeDB();
	
	        Log.d("MenuList","AllData:(items("+listOfTagsStringFormat+"),instructions("+listOfTodosStringFormat+")), numberOfItems:("+allTags.size()+"), numberOfInstructions:("+tempListOfTodos.size()+")");
	        
			//create ui interface
		  final ListView menuItemListView = (ListView) findViewById(R.id.listView1);
		  menuItemListView.setAdapter(new myAdapter());
		  menuItemListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				 DatabaseHelper db = new DatabaseHelper(getApplication(),stationSelected);
				 if(db.getAllTags().size()>0)
				 {		 
					 String tagText = db.getAllTags().get(arg2).getTagName();
					 
					
					
					Tag tagThatWasClickedOn =  allTags.get(arg2);
					 long tagId = tagThatWasClickedOn.getId();
					 db.close();
				 
					Intent intent = new Intent(MenuList.this,AddOrEditForm.class);
					//intent.putExtra("TagId", tagId);
					intent.putExtra("Tag", tagText);
					intent.putExtra("Station",stationSelected);
					
					
					finish();
					startActivity(intent);
					Log.d("MenuList","Item On List was Clicked: id("+tagId+"), Tag that was clicked on: "+tagThatWasClickedOn.getTagName());
				 }
				 
			}
		});
		  
		  
		  Button addNew = (Button) findViewById(R.id.buttonAddNewItem);
		  addNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View e) {
				final View view1;
				final PopupWindow mpopup;
                     Log.d("gatherFormData","add was clicked");

                     view1 = getLayoutInflater().inflate(R.layout.create_new, null);
                     view1.setBackgroundColor(Color.GRAY);

                     mpopup = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                     mpopup.setFocusable(true);

                     mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                     mpopup.showAtLocation(view1, Gravity.CENTER,Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL+50);
                    
                    
                     
                     
                     
                     Button save = (Button) view1.findViewById(R.id.buttonSave);
                     save.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								 Log.d("list","popup save button clicked");
								 //intialize xml
								 EditText editTextName = (EditText) view1.findViewById(R.id.editTextName);
								 String newName = editTextName.getText().toString();
								 
								 //access database to check if its in the database
								 DatabaseHelper db = new DatabaseHelper(getApplicationContext(),stationSelected);
								 allTags = db.getAllTags();
								 db.close();
								 
								 Boolean inDB = false;
								 Log.d("list","allTags gathered: tabsize: "+allTags.toString());
								
								 //search in Tag list
								 Log.d("list","size:"+allTags.size());
								 for(int i = 0; i < allTags.size(); i ++)
								 {
									 if(allTags.get(i).getTagName().equals(newName))
									 {
										 Log.d("Add","Tag("+newName+")Was already found in Database");
										 inDB = true;
										 break;
									 }
								 }
								 
								 if(!inDB)
								 {
									 //add name to 
									 db = new DatabaseHelper(getApplicationContext(),stationSelected);
									
									db.createTag(new Tag(newName));
									 db.close();
									finish();
									Intent intent = new Intent(MenuList.this,AddOrEditForm.class);
									intent.putExtra("Tag", newName);
									intent.putExtra("Station", stationSelected);
									
									Toast successfulAdd = Toast.makeText(getApplication(), newName + " has been added.",Toast.LENGTH_SHORT);
									successfulAdd.show();
									startActivity(intent);
									
								 }else
								 {
									 //print error message about item already existing in DB
									 Toast error = Toast.makeText(getApplication(), "Error..Empty Field or "+ newName +" is already saved to the database",Toast.LENGTH_SHORT);
									 error.show();
								 }
								 
								 //close popup
								 mpopup.dismiss();
								 
							}
						});
                     
                     Button cancel = (Button) view1.findViewById(R.id.buttonCancel);
                     cancel.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								 mpopup.dismiss();
							}
						});
			}
			
		});
		
	
	}
//errors out when adding the first item
//cancel does not provide any real funtionality
//i can put in duplicates
	class myAdapter extends ArrayAdapter<Tag>
	{
		
	    myAdapter() {
	    	
	    	
	        super(getApplication(), R.layout.list_item, allTags);
	       
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

	        	
	       holder.populateForm(allTags.get(position),position,getApplicationContext());


	        return row;
	    }
	}  //end of class

	  //used to fill the form
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

        void populateForm(Tag tag, int position,Context context)
        {
        	if(tag.getTagName() != null)
        	{
        		
        		name.setText(position+1+") "+tag.getTagName());
        		
        		station.setText(tag.getTagNotes());
                if(tag.imagePath.equals("drawable/unknown"))
                {
                    icon.setImageResource(R.drawable.unknown);

                } else
                {
                    File imgFile = new File(tag.imagePath);
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        myBitmap = Bitmap.createScaledBitmap(myBitmap, 160, 160, true);
                        icon.setImageBitmap(myBitmap);
                    }else{



                        int indentifierNumber = context.getResources().getIdentifier(tag.imagePath, null,context.getPackageName());
                       Drawable drawable = context.getResources().getDrawable(indentifierNumber);
                       icon.setImageDrawable(drawable);
                    }

                }
        	}

            /*
            //set icon in data
            if(tag.getTagStation().equals("Select A Station"))
            {
                icon.setImageResource(R.drawable.unknown);
            }else
            if(tag.getTagStation().equals("Window"))
            {
                icon.setImageResource(R.drawable.window);
            }else
            if(tag.getTagStation().equals("Grill"))
            {
                icon.setImageResource(R.drawable.grill);
            }else
            if(tag.getTagStation().equals("Appitizer"))
            {
                icon.setImageResource(R.drawable.app);
            }
   
            else
            if(tag.getTagStation().equals("Saute"))
            {
                icon.setImageResource(R.drawable.saute);
            }else
            if(tag.getTagStation().equals("ProtienPrep"))
            {
                icon.setImageResource(R.drawable.protien);
            }else
            if(tag.getTagStation().equals("VeggiePrep"))
            {
                icon.setImageResource(R.drawable.veggie);
            }else
            if(tag.getTagStation().equals("SaucePrep"))
            {
                icon.setImageResource(R.drawable.sauce);
            }else
            if(tag.getTagStation().equals("PastaPrep"))
            {
                icon.setImageResource(R.drawable.pasta);
            }else
            if(tag.getTagStation().equals(""))
            {
                icon.setImageResource(R.drawable.unknown);
            } else 
            {
                icon.setImageResource(R.drawable.unknown);
            }
            */


			

        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}

}


