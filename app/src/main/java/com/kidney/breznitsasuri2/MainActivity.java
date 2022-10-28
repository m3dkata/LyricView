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
        suriList.add(new Suri("Ал-Фатиха - الفاتحة", 0, 1, "Ал-Бакара - البقرة, Аят 127"));
        suriList.add(new Suri("Ал-Бакара - البقرة", 1, 2,"Ал-Бакара - البقرة, Аят 128"));
        suriList.add(new Suri("Ал-Имран - آل عمران", 2, 3,"Ал-Бакара - البقرة, Аят 201"));
        suriList.add(new Suri("Ан-Ниса - النساء", 3, 4,"Ал-Бакара - البقرة, Аят 250"));
        suriList.add(new Suri("Ал-Маида - المائدة", 4, 5,"Ал-Бакара - البقرة, Аят 286"));
        suriList.add(new Suri("Ал-Анам - الأنعام", 5, 6,"Ал-Имран - آل عمران, Аят 8"));
        suriList.add(new Suri("Ал-Араф - الأعراف", 6, 7,"Ал-Имран - آل عمران, Аят 9"));
        suriList.add(new Suri("Ал-Анфал - الأنفال", 7, 8,"Ал-Имран - آل عمران, Аят 16"));
        suriList.add(new Suri("Ат-Тауба - التوبة", 8, 9,"Ал-Имран - آل عمران, Аят 53"));
        suriList.add(new Suri("Юнус - يونس", 9, 10,"Ал-Имран - آل عمران, Аят 147"));
        suriList.add(new Suri("Худ - هود", 10, 11,"Ал-Имран - آل عمران, Аят 191"));
        suriList.add(new Suri("Юсуф - يوسف", 11, 12,"Ал-Имран - آل عمران, Аят 192"));
        suriList.add(new Suri("Ар-Раад - الرعد", 12, 13,"Ал-Имран - آل عمران, Аят 193"));
        suriList.add(new Suri("Ибрахим - ابراهيم", 13, 14,"Ал-Имран - آل عمران, Аят 194"));



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