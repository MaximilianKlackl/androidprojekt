package at.htlwels.klackl.timerecording.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.htlwels.klackl.timerecording.R;
import at.htlwels.klackl.timerecording.data.dao.RecordingDao;
import at.htlwels.klackl.timerecording.data.dao.TaskDao;
import at.htlwels.klackl.timerecording.data.entity.Recording;
import at.htlwels.klackl.timerecording.data.entity.Task;

@Database(entities = {Task.class, Recording.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();
    public abstract RecordingDao recordingDao();

    private static volatile  AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, context.getString(R.string.database_name))
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }
}