package com.example.todo;


 
public class Todo {
 
    int id;
    String note = "";
    String amount = "";
    String instruction = "";
    int status;
    String created_at;
 
    // constructors
    public Todo() {
    }
 
    public Todo(String note, int status) {
        this.note = note;
        this.status = status;
    }
 
    public Todo(int id, String note, int status) {
        this.id = id;
        this.note = note;
        this.status = status;
    }
 
    // setters
    public void setId(int id) {
        this.id = id;
    }
 
    public void setNote(String note) {
        this.note = note;
    }
    
    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
 
    public void setStatus(int status) {
        this.status = status;
    }
     
    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }
 
    // getters
    public long getId() {
        return this.id;
    }
 
    public String getNote() {
        return this.note;
    }
    
    public String getAmount() {
        return this.amount;
    }
    
    public String getInstruction() {
        return this.instruction;
    }
 
    public int getStatus() {
        return this.status;
    }
}
