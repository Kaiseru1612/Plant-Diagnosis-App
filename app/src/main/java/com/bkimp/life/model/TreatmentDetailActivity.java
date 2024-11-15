package com.bkimp.life.model;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bkimp.life.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkimp.life.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Map;

//public class TreatmentDetailActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_treatment_detail);
//
//        // Get the JSON data from the Intent
//        String json = getIntent().getStringExtra("enTreatments");
//
//        if (json != null && !json.isEmpty()) {
//            try {
//                // Deserialize the JSON into the TreatmentData object
//                Gson gson = new Gson();
//                TreatmentData treatmentData = gson.fromJson(json, TreatmentData.class);
//
//                // Log the entire treatment data to verify
//                Log.d("TreatmentDetailActivity", "Received Treatment Data: " + treatmentData);
//
//                // Now you can access the treatments
//                if (treatmentData != null && treatmentData.getTreatments() != null) {
//                    for (Treatment treatment : treatmentData.getTreatments()) {
//                        // Log category
//                        Log.d("Treatment", "Category: " + treatment.getCategory());
//
//                        // Log EN Treatment (Ensure TreatmentDetails has a toString method)
//                        TreatmentDetail enTreatment = (TreatmentDetail) treatment.getEn();
//                        if (enTreatment != null) {
//                            Log.d("Treatment", "EN Treatment: " + enTreatment);
//                        } else {
//                            Log.e("Treatment", "EN Treatment is null");
//                        }
//                    }
//                } else {
//                    Log.e("TreatmentDetailActivity", "Treatments list is null");
//                }
//            } catch (JsonSyntaxException e) {
//                Log.e("TreatmentDetailActivity", "Error parsing JSON", e);
//            }
//        } else {
//            Log.e("TreatmentDetailActivity", "No treatment data available");
//        }
//    }
//}
//public class TreatmentDetailActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_treatment_detail);
//
//        // Get the JSON data from the Intent
//        String json = getIntent().getStringExtra("enTreatments");
//
//        if (json != null && !json.isEmpty()) {
//            try {
//                // Deserialize the JSON into the TreatmentData object
//                Gson gson = new Gson();
//                TreatmentData treatmentData = gson.fromJson(json, TreatmentData.class);
//
//                // Log the entire treatment data to verify
//                Log.d("TreatmentDetailActivity", "Received Treatment Data: " + treatmentData);
//
//                // Now you can access the treatments
//                if (treatmentData != null && treatmentData.getTreatments() != null) {
//                    for (Treatment treatment : treatmentData.getTreatments()) {
//                        // Log category
//                        Log.d("Treatment", "Category: " + treatment.getCategory());
//
//                        // Log EN Treatment (Ensure TreatmentDetails has a toString method)
//                        TreatmentDetails enTreatment = (TreatmentDetails) treatment.getEn();
//                        if (enTreatment != null) {
//                            Log.d("Treatment", "EN Treatment: " + enTreatment);
//                        } else {
//                            Log.e("Treatment", "EN Treatment is null");
//                        }
//                    }
//                } else {
//                    Log.e("TreatmentDetailActivity", "Treatments list is null");
//                }
//            } catch (JsonSyntaxException e) {
//                Log.e("TreatmentDetailActivity", "Error parsing JSON", e);
//            }
//        } else {
//            Log.e("TreatmentDetailActivity", "No treatment data available");
//        }
//    }
//}

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TreatmentDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Map<String, Disease>> diseasesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_detail);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use a vertical layout

        // Get the JSON data from the Intent
        String json = getIntent().getStringExtra("enTreatments");

        if (json != null && !json.isEmpty()) {
            try {
                // Deserialize JSON into List<Map<String, Disease>>
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Map<String, Disease>>>() {}.getType();
                diseasesList = gson.fromJson(json, listType);

                if (diseasesList != null && !diseasesList.isEmpty()) {
                    // Create and set the adapter with the data
                    TreatmentAdapter adapter = new TreatmentAdapter(diseasesList);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener((diseaseName, disease) -> {
                        Intent intent = new Intent(this, DiseaseDetailActivity.class);
                        intent.putExtra("diseaseName", diseaseName);
                        intent.putExtra("disease", disease); // Disease must implement Serializable or Parcelable
                        startActivity(intent);
                    });
                } else {
                    Log.e("TreatmentDetailActivity", "Diseases list is empty or null");
                    Toast.makeText(this, "No diseases data available", Toast.LENGTH_SHORT).show();
                }

            } catch (JsonSyntaxException e) {
                Log.e("TreatmentDetailActivity", "Error parsing JSON", e);
            }
        } else {
            Log.e("TreatmentDetailActivity", "No treatment data available");
            Toast.makeText(this, "No treatment data available", Toast.LENGTH_SHORT).show();
        }
    }

}




