package at.htlwels.klackl.timerecording.data.entity;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import at.htlwels.klackl.timerecording.util.DateTimeStringFormatter;

@Entity
public class Recording {

    @PrimaryKey(autoGenerate = true)
    public long recordingId;

    public long taskId;

    public long startDateTime;
    public long endDateTime;

    public Recording(long taskId, long startDateTime, long endDateTime) {
        this.taskId = taskId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Recording(long recordingId, long taskId, long startDateTime, long endDateTime) {
        this.taskId = taskId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.recordingId = recordingId;
    }

    public Recording() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String toString() {
        return DateTimeStringFormatter.formatString(startDateTime, endDateTime);
    }
}
