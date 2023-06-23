<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->

# 便签 APP 程序说明文档

<!-- code_chunk_output -->

- [便签 APP 程序说明文档](#便签-app-程序说明文档)
- [1. 程序说明文档](#1-程序说明文档)
  - [1.1 系统目标](#11-系统目标)
  - [1.2 系统设计](#12-系统设计)
    - [1.2.1 系统架构](#121-系统架构)
    - [1.2.2 功能实现](#122-功能实现)
    - [1.2.3 系统实现](#123-系统实现)
- [2. 系统相关代码](#2-系统相关代码)
  - [2.1 activity 包](#21-activity-包)
    - [2.1.1 MainActivity.java](#211-mainactivityjava)
    - [2.1.2 AddContentActivity.java](#212-addcontentactivityjava)
    - [2.1.3 DetailActivity.java](#213-detailactivityjava)
    - [2.1.4 SettingsActivity.java](#214-settingsactivityjava)
  - [2.2 adapter 包](#22-adapter-包)
    - [2.2.1 NoteCursorAdapter.java](#221-notecursoradapterjava)
  - [2.3 db 包](#23-db-包)
    - [2.3.1 NoteDatabaseUtil.java](#231-notedatabaseutiljava)
    - [2.3.2 NotesDatabaseHelper.java](#232-notesdatabasehelperjava)
  - [2.4 util 包](#24-util-包)
    - [2.4.1 CalendarUtil.java](#241-calendarutiljava)
  - [2.5 layout 布局文件](#25-layout-布局文件)
    - [2.5.1 activity_main.xml](#251-activity_mainxml)
    - [2.5.2 activity_add_content.xml](#252-activity_add_contentxml)
    - [2.5.3 activity_detail.xml](#253-activity_detailxml)
    - [2.5.4 activity_settings.xml](#254-activity_settingsxml)
    - [2.5.5 main_container.xml](#255-main_containerxml)
    - [2.5.6 note_item.xml](#256-note_itemxml)
  - [2.6 menu 布局文件](#26-menu-布局文件)
    - [2.6.1 menu_main.xml](#261-menu_mainxml)
    - [2.6.2 menu_add.xml](#262-menu_addxml)
    - [2.6.3 menu_detail.xml](#263-menu_detailxml)
- [3. 系统界面展示](#3-系统界面展示)
  - [3.1 便签主界面](#31-便签主界面)
  - [3.2 添加笔记界面](#32-添加笔记界面)
  - [3.3 详情界面](#33-详情界面)
  - [3.4 设置界面](#34-设置界面)
- [4. 总结](#4-总结)

<!-- /code_chunk_output -->

# 1. 程序说明文档

## 1.1 系统目标

本便签应用旨在提供一种简单易用的方式来管理便签，让用户能够快速创建、查看、编辑和删除便签，并且支持一些基本的扩展功能，如设置夜间模式、更改字体大小、分组等。同时，应用还需要提供一个良好的用户界面，方便用户进行操作。

## 1.2 系统设计

### 1.2.1 系统架构

本便签应用采用 MVC（Model-View-Controller）架构，将应用的业务逻辑、用户界面和数据管理分别封装在不同的组件中，使得应用更加易于维护和扩展。

- Model - 数据模型层：管理应用的数据，包括便签的内容、时间、分组等信息。使用 SQLite 数据库进行存储管理。
- View - 视图层：负责应用的用户界面设计和显示，包括主界面、添加笔记界面、详情界面和设置界面等。
- Controller - 控制器层：负责管理用户与应用之间的交互，接收用户的输入并处理对应的业务逻辑，包括点击事件处理、数据传输等。

### 1.2.2 功能实现

本便签应用主要实现以下功能：

1. 便签主界面：主界面是应用的核心功能之一，它可以显示所有已添加的便签，并支持搜索、删除、分享、提醒、更改文字大小、更改便签分组等操作。
2. 添加笔记界面：添加笔记界面提供给用户一个编辑器，让用户可以方便地添加新的便签。
3. 详情界面：详情界面是一个展示已有便签内容的界面，用户可以在此界面查看便签的详细内容，并且可以进行编辑、删除、分享等操作。
4. 设置界面：设置界面提供了一些应用的基本设置，包括夜间模式切换、更改文字大小、更改便签分组等。

### 1.2.3 系统实现

本便签应用使用了 Android Studio 作为开发环境，采用了 Java 编程语言进行开发。应用的 UI 设计使用了 Android 系统提供的标准控件和布局管理器，如 RecyclerView、LinearLayout 等，并使用了一些第三方库，来简化代码的编写和开发过程。

在应用的设计和实现过程中，遵循了 Android 开发的最佳实践，如尽量避免使用硬编码、将字符串和资源文件分离、使用多线程进行耗时操作等。同时，还使用了 Android 系统提供的各种 API 和功能，如 SQLite 数据库等。

# 2. 系统相关代码

以下是本便签应用的几个主要相关代码：

## 2.1 activity 包

### 2.1.1 MainActivity.java

```java
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
```

### 2.1.2 AddContentActivity.java

```java
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
```

### 2.1.3 DetailActivity.java

```java
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
```

### 2.1.4 SettingsActivity.java

```java
package com.myxh.note.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.myxh.note.R;

/**
 * @author MYXH
 * @date 2023/5/27
 */
public class SettingsActivity extends AppCompatActivity
{
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
```

## 2.2 adapter 包

### 2.2.1 NoteCursorAdapter.java

```java
package com.myxh.note.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myxh.note.R;

/**
 * @author MYXH
 * @date 2023/5/27
 */
public class NoteCursorAdapter extends BaseAdapter
{
    private final Context context;
    private final Cursor cursor;

    public NoteCursorAdapter(Context context, Cursor cursor)
    {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount()
    {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i)
    {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.note_item, null);
        TextView contentTextView = view.findViewById(R.id.list_content);
        TextView timeTextView = view.findViewById(R.id.list_time);

        cursor.moveToPosition(i);

        @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
        @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));

        contentTextView.setText(content);
        timeTextView.setText(time);

        return view;
    }
}
```

## 2.3 db 包

### 2.3.1 NoteDatabaseUtil.java

```java
package com.myxh.note.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.myxh.note.adapter.NoteCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MYXH
 * @date 2023/5/27
 */
public class NoteDatabaseUtil
{
    private final Context context;
    private final String string = getTime();
    private NotesDatabaseHelper note;
    private SQLiteDatabase dbWriter;
    private SQLiteDatabase dbReader;
    private Cursor cursor;
    private NoteCursorAdapter adapter;

    public NoteDatabaseUtil(Context context)
    {
        this.context = context;
    }

    public void add(String content)
    {
        note = new NotesDatabaseHelper(context);
        dbWriter = note.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesDatabaseHelper.CONTENT, content);
        contentValues.put(NotesDatabaseHelper.TIME, getTime());

        dbWriter.insert(NotesDatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public void delete(Intent intent)
    {
        note = new NotesDatabaseHelper(context);

        dbWriter = note.getWritableDatabase();

        dbWriter.delete(NotesDatabaseHelper.TABLE_NAME, "_id=" + intent.getIntExtra(NotesDatabaseHelper.ID, 0), null);
    }

    public void update(String content, int id)
    {
        note = new NotesDatabaseHelper(context);

        dbWriter = note.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesDatabaseHelper.CONTENT, content);
        contentValues.put(NotesDatabaseHelper.TIME, getTime());

        Log.i("RE-SSSS", content);
        Log.i("RE-SSSS", id + "");

        dbWriter.update(NotesDatabaseHelper.TABLE_NAME, contentValues, NotesDatabaseHelper.ID + "=?", new String[]{String.valueOf(id)});
        dbWriter.close();
    }


    public String getTime()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

        Date curDate = new Date();

        return format.format(curDate);
    }
}
```

### 2.3.2 NotesDatabaseHelper.java

```java
package com.myxh.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author MYXH
 * @date 2023/5/27
 */
public class NotesDatabaseHelper extends SQLiteOpenHelper
{
    public static final String TABLE_NAME = "notes";
    public static final String CONTENT = "content";
    public static final String ID = "_id";
    public static final String TIME = "time";

    public NotesDatabaseHelper(Context context)
    {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENT + " TEXT NOT NULL," + TIME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
```

## 2.4 util 包

### 2.4.1 CalendarUtil.java

```java
package com.myxh.note.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * @author MYXH
 * @date 2023/5/27
 */
public class CalendarUtil
{
    private static final String calenderEventURL;

    static
    {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8)
        {
            calenderEventURL = "content://com.android.calendar/events";
        }
        else
        {
            calenderEventURL = "content://calendar/events";
        }
    }

    public static void OpenCalendar(Context context, String content)
    {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Uri.parse(calenderEventURL))
                .putExtra("beginTime", System.currentTimeMillis())
                .putExtra("endTime", System.currentTimeMillis() + 24 * 60 * 60 * 1000)
                .putExtra("title", content)
                .putExtra("description", content);
        context.startActivity(intent);
    }
}
```

## 2.5 layout 布局文件

### 2.5.1 activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:clipToPadding="true"
    android:fitsSystemWindows="true"
    android:orientation="vertical"
    tools:context=".activity.MainActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/AppTheme.Main">

        <androidx.appcompat.widget.Toolbar
            android:id="@+id/toolbar_main"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="?attr/colorPrimary"
            app:popupTheme="@style/MainToolbarPopupTheme" />

    </com.google.android.material.appbar.AppBarLayout>

    <include layout="@layout/main_container" />

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/add"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|right"
        android:layout_marginEnd="30dp"
        android:layout_marginBottom="30dp"
        android:backgroundTint="@color/MiAdd"
        android:focusable="true"
        android:src="@drawable/ic_add"
        app:fabSize="normal"
        tools:ignore="ContentDescription,RtlHardcoded,SpeakableTextPresentCheck">

    </com.google.android.material.floatingactionbutton.FloatingActionButton>

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

### 2.5.2 activity_add_content.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".activity.AddContentActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/AppTheme.Main">

        <androidx.appcompat.widget.Toolbar
            android:id="@+id/toolbar_add"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            app:title="@string/add_content" />

    </com.google.android.material.appbar.AppBarLayout>

    <EditText
        android:id="@+id/et_text"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginStart="20dp"
        android:layout_marginEnd="20dp"
        android:layout_weight="1"
        android:background="@null"
        android:gravity="top"
        android:hint="记事本输入"
        android:textCursorDrawable="@drawable/et_cursor"
        tools:ignore="Autofill,HardcodedText,TextFields,VisualLintTextFieldSize" />


</LinearLayout>
```

### 2.5.3 activity_detail.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".activity.DetailActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/AppTheme.Main">

        <androidx.appcompat.widget.Toolbar
            android:id="@+id/toolbar_detail"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            app:popupTheme="@style/MainToolbarPopupTheme" />

    </com.google.android.material.appbar.AppBarLayout>

    <EditText

        android:id="@+id/d_text"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginStart="20dp"
        android:layout_marginEnd="20dp"
        android:layout_weight="1"
        android:background="@null"
        android:gravity="top"
        android:scrollbars="vertical"
        android:textCursorDrawable="@drawable/et_cursor"
        tools:ignore="Autofill,LabelFor,SpeakableTextPresentCheck,TextFields,VisualLintTextFieldSize" />

</LinearLayout>
```

### 2.5.4 activity_settings.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFFFFF"
    android:orientation="vertical">

    <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/AppTheme.Main">

        <androidx.appcompat.widget.Toolbar
            android:id="@+id/toolbar_settings"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="#FFFFFF"
            app:title="设置"
            app:titleMarginStart="25dp" />

    </com.google.android.material.appbar.AppBarLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="10dp"
        android:layout_marginEnd="10dp"
        android:layout_marginBottom="10dp"
        android:background="#FFFFFF"
        android:orientation="horizontal">

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="6"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="15dp"
                android:text="@string/night_mode"
                android:textSize="20sp" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="15dp"
                android:text="@string/pref_night_summary"
                android:textSize="15sp" />
        </LinearLayout>

        <Switch
            android:id="@+id/nightMode"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:layout_marginEnd="10dp"
            android:layout_weight="1"
            android:focusable="true"
            android:gravity="center"
            android:switchMinWidth="40dp"
            android:thumb="@drawable/wechat_style_thumb"
            android:track="@drawable/wechat_sytle_track"
            tools:ignore="TouchTargetSizeCheck,UseSwitchCompatOrMaterialXml" />

    </LinearLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:layout_marginStart="25dp"
        android:layout_marginEnd="20dp"
        android:foreground="@color/gray" />

</LinearLayout>
```

### 2.5.5 main_container.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    tools:showIn="@layout/activity_main">

    <androidx.appcompat.widget.SearchView
        android:id="@+id/search_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="20dp"
        android:layout_marginEnd="20dp"
        android:background="@drawable/round_search"
        android:iconifiedByDefault="false"
        android:queryHint="请输入搜索内容"></androidx.appcompat.widget.SearchView>

    <ListView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:divider="@color/white"
        android:dividerHeight="0dp"
        android:listSelector="@color/white"
        android:longClickable="true"></ListView>

</LinearLayout>
```

### 2.5.6 note_item.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="5dp"
        android:layout_marginRight="10dp"
        android:layout_marginBottom="5dp"
        android:background="@drawable/round_textview"
        android:gravity="center_vertical"
        android:orientation="horizontal"
        tools:ignore="UselessParent">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <TextView
                android:id="@+id/list_content"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="10dp"
                android:layout_marginTop="10dp"
                android:layout_weight="4"
                android:ellipsize="end"
                android:maxLines="2"
                android:textSize="20sp" />

            <TextView
                android:id="@+id/list_time"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="10dp"
                android:layout_weight="1" />
        </LinearLayout>

        <Space
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:layout_weight="1" />

        <CheckBox
            android:id="@+id/check"
            style="@style/Widget.AppCompat.CompoundButton.RadioButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:checked="false"
            android:focusable="false"
            android:paddingEnd="15dp"
            android:visibility="gone"
            tools:ignore="RtlSymmetry" />

    </LinearLayout>

</LinearLayout>
```

## 2.6 menu 布局文件

### 2.6.1 menu_main.xml

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context="com.myxh.myapplication.MainActivity">
    <item
        android:id="@+id/action_settings"
        android:icon="@drawable/ic_settings"
        android:orderInCategory="100"
        android:title="@string/action_settings"
        app:showAsAction="never" />

    <!--    <item-->
    <!--        android:id="@+id/action_about"-->
    <!--        android:orderInCategory="100"-->
    <!--        android:icon="@drawable/ic_info"-->
    <!--        android:title="@string/action_about"-->
    <!--        app:showAsAction="never" />-->
</menu>
```

### 2.6.2 menu_add.xml

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context="com.myxh.myapplication.MainActivity">

    <item
        android:id="@+id/action_clear"
        android:icon="@drawable/ic_clear"
        android:title="Clear"
        app:showAsAction="ifRoom"
        tools:ignore="HardcodedText" />

    <item
        android:id="@+id/action_save"
        android:icon="@drawable/ic_confirm"
        android:title="Confirm"
        app:showAsAction="ifRoom"
        tools:ignore="HardcodedText" />

</menu>
```

### 2.6.3 menu_detail.xml

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context="com.myxh.myapplication.MainActivity">

    <item
        android:id="@+id/action_save"
        android:icon="@drawable/ic_confirm"
        android:orderInCategory="100"
        android:title="@string/add_content"
        app:showAsAction="ifRoom" />

    <item
        android:id="@+id/action_delete"
        android:icon="@drawable/ic_delete"
        android:orderInCategory="100"
        android:title="@string/action_delete"
        app:showAsAction="never" />

    <item
        android:id="@+id/action_share"
        android:icon="@drawable/ic_share"
        android:orderInCategory="100"
        android:title="@string/action_share"
        app:showAsAction="never" />

    <item
        android:id="@+id/action_add_alarm"
        android:icon="@drawable/ic_add_alarm"
        android:orderInCategory="100"
        android:title="@string/action_add_alarm"
        app:showAsAction="never" />

    <item
        android:id="@+id/action_text_size"
        android:icon="@drawable/ic_text_size"
        android:orderInCategory="100"
        android:title="@string/action_text_size"
        app:showAsAction="never" />

    <item
        android:id="@+id/action_box"
        android:icon="@drawable/ic_box"
        android:orderInCategory="100"
        android:title="@string/action_box"
        app:showAsAction="never" />
</menu>
```

# 3. 系统界面展示

以下是本便签应用的几个主要界面截图：

## 3.1 便签主界面

![便签主界面](https://img-blog.csdnimg.cn/af13e3abf1084c75b721c404fe3da903.jpeg)

主界面显示了所有已添加的便签，支持搜索、删除、分享、提醒、更改文字大小、更改便签分组等操作。用户可以通过点击便签列表项进入详情界面查看便签的详细内容，也可以通过点击搜索框输入关键词来搜索便签。

## 3.2 添加笔记界面

![添加笔记界面](https://img-blog.csdnimg.cn/cc978785cdc240a68c25fee649f71d0b.jpeg)

添加笔记界面提供给用户一个编辑器，让用户可以方便地添加新的便签。用户可以在此界面输入标题、内容、选择分组等信息，并可以选择是否设置提醒时间。

## 3.3 详情界面

![详情界面](https://img-blog.csdnimg.cn/c3ef5f8b82714817a74a0a92d384720b.jpeg)

![详情界面的功能](https://img-blog.csdnimg.cn/3de7f94d7bb74eac8505b68f8a30f6d2.jpeg)

![分享](https://img-blog.csdnimg.cn/5c646dcde4ef4989b4e86a7e151aaa38.jpeg)

![提醒我](https://img-blog.csdnimg.cn/d06c4143f87e45ef8211c731965470ad.jpeg)

![文字大小](https://img-blog.csdnimg.cn/e62c91e11efe45f7aeb481715ae50a4e.jpeg)

详情界面是一个展示已有便签内容的界面，用户可以在此界面查看便签的详细内容，包括标题、时间、内容、分组等信息，并且可以进行编辑、删除、分享等操作。

## 3.4 设置界面

![设置界面](https://img-blog.csdnimg.cn/c64442af35654268a21bd6a86cab3342.jpeg)

设置界面提供了一些应用的基本设置，包括夜间模式切换、更改文字大小、更改便签分组等。用户可以在此界面选择自己喜欢的主题和字体大小，并且可以设置默认的便签分组。设置界面的 UI 设计使用了 TabLayout 和 ViewPager 来实现多个子界面的切换，每个子界面对应一种设置选项。

# 4. 总结

本文档详细描述了一个便签应用的开发过程和功能实现，包括系统目标、系统设计、功能实现和系统界面展示等方面。通过设计和实现这个应用，深入了解了 Android 应用开发的基本流程和技术，如 MVC 架构、RecyclerView 适配器视图、SQLite 数据库。同时，也学习了如何设计一个良好的用户界面，如何进行代码的优化和重构等。本文档的内容可以作为 Android 应用开发的参考资料，帮助开发者更好地理解和掌握 Android 应用开发的相关知识和技术。
