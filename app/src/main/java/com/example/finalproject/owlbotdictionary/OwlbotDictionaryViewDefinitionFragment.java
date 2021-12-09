package com.example.finalproject.owlbotdictionary;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;

/**
 * create an instance of this fragment.
 */
public class OwlbotDictionaryViewDefinitionFragment extends Fragment {

    private static final String WORD = "word";
    private static final String DEFINITION = "definition";

    private Word word;
    private Definition definition;

    TextView tvWord, tvType, tvPronunciation, tvDefinition, tvExample;
    ImageView ivImage;
    Button btnSave, btnDelete, btnBack;

    OwlbotDicitonaryDatabase owlbotDicitonaryDatabase;

    public OwlbotDictionaryViewDefinitionFragment() {
        // Required empty public constructor
    }

    public static OwlbotDictionaryViewDefinitionFragment newInstance(Word word, Definition definition) {
        OwlbotDictionaryViewDefinitionFragment fragment = new OwlbotDictionaryViewDefinitionFragment();
        Bundle args = new Bundle();
        args.putSerializable(WORD, word);
        args.putSerializable(DEFINITION, definition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            word = (Word) getArguments().getSerializable(WORD);
            definition = (Definition) getArguments().getSerializable(DEFINITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owlbot_dictionary_view_definition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvWord = view.findViewById(R.id.tvWord);
        tvDefinition = view.findViewById(R.id.tvDefinition);
        tvExample = view.findViewById(R.id.tvExample);
        tvPronunciation = view.findViewById(R.id.tvPronunciation);
        tvType = view.findViewById(R.id.tvType);
        btnBack = view.findViewById(R.id.btnBack);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnSave = view.findViewById(R.id.btnSave);
        ivImage = view.findViewById(R.id.ivImage);

        owlbotDicitonaryDatabase = new OwlbotDicitonaryDatabase(getContext());

        tvWord.setText(word.getWord());
        tvType.setText("("+definition.getType()+")");
        tvPronunciation.setText(word.getPronunciation());
        tvDefinition.setText(definition.getDefinition());
        tvExample.setText(definition.getExample());

        if(word.getId() !=null){
            btnSave.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
        }
        else{
            btnSave.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Save Definition")
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
                                owlbotDicitonaryDatabase.insertDefinition(word, definition);
                                Toast.makeText(getContext(), "Definition Saved", Toast.LENGTH_SHORT).show();
                                btnDelete.setVisibility(View.VISIBLE);
                                btnSave.setVisibility(View.GONE);
                            }
                        })
                        .setCancelable(false);

                dialog.create().show();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete Definition")
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
                                owlbotDicitonaryDatabase.deleteDefinition(word, definition);
                                Toast.makeText(getContext(), "Definition Delete", Toast.LENGTH_SHORT).show();
                                btnDelete.setVisibility(View.GONE);
                                btnSave.setVisibility(View.VISIBLE);
                            }
                        })
                        .setCancelable(false);

                dialog.create().show();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}