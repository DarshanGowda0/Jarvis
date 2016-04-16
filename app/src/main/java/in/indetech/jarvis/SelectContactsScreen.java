package in.indetech.jarvis;import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class SelectContactsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts_screen);
        getContacts();

    }

    //get whatsApp contacts
    public void getContacts() {
        Cursor c = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"},
                null);

        ArrayList<String> myWhatsappContacts = new ArrayList<>();
        int contactNameColumn = c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
        while (c.moveToNext()) {
            // You can also read RawContacts.CONTACT_ID to read the
            // ContactsContract.Contacts table or any of the other related ones.
            myWhatsappContacts.add(c.getString(contactNameColumn));
        }


        for (int i = 0; i < myWhatsappContacts.size(); i++)
            Log.d("test", myWhatsappContacts.get(i));

        c.close();

    }
}


