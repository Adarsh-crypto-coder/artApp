package com.example.artapp;

import androidx.recyclerview.widget.RecyclerView;

import com.example.artapp.databinding.ActivityArtworkListEntryBinding;
import com.squareup.picasso.Picasso;

public class ArtworkViewHolder extends RecyclerView.ViewHolder {
    ActivityArtworkListEntryBinding activityArtworkListEntryBinding;

    ArtworkViewHolder(ActivityArtworkListEntryBinding binding){
        super(binding.getRoot());
        this.activityArtworkListEntryBinding = binding;
    }

    public void bind(Artwork artwork){
        activityArtworkListEntryBinding.titleTextView.setText(artwork.getTitle());
        Picasso.get().load(artwork.getThumbnailUrl()).into(activityArtworkListEntryBinding.imageEntry);
    }
}
