package com.example.finalproject.owlbotdictionary;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class SavedDefinitionListAdapter extends RecyclerView.Adapter<SavedDefinitionListAdapter.ViewHolder> {

    List<Word> words;
    OnDefinitionClickListener onDefinitionClickListener;

    public SavedDefinitionListAdapter(List<Word> words, OnDefinitionClickListener onDefinitionClickListener) {
        this.words = words;
        this.onDefinitionClickListener = onDefinitionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_saved_definition, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvWord.setText(words.get(position).getWord());
        holder.tvDefinition.setText(words.get(position).getDefinitions().get(0).getDefinition());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDefinitionClickListener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvDefinition, tvWord;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvDefinition = itemView.findViewById(R.id.tvDefinition);
            card = itemView.findViewById(R.id.card);
        }
    }
}
