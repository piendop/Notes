package com.example.piendop.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapShader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        //taking note
        takingNote();
    }

    /*********Taking Note function************/
    private void takingNote() {
        //create an intent to get index of list view
        Intent intent = getIntent();
        int index= intent.getIntExtra("noteIndex",0);




        /**************************/
        index = updateIndex(index);

        /**************************/
        updateEditText(index);

    }

    /***UPDATE INDEX CASE 1: CLICK TO ITEM NO UPDATE CASE 2: ADD NEW NOTE UPDATE***/
    private int updateIndex(int index) {
        EditText editText =(EditText) findViewById(R.id.editText);
        //Now we have two cases if index = 0 , the first note click on Listview
        //else the  >second note add a new note
        if(index==-1){//must update index
            editText.setText("");
            //since we don't have the item right now (we didn't add it to notes array lit)
            //add dummy values to notes array list
            //so now we have this index
            index=MainActivity.notes.size();
            MainActivity.notes.add(index,"");
        }else{//this item is already existed so we have to set text by the data stored in share preference
            Log.i("Index",Integer.toString(index));
            editText.setText(MainActivity.notes.get(index));
        }
        return index;
    }

    /********UPDATE EDIT TEXT AFTER TAKING NOTE***************/
    private void updateEditText(final int index) {
        Log.i("Index",Integer.toString(index));
        EditText editText =(EditText) findViewById(R.id.editText);
        //update text when taking note
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //we take note by update editText
                //the trick is that we use a update every change of the text we use index to do that
                //we use set function to update text immediately
                MainActivity.notes.set(index,charSequence.toString());
                //update changes in data
                MainActivity.arrayAdapter.notifyDataSetChanged();
                //then we must store a new text to shared preference
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences
                        ("com.example.piendop.notes",MODE_PRIVATE);
                //create a new hash set add all notes in array list notes
                HashSet<String> set =  new HashSet<>(MainActivity.notes);
                //put string to store permanently update new data need to be stored
                sharedPreferences.edit().putStringSet("notes",set).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}
