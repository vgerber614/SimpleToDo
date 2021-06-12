package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    // xml items
    EditText editItemText;
    Button saveBtn;

    /**
     * onCreate
     * initializes the activity
     * sets the results of the intent based on user input
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // grab view objects from activity_edit by id
        editItemText = findViewById(R.id.editItemText);
        saveBtn = findViewById(R.id.saveBtn);

        getSupportActionBar().setTitle("Edit Item");

        // get data passed from main activity
        String itemText = getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT);

        editItemText.setText(itemText);

        // when save button is clicked, update changes
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemText = editItemText.getText().toString();
                int itemPosition = getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION);

                // create an intent -- components used to communicate between one activities
                Intent intent = new Intent();

                // pass the data (results of editing) through the intent
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, itemText);
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, itemPosition);

                // set results of the intent
                setResult(RESULT_OK, intent);

                // finish the activity -- close current screen and return to main activity
                finish();
            }
        });
    }
}