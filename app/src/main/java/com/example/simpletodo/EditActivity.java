package com.example.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText eItem;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        eItem = findViewById(R.id.eItem);
        btnSave =  findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit item");

        eItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.KEY_ITEM_TEXT,eItem.getText().toString());
                intent.putExtra(MainActivity.Key_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.Key_ITEM_POSITION));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}