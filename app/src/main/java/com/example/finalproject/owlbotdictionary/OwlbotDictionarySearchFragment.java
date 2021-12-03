package com.example.finalproject.owlbotdictionary;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwlbotDictionarySearchFragment extends Fragment {

    EditText etSearchWord;
    RecyclerView recyclerSearch;
    Button btnSearch;
    ProgressBar progressSearch;

    public OwlbotDictionarySearchFragment() {
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
        return inflater.inflate(R.layout.fragment_owlbot_dictionary_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearchWord = view.findViewById(R.id.etSearchWord);
        recyclerSearch = view.findViewById(R.id.recyclerSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        progressSearch = view.findViewById(R.id.progressSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchWord(etSearchWord.getText().toString()).execute();
            }
        });

    }

    public class SearchWord extends AsyncTask<Void, Void, Word> {

        String searchWord;

        public SearchWord(String searchWord) {
            this.searchWord = searchWord;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressSearch.setVisibility(View.VISIBLE);
        }

        @Override
        protected Word doInBackground(Void... voids) {

            try {
                URL url = new URL("https://owlbot.info/api/v4/dictionary/"+searchWord);
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Authorization","Token de5338c14f0eef6ebc770fc35f85d34ea8f7b9be");

                InputStream stream = connection.getInputStream();

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                JSONObject response = new JSONObject(responseStrBuilder.toString());
                if(!"".equals(response.optString("message","")))
                    Toast.makeText(getContext(), "Error Fetching Definitions", Toast.LENGTH_SHORT).show();
                else {
                    Word word = new Word();
                    word.setWord(response.getString("word"));
                    word.setPronunciation(response.getString("pronunciation"));
                    JSONArray definitionJsonArray = response.getJSONArray("definitions");
                    List<Definition> definitions = new ArrayList<>();
                    for (int i = 0; i < definitionJsonArray.length(); i++) {
                        Definition definition = new Definition();
                        definition.setDefinition(definitionJsonArray.getJSONObject(i).getString("definition"));
                        definition.setType(definitionJsonArray.getJSONObject(i).getString("type"));
                        definition.setExample(definitionJsonArray.getJSONObject(i).getString("example"));
                        definition.setImageUrl(definitionJsonArray.getJSONObject(i).getString("image_url"));
                        definition.setEmoji(definitionJsonArray.getJSONObject(i).getString("emoji"));
                        definitions.add(definition);
                    }
                    word.setDefinitions(definitions);
                    return word;
                }
            }catch(FileNotFoundException fileNotFound){
                Toast.makeText(getContext(), "Definition not found", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Word word) {
            super.onPostExecute(word);
            Toast.makeText(getContext(), "Search Complete", Toast.LENGTH_SHORT).show();
            progressSearch.setVisibility(View.GONE);
        }
    }


}