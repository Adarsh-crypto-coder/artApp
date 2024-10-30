package com.example.artapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.util.Linkify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artapp.databinding.ActivityCopyrightBinding;

public class CopyRightActivity extends AppCompatActivity {
    @NonNull ActivityCopyrightBinding imageBinding;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        imageBinding = ActivityCopyrightBinding.inflate(getLayoutInflater());
        setContentView(imageBinding.getRoot());

        imageBinding.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(CopyRightActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        imageBinding.textView5.setPaintFlags(imageBinding.textView5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        imageBinding.textView6.setPaintFlags(imageBinding.textView6.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            Linkify.addLinks(imageBinding.textView5, Linkify.WEB_URLS);
            imageBinding.textView5.setLinkTextColor(Color.BLUE);


            Linkify.addLinks(imageBinding.textView6, Linkify.WEB_URLS);
            imageBinding.textView6.setLinkTextColor(Color.BLUE);


    }
}
