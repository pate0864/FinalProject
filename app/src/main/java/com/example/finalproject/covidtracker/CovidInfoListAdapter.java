package com.example.finalproject.covidtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class CovidInfoListAdapter extends RecyclerView.Adapter<CovidInfoListAdapter.MyViewHolder> {

    List<CovidInfo> list;
    Context context;

    public CovidInfoListAdapter(List<CovidInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.covid_info_list_item, null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textCases.setText(""+list.get(position).getChangeCases());
        holder.textRecoveries.setText(""+list.get(position).getChangeRecoveries());
        holder.textFatalities.setText(""+list.get(position).getChangeFatalities());
        holder.textDate.setText(list.get(position).getDate());
        holder.cardView.setOnClickListener(v->{
            ((CovidInfoTrackerActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, CovidInfoDetailFragment.newInstance(list.get(position)))
                    .addToBackStack("details")
                    .commit();
        });
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textDate, textCases, textFatalities, textRecoveries;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.textDate);
            textCases = itemView.findViewById(R.id.textChangeCases);
            textFatalities = itemView.findViewById(R.id.textChangeFatalities);
            textRecoveries = itemView.findViewById(R.id.textChangeRecoveries);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
