package com.myxh.note.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.myxh.note.R;
import com.myxh.note.db.NoteDatabaseUtil;

import java.util.Objects;

/**
 * @author MYXH
 * @date 2023/5/27
 */
public class AddContentActivity extends AppCompatActivity
{
    private EditText et_text;
    private NoteDatabaseUtil noteDatabaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_content);

        noteDatabaseUtil = new NoteDatabaseUtil(this);
        et_text = findViewById(R.id.et_text);

        Toolbar toolbar = findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_add, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_save:
                noteDatabaseUtil.add(et_text.getText().toString());
                finish();
                break;
            case R.id.action_clear:
                et_text.setText("");
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }
}
