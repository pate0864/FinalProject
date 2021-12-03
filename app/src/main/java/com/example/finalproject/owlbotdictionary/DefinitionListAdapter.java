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

public class DefinitionListAdapter extends RecyclerView.Adapter<DefinitionListAdapter.ViewHolder> {

    List<Definition> definitionList;
    OnDefinitionClickListener onDefinitionClickListener;

    public DefinitionListAdapter(List<Definition> definitionList, OnDefinitionClickListener onDefinitionClickListener) {
        this.definitionList = definitionList;
        this.onDefinitionClickListener = onDefinitionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_definition, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvDefinition.setText(definitionList.get(position).getDefinition());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDefinitionClickListener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return definitionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvDefinition;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDefinition = itemView.findViewById(R.id.tvDefinition);
            card = itemView.findViewById(R.id.card);
        }
    }
}
