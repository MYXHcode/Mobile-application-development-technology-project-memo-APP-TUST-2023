package com.myxh.note.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myxh.note.R;
import com.myxh.note.adapter.NoteCursorAdapter;
import com.myxh.note.db.NotesDatabaseHelper;

import java.lang.reflect.Method;

/**
 * @author MYXH
 * @date 2023/5/27
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private SQLiteDatabase dbReader;
    private ListView listView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        NotesDatabaseHelper notesDatabaseHelper = new NotesDatabaseHelper(this);

        dbReader = notesDatabaseHelper.getReadableDatabase();

        initView();
    }

    @SuppressLint("Range")
    public void initView()
    {
        listView = findViewById(R.id.list);

        FloatingActionButton floatingActionButton = findViewById(R.id.add);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        SearchView mSearchView = findViewById(R.id.search_view);
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        floatingActionButton.setOnClickListener(this);
        listView.setOnItemClickListener((adapterView, view, i, l) ->
        {
            cursor.moveToPosition(i);

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(NotesDatabaseHelper.ID, cursor.getInt(cursor.getColumnIndex(NotesDatabaseHelper.ID)));
            intent.putExtra(NotesDatabaseHelper.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.CONTENT)));
            intent.putExtra(NotesDatabaseHelper.TIME, cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.TIME)));
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(this, AddContentActivity.class);

        if (view.getId() == R.id.add)
        {
            startActivity(intent);
        }
    }

    public void showItem()
    {
        cursor = dbReader.query(NotesDatabaseHelper.TABLE_NAME, null, null, null,
                null, null, null);

        NoteCursorAdapter adapter = new NoteCursorAdapter(this, cursor);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        showItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        if (menu != null)
        {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder"))
            {
                try
                {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }
}
