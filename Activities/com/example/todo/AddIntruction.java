package com.example.todo;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddIntruction extends Activity {

	
	TextView title;
	EditText content;
	EditText amount;
	EditText instruction;
	
	Button submit;
	View deleteButtonView;
	Button delete;
	Button cancel;
	
	long tagId;
	long todoId;
	int todoIndex;
	String tagText;
	
	String contentPassed;
	String amountPassed;
	String instructionPassed;
	String stationSelected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_intruction);
		
		
		
		Intent intent = getIntent();
		//long tagId = intent.getLongExtra("TagId", -1);
		stationSelected = intent.getStringExtra("Station");
	    tagId = intent.getExtras().getLong("TagId"); //gets the index from list passed through an intent
	    todoId = intent.getLongExtra("todoId",-1); //gets the index from list passed through an intent
	    todoIndex = intent.getIntExtra("todoIndex",-1); 
	    tagText = intent.getStringExtra("Tag");
		contentPassed = intent.getStringExtra("Content");
		amountPassed = intent.getStringExtra("Amount");
		instructionPassed = intent.getStringExtra("Instruction");
		
		Log.d("AddInstruction","indexpassed: "+todoIndex);
		//Initialize xml components
	    title = (TextView) findViewById(R.id.addInstructionsDisclaimer);
		title.setText("Add Instructions for: "+tagText);
	    content = (EditText) findViewById(R.id.editTextContents);
		content.setText(contentPassed);
	    amount = (EditText) findViewById(R.id.editTextAmount);
		amount.setText(amountPassed);
	    instruction = (EditText) findViewById(R.id.editTextInstruction);
		instruction.setText(instructionPassed);
		
		
		if(todoIndex == -1)
		{
			Log.d("AddInstruction","Create new instruction");
			//delete.setVisibility(View.INVISIBLE);
		
			
		}else
		{
			Log.d("AddInstruction","Item to edit instruction to: "+tagText);
		}
		
		
				
		//listen for onclick of submit button
	    submit = (Button) findViewById(R.id.buttonSubitInstruction);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Log.d("addInstruction","submit was clicked");
				//check which tag the instruction should be added to from intent extras 
				//Prepare database
				
				//if index is equal to -1 then save else update
				if(todoIndex < 0)
				{
					//create todo object
					Todo instructionToAdd = new Todo(content.getText().toString(),0);  //contents, status
					instructionToAdd.setAmount(amount.getText().toString());
					instructionToAdd.setInstruction(instruction.getText().toString());
					
					//open data base and add instruction
					DatabaseHelper db = new DatabaseHelper(getApplicationContext(),stationSelected);
				    db.createToDo(instructionToAdd, new long[] { db.getTagId(tagText) });
				    db.close();
				    
				    //message to user
				    Toast instructionAdded = Toast.makeText(getApplication(),"Instruction added: "+content.getText().toString(),Toast.LENGTH_SHORT);
				    instructionAdded.show();
				} else
				{
					//open database
					DatabaseHelper db = new DatabaseHelper(getApplicationContext(),stationSelected);
					
					//List<Todo> tempTodoList = db.getAllToDosByTag(tagText);
					List<Todo> tempTodoList = db.getAllToDos();
					Log.d("AddInstruction","EditMode- which todoid: "+todoId);
					
					
					///List<Todo> tempTodo = db.getAllToDosByTag(tagText);
					//int sizeOfList = tempTodo.size();
					
					
						Log.d("AddInstruction","index:"+todoIndex);
					
					Todo update = tempTodoList.get(todoIndex);
					//Todo update = tempTodoList.get((int) (todoId));
					update.setId((int) todoId);
					update.setNote(content.getText().toString());
					update.setAmount(amount.getText().toString());
					update.setInstruction(instruction.getText().toString());
					Log.d("AddInstruction","TodoUsedForUpdate: "+update.getId());
					int success = db.updateToDo(update);
					
					Log.d("AddInstruction","id: "+tempTodoList.get(todoIndex).getId()+ 
							",name: "+tempTodoList.get(todoIndex).getNote()+
							",amount: "+tempTodoList.get(todoIndex).getAmount()+
							",instruction: "+tempTodoList.get(todoIndex).getInstruction()+
							",Success: "+success);
					//close database
					db.close();
				}
				
				finish();
			   
			}
		});
		
	    cancel = (Button) findViewById(R.id.buttonInstructionCancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		}); //end of cancel button on click
		
	    delete = (Button) findViewById(R.id.buttonInstructionDelete);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatabaseHelper db = new DatabaseHelper(getApplicationContext(),stationSelected);
				Log.d("AddInstruction","instructionID to delete: "+todoId);
				List<Todo> tempTodo = db.getAllToDosByTag(tagText);
				Todo todoToDelete = tempTodo.get(todoIndex);
				
				Log.d("AddInstruction","instruction to Delete: " + todoToDelete.getId() + ", "+ todoToDelete.getNote());
				
				//db.deleteToDo(todoToDelete.getId());
				db.deleteToDoTag(todoToDelete.getId());
				db.close();
				finish();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_intruction, menu);
		return true;
	}

}
