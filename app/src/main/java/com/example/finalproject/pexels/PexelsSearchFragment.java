package com.example.finalproject.pexels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 */
public class PexelsSearchFragment extends Fragment implements View.OnClickListener, PexelsImageRecyclerAdapter.OnClickImageListener {

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

        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonSearch.setOnClickListener(this::onClick);
        editTextSearch.setText(getActivity().getSharedPreferences("pexels", Context.MODE_PRIVATE).getString("searchText", ""));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if (editTextSearch.getText().toString().equals(""))
            Snackbar.make(getView(), (R.string.pexels_no_keword), BaseTransientBottomBar.LENGTH_SHORT).show();
        else {
            getActivity().getSharedPreferences("pexels", Context.MODE_PRIVATE).edit().putString("searchText", editTextSearch.getText().toString()).apply();


            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Getting images");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new SearchImages(editTextSearch.getText().toString(),getActivity(), recyclerViewSearch,  dialog));
        }
    }

    @Override
    public void onImageClick(PexelImage image) {
        Intent intent = new Intent(getContext(), PexelsImageViewActivity.class);
        intent.putExtra("image", image);
        startActivity(intent);
    }

    class SearchImages implements Runnable, PexelsImageRecyclerAdapter.OnClickImageListener {

        String searchKeyword;
        Context context;
        RecyclerView recyclerView;
        ProgressDialog dialog;

        public SearchImages(String searchKeyword, Context context, RecyclerView recyclerView, ProgressDialog dialog) {
            this.searchKeyword = searchKeyword;
            this.context = context;
            this.recyclerView = recyclerView;
            this.dialog = dialog;
        }

        @Override
        public void onImageClick(PexelImage image) {
            Intent intent = new Intent(getContext(), PexelsImageViewActivity.class);
            intent.putExtra("image", image);
            startActivity(intent);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            getActivity().runOnUiThread(()->dialog.show());
            try {
                URL url = new URL("https://api.pexels.com/v1/search?query=" + searchKeyword);
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Authorization", getString(R.string.api_key_pexels));

                String s = (new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
                )).lines()
                        .collect(Collectors.joining("\n"));

                if ("".equals(s) || s == null)
                    Toast.makeText(getContext(), "No Images Found", Toast.LENGTH_SHORT).show();
                else {
                    List<PexelImage> images = new ArrayList<>();

                    JSONArray array = (new JSONObject(s)).getJSONArray("photos");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        PexelImage image = new PexelImage();
                        image.setId(json.getInt("id"));
                        image.setHeight(json.getInt("height"));
                        image.setWidth(json.getInt("width"));
                        image.setPhotographer(json.getString("photographer"));
                        image.setPhotographerUrl(json.getString("photographer_url"));
                        image.setUrl(json.getString("url"));

                        JSONObject imagesUrl = json.getJSONObject("src");
                        image.setOriginalUrl(imagesUrl.getString("original"));
                        image.setMediumUrl(imagesUrl.getString("medium"));
                        images.add(image);
                    }
                    getActivity().runOnUiThread(() -> {
                        dialog.dismiss();
                        recyclerView.setAdapter(new PexelsImageRecyclerAdapter(images, context, this::onImageClick));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }

}