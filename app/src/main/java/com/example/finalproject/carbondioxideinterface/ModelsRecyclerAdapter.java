package com.example.finalproject.carbondioxideinterface;

import android.annotation.SuppressLint;
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

public class ModelsRecyclerAdapter extends RecyclerView.Adapter<ModelsRecyclerAdapter.ViewHolder> {

    JSONArray array;
    OnMakerSelectedListener makerSelectedListener;

    public ModelsRecyclerAdapter(JSONArray array, OnMakerSelectedListener makerSelectedListener) {
        this.array = array;
        this.makerSelectedListener = makerSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_vehicle_model, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            holder.tvModel.setText(array.getJSONObject(position).getJSONObject("data").getJSONObject("attributes").getString("name"));
            holder.tvYear.setText("Year: "+array.getJSONObject(position).getJSONObject("data").getJSONObject("attributes").getString("year"));
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

        TextView tvModel, tvYear;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModel = itemView.findViewById(R.id.tvModelName);
            tvYear = itemView.findViewById(R.id.tvYear);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}