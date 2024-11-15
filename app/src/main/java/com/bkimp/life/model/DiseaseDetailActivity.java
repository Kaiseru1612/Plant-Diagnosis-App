package com.bkimp.life.model;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bkimp.life.R;

public class DiseaseDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);

        // Retrieve data from Intent
        String diseaseName = getIntent().getStringExtra("diseaseName");
        Disease disease = (Disease) getIntent().getSerializableExtra("disease"); // Or Parcelable

        // Set data in TextViews
        TextView diseaseNameTextView = findViewById(R.id.diseaseNameTextView);
        TextView diseaseDescriptionTextView = findViewById(R.id.diseaseDescriptionTextView);
        TextView organicTreatmentTextView = findViewById(R.id.organicTreatmentTextView);
        TextView conventionalTreatmentTextView = findViewById(R.id.conventionalTreatmentTextView);
        TextView biologicalTreatmentTextView = findViewById(R.id.biologicalTreatmentTextView);

        diseaseNameTextView.setText(diseaseName);
        diseaseDescriptionTextView.setText(disease.getDescription());
        organicTreatmentTextView.setText(disease.getOrganicTreatment().getLongContent());
        conventionalTreatmentTextView.setText(disease.getConventionalTreatment().getLongContent());
        biologicalTreatmentTextView.setText(disease.getBiologicalTreatment().getLongContent());
    }

}
