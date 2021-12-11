package com.example.finalproject.carbondioxideinterface;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class CarbonDioxideSavedFragment extends Fragment {

    RecyclerView rvSavedEmission;
    TextView tvNotFound;

    public CarbonDioxideSavedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carbon_dioxide_saved, container, false);
        rvSavedEmission = view.findViewById(R.id.rvSavedEmission);
        tvNotFound = view.findViewById(R.id.tvNotFound);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        CarbonDioxideEmissionDatabase database = new CarbonDioxideEmissionDatabase(getContext());

        List<CarbonEmissionEstimate> estimateList = database.getSavedCarbonEmissions();

        if(estimateList.isEmpty())
        {
            tvNotFound.setVisibility(View.VISIBLE);
            rvSavedEmission.setVisibility(View.GONE);
        }else{
            tvNotFound.setVisibility(View.GONE);
            rvSavedEmission.setVisibility(View.VISIBLE);
        }

        rvSavedEmission.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSavedEmission.setAdapter(new SavedEmissionRecyclerAdapter(estimateList, new SavedEmissionRecyclerAdapter.OnEmissionSelectedListener() {
            @Override
            public void onEmissionSelected(CarbonEmissionEstimate estimate) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, CarbonDioxideEmissionFragment.newInstance(estimate))
                        .addToBackStack("EmissionDetails")
                        .commit();
            }
        }));
    }
}