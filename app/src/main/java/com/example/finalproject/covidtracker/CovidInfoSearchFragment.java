package com.example.finalproject.covidtracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_covid_info_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);

        layoutProgress.setVisibility(View.GONE);

        editTextSearch.setOnClickListener(v->{
            showDateSelector();
        });

        buttonSearch.setOnClickListener(v->{
            if(!"".equals(editTextSearch.getText().toString())){
                new FetchCovidInfo(editTextSearch.getText().toString()).execute();
            }else
                Toast.makeText(getContext(), "Please Select a Date", Toast.LENGTH_SHORT).show();
        });
        getSharedPreference();
    }

    private void showDateSelector(){
        selectedDate = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, monthOfYear, dayOfMonth) -> {
            selectedDate.set(year, monthOfYear, dayOfMonth);
            editTextSearch.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE)).show();
    }

    private void bindViews(View view){
        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        recyclerCovidInfo = view.findViewById(R.id.recyclerCovidInfo);
        layoutProgress = view.findViewById(R.id.layoutProgress);
    }

    private void saveSharedPreference(){
        sharedPreferences = getActivity().getSharedPreferences("CovidInfoTracker", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("covidDate",editTextSearch.getText().toString()).apply();
    }

    private void getSharedPreference(){
        sharedPreferences = getActivity().getSharedPreferences("CovidInfoTracker", Context.MODE_PRIVATE);
        String date = sharedPreferences.getString("covidDate","");
        editTextSearch.setText(date);
        selectedDate = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            selectedDate.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    class FetchCovidInfo extends AsyncTask<Void, Void, List<CovidInfo>>{

        String date;

        public FetchCovidInfo(String date) {
            this.date = date;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layoutProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<CovidInfo> doInBackground(Void... voids) {
            JSONArray data;
            List<CovidInfo>infoList = new ArrayList<>();
            try {
                URL mUrl = new URL("https://api.covid19tracker.ca/reports?after="+date);
                HttpURLConnection connection = (HttpURLConnection)
                        mUrl.openConnection();

                data = (new JSONObject(getString(connection.getInputStream()))).getJSONArray("data");
                for(int i=0;i< data.length();i++){
                    CovidInfo info = new CovidInfo();
                    info.fromJSON(data.getJSONObject(i));
                    infoList.add(info);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return infoList;
        }

        @Override
        protected void onPostExecute(List<CovidInfo> infoList) {
            super.onPostExecute(infoList);
            layoutProgress.setVisibility(View.GONE);
            if (infoList.isEmpty()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.covid_no_data), Toast.LENGTH_SHORT).show();
            } else {
                recyclerCovidInfo.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerCovidInfo.setAdapter(new CovidInfoListAdapter(infoList, getActivity()));
                saveSharedPreference();
            }
        }

        private String getString(InputStream is) {
            String line = "";
            InputStreamReader isr = new
                    InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while (true) {
                try {
                    if ((line = br.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.append(line);
            }
            return sb.toString();
        }
    }
}