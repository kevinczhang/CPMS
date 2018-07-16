package com.example.home_zhang.cpms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.home_zhang.cpms.DAL.DatabaseHelper;
import com.example.home_zhang.cpms.activity.LoginActivity;
import com.example.home_zhang.cpms.adapter.CustomAdapter;
import com.example.home_zhang.cpms.model.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    List<Problem> data = new ArrayList<>();
    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchView searchView = (SearchView) findViewById(R.id.searchProblemView);
        searchView.setQueryHint("Search here");
        searchView.setQueryRefinementEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        fillProblemList(recyclerView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                final List<Problem> filteredList = new ArrayList<>();

                for (int i = 0; i < data.size(); i++) {
                    final String text = data.get(i).getTitle().toLowerCase();
                    final String number = data.get(i).getNo();
                    final String difficulty = data.get(i).getDifficultyLevel().toLowerCase();
                    if (text.contains(newText) || number.contains(newText) || difficulty.contains(newText)) {
                        filteredList.add(data.get(i));
                    }
                }
                RecyclerView.Adapter adapter = new CustomAdapter(filteredList);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String exampleText = prefs.getString("example_list", "");

        Set<String> exampleTexts = prefs.getStringSet("show_levels", new HashSet<String>());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String authToken = sharedPreferences.getString("authToken", "");

        SearchView searchView = (SearchView) findViewById(R.id.searchProblemView);
        searchView.setQueryHint(authToken);
    }

    private void fillProblemList(RecyclerView recyclerView) {
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            db.createDataBase();
            db.openDataBase();

            SQLiteDatabase sd = db.getReadableDatabase();
            Cursor cursor = sd.query("questions",
                    new String[]{"id", "source_number", "title", "level", "topics"},
                    null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                int number = cursor.getInt(1);
                String title = cursor.getString(2);
                String level = cursor.getString(3);
                String topics = cursor.getString(4);
                this.data.add(new Problem(String.valueOf(number), title, level, topics, id));
            }
            Collections.sort(this.data, new Comparator<Problem>() {
                @Override
                public int compare(Problem p1, Problem p2) {
                    return Integer.parseInt(p1.getNo()) - Integer.parseInt(p2.getNo());
                }
            });
            RecyclerView.Adapter adapter = new CustomAdapter(this.data);
            recyclerView.setAdapter(adapter);
            sd.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
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

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

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

        if (id == R.id.nav_problems) { // All problems
            final List<Problem> filteredList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                filteredList.add(data.get(i));
            }
            RecyclerView.Adapter adapter = new CustomAdapter(filteredList);
            recyclerView.setAdapter(adapter);
        } else if (id == R.id.nav_remember) { // Problems to remember
            final List<Problem> filteredList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                final List<String> tags = Arrays.asList(data.get(i).getSpecialTags().split(","));
                if (tags.contains("0")) {
                    filteredList.add(data.get(i));
                }
            }
            RecyclerView.Adapter adapter = new CustomAdapter(filteredList);
            recyclerView.setAdapter(adapter);
        } else if (id == R.id.nav_facebook) { // Facebook problems
            final List<Problem> filteredList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                final List<String> tags = Arrays.asList(data.get(i).getCompanies().split(","));
                if (tags.contains("0")) {
                    filteredList.add(data.get(i));
                }
            }
            RecyclerView.Adapter adapter = new CustomAdapter(filteredList);
            recyclerView.setAdapter(adapter);
        } else if (id == R.id.nav_codeSnippet) { // Code Snippets
            final List<Problem> filteredList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                final List<String> tags = Arrays.asList(data.get(i).getSpecialTags().split(","));
                if (tags.contains("1")) {
                    filteredList.add(data.get(i));
                }
            }
            RecyclerView.Adapter adapter = new CustomAdapter(filteredList);
            recyclerView.setAdapter(adapter);
        } else if (id == R.id.nav_recent) { // Most recent facebook interview questions
            final List<Problem> filteredList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                final List<String> tags = Arrays.asList(data.get(i).getSpecialTags().split(","));
                if (tags.contains("2")) {
                    filteredList.add(data.get(i));
                }
            }
            RecyclerView.Adapter adapter = new CustomAdapter(filteredList);
            recyclerView.setAdapter(adapter);
        } else if (id == R.id.nav_account) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
