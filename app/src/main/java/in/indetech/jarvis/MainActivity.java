package in.indetech.jarvis;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String time = Constants.getCurrentTime();
        Log.d("time test", time);
        String to_date = (Integer.parseInt(Constants.getDate()) - 7) + "";
        if(to_date.length()==1){
            to_date = "0"+to_date;
        }
        final String to_time = time.substring(0, 8) + to_date;

        setUpGraph(time,to_time);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



                getMessages(time, to_time);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpGraph(String time, String to_time) {

//        BarChart mBarChart = (BarChart) findViewById(R.id.barChart);

        PieChart pieChart = (PieChart) findViewById(R.id.piechart);

        DbHelper dbHelper = new DbHelper(MainActivity.this);
        ArrayList<String> users = dbHelper.getAllUsers();

        for(int i=0;i<users.size();i++){
            float count =(float) dbHelper.getAllMessagesCount(users.get(i),time,to_time);
//            mBarChart.addBar(new BarModel(user,count, Color.RED));
            pieChart.addPieSlice(new PieModel("Freetime", count, Color.parseColor(Constants.color_array[i%12])));
        }


//        mBarChart.addBar(new BarModel(2.3f, 0xFF123456));
//        mBarChart.addBar(new BarModel(2.f,  0xFF343456));
//        mBarChart.addBar(new BarModel(3.3f, 0xFF563456));
//        mBarChart.addBar(new BarModel(1.1f, 0xFF873F56));
//        mBarChart.addBar(new BarModel(2.7f, 0xFF56B7F1));
//        mBarChart.addBar(new BarModel(2.f,  0xFF343456));
//        mBarChart.addBar(new BarModel(0.4f, 0xFF1FF4AC));
//        mBarChart.addBar(new BarModel(4.f,  0xFF1BA4E6));

        pieChart.startAnimation();

    }

    private void getMessages(final String from, final String to) {

        Log.d("test time", "from " + from + " to " + to);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                DbHelper dbHelper = new DbHelper(MainActivity.this);
                ArrayList<String> users = dbHelper.getAllUsers();
                for (String username : users) {

                    int count = dbHelper.getAllMessagesCount(username, from, to);
                    Log.d("test message count", "" + count);
                }
                return null;
            }
        }.execute();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
