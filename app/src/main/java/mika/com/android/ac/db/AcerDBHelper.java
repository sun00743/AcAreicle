/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/9/26
 */

public class AcerDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "acer.db";
    public static final int VERSION = 2;

    static final String TABE_ACER = "acer";

    public AcerDBHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    public AcerDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABE_ACER + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "username VARCHAR(25)," +
                "userImg TEXT," +
                "access_token TEXT," +
                "userGroupLevel INTEGER," +
                "mobileCheck INTEGER," +
                "time INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB", "onUpgrade::oldVersion=" + oldVersion + ",newVersion=" + newVersion);
        db.execSQL("ALTER TABLE " + TABE_ACER + " ADD time INTEGER");
    }
}
