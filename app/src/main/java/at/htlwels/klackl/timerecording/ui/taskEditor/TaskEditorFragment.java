package at.htlwels.klackl.timerecording.ui.taskEditor;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.List;
import java.util.stream.Collectors;

import at.htlwels.klackl.timerecording.R;
import at.htlwels.klackl.timerecording.data.entity.Task;
import at.htlwels.klackl.timerecording.data.repository.TaskRepository;
import at.htlwels.klackl.timerecording.databinding.FragmentTaskEditorBinding;

public class TaskEditorFragment extends Fragment {

    private LiveData<List<Task>> allTask;
    private TaskRepository taskRepository;

    private Spinner spinner;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskRepository = new TaskRepository(requireActivity().getApplication());
        allTask = taskRepository.getAllTasks();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return FragmentTaskEditorBinding.inflate(inflater, container, false).getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        spinner = view.findViewById(R.id.dashboard_taskSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item);

        allTask.observe(getViewLifecycleOwner(), tasks -> {
            List<String> names = tasks.stream().map(t -> String.format("%s: %s", t.taskId, t.name)).collect(Collectors.toList());
            adapter.clear();
            adapter.addAll(names);
        });

        spinner.setAdapter(adapter);


        view.findViewById(R.id.dashboard_editButton).setOnClickListener(view13 -> {
            navigateToActionsWithArgs(R.id.navigation_editTaskFragment);
        });

        view.findViewById(R.id.dashboard_addButton).setOnClickListener(view12 -> {
            navController.navigate(R.id.navigation_createTaskFragment);
        });

        view.findViewById(R.id.dashboard_detailsTaskButton).setOnClickListener(view14 -> {
            navigateToActionsWithArgs(R.id.navigation_timeRecordingFragment);
        });

        view.findViewById(R.id.dashboard_deleteButton).setOnClickListener(view1 -> {
            try{
                String id = ((String) spinner.getSelectedItem()).split(":")[0];
                taskRepository.delete(Long.parseLong(id));
            } catch (Exception e) {
                Log.w(getString(R.string.LOGGING_TAG_ERROR), e.getMessage());
            }
        });
    }

    private void navigateToActionsWithArgs(int fragmentId) {
        Bundle bundle = new Bundle();
        try {
            bundle.putString("taskId", ((String) spinner.getSelectedItem()).split(":")[0]);
            bundle.putString("name", ((String) spinner.getSelectedItem()).split(":")[1].trim());
        }catch (Exception e) {
            Log.w(getString(R.string.LOGGING_TAG_ERROR), e.getMessage());
        }
        navController.navigate(fragmentId, bundle);
    }
}