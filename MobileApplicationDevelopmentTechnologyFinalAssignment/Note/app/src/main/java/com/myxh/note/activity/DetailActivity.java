package com.myxh.note.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.myxh.note.R;
import com.myxh.note.db.NoteDatabaseUtil;
import com.myxh.note.db.NotesDatabaseHelper;
import com.myxh.note.util.CalendarUtil;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author MYXH
 * @date 2023/5/27
 */
public class DetailActivity extends AppCompatActivity
{
    private EditText editText;
    private String wordSizePrefs;
    private int checkedItem;
    private byte[] bytes;
    private NoteDatabaseUtil noteDatabaseUtil;
    private int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        noteDatabaseUtil = new NoteDatabaseUtil(this);

        editText = findViewById(R.id.d_text);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);

        editText.setText(getIntent().getStringExtra(NotesDatabaseHelper.CONTENT));
        _id = getIntent().getIntExtra(NotesDatabaseHelper.ID, 0);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v ->
        {
            finish();
        });

        NotesDatabaseHelper note = new NotesDatabaseHelper(this);
        SQLiteDatabase dbWriter = note.getWritableDatabase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

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
                noteDatabaseUtil.update(editText.getText().toString(), _id);
                finish();
                break;
            case R.id.action_delete:
                Intent intents = getIntent();
                noteDatabaseUtil.delete(intents);
                finish();
                break;

            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, editText.getText().toString().replaceAll("<img src='(.*?)'/>", "[图片]").replaceAll("<voice src='(.*?)'/>", "[语音]"));
                startActivity(Intent.createChooser(intent, "分享到"));
                break;

            case R.id.action_add_alarm:
                CalendarUtil.OpenCalendar(this, NotesDatabaseHelper.TABLE_NAME);
                break;

            case R.id.action_text_size:
                final String[] wordSizes = new String[]{"正常", "大", "超大"};
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                AlertDialog alertDialog = builder.setTitle("选择字体大小").setSingleChoiceItems(wordSizes, checkedItem, (dialogInterface, i) ->
                {
                    wordSizePrefs = wordSizes[i];
                    float wordSize = getWordSize(wordSizePrefs);
                    SharedPreferences prefs = getSharedPreferences("Setting", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("WordSize", wordSizePrefs);
                    editor.apply();
                    new Thread(() ->
                    {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getText());
                        runOnUiThread(() ->
                        {
                            if (bytes == null)
                            {
                                spannableStringBuilder.setSpan(new TypefaceSpan("serif"), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                                // 设置字体前景色
                                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            editText.setText(spannableStringBuilder);
                            editText.setTextSize(wordSize);
                        });
                    }).start();
                }).create();
                alertDialog.show();
                break;
            case R.id.action_box:
                Toast.makeText(DetailActivity.this, "开发中，敬请期待", Toast.LENGTH_SHORT).show();

                break;
            default:
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

    private float getWordSize(String str)
    {
        switch (str)
        {
            case "正常":
                checkedItem = 0;
                return 20;
            case "大":
                checkedItem = 1;
                return 25;
            case "超大":
                checkedItem = 2;
                return 30;
        }
        return 20;
    }
}
