package com.example.finalproject.covidtracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 */
public class CovidInfoSearchFragment extends Fragment {

    EditText editTextSearch;
    Button buttonSearch;
    RecyclerView recyclerCovidInfo;
    LinearLayout layoutProgress;

    Calendar selectedDate;
    SharedPreferences sharedPreferences;

    public CovidInfoSearchFragment() {
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
        return inflater.inflate(R.layout.fragment_covid_info_search, container, false);
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);

        layoutProgress.setVisibility(View.GONE);

        editTextSearch.setOnClickListener(v -> {
            showDateSelector();
        });

        buttonSearch.setOnClickListener(v -> {
            if (!"".equals(editTextSearch.getText().toString())) {
                Executors.newSingleThreadExecutor()
                    .execute(new FetchCovidInfo(editTextSearch.getText().toString()));
            } else
                Toast.makeText(getContext(), "Please Select a Date", Toast.LENGTH_SHORT).show();
        });
        getSharedPreference();
    }

    /**
     * This function is used to choose the date from where the user want to search
     */
    private void showDateSelector() {
        selectedDate = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, monthOfYear, dayOfMonth) -> {
            selectedDate.set(year, monthOfYear, dayOfMonth);
            editTextSearch.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE)).show();
    }

    private void bindViews(View view) {
        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        recyclerCovidInfo = view.findViewById(R.id.recyclerCovidInfo);
        layoutProgress = view.findViewById(R.id.layoutProgress);
    }

    /**
     *
     */
    private void saveSharedPreference() {
        sharedPreferences = getActivity().getSharedPreferences("CovidInfoTracker", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("covidDate", editTextSearch.getText().toString()).apply();
    }

    private void getSharedPreference() {
        sharedPreferences = getActivity().getSharedPreferences("CovidInfoTracker", Context.MODE_PRIVATE);
        String date = sharedPreferences.getString("covidDate", "");
        editTextSearch.setText(date);
        selectedDate = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            selectedDate.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    class FetchCovidInfo implements Runnable {

        String date;

        public FetchCovidInfo(String date) {
            this.date = date;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private String getString(InputStream is) {
            return (new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            )).lines()
                    .collect(Collectors.joining("\n"));
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            getActivity().runOnUiThread(() -> {
                layoutProgress.setVisibility(View.VISIBLE);
            });

            /**
             * Using JSON string data and uml to display the data from the server
             */
            JSONArray data;
            List<CovidInfo> infoList = new ArrayList<>();
            try {
                URL mUrl = new URL("https://api.covid19tracker.ca/reports?after=" + date);
                HttpURLConnection connection = (HttpURLConnection)
                        mUrl.openConnection();

                data = (new JSONObject(getString(connection.getInputStream()))).getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    CovidInfo info = new CovidInfo();
                    info.fromJSON(data.getJSONObject(i));
                    infoList.add(info);
                }

                getActivity().runOnUiThread(()->{
                    layoutProgress.setVisibility(View.GONE);
                    if (infoList.isEmpty()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.covid_no_data), Toast.LENGTH_SHORT).show();
                    } else {
                        recyclerCovidInfo.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerCovidInfo.setAdapter(new CovidInfoListAdapter(infoList, getActivity()));
                        saveSharedPreference();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}