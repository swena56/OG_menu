package com.example.todo;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
 
public class DatabaseHelper extends SQLiteOpenHelper {
 
    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();
 
    // Database Version
    private static final int DATABASE_VERSION = 22;
    //22 is real data
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Table Names
    private static final String TABLE_TODO = "todos";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_TODO_TAG = "todo_tags";
 
    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
 
    // NOTES Table - column nmaes
    private static final String KEY_TODO = "todo";
    private static final String KEY_STATUS = "status";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_INSTRUCTION = "instruction";
    
 
    // TAGS Table - column names
    private static final String KEY_TAG_NAME = "tag_name";
    private static final String KEY_TAG_PLATE = "tag_plate";
    private static final String KEY_TAG_COOKTIME = "tag_cook_time";
    private static final String KEY_TAG_STATION = "tag_station";
    private static final String KEY_TAG_IMAGE = "tag_image_data";
    private static final String KEY_TAG_IMAGEPATH = "tag_image_path";
    private static final String KEY_TAG_RESOURCE_NUM= "tag_resource_number";
    
 
    // NOTE_TAGS Table - column names
    private static final String KEY_TODO_ID = "todo_id";
    private static final String KEY_TAG_ID = "tag_id";
 
    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_CREATED_AT
            + " DATETIME, "+ KEY_AMOUNT + " TEXT," + KEY_INSTRUCTION + " TEXT" + ")";
 
    // Tag table create statement
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME," + KEY_TAG_PLATE + " TEXT," + KEY_TAG_COOKTIME + " TEXT,"
            + KEY_TAG_STATION + " TEXT, " + KEY_TAG_IMAGE + " BLOB, "
            + KEY_TAG_IMAGEPATH +  " TEXT, "+ KEY_TAG_RESOURCE_NUM+  " INTEGER "+"  )";
 
    // todo_tag table create statement
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_TODO_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";
 
    public DatabaseHelper(Context context,String stationName) {
        super(context, DATABASE_NAME+stationName, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_TODO_TAG);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TAG);
 
