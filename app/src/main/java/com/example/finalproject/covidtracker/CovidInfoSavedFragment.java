package com.example.finalproject.covidtracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CovidInfoSavedFragment extends Fragment {

    RecyclerView recyclerSavedInfo;

    public CovidInfoSavedFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_covid_info_saved, container, false);
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerSavedInfo = view.findViewById(R.id.recyclerSavedInfo);
    }

    /**
     * This function will resume the application if the data is not founded in the database
     */
    @Override
    public void onResume() {
        super.onResume();
        CovidInfoDatabase database = new CovidInfoDatabase(getContext());
        List<CovidInfo> list = database.getAllSavedInfo();
        if(list.isEmpty())
            Snackbar.make(getView(),getString(R.string.covid_no_data), BaseTransientBottomBar.LENGTH_LONG).show();

        recyclerSavedInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSavedInfo.setAdapter(new CovidInfoListAdapter(list, getActivity()));
    }
}