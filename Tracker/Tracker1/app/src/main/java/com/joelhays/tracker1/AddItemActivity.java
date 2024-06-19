package com.joelhays.tracker1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText editTextItemName, editTextItemQuantity, editTextItemThreshold;
    private Button buttonSaveItem;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        dbHelper = new DatabaseHelper(this);

        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemQuantity = findViewById(R.id.editTextItemQuantity);
        editTextItemThreshold = findViewById(R.id.editTextItemThreshold);
        buttonSaveItem = findViewById(R.id.buttonSaveItem);

        buttonSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextItemName.getText().toString();
                String quantityStr = editTextItemQuantity.getText().toString();
                String thresholdStr = editTextItemThreshold.getText().toString();

                // Check if any of the fields are empty
                if (name.isEmpty() || quantityStr.isEmpty() || thresholdStr.isEmpty()) {
                    Toast.makeText(AddItemActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Parse the quantity and threshold values
                int quantity = Integer.parseInt(quantityStr);
                int threshold = Integer.parseInt(thresholdStr);

                // Add the item to the database
                if (dbHelper.addItem(name, quantity, threshold)) {
                    Toast.makeText(AddItemActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Handle the case where adding the item fails
                    Toast.makeText(AddItemActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
