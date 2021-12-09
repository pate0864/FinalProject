package com.example.finalproject.covidtracker;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CovidInfoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CovidInfoDetailFragment extends Fragment {

    CovidInfoDatabase database;
    CovidInfo info;
    boolean saved = false;

    TextView textDate, textChangeCases,textChangeRecoveries, textChangeFatalities, textChangeHospitalization, textChangeVaccinations;
    TextView textTotalCases,textTotalRecoveries, textTotalFatalities, textTotalHospitalization, textTotalVaccinations;
    Button buttonAction, buttonBack;

    public CovidInfoDetailFragment() {
        /**
         * Required empty public constructor
         */
    }
    public static CovidInfoDetailFragment newInstance(CovidInfo covidInfo) {
        CovidInfoDetailFragment fragment = new CovidInfoDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("covidInfo", covidInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            info = (CovidInfo) getArguments().getSerializable("covidInfo");
        }
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * This function will jump to the Fragment layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_covid_info_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);

        database = new CovidInfoDatabase(getContext());
        saved = database.isCovidInfoSaved(info);

        if(saved)
            buttonAction.setText(getResources().getString(R.string.covid_delete));
        else
            buttonAction.setText(getResources().getString(R.string.covid_save));

        textDate.setText(info.getDate());
        textChangeCases.setText(""+info.getChangeCases());
        textChangeFatalities.setText(""+info.getChangeFatalities());
        textChangeRecoveries.setText(""+info.getChangeRecoveries());
        textChangeHospitalization.setText(""+info.getChangeHospitalization());
        textChangeVaccinations.setText(""+info.getChangeVaccinations());
        textTotalCases.setText(""+info.getTotalCases());
        textTotalFatalities.setText(""+info.getTotalFatalities());
        textTotalRecoveries.setText(""+info.getTotalRecoveries());
        textTotalHospitalization.setText(""+info.getTotalHospitalization());
        textTotalVaccinations.setText(""+info.getTotalVaccinations());

        buttonAction.setOnClickListener(v->{
            if(saved){
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete Covid Information")
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
                                dialogInterface.dismiss();
                                database.deleteCovidInfo(info);
                                Toast.makeText(getContext(), (R.string.covid_delete_successful), Toast.LENGTH_SHORT).show();
                                saved = !saved;
                                if(saved)
                                    buttonAction.setText(getResources().getString(R.string.covid_delete));
                                else
                                    buttonAction.setText(getResources().getString(R.string.covid_save));
                            }
                        })
                        .setCancelable(false);

                dialog.create().show();
            }else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Save Covid Information")
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
                                database.addCovidInfo(info);
                                Toast.makeText(getContext(), (R.string.covid_save_successful), Toast.LENGTH_SHORT).show();
                                saved = !saved;
                                if(saved)
                                    buttonAction.setText(getResources().getString(R.string.covid_delete));
                                else
                                    buttonAction.setText(getResources().getString(R.string.covid_save));
                            }
                        })
                        .setCancelable(false);

                dialog.create().show();
            }

        });

        buttonBack.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void bindViews(View view){
        textDate = view.findViewById(R.id.textDate);
        textChangeCases = view.findViewById(R.id.textChangeCases);
        textChangeFatalities = view.findViewById(R.id.textChangeFatalities);
        textChangeHospitalization = view.findViewById(R.id.textChangeHospitalization);
        textChangeRecoveries = view.findViewById(R.id.textChangeRecoveries);
        textChangeVaccinations = view.findViewById(R.id.textChangeVaccination);
        textTotalCases = view.findViewById(R.id.textTotalCases);
        textTotalFatalities = view.findViewById(R.id.textTotalFatalities);
        textTotalHospitalization = view.findViewById(R.id.textTotalHospitalization);
        textTotalRecoveries = view.findViewById(R.id.textTotalRecoveries);
        textTotalVaccinations = view.findViewById(R.id.textTotalVaccination);
        buttonAction = view.findViewById(R.id.buttonAction);
        buttonBack = view.findViewById(R.id.buttonBack);
    }
}