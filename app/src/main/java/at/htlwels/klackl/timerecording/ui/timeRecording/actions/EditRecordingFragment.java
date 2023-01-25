package at.htlwels.klackl.timerecording.ui.timeRecording.actions;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import at.htlwels.klackl.timerecording.R;
import at.htlwels.klackl.timerecording.data.entity.Recording;
import at.htlwels.klackl.timerecording.data.repository.RecordingRepository;

public class EditRecordingFragment extends Fragment {

    private final Calendar cal = Calendar.getInstance();
    private RecordingRepository recordingRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordingRepository = new RecordingRepository(requireActivity().getApplication());

        Button saveButton = view.findViewById(R.id.recording_saveButton);
        Button cancelButton = view.findViewById(R.id.recording_cancelButton);
        EditText fromDateEditText = view.findViewById(R.id.editText_fromDate);
        EditText fromTimeEditText = view.findViewById(R.id.editText_fromTime);
        EditText toDateEditText = view.findViewById(R.id.editText_toDate);
        EditText toTimeEditText = view.findViewById(R.id.editText_toTime);

        NavController navController = Navigation.findNavController(view);

        long fromMillis = getArguments().getLong("from");
        long toMillis = getArguments().getLong("to");
        long id = getArguments().getLong("id");
        long taskId = getArguments().getLong("taskId");

        LocalDateTime fromDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(fromMillis), ZoneId.systemDefault());

        LocalDateTime toDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(toMillis), ZoneId.systemDefault());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        fromDateEditText.setText(dateFormatter.format(fromDateTime));
        fromTimeEditText.setText(timeFormatter.format(fromDateTime));

        toDateEditText.setText(dateFormatter.format(toDateTime));
        toTimeEditText.setText(timeFormatter.format(toDateTime));

        saveButton.setOnClickListener(view1 -> {
            try {
                int day1 = Integer.parseInt(fromDateEditText.getText().toString().split("/")[0]);
                int month1 = Integer.parseInt(fromDateEditText.getText().toString().split("/")[1]);
                int year1 = Integer.parseInt(fromDateEditText.getText().toString().split("/")[2]);
                int hour1 = Integer.parseInt(fromTimeEditText.getText().toString().split(":")[0]);
                int minutes1 = Integer.parseInt(fromTimeEditText.getText().toString().split(":")[1]);

                int day2 = Integer.parseInt(toDateEditText.getText().toString().split("/")[0]);
                int month2 = Integer.parseInt(toDateEditText.getText().toString().split("/")[1]);
                int year2 = Integer.parseInt(toDateEditText.getText().toString().split("/")[2]);
                int hour2 = Integer.parseInt(toTimeEditText.getText().toString().split(":")[0]);
                int minutes2 = Integer.parseInt(toTimeEditText.getText().toString().split(":")[1]);

                cal.set(year1, month1, day1, hour1, minutes1);
                long millisFrom = cal.toInstant().toEpochMilli();

                cal.set(year2, month2, day2, hour2, minutes2);
                long millisTo = cal.toInstant().toEpochMilli();

                recordingRepository.update(new Recording(id, taskId, millisFrom, millisTo));

                navController.navigateUp();

            }catch (Exception e) {
                Log.w(getString(R.string.LOGGING_TAG_ERROR), e.getMessage());
            }
        });

        cancelButton.setOnClickListener(view12 -> {
            navController.navigateUp();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_recording, container, false);
    }

}