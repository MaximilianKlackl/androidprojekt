package at.htlwels.klackl.timerecording.ui.timeRecording;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import at.htlwels.klackl.timerecording.R;
import at.htlwels.klackl.timerecording.data.entity.Recording;
import at.htlwels.klackl.timerecording.data.repository.RecordingRepository;


public class TimeRecordingFragment extends Fragment {


    private String taskId;
    private String name;

    private LiveData<List<Recording>> recordings;
    private RecordingRepository recordingRepository;

    private Spinner spinner;
    private NavController navController;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recordingRepository = new RecordingRepository(requireActivity().getApplication());

        name = getArguments().getString("name");
        taskId = getArguments().getString("taskId");

        recordings = recordingRepository.findById(Long.parseLong(taskId));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Time Recording: " + name);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        spinner = view.findViewById(R.id.recording_taskSpinner);

        ArrayAdapter adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item);

        recordings.observe(getViewLifecycleOwner(), r -> {
            adapter.clear();
            adapter.addAll(r);
        });

        spinner.setAdapter(adapter);


        view.findViewById(R.id.recording_editButton).setOnClickListener(view13 -> {

            Recording recording = (Recording) spinner.getSelectedItem();
            Bundle bundle = new Bundle();
            bundle.putLong("from", recording.startDateTime);
            bundle.putLong("to", recording.endDateTime);
            bundle.putLong("id", recording.recordingId);
            bundle.putLong("taskId", recording.taskId);

            navController.navigate(R.id.navigation_editRecordingFragment, bundle);
        });

        view.findViewById(R.id.recording_addButton).setOnClickListener(view12 -> {
            navController.navigate(R.id.navigation_createRecordingFragment);
        });

        view.findViewById(R.id.recording_deleteButton).setOnClickListener(view1 -> {
            try {
                Recording recording = (Recording) spinner.getSelectedItem();
                recordingRepository.delete(recording.recordingId);
            } catch (Exception e) {
                Log.w(getString(R.string.LOGGING_TAG_ERROR), e.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_recording, container, false);
    }
}