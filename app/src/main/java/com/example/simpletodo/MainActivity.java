package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    private static final int EDIT_TEXT_CODE = 20;

    // data model
    List<String> items;

    // xml items
    Button addBtn;
    EditText addItemText;
    RecyclerView todoItemsRV;

    // adapter for the recycler view
    ItemsAdapter itemsAdapter;


    /**
     * onCreate
     * Initializes the activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // grab view objects from activity_main by id
        addBtn = findViewById(R.id.addBtn);
        addItemText = findViewById(R.id.addItemText);
        todoItemsRV = findViewById(R.id.todoItemsRV);

        // load items from data file
        loadItems();

        // create listeners for adapter and implement listener callbacks
        // to handle event based on the data (item position) received from the items adapter.
        // uses the listener interfaces defined in the ItemsAdapter class
        ItemsAdapter.OnLongClickListener longClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // remove item from model
                items.remove(position);
                // notify adapter
                itemsAdapter.notifyItemRemoved(position);
                // provide user feedback with toast
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemsAdapter.OnClickListener clickListener = new ItemsAdapter.OnClickListener() {
            // opens edit activity for this item
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position " + position);
                // create the new activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                // pass the data to edit
                intent.putExtra(KEY_ITEM_TEXT, items.get(position));
                intent.putExtra(KEY_ITEM_POSITION, position);
                // display activity
                startActivityForResult(intent, EDIT_TEXT_CODE);
            }
        };

        // construct the adapter and set to the recycler view
        itemsAdapter = new ItemsAdapter(items, longClickListener, clickListener);
        todoItemsRV.setAdapter(itemsAdapter);
        todoItemsRV.setLayoutManager(new LinearLayoutManager(this));

        // set listeners (assign to buttons)
        // uses the listener interface defined in android's view class
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // on click, do this:
                String newItem = addItemText.getText().toString();
                // add new item to model
                items.add(newItem);
                // notify adapter of new item
                itemsAdapter.notifyItemInserted(items.size() - 1);
                // clear text field
                addItemText.setText("");
                // provide user feedback with toast
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }


    /**
     * onActivityResult
     * handle result for the edit activity.
     * defines actions to perform when we return from the edit activity based on returned result.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // get position of item modified
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            // update model
            items.set(position, itemText);
            // notify adapter
            itemsAdapter.notifyItemChanged(position);
            // persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // log warning
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }


    /**
     * DATA FILE HANDLERS - implement data persistence using file system
     */

    // returns file storing list of todo items
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // loads items by reading every line of data.txt file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>(); // set items to empty list
        }
    }

    // saves items by writing them into the data file
    private void saveItems() {
        // write items to file
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving items", e);
        }
    }
}