        // create new tables
        onCreate(db);
    }
 
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
      db.delete(TABLE_TAG, null, null);
      db.delete(TABLE_TODO, null, null);
      db.delete(TABLE_TODO_TAG, null, null);
        
       
    }
    // ------------------------ "todos" table methods ----------------//
 
     /**
     * Creating a todo
     */
    public long createToDo(Todo todo, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getNote());
        values.put(KEY_STATUS, todo.getStatus());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_AMOUNT, todo.getAmount());
        values.put(KEY_INSTRUCTION, todo.getInstruction());
 
        // insert row
        long todo_id = db.insert(TABLE_TODO, null, values);
 
        // insert tag_ids
        for (long tag_id : tag_ids) {
            createTodoTag(todo_id, tag_id);
        }
 
        return todo_id;
    }
 
    /**
     * get single todo
     */
    public Todo getTodo(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " WHERE "
                + KEY_ID + " = " + todo_id;
 
        Log.d("DatabaseHelper", "getTodo(), QueryUsed: "+selectQuery);
 
        Cursor c = db.rawQuery(selectQuery, null);
 
        if (c != null)
            c.moveToFirst();
 
        Todo td = new Todo();
        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
        td.setAmount((c.getString(c.getColumnIndex(KEY_AMOUNT))));
        td.setInstruction((c.getString(c.getColumnIndex(KEY_INSTRUCTION))));
        td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
 
        return td;
    }
 
    /**
     * getting all todos
     * */
    public List<Todo> getAllToDos() {
        List<Todo> todos = new ArrayList<Todo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;
 
        //Log.d("DatabaseHelper", "getAllToDos(), queryUsed: "+selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Todo td = new Todo();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setAmount((c.getString(c.getColumnIndex(KEY_AMOUNT))));
                td.setInstruction((c.getString(c.getColumnIndex(KEY_INSTRUCTION))));
                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
 
                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }
 
        return todos;
    }
 
    /**
     * getting all todos under single tag
     * */
    public List<Todo> getAllToDosByTag(String tag_name) {
        List<Todo> todos = new ArrayList<Todo>();
 
        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_TODO_ID ;
 
        
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        String tempListOfTodos = "";
        if (c.moveToFirst()) {
            do {
                Todo td = new Todo();
                
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setAmount((c.getString(c.getColumnIndex(KEY_AMOUNT))));
                td.setInstruction((c.getString(c.getColumnIndex(KEY_INSTRUCTION))));
                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                tempListOfTodos = tempListOfTodos + td.getNote() + ", ";
                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }
 
        //Log.d("DatabaseHelper", "getAllToDosByTag(), queryUsed("+selectQuery+"),tag("+tag_name+"),instructions("+tempListOfTodos+")");
        return todos;
    }
 
  
    /**
     * getting todo count
     */
    public int getToDoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        int count = cursor.getCount();
        cursor.close();
 
        // return count
        return count;
    }
 
    /**
     * Updating a todo
     */
    public int updateToDo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getNote());
        values.put(KEY_STATUS, todo.getStatus());
        values.put(KEY_AMOUNT, todo.getAmount());
        values.put(KEY_INSTRUCTION, todo.getInstruction());
 
        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });
    }
 
    /**
     * Deleting a todo
     */
    public void deleteToDo(long tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }
 
    // ------------------------ "tags" table methods ----------------//
 
    /**
     * Creating tag
     */
    public long createTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_TAG_PLATE, tag.getTagPlate());
        values.put(KEY_TAG_COOKTIME, tag.getTagCookTime());
        values.put(KEY_TAG_STATION, tag.getTagStation());
        values.put(KEY_TAG_IMAGEPATH, tag.imagePath);
        values.put(KEY_TAG_RESOURCE_NUM, tag.resourceIdentifier);
        if(tag.getRawImageData()==null)
        {
           values.put(KEY_TAG_IMAGE, "");
        }else
        {

        values.put(KEY_TAG_IMAGE, tag.getRawImageData().array());
        }
        // insert row
        long tag_id = db.insert(TABLE_TAG, null, values);
      
        return tag_id;
    }
 
    
    /**
     * get tagid from tag name
     * */
    public long getTagId(String tagName) 
    {
    	
    	 String selectQuery = "SELECT  * FROM " + TABLE_TAG + " WHERE " + KEY_TAG_NAME + " = '"+tagName+"'";
    	 
         //Log.d("DatabaseHelper", "getAllTags(),queryUsed: "+selectQuery);
  
         SQLiteDatabase db = this.getReadableDatabase();	
         Cursor c = db.rawQuery(selectQuery, null);
         c.moveToFirst();
         long tagId = c.getInt((c.getColumnIndex(KEY_ID)));
    
     return tagId;
    }
    
    /**
     * getting all tags
     * */
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<Tag>();
        String selectQuery = "SELECT  * FROM " + TABLE_TAG + " ORDER BY " + KEY_TAG_NAME + " ASC";
 
        //Log.d("DatabaseHelper", "getAllTags(),queryUsed: "+selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

         /*
        byte[] blob = c.getBlob(c.getColumnIndex("image"));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
         Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        iview.setImageBitmap(bitmap);﻿

         */

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Tag t = new Tag();
                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setTagName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));
                t.setTagPlate(c.getString(c.getColumnIndex(KEY_TAG_PLATE)));
                t.setTagCookTime(c.getString(c.getColumnIndex(KEY_TAG_COOKTIME)));
                t.setTagStation(c.getString(c.getColumnIndex(KEY_TAG_STATION)));
                t.imagePath = c.getString(c.getColumnIndex(KEY_TAG_IMAGEPATH));
                t.resourceIdentifier = c.getColumnIndex(KEY_TAG_RESOURCE_NUM);
                if(c.getBlob(c.getColumnIndex(KEY_TAG_IMAGE))!=null)
                {
                    byte[] bitmapData = c.getBlob(c.getColumnIndex(KEY_TAG_IMAGE));
                    Log.d("DatabaseHelper","getAllTags"+bitmapData.toString());
                    Bitmap image = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
                    t.setImage(image);
                }else
                {
                    t.setImage(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.boat));
                }
                // adding to tags list
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }
    
  
    
    /**
     * get single tag
     */
    public Tag getTag(long tagId) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        String selectQuery = "SELECT  * FROM " + TABLE_TAG + " WHERE "
                + KEY_ID + " = " + tagId;
 
        Log.d("DatabaseHelper", "getTag(), QueryUsed: "+selectQuery);
 
        Cursor c = db.rawQuery(selectQuery, null);
 
        if (c != null)
            c.moveToFirst();
 
        Tag tag = new Tag();
        tag.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        tag.setTagName((c.getString(c.getColumnIndex(KEY_TAG_NAME))));
        tag.setTagPlate((c.getString(c.getColumnIndex(KEY_TAG_PLATE))));
        tag.setTagCookTime((c.getString(c.getColumnIndex(KEY_TAG_COOKTIME))));
        tag.setTagStation((c.getString(c.getColumnIndex(KEY_TAG_STATION))));
        tag.imagePath = c.getString(c.getColumnIndex(KEY_TAG_IMAGEPATH));
        tag.resourceIdentifier = c.getColumnIndex(KEY_TAG_RESOURCE_NUM);
        byte[] bitmapData = c.getBlob(c.getColumnIndex(KEY_TAG_IMAGE));

        Bitmap image = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
        tag.setImage(image);
        //tag.setImage((c.getString(c.getColumnIndex(KEY_TAG_IMAGE))));
        Log.d("DatabaseHelper","(getTag),image size: "+bitmapData.length);
        return tag;
    }
 
    /**
     * Updating a tag
     */
    public int updateTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();


        //log data
        Log.d("DatabaseHelper", "UpdateTag() "+tag.id+
        		"\ntagName: "+tag.tag_name+
        		"\nTagPlate "+tag.getTagPlate());
              //  "\nImageData: "+tag.getImage().toString());
        
        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());
        values.put(KEY_TAG_PLATE, tag.getTagPlate());
        values.put(KEY_TAG_COOKTIME, tag.getTagCookTime());
        values.put(KEY_TAG_STATION, tag.getTagStation());
        values.put(KEY_TAG_IMAGEPATH, tag.imagePath);
        values.put(KEY_TAG_RESOURCE_NUM, tag.resourceIdentifier);
        if(tag.getImage()!=null)
        {
            values.put(KEY_TAG_IMAGE,tag.getRawImageData().array());
            Log.d("DatabaseHelper","rawdata:"+tag.getRawImageData().array().toString());
        }else
        {
            Log.d("DatabaseHelper","no image was saved");
        }
        // updating row
        return db.update(TABLE_TAG, values, KEY_ID + " = ?",
                new String[] { String.valueOf(tag.getId()) });
    }
 
    /**
     * Deleting a tag
     */
    public void deleteTag(Tag tag, boolean should_delete_all_tag_todos) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        // before deleting tag
        // check if todos under this tag should also be deleted
        if (should_delete_all_tag_todos) {
            // get all todos under this tag
            List<Todo> allTagToDos = getAllToDosByTag(tag.getTagName());
 
            // delete all todos
            for (Todo todo : allTagToDos) {
                // delete todo
                deleteToDo(todo.getId());
            }
        }
 
        // now delete the tag
        db.delete(TABLE_TAG, KEY_ID + " = ?",
                new String[] { String.valueOf(tag.getId()) });
    }
 
    /**
     * Deleting all Tags
     */
    public void deleteAllTags() {
        SQLiteDatabase db = this.getWritableDatabase();
 
        // before deleting tag
        // check if todos under this tag should also be deleted
        
        db.delete(TABLE_TAG, null, null);
        db.close();
     
    }
    
    // ------------------------ "todo_tags" table methods ----------------//
 
    /**
     * Creating todo_tag
     */
    public long createTodoTag(long todo_id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_ID, todo_id);
        values.put(KEY_TAG_ID, tag_id);
        values.put(KEY_CREATED_AT, getDateTime());
 
        long id = db.insert(TABLE_TODO_TAG, null, values);
 
        return id;
    }
 
    /**
     * Updating a todo tag
     */
    public int updateNoteTag(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TAG_ID, tag_id);
 
        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
 
    /**
     * Deleting a todo tag
     */
    public void deleteToDoTag(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
 
    
    
    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
 
    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    
   
}
 
