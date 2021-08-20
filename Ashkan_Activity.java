//This Project Is About Kotlin Select Contact.
//------------------------------
package com.example;
//------------------------------
import java.io.IOException;
import java.io.InputStream;
//------------------------------
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.content.ContentUris;
import android.provider.ContactsContract;
import android.util.Log;
import android.content.Intent;
import android.graphics.BitmapFactory;
//------------------------------
public class Ashkan_Activity extends Activity{
 
    private static final String TAG = Ashkan_Activity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClickShowContacts(View btnSelectContact) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();
 
            retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();
 
        }
    }

    private void retrieveContactPhoto() {
 
        Bitmap photo = null;
 
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
 
            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                ImageView imageView = (ImageView) findViewById(R.id.img_contact);
                imageView.setImageBitmap(photo);
            }
 
            assert inputStream != null;
            inputStream.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }


    private void retrieveContactNumber() {
 
        String contactNumber = null;
 
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
 
        if (cursorID.moveToFirst())
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);
 
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
 
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
 
                new String[]{contactID},
                null);
 
        if (cursorPhone.moveToFirst())
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
 

        cursorPhone.close();
 
        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }


    private void retrieveContactName() {
	 
        String contactName = null;
 
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
 
        if (cursor.moveToFirst())
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        
        cursor.close();
 
        Log.d(TAG, "Contact Name: " + contactName);
 
    }
}