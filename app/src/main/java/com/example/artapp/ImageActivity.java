package com.example.artapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artapp.databinding.ActivityImageBinding;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    ActivityImageBinding activityImageBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityImageBinding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(activityImageBinding.getRoot());

        PhotoView photoView = activityImageBinding.imageView2;


        photoView.setMaximumScale(12.25f);
        photoView.setMediumScale(3.5f);
        photoView.setMinimumScale(1.0f);

        photoView.setOnScaleChangeListener((scaleFactor, focusX, focusY) -> {
            float currentScale = photoView.getScale();
            activityImageBinding.textView10.setText(String.format("Scale: %.0f%%", currentScale * 100));
        });

        String artworkId = getIntent().getStringExtra("artworkId");
        String imageId = getIntent().getStringExtra("image_id");
        String title = getIntent().getStringExtra("title");
        String artistDisplay = getIntent().getStringExtra("artist_display");

        activityImageBinding.textView8.setText(title);
        activityImageBinding.textView9.setText(artistDisplay);
        String fullImageUrl = new Artwork(artworkId, imageId, null, null,
                null, null, null, null, null,
                null, title, null, null).getFullImageUrl();
        Picasso.get().load(fullImageUrl).into(activityImageBinding.imageView2);

        activityImageBinding.activityImageView.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}