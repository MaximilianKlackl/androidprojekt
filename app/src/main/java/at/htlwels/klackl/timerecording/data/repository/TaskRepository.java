package at.htlwels.klackl.timerecording.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import at.htlwels.klackl.timerecording.data.dao.TaskDao;
import at.htlwels.klackl.timerecording.data.database.AppDatabase;
import at.htlwels.klackl.timerecording.data.entity.Task;

public class TaskRepository {

    private final TaskDao mTaskDao;
    private final LiveData<List<Task>> mAllTasks;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mAllTasks = mTaskDao.findAll();
    }

    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    public void insert (Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> mTaskDao.insert(task));
    }

    public void update (Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> mTaskDao.update(task));
    }

    public void delete(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> mTaskDao.delete(id));
    }
}