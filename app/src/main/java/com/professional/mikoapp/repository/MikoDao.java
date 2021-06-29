package com.professional.mikoapp.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MikoDao {
    @Insert
    void insert(Miko miko);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Miko miko);

    @Delete
    void delete(Miko miko);

    @Query("SELECT * FROM Miko")
    LiveData<List<Miko>> getRecords();

    @Query("SELECT EXISTS(SELECT * FROM Miko WHERE id = :id)")
    boolean isRowIsExist(int id);
}
