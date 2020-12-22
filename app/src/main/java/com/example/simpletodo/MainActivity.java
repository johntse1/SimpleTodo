package com.example.simpletodo;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String Key_ITEM_POSITION= "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText eItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        eItem = findViewById(R.id.ettitem);
        rvItems=findViewById(R.id.rvitems);

        loaditems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int pos) {
                //Delete the item at long pressed position, Notify adapter
                items.remove(pos);
                itemsAdapter.notifyItemRemoved(pos);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener(){
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","single click at position "+position);
                //Create the new activity and pass the relevant data being edited, and display
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(Key_ITEM_POSITION, position);
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemsAdapter= new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem =eItem.getText().toString();
                //add and notify
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size()-1);
                eItem.setText("");
                Toast.makeText(getApplicationContext(),"Item added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    //Handles the result
    @SuppressLint("ShowToast")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK&&requestCode==EDIT_TEXT_CODE){
            //REceive text and update value at original position
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt((Key_ITEM_POSITION));
            items.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(),"Item updated successfully",Toast.LENGTH_SHORT).show();
        }
        else{
            Log.w("Main activity","unknown call");
        }
    }

    //Return file where list is stored
    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }
    private void loaditems(){
        //Read all lines and add into arraylist
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Error reading items",e);
             items=new ArrayList<>();
        }
    }
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity","Errorreadingitems",e);
        }
    }
    //Reads data file and loads item by reading line of the data file
    //Writes the data file
}
