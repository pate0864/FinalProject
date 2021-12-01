package com.example.finalproject.carbondioxideinterface;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarbonDioxideEmissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarbonDioxideEmissionFragment extends Fragment {

    private static final String ESTIMATE = "estimate";

    private CarbonEmissionEstimate estimate;

    TextView tvCarbonInG,tvCarbonInKg,tvCarbonInLb,tvCarbonInMt,tvDistance, tvMaker, tvModel;

    public CarbonDioxideEmissionFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static CarbonDioxideEmissionFragment newInstance(CarbonEmissionEstimate estimate) {
        CarbonDioxideEmissionFragment fragment = new CarbonDioxideEmissionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ESTIMATE, estimate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            estimate = (CarbonEmissionEstimate) getArguments().getSerializable(ESTIMATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carbon_dioxide_emission, container, false);
        tvCarbonInG = view.findViewById(R.id.tvCarbonG);
        tvCarbonInKg = view.findViewById(R.id.tvCarbonKg);
        tvCarbonInLb = view.findViewById(R.id.tvCarbonLb);
        tvCarbonInMt = view.findViewById(R.id.tvCarbonMt);
        tvDistance = view.findViewById(R.id.tvDistance);
        tvMaker = view.findViewById(R.id.tvVehicleMaker);
        tvModel = view.findViewById(R.id.tvVehicleModel);

        tvCarbonInG.setText(estimate.getCarbonInG()+" g");
        tvCarbonInKg.setText(estimate.getCarbonInKg()+" Kg");
        tvCarbonInLb.setText(estimate.getCarbonInLb()+" lb");
        tvCarbonInMt.setText(estimate.getCarbonInMt()+" mt");
        tvDistance.setText(estimate.getDistance()+" "+estimate.getDistanceUnit());
        tvModel.setText(estimate.getVehicleModel());
        tvModel.setText(estimate.getVehicleMaker());

        return view;
    }
}