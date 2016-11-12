package me.zhanghai.android.douya.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import me.zhanghai.android.douya.network.api.info.acapi.Acer;

/**
 * Created by Administrator on 2016/9/26.
 */

public class AcerDB {
    private AcerDBHelper helper;
    public AcerDB(Context context){

        helper = new AcerDBHelper(context);
    }
    @Override
    protected void finalize() throws Throwable {
        helper.close();
        super.finalize();

    }
    public void saveAcer(Acer acer){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO "+AcerDBHelper.TABE_ACER + " (userId, username, userImg, access_token, userGroupLevel,mobileCheck, time) VALUES(?,?,?,?,?,?,?)",
                new Object[] { acer.userId, acer.username, acer.userImg,acer.access_token, acer.userGroupLevel, acer.mobileCheck,acer.time});
        db.close();
    }

    public Acer getAcer(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor query = db.rawQuery("SELECT * FROM "+AcerDBHelper.TABE_ACER, null);
        Acer acer = null;
        if (query.moveToFirst()) {
            acer = new Acer();
            acer.userId = query.getInt(query.getColumnIndex("userId"));
            acer.username = query.getString(query.getColumnIndex("username"));
            acer.userImg = query.getString(query.getColumnIndex("userImg"));
            acer.access_token = query.getString(query.getColumnIndex("access_token"));
            acer.userGroupLevel = query.getInt(query.getColumnIndex("userGroupLevel"));
            acer.mobileCheck = query.getInt(query.getColumnIndex("mobileCheck"));
        }
        query.close();
        db.close();
        return acer;
    }
    public void logout() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + AcerDBHelper.TABE_ACER);
        db.close();
    }

    // TODO : fav
}
