package in.indetech.jarvis;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
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

public class SelectContactsScreen extends AppCompatActivity {

    ListView mListView;
    ArrayList<String> myWhatsappContacts = new ArrayList<>();
    ArrayList<ContactData> contactList = new ArrayList<>();
    SharedPreferences preferences;
    int reqCode = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts_screen);
        preferences = PreferenceManager.getDefaultSharedPreferences(SelectContactsScreen.this);
        checkNotificationPermission();
        getContacts();
        setUpListView();

    }

    private void checkNotificationPermission() {


        if (!preferences.getBoolean(Constants.PERMISSION_PREF, false)) {

            Intent callSettingIntent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivityForResult(callSettingIntent, reqCode);

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


        for (int i = 0; i < myWhatsappContacts.size(); i++) {
            ContactData contactData = new ContactData();
            contactData.name = myWhatsappContacts.get(i);
            contactData.selected = false;
            contactList.add(contactData);
        }

        c.close();

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

        for (ContactData contactData : contactList) {
            if (contactData.selected) {
                Log.d("test contacts", contactData.name);
            }
        }

        storeInPrefs();

        Intent intent = new Intent(SelectContactsScreen.this, MainActivity.class);
        startActivity(intent);

        finish();

    }

    private void storeInPrefs() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SelectContactsScreen.this);
        preferences.edit().putBoolean(Constants.ALL_PERMISSIONS_DONE, true).apply();

    }

}


