package com.example.piendop.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    //static global variables
    static ArrayList<String> notes = new ArrayList<>();//store notes data static for update
    static ArrayAdapter<String> arrayAdapter;//used to present notes' data in list view
                                            // static for update


    /***************SET UP MENU**********************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //link layout with menu
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        //add a new note start an intent
        if(item.getItemId()==R.id.newNote){
            //when we delete am element in array list it doesn't decrease
            // the size so we have to be careful
            startIntentToNoteActivity(-1);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //share preference for get data from storage
        SharedPreferences sharedPreferences = getApplicationContext().
                getSharedPreferences("com.example.piendop.notes",MODE_PRIVATE);

        //bc if we add the array list every time with shared preference we have to clear it but now
        // we use hash set so that we no need to clear before add a new list item
        //hash set get data from shared preferences and if no data in share references it will be null
        HashSet<String> set = (HashSet<String>)sharedPreferences.getStringSet("notes",null);
        if(set==null) {//no item in list view
            notes.add("Example note");
        }else{//have items on the list
            //update data by create a new notes based on hash set
            notes = new ArrayList<>(set);
        }
        //find list view to set array adapter with layout and data
        ListView listView = findViewById(R.id.listView);
        /*************************************/
        setLinkBetweenDataAndListView(listView);
        /*************************************/
        deleteNote(listView);
    }


    /***array Adapter is set to link data with list view present data in layout of list view****/
    private void setLinkBetweenDataAndListView(ListView listView) {
        //create array adapter to link data with layout
        arrayAdapter = new ArrayAdapter<>
                (getApplicationContext(),R.layout.black_text,R.id.textView,notes);

        listView.setAdapter(arrayAdapter);
        //start activity when click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startIntentToNoteActivity(i);
            }
        });
    }

    /******START INTENT TO NOTE ACTIVITY*********/
    private void startIntentToNoteActivity(int i) {
        //intent to second activity
        Intent intent = new Intent(getApplicationContext(),NoteActivity.class);
        intent.putExtra("noteIndex",i);
        //start intent when click on item in list view
        startActivity(intent);
    }

    /*********DELETE A NOTE********/

    private void deleteNote(ListView listView) {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View itemView, int i, long l) {
                final int indexItemDeleted = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //if yes remove the note
                                notes.remove(indexItemDeleted);

                                //update changes in data of list view
                                arrayAdapter.notifyDataSetChanged();

                                //update data in share preference
                                SharedPreferences sharedPreferences = getApplicationContext().
                                        getSharedPreferences("com.example.piendop.notes",MODE_PRIVATE);
                                HashSet<String> set =  new HashSet<>(notes);
                                sharedPreferences.edit().putStringSet("notes",set).apply();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
    }

}
