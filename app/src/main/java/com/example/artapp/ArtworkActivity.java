package com.example.artapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artapp.databinding.ActivityArtworkBinding;
import com.squareup.picasso.Picasso;

public class ArtworkActivity extends AppCompatActivity {

        ActivityArtworkBinding artworkBinding;
        Artwork artwork;

        protected void onCreate(Bundle savedInstance) {
                super.onCreate(savedInstance);
                artworkBinding = ActivityArtworkBinding.inflate(getLayoutInflater());
                setContentView(artworkBinding.getRoot());
                artworkBinding.imageView.setOnClickListener(v -> {
                        Intent intent = new Intent(ArtworkActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                });

                Intent mainIntent = getIntent();
                artworkBinding.galleryTitle.setOnClickListener(view -> {
                        Linkify.addLinks(artworkBinding.galleryTitle, Linkify.WEB_URLS);
                        artworkBinding.galleryTitle.setLinkTextColor(Color.BLACK);

                });


                artworkBinding.galleryTitle.setPaintFlags(
                        artworkBinding.galleryTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                artworkBinding.fullImageView.setOnClickListener(view -> {
                        Intent intent = new Intent(this, ImageActivity.class);
                        intent.putExtra("artworkId",mainIntent.getStringExtra("artworkId"));
                        intent.putExtra("image_id", mainIntent.getStringExtra("image_id"));
                        intent.putExtra("title", mainIntent.getStringExtra("title"));
                        intent.putExtra("artist_display", mainIntent.getStringExtra("artist_display"));
                        startActivity(intent);
                });

                String artworkId = getIntent().getStringExtra("artworkId");
                String title = getIntent().getStringExtra("title");
                String dateDisplay = getIntent().getStringExtra("date_display");
                String artistDisplay = getIntent().getStringExtra("artist_display");
                String imageId = getIntent().getStringExtra("image_id");
                String departmentTitle = getIntent().getStringExtra("department_title");
                String galleryTitle = getIntent().getStringExtra("gallery_title");
                String placeofOrigin = getIntent().getStringExtra("place_of_origin");
                String dimensions = getIntent().getStringExtra("dimensions");
                String credit = getIntent().getStringExtra("credit_line");
                String artWorkTypeTitle = getIntent().getStringExtra("artwork_type_title");
                String mediumDisplay = getIntent().getStringExtra("medium_display");
                String galleryId = getIntent().getStringExtra("gallery_id");

                if (galleryId != null && !galleryId.equals("N/A") && !galleryId.equals("null")) {
                        artworkBinding.galleryTitle.setText(galleryTitle);
                        String galleryUrl = "https://www.artic.edu/galleries/" + galleryId;
                        artworkBinding.galleryTitle.setOnClickListener(view -> {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(galleryUrl));
                                startActivity(browserIntent);
                        });
                        artworkBinding.galleryTitle.setPaintFlags(
                                artworkBinding.galleryTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        artworkBinding.galleryTitle.setTextColor(Color.BLACK);
                } else {
                        artworkBinding.galleryTitle.setText("Not on Display");
                        artworkBinding.galleryTitle.setPaintFlags(0);
                        artworkBinding.galleryTitle.setTextColor(Color.BLACK);
                        artworkBinding.galleryTitle.setClickable(false);
                }

                if (artistDisplay != null && artistDisplay.contains("\n")) {
                        String[] artistParts = artistDisplay.split("\n");
                        artworkBinding.artistDisplay.setText(artistParts[0]);
                        artworkBinding.splitArtistDisplay.setText(artistParts[1]);
                } else {
                        artworkBinding.artistDisplay.setText(artistDisplay);
                        artworkBinding.splitArtistDisplay.setText("");
                }

                String combinedTypeAndMedium = String.format("%s - %s", artWorkTypeTitle, mediumDisplay);
                artworkBinding.textView15.setText(combinedTypeAndMedium);

                String combinedType = artWorkTypeTitle + " - " + mediumDisplay;

                artworkBinding.activityTitle.setText(title);
                artworkBinding.dateDisplay.setText(dateDisplay);
                artworkBinding.departmentTitle.setText(departmentTitle);
                artworkBinding.placeOrigin.setText(placeofOrigin);
                artworkBinding.dimensions.setText(dimensions);
                artworkBinding.credit.setText(credit);
                artworkBinding.textView15.setText(combinedType);

                String fullImageUrl = new Artwork(artworkId, imageId, null, null,
                        null, null, null, null, null,
                        null, title,null,null).getFullImageUrl();
                Picasso.get().load(fullImageUrl).into(artworkBinding.fullImageView);

        }

}

