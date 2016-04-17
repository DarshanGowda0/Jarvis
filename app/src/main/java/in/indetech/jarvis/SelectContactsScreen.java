package in.indetech.jarvis;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SelectContactsScreen extends AppCompatActivity {

    ListView mListView;
    ArrayList<String> myWhatsappContacts = new ArrayList<>();
    ArrayList<ContactData> contactList = new ArrayList<>();
    ArrayList<String> selected_users = new ArrayList<>();
    SharedPreferences preferences;
    int reqCode = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts_screen);
        preferences = PreferenceManager.getDefaultSharedPreferences(SelectContactsScreen.this);
        checkNotificationPermission();
        getContacts();
//        getSelectedUsers();
        setUpListView();

    }

    private void getSelectedUsers() {

        DbHelper dbHelper = new DbHelper(SelectContactsScreen.this);
        selected_users = dbHelper.getAllUsers();

        for (String user : selected_users) {
            for (int i = 0; i < contactList.size(); i++) {
                if(contactList.get(i).name.equals(user)){
                    contactList.get(i).selected=true;
                }
            }
        }


    }

    private void checkNotificationPermission() {


        if (!preferences.getBoolean(Constants.PERMISSION_PREF, false)) {

            new AlertDialog.Builder(SelectContactsScreen.this).setTitle("Permission required")
                    .setMessage("This app requires you to grant notification access")
            .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent callSettingIntent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                    startActivityForResult(callSettingIntent, reqCode);

                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == reqCode) {
            preferences.edit().putBoolean(Constants.PERMISSION_PREF, true).apply();

        } else
            preferences.edit().putBoolean(Constants.PERMISSION_PREF, false).apply();
    }

    private void setUpListView() {

        mListView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectContactsScreen.this, android.R.layout.simple_list_item_checked, myWhatsappContacts);

        mListView.setAdapter(arrayAdapter);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactList.get(position).selected = !contactList.get(position).selected;
            }
        });


    }

    //get whatsApp contacts
    public void getContacts() {
        Cursor c = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"},
                null);

        int contactNameColumn = c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
        while (c.moveToNext()) {
            // You can also read RawContacts.CONTACT_ID to read the
            // ContactsContract.Contacts table or any of the other related ones.
            myWhatsappContacts.add(c.getString(contactNameColumn));
        }

        sortArray();

        for (int i = 0; i < myWhatsappContacts.size(); i++) {
            ContactData contactData = new ContactData();
            contactData.name = myWhatsappContacts.get(i);
            contactData.selected = false;
            contactList.add(contactData);
        }

        c.close();

    }

    private void sortArray() {

        Collections.sort(myWhatsappContacts, new Comparator<String>() {
            @Override
            public int compare(String name2, String name1)
            {

                return  name2.compareTo(name1);
            }
        });

    }


    public class ContactData {
        String name;
        boolean selected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_game:
                moveToNextActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void moveToNextActivity() {

        storeInDb();

        storeInPrefs();

        Intent intent = new Intent(SelectContactsScreen.this, MainActivity.class);
        startActivity(intent);

        finish();

    }

    private void storeInDb() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                DbHelper dbHelper = new DbHelper(SelectContactsScreen.this);


                for (ContactData contactData : contactList) {
                    if (contactData.selected) {
                        dbHelper.insertUsers(contactData.name);
                    }
                }

                return null;
            }
        }.execute();

    }

    private void storeInPrefs() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SelectContactsScreen.this);
        preferences.edit().putBoolean(Constants.ALL_PERMISSIONS_DONE, true).apply();

    }

}


