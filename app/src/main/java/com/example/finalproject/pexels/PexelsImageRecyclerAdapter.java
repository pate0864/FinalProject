package com.example.finalproject.pexels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class PexelsImageRecyclerAdapter extends RecyclerView.Adapter<PexelsImageRecyclerAdapter.MyView> {

    List<PexelImage> images;
    Context context;
    OnClickImageListener onClickImageListener;

    public PexelsImageRecyclerAdapter(List<PexelImage> images, Context context, OnClickImageListener onClickImageListener) {
        this.images = images;
        this.context = context;
        this.onClickImageListener = onClickImageListener;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pexels_image_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        holder.textViewPhotographer.setText(images.get(position).getPhotographer());
        if("".equals(images.get(position).getSavePath()) || images.get(position).getSavePath() == null)
            new FetchImage(images.get(position).getMediumUrl(), holder.imageView, holder.progressImage).execute();
        else{
            holder.progressImage.setVisibility(View.GONE);
            holder.imageView.setImageBitmap(ImageUtils.getImageFromGallery(images.get(position).getSavePath(), context));
        }
        holder.card.setOnClickListener(v->{
            onClickImageListener.onImageClick(images.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static interface OnClickImageListener{
        void onImageClick(PexelImage image);
    }

    class MyView extends RecyclerView.ViewHolder{

        TextView textViewPhotographer;
        ImageView imageView;
        ProgressBar progressImage;
        CardView card;

        public MyView(@NonNull View itemView) {
            super(itemView);
            textViewPhotographer = itemView.findViewById(R.id.textViewPhotographer);
            imageView = itemView.findViewById(R.id.imageViewPhoto);
            progressImage = itemView.findViewById(R.id.progressImage);
            card = itemView.findViewById(R.id.card);
        }
    }
}
