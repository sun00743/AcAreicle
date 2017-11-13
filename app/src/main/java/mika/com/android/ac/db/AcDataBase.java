package mika.com.android.ac.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import mika.com.android.ac.db.dao.AcerDao;
import mika.com.android.ac.network.api.info.acapi.Acer;

/**
 * Created by mika on 2017/11/9
 */

@Database(entities = {Acer.class}, version = 1)
public abstract class AcDataBase extends RoomDatabase {

    public abstract AcerDao acerDao();

}
