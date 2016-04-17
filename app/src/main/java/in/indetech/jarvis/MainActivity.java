package in.indetech.jarvis;

import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.communication.IOnItemFocusChangedListener;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ValueLineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MainActivity");

        final String time = Constants.getCurrentTime();
        Log.d("time test", time);
        String to_date = (Integer.parseInt(Constants.getDate()) - 7) + "";
        if (to_date.length() == 1) {
            to_date = "0" + to_date;
        }
        final String to_time = time.substring(0, 8) + to_date;

        setUpSpinner();

        setUpGraph(time, to_time);
        setUpLineGraph();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//
//                getMessages(time, to_time);
//
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpLineGraph() {

        lineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);
        loadData(0xFF63CBB0);

    }

    private void loadData(int color) {
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(color);
        for (int i = 0; i < 9; i++) {
            series.addPoint(new ValueLinePoint("Day "+i,generateRandomNumber()));
        }
        lineChart.clearChart();
        lineChart.addSeries(series);
        lineChart.startAnimation();

    }

    private float generateRandomNumber() {

        Random rand = new Random();
        int va = rand.nextInt(100);
        Log.d("rand", "" + va);
        return va;
    }

    private void setUpSpinner() {


        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getSupportActionBar().getThemedContext(), R.array.listItems, android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.modes);
        spinner.setAdapter(spinnerAdapter);


    }

    private void setUpGraph(String time, String to_time) {

//        BarChart mBarChart = (BarChart) findViewById(R.id.barChart);

        final PieChart pieChart = (PieChart) findViewById(R.id.piechart);

        DbHelper dbHelper = new DbHelper(MainActivity.this);
        ArrayList<String> users = dbHelper.getAllUsers();

        for (int i = 0; i < users.size(); i++) {
            float count = (float) dbHelper.getAllMessagesCount(users.get(i), time, to_time);
//            mBarChart.addBar(new BarModel(user,count, Color.RED));
            pieChart.addPieSlice(new PieModel(users.get(i), count, Color.parseColor(Constants.color_array[i % 12])));
        }

        pieChart.startAnimation();
        pieChart.setOnItemFocusChangedListener(new IOnItemFocusChangedListener() {
            @Override
            public void onItemFocusChanged(int _Position) {

                loadData(Color.parseColor(Constants.color_array[_Position]));
//                lineChart.notify();
            }
        });



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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        int id = item.getItemId();

        if (id == R.id.home) {

        } else if (id == R.id.friends) {
            intent = new Intent(MainActivity.this, SelectContactsScreen.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
