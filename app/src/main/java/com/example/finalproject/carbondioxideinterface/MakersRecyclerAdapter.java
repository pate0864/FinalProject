package com.example.finalproject.carbondioxideinterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import org.json.JSONArray;
import org.json.JSONException;

public class MakersRecyclerAdapter extends RecyclerView.Adapter<MakersRecyclerAdapter.ViewHolder> {

    JSONArray array;
    OnMakerSelectedListener makerSelectedListener;

    public MakersRecyclerAdapter(JSONArray array, OnMakerSelectedListener makerSelectedListener) {
        this.array = array;
        this.makerSelectedListener = makerSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_vehicle_maker, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.tvMaker.setText(array.getJSONObject(position).getJSONObject("data").getJSONObject("attributes").getString("name"));
            holder.tvNumberOfModel.setText("Number of Models: "+array.getJSONObject(position).getJSONObject("data").getJSONObject("attributes").getString("number_of_models"));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        makerSelectedListener.onMakerSelected(array.getJSONObject(position).getJSONObject("data").getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public interface OnMakerSelectedListener{
        public void onMakerSelected(String id);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvMaker, tvNumberOfModel;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaker = itemView.findViewById(R.id.tvMakerName);
            tvNumberOfModel = itemView.findViewById(R.id.tvNumberOfModel);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
