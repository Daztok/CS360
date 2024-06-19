package com.joelhays.tracker1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GridView gridViewInventory;
    private Button buttonAddItem, buttonSMSNotification;
    private DatabaseHelper dbHelper;
    private CustomCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        dbHelper = new DatabaseHelper(this);

        gridViewInventory = findViewById(R.id.gridViewInventory);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonSMSNotification = findViewById(R.id.buttonSMSNotification);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddItemActivity.class));
            }
        });

        buttonSMSNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SMSActivity.class));
            }
        });
        // Loads the inventory data from the database when the activity is created
        loadInventoryData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInventoryData();
    }
    // Loads the inventory data from the database into the GridView
    private void loadInventoryData() {
        Cursor cursor = dbHelper.getAllItems();
        String[] from = { "name", "quantity" };
        int[] to = { R.id.textViewItemName, R.id.textViewItemQuantity };

        adapter = new CustomCursorAdapter(this, R.layout.grid_item_layout, cursor, from, to, 0);
        gridViewInventory.setAdapter(adapter);
    }
}
