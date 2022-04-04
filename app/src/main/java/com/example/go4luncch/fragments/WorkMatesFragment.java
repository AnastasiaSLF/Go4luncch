package com.example.go4luncch.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.go4luncch.R;
import com.example.go4luncch.ViewModelFactory;
import com.example.go4luncch.adapters.WorkmatesAdapter;
import com.example.go4luncch.models.User;
import com.example.go4luncch.viewmodels.WorkmatesViewModel;

import java.util.List;


public class WorkMatesFragment extends Fragment {
    private WorkmatesViewModel listWorkmatesViewModel;

    private ProgressBar progressBar;
    private TextView noWorkmates;
    private RecyclerView recyclerView;
    public static final String TAG = "WorkmatesFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_work_mates, container, false);
        listWorkmatesViewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance()).get(WorkmatesViewModel.class);

        noWorkmates = view.findViewById(R.id.workmates_no_workmates);
        progressBar = view.findViewById(R.id.workmates_progress_bar);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.workmates_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        progressBar.setVisibility(View.VISIBLE);

        getWorkmatesExceptCurrentUser();

        return view;

    }

    private void getWorkmatesExceptCurrentUser() {
        listWorkmatesViewModel.getWorkmates().observe(
                requireActivity(),
                changedListWorkmates -> {
                    //Do something with the changed value
                    if (changedListWorkmates != null) {
                        sendResultsToAdapter(changedListWorkmates);
                    } else {
                        noWorkmatesToShow();
                    }
                });
    }

    private void noWorkmatesToShow() {
        progressBar.setVisibility(View.GONE);
        noWorkmates.setText(R.string.workmates_list_no_workmates_to_show);
        noWorkmates.setVisibility(View.VISIBLE);
    }
    private void sendResultsToAdapter(List<User> lWorkmatesExceptCurrentUser) {
        if (lWorkmatesExceptCurrentUser == null || lWorkmatesExceptCurrentUser.isEmpty()) {
            noWorkmatesToShow();
        } else {
            recyclerView.setAdapter(new WorkmatesAdapter(lWorkmatesExceptCurrentUser, this.getActivity(), TAG));        }
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
