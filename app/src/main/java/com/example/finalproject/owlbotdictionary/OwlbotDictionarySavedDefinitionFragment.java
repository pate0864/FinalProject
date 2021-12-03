package com.example.finalproject.owlbotdictionary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwlbotDictionarySavedDefinitionFragment extends Fragment {


    public OwlbotDictionarySavedDefinitionFragment() {
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
        return inflater.inflate(R.layout.fragment_owlbot_dictionary_saved_definition, container, false);
    }
}