package com.example.finalproject.carbondioxideinterface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalproject.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * create an instance of this fragment.
 */
public class CarbonDioxideEmissionFragment extends Fragment {

    private static final String ESTIMATE = "estimate";

    private CarbonEmissionEstimate estimate;

    TextView tvCarbonInG,tvCarbonInKg,tvCarbonInLb,tvCarbonInMt,tvDistance, tvMaker, tvModel;
    Button btnSave, btnBack, btnDelete;

    CarbonDioxideEmissionDatabase database;

    public CarbonDioxideEmissionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_carbon_dioxide_emission, container, false);
        tvCarbonInG = view.findViewById(R.id.tvCarbonG);
        tvCarbonInKg = view.findViewById(R.id.tvCarbonKg);
        tvCarbonInLb = view.findViewById(R.id.tvCarbonLb);
        tvCarbonInMt = view.findViewById(R.id.tvCarbonMt);
        tvDistance = view.findViewById(R.id.tvDistance);
        tvMaker = view.findViewById(R.id.tvVehicleMaker);
        tvModel = view.findViewById(R.id.tvVehicleModel);

        btnBack = view.findViewById(R.id.btnBack);
        btnSave = view.findViewById(R.id.btnSave);
        btnDelete = view.findViewById(R.id.btnDelete);

        tvCarbonInG.setText(estimate.getCarbonInG()+" g");
        tvCarbonInKg.setText(estimate.getCarbonInKg()+" Kg");
        tvCarbonInLb.setText(estimate.getCarbonInLb()+" lb");
        tvCarbonInMt.setText(estimate.getCarbonInMt()+" mt");
        tvDistance.setText(estimate.getDistance()+" "+estimate.getDistanceUnit());
        tvModel.setText(estimate.getVehicleModel());
        tvModel.setText(estimate.getVehicleMaker());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = new CarbonDioxideEmissionDatabase(getContext());

        if(database.getCarbonEmissionById(estimate.getEstimateId()) !=null){
            btnSave.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
        }else{
            btnSave.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Save Estimate")
                        .setMessage("Are you sure you want to save?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                database.insertCarbonEmission(estimate);
                                btnSave.setVisibility(View.GONE);
                                btnDelete.setVisibility(View.VISIBLE);
                                Snackbar.make(view, getResources().getString(R.string.emission_saved_success), Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false);

                dialog.create().show();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete Estimate")
                        .setMessage("Are you sure you want to delete?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int count = database.deleteCarbonEmission(estimate);
                                if (count >0){
                                    btnSave.setVisibility(View.VISIBLE);
                                    btnDelete.setVisibility(View.GONE);
                                    Snackbar.make(view, getResources().getString(R.string.emission_delete_success), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setCancelable(false);

                dialog.create().show();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}