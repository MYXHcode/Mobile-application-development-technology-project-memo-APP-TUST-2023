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
