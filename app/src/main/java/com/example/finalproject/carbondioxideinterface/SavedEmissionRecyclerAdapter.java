package com.example.finalproject.carbondioxideinterface;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class SavedEmissionRecyclerAdapter extends RecyclerView.Adapter<SavedEmissionRecyclerAdapter.ViewHolder> {

    List<CarbonEmissionEstimate> emissionEstimateList;
    OnEmissionSelectedListener onEmissionSelectedListener;

    public SavedEmissionRecyclerAdapter(List<CarbonEmissionEstimate> emissionEstimateList, OnEmissionSelectedListener onEmissionSelectedListener) {
        this.emissionEstimateList = emissionEstimateList;
        this.onEmissionSelectedListener = onEmissionSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_saved_carbon_emission, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvMaker.setText(emissionEstimateList.get(position).getVehicleMaker());
        holder.tvModel.setText(emissionEstimateList.get(position).getVehicleModel());
        holder.tvDistance.setText(emissionEstimateList.get(position).getDistance()+" "+emissionEstimateList.get(position).getDistanceUnit());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEmissionSelectedListener.onEmissionSelected(emissionEstimateList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return emissionEstimateList.size();
    }

    public interface OnEmissionSelectedListener{
        public void onEmissionSelected(CarbonEmissionEstimate estimate);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvMaker, tvModel, tvDistance;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaker = itemView.findViewById(R.id.tvVehicleMaker);
            tvModel = itemView.findViewById(R.id.tvVehicleModel);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            card = itemView.findViewById(R.id.card);
        }
    }
}
