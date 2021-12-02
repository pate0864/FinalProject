package com.example.finalproject.carbondioxideinterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarbonDioxideSearchFragment extends Fragment {

    Button btnSearchMaker,btnResetSearch;
    LinearLayout layoutSelectMaker, layoutSelectModel;
    EditText etDistance;
    RecyclerView rvSelectModel, rvSelectMaker;

    MakersRecyclerAdapter makersRecyclerAdapter;
    ModelsRecyclerAdapter modelsRecyclerAdapter;

    SharedPreferences sharedPreferences;

    public CarbonDioxideSearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_carbon_dioxide_search, container, false);
        btnSearchMaker = view.findViewById(R.id.btnSearchCarbonDioxide);
        btnResetSearch = view.findViewById(R.id.btnResetSearch);
        layoutSelectMaker = view.findViewById(R.id.layoutSelectMakers);
        layoutSelectModel = view.findViewById(R.id.layoutSelectModel);
        rvSelectMaker = view.findViewById(R.id.recyclerViewMakers);
        rvSelectModel = view.findViewById(R.id.recyclerViewModels);
        etDistance = view.findViewById(R.id.etDistance);

        sharedPreferences = getActivity().getSharedPreferences("carbonDioxideEmission", Context.MODE_PRIVATE);
        etDistance.setText(sharedPreferences.getString("distance",""));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSearchMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(etDistance.getText().toString()))
                    new SearchMakers().execute();
                else
                    Toast.makeText(getContext() , "Please Enter a Distance", Toast.LENGTH_SHORT).show();
            }
        });

        btnResetSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSelectMaker.setVisibility(View.GONE);
                layoutSelectModel.setVisibility(View.GONE);
                etDistance.setText(null);
            }
        });

    }

    class SearchMakers extends AsyncTask<Void, Void, JSONArray>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setCancelable(false);
            dialog.setMessage("Searching Makers...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            try {
                URL endPoint = new URL(getResources().getString(R.string.apiMakersUrl));

                // Create connection
                HttpsURLConnection connection =
                        (HttpsURLConnection) endPoint.openConnection();

                connection.setRequestProperty("Authorization","Bearer "+getResources().getString(R.string.apiKeyCarbonEmission));
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("GET");

                return new JSONArray(readStreamAndGetString(connection.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            dialog.cancel();
            layoutSelectMaker.setVisibility(View.VISIBLE);
            layoutSelectModel.setVisibility(View.GONE);
            rvSelectMaker.setLayoutManager(new LinearLayoutManager(getContext()));
            makersRecyclerAdapter = new MakersRecyclerAdapter(jsonArray, new MakersRecyclerAdapter.OnMakerSelectedListener() {
                @Override
                public void onMakerSelected(String id) {

                        new SearchModels(id).execute();

                }
            });
            rvSelectMaker.setAdapter(makersRecyclerAdapter);

            sharedPreferences.edit().putString("distance", etDistance.getText().toString()).apply();
        }

        /**
         * @param is InputStream from the HTTP API call
         * @return String
         * */
        private String readStreamAndGetString(InputStream is) {
            String currentLine = "";
            InputStreamReader inputStreamReader = new
                    InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                try {
                    if ((currentLine = bufferedReader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(currentLine);
            }
            return stringBuilder.toString();
        }
    }

    class SearchModels extends AsyncTask<Void, Void, JSONArray>{
        String vehicleMakerId;

        public SearchModels(String vehicleMakerId) {
            this.vehicleMakerId = vehicleMakerId;
        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setCancelable(false);
            dialog.setMessage("Searching Models...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            try {
                URL endPoint = new URL(getResources().getString(R.string.apiMakersUrl)+ "/" +vehicleMakerId+"/vehicle_models");

                // Create connection
                HttpsURLConnection connection =
                        (HttpsURLConnection) endPoint.openConnection();

                connection.setRequestProperty("Authorization","Bearer "+getResources().getString(R.string.apiKeyCarbonEmission));
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("GET");

                return new JSONArray(readStreamAndGetString(connection.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            dialog.cancel();
            layoutSelectMaker.setVisibility(View.GONE);
            layoutSelectModel.setVisibility(View.VISIBLE);
            rvSelectModel.setLayoutManager(new LinearLayoutManager(getContext()));
            modelsRecyclerAdapter = new ModelsRecyclerAdapter(jsonArray, new ModelsRecyclerAdapter.OnMakerSelectedListener() {
                @Override
                public void onMakerSelected(String id) {
                        new GenerateEstimate(id, Double.parseDouble(etDistance.getText().toString())).execute();
                }
            });
            rvSelectModel.setAdapter(modelsRecyclerAdapter);
        }

        /**
         * @param is InputStream from the HTTP API call
         * @return String
         * */
        private String readStreamAndGetString(InputStream is) {
            String currentLine = "";
            InputStreamReader inputStreamReader = new
                    InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                try {
                    if ((currentLine = bufferedReader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(currentLine);
            }
            return stringBuilder.toString();
        }
    }

    class GenerateEstimate extends AsyncTask<Void, Void, CarbonEmissionEstimate>{
        String modelId;
        double distance;

        public GenerateEstimate(String modelId, double distance) {
            this.modelId = modelId;
            this.distance = distance;
        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setCancelable(false);
            dialog.setMessage("Generating Estimates...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected CarbonEmissionEstimate doInBackground(Void... voids) {
            try {
                URL endPoint = new URL(getResources().getString(R.string.apiEstimatesUrl));

                // Create connection
                HttpsURLConnection connection =
                        (HttpsURLConnection) endPoint.openConnection();

                connection.setRequestProperty("Authorization","Bearer "+getResources().getString(R.string.apiKeyCarbonEmission));
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");

                JSONObject data = new JSONObject();
                data.put("type","vehicle");
                data.put("distance_unit","km");
                data.put("distance_value",distance);
                data.put("vehicle_model_id",modelId);

                connection.setDoOutput(true);
                connection.getOutputStream().write(data.toString().getBytes());
                JSONObject response = new JSONObject(readStreamAndGetString(connection.getInputStream())).getJSONObject("data");

                CarbonEmissionEstimate estimate = new CarbonEmissionEstimate();
                estimate.setEstimateId(response.getString("id"));
                estimate.setDistance(response.getJSONObject("attributes").getDouble("distance_value"));
                estimate.setVehicleMaker(response.getJSONObject("attributes").getString("vehicle_make"));
                estimate.setVehicleModel(response.getJSONObject("attributes").getString("vehicle_model"));
                estimate.setVehicleYear(response.getJSONObject("attributes").getString("vehicle_year"));
                estimate.setDistanceUnit(response.getJSONObject("attributes").getString("distance_unit"));
                estimate.setCarbonInG(response.getJSONObject("attributes").getDouble("carbon_g"));
                estimate.setCarbonInKg(response.getJSONObject("attributes").getDouble("carbon_kg"));
                estimate.setCarbonInLb(response.getJSONObject("attributes").getDouble("carbon_lb"));
                estimate.setCarbonInMt(response.getJSONObject("attributes").getDouble("carbon_mt"));

                return estimate;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(CarbonEmissionEstimate estimate) {
            super.onPostExecute(estimate);
            dialog.cancel();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, CarbonDioxideEmissionFragment.newInstance(estimate))
                    .addToBackStack("EmissionDetails")
                    .commit();
        }

        /**
         * @param is InputStream from the HTTP API call
         * @return String
         * */
        private String readStreamAndGetString(InputStream is) {
            String currentLine = "";
            InputStreamReader inputStreamReader = new
                    InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                try {
                    if ((currentLine = bufferedReader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(currentLine);
            }
            return stringBuilder.toString();
        }
    }

}