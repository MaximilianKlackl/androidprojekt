package at.htlwels.klackl.timerecording.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import at.htlwels.klackl.timerecording.R;
import at.htlwels.klackl.timerecording.data.entity.Recording;
import at.htlwels.klackl.timerecording.data.entity.Task;
import at.htlwels.klackl.timerecording.data.repository.RecordingRepository;
import at.htlwels.klackl.timerecording.data.repository.TaskRepository;
import at.htlwels.klackl.timerecording.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LiveData<List<Task>> allTasks;
    private RecordingRepository recordingRepository;

    private TextView startTimeTextView;
    private TextView currentTimeTextView;
    private Button startStopButton;
    private Spinner spinnerTasks;

    private long startTime = 0;
    private long currentTime = 0;

    private final Handler timerHandler = new Handler();
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            currentTime = System.currentTimeMillis();
            long millis = System.currentTimeMillis() - startTime;

            int hours = (int) (millis / (1000 * 60 * 60));
            int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
            int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

            currentTimeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        recordingRepository = new RecordingRepository(this.requireActivity().getApplication());
        TaskRepository taskRepository = new TaskRepository(this.requireActivity().getApplication());
        allTasks = taskRepository.getAllTasks();

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        startTimeTextView = view.findViewById(R.id.startTimeTextView);
        currentTimeTextView = view.findViewById(R.id.currentTimeTextView);
        startStopButton = view.findViewById(R.id.startStopButton);
        spinnerTasks = view.findViewById(R.id.taskSpinner);

        spinnerTasks.setSelection(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // populate dropdown
        allTasks.observe(getViewLifecycleOwner(), tasks -> {
            List<String> names = tasks.stream().map(t -> String.format("%s: %s", t.taskId, t.name)).collect(Collectors.toList());
            adapter.addAll(names);

            startStopButton.setEnabled(names.size() != 0);
        });

        spinnerTasks.setAdapter(adapter);

        // button listener for start stop event
        startStopButton.setOnClickListener(view1 -> {

                if (startStopButton.getText().equals(getString(R.string.stop_recording))) {
                    timerHandler.removeCallbacks(timerRunnable);
                    startStopButton.setText(R.string.start_recording);

                    startTimeTextView.setText("Start Time");
                    currentTimeTextView.setText("Timer");

                    saveRecording(startTime, currentTime);
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    startStopButton.setText(R.string.stop_recording);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                    LocalDateTime now = LocalDateTime.now();

                    startTimeTextView.setText(String.format("Start Time: %s Uhr", dtf.format(now)));
            }
        });
    }

    private void saveRecording(long startTime, long endTime) {

        try{
            long taskId = Long.parseLong(spinnerTasks.getSelectedItem().toString().split(":")[0]);

            recordingRepository.insert(new Recording(
                    taskId,
                    startTime,
                    endTime
            ));
        }catch (Exception e) {
            Log.w(getString(R.string.LOGGING_TAG_ERROR), e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}