package com.joelhays.tracker1;

import android.content.Context;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CustomCursorAdapter extends SimpleCursorAdapter {

    private DatabaseHelper dbHelper;
    private Context context;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.grid_item_layout, parent, false);
    }

    @Override // Bind the data to the view
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewItemName = view.findViewById(R.id.textViewItemName);
        TextView textViewItemQuantity = view.findViewById(R.id.textViewItemQuantity);
        ImageButton buttonDecrement = view.findViewById(R.id.buttonDecrement);
        ImageButton buttonIncrement = view.findViewById(R.id.buttonIncrement);
        ImageButton buttonDeleteItem = view.findViewById(R.id.buttonDeleteItem);

        final int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String itemName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int itemQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        int itemThreshold = cursor.getInt(cursor.getColumnIndexOrThrow("threshold"));
        int notificationSent = cursor.getInt(cursor.getColumnIndexOrThrow("notification_sent"));

        textViewItemName.setText(itemName);
        textViewItemQuantity.setText(String.valueOf(itemQuantity));

        buttonDecrement.setOnClickListener(new View.OnClickListener() {
            @Override // Decrement the item quantity when the button is clicked
            public void onClick(View v) {
                if (itemQuantity > 0) {
                    int newQuantity = itemQuantity - 1;
                    dbHelper.updateItemQuantity(itemId, newQuantity);
                    notifyIfThresholdCrossed(itemId, itemName, newQuantity, itemThreshold, notificationSent);
                    Cursor newCursor = dbHelper.getAllItems();
                    swapCursor(newCursor);
                }
            }
        });

        buttonIncrement.setOnClickListener(new View.OnClickListener() {
            @Override // Increment the item quantity when the button is clicked
            public void onClick(View v) {
                int newQuantity = itemQuantity + 1;
                dbHelper.updateItemQuantity(itemId, newQuantity);
                notifyIfThresholdCrossed(itemId, itemName, newQuantity, itemThreshold, notificationSent);
                Cursor newCursor = dbHelper.getAllItems();
                swapCursor(newCursor);
            }
        });

        buttonDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override // Delete the item when the button is clicked
            public void onClick(View v) {
                dbHelper.deleteItem(itemId);
                Cursor newCursor = dbHelper.getAllItems();
                swapCursor(newCursor);
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Function to notify the user if the item quantity falls below the threshold
    private void notifyIfThresholdCrossed(int itemId, String itemName, int newQuantity, int threshold, int notificationSent) {
        if (newQuantity <= threshold && notificationSent == 0) {
            // If the item quantity is at or below the threshold and the notification has not been sent,
            sendSMSNotification("Item quantity for " + itemName + " is at or below minimum threshold!");
            dbHelper.updateNotificationSent(itemId, 1);
        } else if (newQuantity > threshold && notificationSent == 1) {
            // If the item quantity is above the threshold and the notification has been sent,
            sendSMSNotification("Item quantity for " + itemName + " is above threshold!");
            dbHelper.updateNotificationSent(itemId, 0);
        }
    }

    private void sendSMSNotification(String message) {
        String phoneNumber = "1234567890";
        // Replace with your phone number
        try {
            // Send the SMS message
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Handle any exceptions that may occur
            Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
