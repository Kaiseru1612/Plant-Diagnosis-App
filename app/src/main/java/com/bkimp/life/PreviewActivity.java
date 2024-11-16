package com.bkimp.life;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PreviewActivity extends AppCompatActivity {

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        // Get the image path from the Intent
        imagePath = getIntent().getStringExtra("imagePath");

        ImageView previewImageView = findViewById(R.id.previewImageView);
        Button detectButton = findViewById(R.id.detectButton);
        Button retakeButton = findViewById(R.id.retakeButton);

        // Load and display the image
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        previewImageView.setImageBitmap(bitmap);

        // Set up button listeners
        detectButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("imagePath", imagePath);
            setResult(RESULT_OK, intent);
            finish(); // Return to the previous activity
        });

        retakeButton.setOnClickListener(view -> finish()); // Close preview, allowing retake
    }
}

