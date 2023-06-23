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
