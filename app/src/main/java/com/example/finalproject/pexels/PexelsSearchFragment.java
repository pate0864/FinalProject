package com.example.finalproject.pexels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.finalproject.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class PexelsSearchFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerViewSearch;
    EditText editTextSearch;
    Button buttonSearch;

    public PexelsSearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pexels_search, container, false);
        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonSearch = view.findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(this::onClick);
        editTextSearch.setText(getActivity().getSharedPreferences("pexels", Context.MODE_PRIVATE).getString("searchText", ""));
        return view;
    }

    @Override
    public void onClick(View view) {
        if(editTextSearch.getText().toString().equals(""))
            Snackbar.make(getView(), (R.string.pexels_no_keword), BaseTransientBottomBar.LENGTH_SHORT).show();
        else{
            getActivity().getSharedPreferences("pexels", Context.MODE_PRIVATE).edit().putString("searchText", editTextSearch.getText().toString()).apply();
            new SearchImages(editTextSearch.getText().toString()).execute();
        }
    }

    class SearchImages extends AsyncTask<Void, Void, String> implements PexelsImageRecyclerAdapter.OnClickImageListener {

        String searchKeyword;
        ProgressDialog dialog;

        public SearchImages(String searchKeyword) {
            this.searchKeyword = searchKeyword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Getting images");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            dialog.setProgress(40);
            try {
                URL url = new URL("https://api.pexels.com/v1/search?query="+searchKeyword);
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Authorization", getString(R.string.api_key_pexels));

                String currentLine = "";
                InputStreamReader inputStreamReader = new
                        InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    try {
                        currentLine = bufferedReader.readLine();
                        if (currentLine == null)
                            break;
                        stringBuilder.append(currentLine);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                dialog.setProgress(70);
                return stringBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.setProgress(95);
            if("".equals(s) || s== null)
                Toast.makeText(getContext(), "No Images Found", Toast.LENGTH_SHORT).show();
            else{
                List<PexelImage> images = new ArrayList<>();
                try {
                    JSONArray array = (new JSONObject(s)).getJSONArray("photos");
                    for(int i=0;i<array.length();i++){
                        JSONObject json = array.getJSONObject(i);
                        PexelImage image =  new PexelImage();
                        image.setId(json.getInt("id"));
                        image.setHeight(json.getInt("height"));
                        image.setWidth(json.getInt("width"));
                        image.setPhotographer(json.getString("photographer"));
                        image.setPhotographerUrl(json.getString("photographer_url"));
                        image.setUrl(json.getString("url"));
                        image.setAvgColor(json.getString("avg_color"));

                        JSONObject imagesUrl = json.getJSONObject("src");
                        image.setOriginalUrl(imagesUrl.getString("original"));
                        image.setMediumUrl(imagesUrl.getString("medium"));
                        images.add(image);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
                recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewSearch.setAdapter(new PexelsImageRecyclerAdapter(images, getContext(), this::onImageClick));
            }

        }

        @Override
        public void onImageClick(PexelImage image) {
            Intent intent = new Intent(getContext(), PexelsImageViewActivity.class);
            intent.putExtra("image", image);
            startActivity(intent);
        }
    }

}