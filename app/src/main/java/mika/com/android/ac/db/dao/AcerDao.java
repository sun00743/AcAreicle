package mika.com.android.ac.db.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import mika.com.android.ac.network.api.info.acapi.Acer;

/**
 * Created by mika on 2017/11/8
 */

public interface AcerDao {

    @Query("SELECT * FROM acer WHERE userId = :id LIMIT 1")
    Acer getAcerById(int id);

    @Insert
    void insert(Acer... acers);

    @Delete
    void delete(Acer acer);

}
