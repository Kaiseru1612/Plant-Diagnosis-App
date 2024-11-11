package com.bkimp.life.model;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bkimp.life.R;
import com.google.gson.Gson;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bkimp.life.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Map;

public class TreatmentDetailActivity extends AppCompatActivity {

    private ListView listView;
    private DiseaseAdapter diseaseAdapter;
    private Map<String, Disease> diseaseMap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_detail);

        listView = findViewById(R.id.listView); // Your ListView in the layout

        // Get the category JSON string passed from the previous activity
        String jsonCategory = getIntent().getStringExtra("category");
        Log.d("TreatmentDetailActivity", "Category: " + jsonCategory);
        if (jsonCategory == null) {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
            return;
        }

        // Deserialize the Category object
        Gson gson = new Gson();
        Category category = gson.fromJson(jsonCategory, Category.class);

        // Assuming you are displaying "EarlyBlight" treatments, you can change this based on your need
        diseaseMap = category.getEn();

        // For now, let's check if EarlyBlight exists
        Disease disease = diseaseMap.get("EarlyBlight");

        if (disease == null) {
            Toast.makeText(this, "Disease not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set up the adapter to display the disease treatments
        diseaseAdapter = new DiseaseAdapter(this, disease);
        listView.setAdapter(diseaseAdapter);
    }
}


