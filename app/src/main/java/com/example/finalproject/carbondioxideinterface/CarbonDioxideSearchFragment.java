package com.example.finalproject.carbondioxideinterface;

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

    Button btnSearchMaker,btnResetSearch, btnCalculateEmission;
    LinearLayout layoutSelectMaker, layoutSelectModel;
    EditText etDistance;
    RecyclerView rvSelectModel, rvSelectMaker;

    MakersRecyclerAdapter makersRecyclerAdapter;
    ModelsRecyclerAdapter modelsRecyclerAdapter;

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
        btnCalculateEmission = view.findViewById(R.id.btnCalculateCarbonDioxide);
        layoutSelectMaker = view.findViewById(R.id.layoutSelectMakers);
        layoutSelectModel = view.findViewById(R.id.layoutSelectModel);
        rvSelectMaker = view.findViewById(R.id.recyclerViewMakers);
        rvSelectModel = view.findViewById(R.id.recyclerViewModels);
        etDistance = view.findViewById(R.id.etDistance);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSearchMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONArray array = new SearchMakers().execute().get();
                    layoutSelectMaker.setVisibility(View.VISIBLE);
                    layoutSelectModel.setVisibility(View.GONE);
                    rvSelectMaker.setLayoutManager(new LinearLayoutManager(getContext()));
                    makersRecyclerAdapter = new MakersRecyclerAdapter(array, new MakersRecyclerAdapter.OnMakerSelectedListener() {
                        @Override
                        public void onMakerSelected(String id) {
                            try {
                                JSONArray models  = new SearchModels(id).execute().get();
                                layoutSelectMaker.setVisibility(View.GONE);
                                layoutSelectModel.setVisibility(View.VISIBLE);
                                rvSelectModel.setLayoutManager(new LinearLayoutManager(getContext()));
                                modelsRecyclerAdapter = new ModelsRecyclerAdapter(models, new ModelsRecyclerAdapter.OnMakerSelectedListener() {
                                    @Override
                                    public void onMakerSelected(String id) {
                                        try {
                                            CarbonEmissionEstimate estimate = new GenerateEstimate(id, Double.parseDouble(etDistance.getText().toString())).execute().get();
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.frameLayout, CarbonDioxideEmissionFragment.newInstance(estimate))
                                                    .addToBackStack("EmissionDetails")
                                                    .commit();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                rvSelectModel.setAdapter(modelsRecyclerAdapter);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    rvSelectMaker.setAdapter(makersRecyclerAdapter);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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