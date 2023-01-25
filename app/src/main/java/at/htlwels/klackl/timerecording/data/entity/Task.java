package at.htlwels.klackl.timerecording.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    public long taskId;
    public String name;

    public Task(String name) {
        this.name = name;
    }
    public Task(long taskId, String name) {
        this.name = name;
        this.taskId = taskId;
    }

    public Task() {

    }
}
