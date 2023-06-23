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
