package at.htlwels.klackl.timerecording.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.htlwels.klackl.timerecording.data.entity.Recording;

@Dao
public interface RecordingDao {

    @Insert
    void insert(Recording... recordings);

    @Update
    void update(Recording... recordings);

    @Query("DELETE FROM Recording WHERE recordingId = :id")
    void delete(long id);

    @Query("SELECT * FROM Recording ORDER BY endDateTime DESC")
    LiveData<List<Recording>> findAll();

    @Query("SELECT * FROM Recording WHERE taskId = :id ORDER BY endDateTime DESC")
    LiveData<List<Recording>> findByTaskId(long id);
}
