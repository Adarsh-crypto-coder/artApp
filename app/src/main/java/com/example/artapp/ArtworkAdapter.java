package com.example.artapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artapp.databinding.ActivityArtworkListEntryBinding;

import java.util.List;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkViewHolder> {
    private final List<Artwork> artworkList;
    private final MainActivity mainActivity;
    private final RecyclerViewAnimation animation;

    public ArtworkAdapter(List<Artwork> userInfos, MainActivity mainAct){
        this.artworkList = userInfos;
        this.mainActivity = mainAct;
        this.animation = new RecyclerViewAnimation(mainAct);
    }
    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityArtworkListEntryBinding binding = ActivityArtworkListEntryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ArtworkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder holder, int position) {
        Artwork artwork = artworkList.get(position);
        holder.bind(artwork);
        holder.itemView.setOnClickListener(mainActivity);
        animation.setAnimation(holder, position);
    }

    public void resetAnimation() {
        animation.resetLastPosition();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ArtworkViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return artworkList.size();
    }
}
