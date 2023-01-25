package at.htlwels.klackl.timerecording.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import at.htlwels.klackl.timerecording.data.dao.RecordingDao;
import at.htlwels.klackl.timerecording.data.database.AppDatabase;
import at.htlwels.klackl.timerecording.data.entity.Recording;

public class RecordingRepository {

    private final RecordingDao mRecordingDao;
    private final LiveData<List<Recording>> mAllRecordings;

    public RecordingRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mRecordingDao = db.recordingDao();
        mAllRecordings = mRecordingDao.findAll();
    }

    LiveData<List<Recording>> getAllRecordings() {
        return mAllRecordings;
    }

    public LiveData<List<Recording>> findById(long id) {
        return mRecordingDao.findByTaskId(id);
    }

    public void insert (Recording recording) {
        AppDatabase.databaseWriteExecutor.execute(() -> mRecordingDao.insert(recording));
    }

    public void update (Recording recording) {
        AppDatabase.databaseWriteExecutor.execute(() -> mRecordingDao.update(recording));
    }

    public void delete(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> mRecordingDao.delete(id));
    }
}
