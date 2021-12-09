package com.example.finalproject.owlbotdictionary;

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
import android.widget.LinearLayout;

import com.example.finalproject.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * Fragment sub class
 */
public class OwlbotDictionarySavedDefinitionFragment extends Fragment {

    RecyclerView recyclerSavedDefinitions;

    public OwlbotDictionarySavedDefinitionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_owlbot_dictionary_saved_definition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerSavedDefinitions = view.findViewById(R.id.recyclerSavedDefinition);
    }

    /**
     * using resume function to resume the application
     */
    @Override
    public void onResume() {
        super.onResume();
        recyclerSavedDefinitions.setLayoutManager(new LinearLayoutManager(getContext()));

        OwlbotDicitonaryDatabase database = new OwlbotDicitonaryDatabase(getContext());
        List<Word> words = database.getSavedDefinition();
        if(words.isEmpty())
            Snackbar.make(getView(),"No Saved Definitions Found", BaseTransientBottomBar.LENGTH_LONG).show();
        recyclerSavedDefinitions.setAdapter(new SavedDefinitionListAdapter(words, new OnDefinitionClickListener() {
            @Override
            public void OnClick(int position) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout,OwlbotDictionaryViewDefinitionFragment.newInstance(words.get(position), words.get(position).getDefinitions().get(0)))
                        .addToBackStack("DefinitionDetail")
                        .commit();
            }
        }));
    }
}