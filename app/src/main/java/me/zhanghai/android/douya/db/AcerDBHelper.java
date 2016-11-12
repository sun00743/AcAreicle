package me.zhanghai.android.douya.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/9/26.
 */

public class AcerDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "acer.db";
    public static final int VERSION = 2;

    public static final String TABE_ACER = "acer";

    public AcerDBHelper(Context context){
        this(context,DB_NAME,null,VERSION);
    }

    public AcerDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AcerDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABE_ACER+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "username VARCHAR(25)," +
                "userImg TEXT," +
                "access_token TEXT," +
                "userGroupLevel INTEGER," +
                "mobileCheck INTEGER,"+
                "time INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB", "onUpgrade::oldVersion="+oldVersion+",newVersion="+newVersion);
        db.execSQL("ALTER TABLE "+ TABE_ACER +" ADD time INTEGER");
    }
}
