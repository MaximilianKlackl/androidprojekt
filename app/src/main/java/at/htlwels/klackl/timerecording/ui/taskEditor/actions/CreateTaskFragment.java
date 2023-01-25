package at.htlwels.klackl.timerecording.ui.taskEditor.actions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import at.htlwels.klackl.timerecording.R;
import at.htlwels.klackl.timerecording.data.entity.Task;
import at.htlwels.klackl.timerecording.data.repository.TaskRepository;

public class CreateTaskFragment extends Fragment {

    private TaskRepository taskRepository;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskRepository = new TaskRepository(requireActivity().getApplication());

        EditText nameEditText = view.findViewById(R.id.EditNameEditText);
        Button saveButton = view.findViewById(R.id.saveTaskButton);
        Button cancelButton = view.findViewById(R.id.cancelTaskButton);

        NavController navController = Navigation.findNavController(view);

        saveButton.setOnClickListener(view1 -> {

            if(!nameEditText.getText().toString().isEmpty()){
                taskRepository.insert(
                    new Task(nameEditText.getText().toString()
                ));
            }

            navController.navigateUp();
        });

        cancelButton.setOnClickListener(view2 -> {
            navController.navigateUp();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_task, container, false);
    }
}