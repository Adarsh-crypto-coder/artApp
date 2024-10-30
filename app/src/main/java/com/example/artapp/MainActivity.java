package com.example.artapp;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.artapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    private RequestQueue requestQueue;
    private static final String artURL = "https://api.artic.edu/api/v1/artworks/search";
    private ArtworkAdapter adapter;
    private List<Artwork> artworks = new ArrayList<>();
    private static final String GALLERIES_URL = "https://api.artic.edu/api/v1/galleries";
    private Set<String> galleryIds = new HashSet<>();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.copyrightTV.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CopyRightActivity.class);
            startActivity(intent);
        });
        requestQueue = Volley.newRequestQueue(this);

        adapter = new ArtworkAdapter(artworks, this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.copyrightTV.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CopyRightActivity.class);
            startActivity(intent);
        });

        binding.search.setOnClickListener(view -> {
            binding.recyclerView.setBackground(null);
            performSearch();
        });
        binding.clearSearch.setOnClickListener(v -> binding.editText.setText(""));

        binding.random.setOnClickListener(v ->{
            binding.recyclerView.setBackground(null);
            fetchRandomArtwork();
        });

        binding.copyrightTV.setPaintFlags(
                binding.copyrightTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    private void fetchRandomArtwork() {
        if (galleryIds.isEmpty()) {
            fetchGalleryIds(1);
        } else {
            selectRandomGallery();
        }
    }

    private void fetchGalleryIds(int page) {
        binding.progressBar.setVisibility(View.GONE);
        Uri.Builder builder  = Uri.parse(GALLERIES_URL).buildUpon();
                builder.appendQueryParameter("limit", "100");
                builder.appendQueryParameter("fields", "id");
                builder.appendQueryParameter("page", String.valueOf(page));
                String url = builder.build().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        JSONObject pagination = response.getJSONObject("pagination");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject gallery = data.getJSONObject(i);
                            galleryIds.add(gallery.getString("id"));
                        }
                        int currentPage = pagination.getInt("current_page");
                        int totalPages = pagination.getInt("total_pages");
                        if (currentPage < totalPages) {
                            fetchGalleryIds(currentPage + 1);
                        } else {
                            selectRandomGallery();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing gallery IDs: " + e.getMessage());
                        Toast.makeText(MainActivity.this, "Error fetching galleries", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching gallery IDs: " + error.toString());
                    Toast.makeText(MainActivity.this, "Error fetching galleries", Toast.LENGTH_SHORT).show();
                });
        requestQueue.add(request);
    }

    private void selectRandomGallery() {
        String[] galleryArray = galleryIds.toArray(new String[0]);
        String randomGalleryId = galleryArray[random.nextInt(galleryArray.length)];
        fetchArtworksFromGallery(randomGalleryId);
    }

    private void fetchArtworksFromGallery(String galleryId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Uri.Builder urlBuilder2 = Uri.parse("https://api.artic.edu/api/v1/artworks/search").buildUpon();
                urlBuilder2.appendQueryParameter("query[term][gallery_id]", galleryId);
                urlBuilder2.appendQueryParameter("limit", "100");
                urlBuilder2.appendQueryParameter("fields", "title,date_display,artist_display,medium_display," +
                        "artwork_type_title,image_id,dimensions,department_title,credit_line," +
                        "place_of_origin,gallery_title,gallery_id,id");
                String url3 = urlBuilder2.build().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url3, null,
                response -> {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        if (data.length() == 0) {
                            selectRandomGallery();
                            return;
                        }
                        JSONObject artworkJson = data.getJSONObject(random.nextInt(data.length()));
                        Artwork randomArtwork = new Artwork(
                                artworkJson.getString("id"),
                                artworkJson.getString("image_id"),
                                artworkJson.optString("gallery_id", "N/A"),
                                artworkJson.optString("gallery_title", "N/A"),
                                artworkJson.optString("department_title", "N/A"),
                                artworkJson.optString("credit_line", "N/A"),
                                artworkJson.optString("dimensions", "N/A"),
                                artworkJson.optString("medium_display", "N/A"),
                                artworkJson.optString("date_display", "N/A"),
                                artworkJson.optString("artist_display", "N/A"),
                                artworkJson.getString("title"),
                                artworkJson.optString("place_of_origin", "N/A"),
                                artworkJson.optString("artwork_type_title", "N/A")
                        );

                        List<Artwork> randomArtworkList = new ArrayList<>();
                        randomArtworkList.add(randomArtwork);
                        updateData(randomArtworkList);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing artwork data: " + e.getMessage());
                        Toast.makeText(MainActivity.this, "Error fetching artwork", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching artwork: " + error.toString());
                    Toast.makeText(MainActivity.this, "Error fetching artwork", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(request);
    }

    private void performSearch() {
        String query = binding.editText.getText().toString().trim();
        if (query.length() < 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Search string too short")
                    .setMessage("Please try a longer search string")
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("Ok", (dialog, which) -> {
                    })
                    .show();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        downloadArtworks(query);
    }

    private void downloadArtworks(String query) {
        Uri.Builder uriBuilder = Uri.parse(artURL).buildUpon();
                uriBuilder.appendQueryParameter("q", query);
                uriBuilder.appendQueryParameter("limit", "15");
                uriBuilder.appendQueryParameter("page","1");
                uriBuilder.appendQueryParameter("fields", "id,title,image_id,gallery_id,gallery_title,department_title,credit_line," +
                        "dimensions,medium_display,date_display,artist_display,place_of_origin,artwork_type_title");
                String url1 = uriBuilder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
                response -> {
                    Log.d(TAG, "Response: " + response.toString());
                    parseJSON(response);
                },
                error -> {
                    Log.d(TAG, "Error: " + error.toString());
                    updateData(null);
                });

        requestQueue.add(jsonObjectRequest);
    }


    private void parseJSON(JSONObject response) {
        try {
            JSONArray dataArray = response.getJSONArray("data");
            List<Artwork> artworkList = new ArrayList<>();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject artworkJson = dataArray.getJSONObject(i);
                String id = artworkJson.getString("id");
                String title = artworkJson.getString("title");
                String imageId = artworkJson.getString("image_id");
                String galleryId = artworkJson.optString("gallery_id", "N/A");
                String galleryTitle = artworkJson.optString("gallery_title", "N/A");
                String departmentTitle = artworkJson.optString("department_title", "N/A");
                String creditLine = artworkJson.optString("credit_line", "N/A");
                String dimensions = artworkJson.optString("dimensions", "N/A");
                String mediumDisplay = artworkJson.optString("medium_display", "N/A");
                String dateDisplay = artworkJson.optString("date_display", "N/A");
                String artistDisplay = artworkJson.optString("artist_display", "N/A");
                String placeOfOrigin = artworkJson.optString("place_of_origin","N/A");
                String artWorkTypeTitle = artworkJson.optString("artwork_type_title","N/A");

                Artwork artwork = new Artwork(id, imageId, galleryId, galleryTitle, departmentTitle, creditLine,
                        dimensions, mediumDisplay, dateDisplay, artistDisplay, title,placeOfOrigin,artWorkTypeTitle);
                artworkList.add(artwork);
            }
            updateData(artworkList);
        } catch (JSONException e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            updateData(null);
        }
    }

    private void updateData(List<Artwork> artworkList) {
        binding.progressBar.setVisibility(View.GONE);
        if (artworkList != null && !artworkList.isEmpty()) {
            artworks.clear();
            artworks.addAll(artworkList);
            adapter.resetAnimation();
            adapter.notifyDataSetChanged();
            binding.recyclerView.setAlpha(1.0f);
            binding.recyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setAlpha(0.4f);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No search results found")
                    .setMessage("No results found for '" + binding.editText.getText().toString().trim() + "'. Please try another search string")
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("Ok", (dialog, which) -> {
                    })
                    .show();
        }
    }

    @Override
    public void onClick(View view) {
        int pos = binding.recyclerView.getChildAdapterPosition(view);
        Artwork current = artworks.get(pos);
        Intent intent = new Intent(this,ArtworkActivity.class);
        intent.putExtra("artworkId",current.getId());
        intent.putExtra("title",current.getTitle());
        intent.putExtra("date_display",current.getDateDisplay());
        intent.putExtra("artist_display",current.getArtistDisplay());
        intent.putExtra("image_id",current.getImageId());
        intent.putExtra("department_title",current.getDepartmentTitle());
        intent.putExtra("gallery_title",current.getGalleryTitle());
        intent.putExtra("dimensions",current.getDimensions());
        intent.putExtra("credit_line",current.getCreditLine());
        intent.putExtra("place_of_origin",current.getPlaceOfOrigin());
        intent.putExtra("artwork_type_title",current.getArtworkTypeTitle());
        intent.putExtra("medium_display",current.getMediumDisplay());
        intent.putExtra("gallery_id",current.getGalleryId());
        startActivity(intent);
    }
}