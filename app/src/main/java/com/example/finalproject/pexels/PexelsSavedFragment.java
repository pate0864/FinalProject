package com.example.finalproject.pexels;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PexelsSavedFragment extends Fragment implements PexelsImageRecyclerAdapter.OnClickImageListener {

    RecyclerView recyclerViewSavedImages;

    public PexelsSavedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pexels_saved, container, false);

        recyclerViewSavedImages = view.findViewById(R.id.recyclerSavedImages);
        recyclerViewSavedImages.setLayoutManager(new LinearLayoutManager(getContext()));

        PexelsImageDatabase database = new PexelsImageDatabase(getContext());
        List<PexelImage> images = database.getSavedImages();

        if(images.isEmpty())
            Snackbar.make(view, "No Saved Images Found", BaseTransientBottomBar.LENGTH_LONG);

        recyclerViewSavedImages.setAdapter(new PexelsImageRecyclerAdapter(images,getContext() ,this::onImageClick ));

        return view;
    }

    @Override
    public void onImageClick(PexelImage image) {
        Intent intent = new Intent(getContext(), PexelsImageViewActivity.class);
        intent.putExtra("image", image);
        startActivity(intent);
    }
}