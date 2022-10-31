package com.kidney.breznitsasuri2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private ArrayList<Suri> suriList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSearchWidgets();
        setTitle("     ДУИ ОТ КОРАНА");
        centerTitle();

        recyclerView = findViewById(R.id.recycler_view);

        suriList = new ArrayList<Suri>();
        suriList.add(new Suri("Ал-Бакара, Аят 127", 0, 1, "البقرة"));
        suriList.add(new Suri("Ал-Бакара, Аят 128", 1, 2,"البقرة"));
        suriList.add(new Suri("Ал-Бакара, Аят 201", 2, 3,"البقرة"));
        suriList.add(new Suri("Ал-Бакара, Аят 250", 3, 4,"البقرة"));
        suriList.add(new Suri("Ал-Бакара, Аят 286", 4, 5,"البقرة"));
        suriList.add(new Suri("Ал-Имран, Аят 8", 5, 6,"آل عمران"));
        suriList.add(new Suri("Ал-Имран, Аят 9", 6, 7,"آل عمران"));
        suriList.add(new Suri("Ал-Имран, Аят 16", 7, 8,"آل عمران"));
        suriList.add(new Suri("Ал-Имран, Аят 53", 8, 9,"آل عمران"));
        suriList.add(new Suri("Ал-Имран, Аят 147", 9, 10,"آل عمران"));
        suriList.add(new Suri("Ал-Имран, Аят 191", 10, 11,"آل عمران"));
        suriList.add(new Suri("Ал-Имран, Аят 192", 11, 12,"آل عمران"));
        suriList.add(new Suri("Ал-Имран, Аят 193", 12, 13,"آل عمران"));
        suriList.add(new Suri("Ал-Имран, Аят 194", 13, 14,"آل عمران"));



        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        SuriListAdapter suriAdapter = new SuriListAdapter(suriList,this);
        recyclerView.setAdapter(suriAdapter);

    }

    private void initSearchWidgets()
    {
        SearchView searchView = (SearchView) findViewById(R.id.search_item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<Suri> searchSuri = new ArrayList<Suri>();
                for (Suri suri: suriList){
                    if (suri.getName().toLowerCase().contains(s.toLowerCase()))
                    {
                        searchSuri.add(suri);
                    }
                }
                SuriListAdapter adapter = new SuriListAdapter(searchSuri, getApplicationContext());
                recyclerView.setAdapter(adapter);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<Suri> searchSuri = new ArrayList<Suri>();
                for (Suri suri: suriList){
                    if (suri.getName().toLowerCase().contains(s.toLowerCase()))
                    {
                        searchSuri.add(suri);
                    }
                }
                SuriListAdapter adapter = new SuriListAdapter(searchSuri, getApplicationContext());
                recyclerView.setAdapter(adapter);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new SuriListAdapter(suriList,getApplicationContext()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void centerTitle() {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_btn:
                Intent intent = new Intent(this,InfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                return true;
            case R.id.namazTime:
                intent = new Intent(this,Namaz.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}