package at.htlwels.klackl.timerecording.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.htlwels.klackl.timerecording.data.entity.Task;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task... task);

    @Update
    void update(Task... task);

    @Query("DELETE FROM Task WHERE taskId = :id")
    void delete(long id);

    @Query("DELETE FROM Task")
    void deleteAll();

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> findAll();
}
