package com.bkimp.life.model;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.bkimp.life.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiseaseDetailActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private TextView diseaseTitle;
    private CustomExpandableListAdapter customExpandableListAdapter;
    private List<String> treatmentTitles;
    private HashMap<String, List<String>> treatmentDetails;
    String diseaseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);

        // Retrieve the disease name from the Intent
        diseaseName = getIntent().getStringExtra("diseaseName");
        String category = getIntent().getStringExtra("category");

        if (diseaseName == null || category == null) {
            Log.e("DiseaseDetailActivity", "Disease name or category is null!");
            return;
        }

        // Set the disease name to the TextView
        TextView diseaseNameTextView = findViewById(R.id.diseaseNameTextView);
        diseaseNameTextView.setText(diseaseName);

        ViewFlipper viewFlipper = findViewById(R.id.viewFlipper);

        // If you want to add more images programmatically
        if (viewFlipper != null) {
            loadCategoryImages(category, viewFlipper);
        }

        // Retrieve the Disease object if needed
        Disease disease = (Disease) getIntent().getSerializableExtra("disease");

        // Set up expandable list if disease object is not null
        if (disease != null) {
            TextView diseaseDescriptionTextView = findViewById(R.id.DiseaseDescription);
            diseaseDescriptionTextView.setText(disease.getDescription());
            setupExpandableList(disease);
        }
    }


    private void setupExpandableList(Disease disease) {
        // Parent list (titles)
        List<String> parentList = Arrays.asList(
                "Organic Treatment",
                "Conventional Treatment",
                "Biological Treatment"
        );

        // Child list (details)
        HashMap<String, List<String>> childList = new HashMap<>();

        childList.put("Organic Treatment", Arrays.asList(
                disease.getOrganicTreatment().getTitle(),
                disease.getOrganicTreatment().getShortContent(),
                disease.getOrganicTreatment().getLongContent()
        ));

        childList.put("Conventional Treatment", Arrays.asList(
                disease.getConventionalTreatment().getTitle(),
                disease.getConventionalTreatment().getShortContent(),
                disease.getConventionalTreatment().getLongContent()
        ));

        childList.put("Biological Treatment", Arrays.asList(
                disease.getBiologicalTreatment().getTitle(),
                disease.getBiologicalTreatment().getShortContent(),
                disease.getBiologicalTreatment().getLongContent()
        ));

        // Link data to the expandable list view
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(this, parentList, childList);
        expandableListView.setAdapter(adapter);
    }

    private void loadCategoryImages(String category, ViewFlipper viewFlipper) {
        AssetManager assetManager = getAssets();

        try {
            // Define the path to images for the given category
            String imagePath = "images/plant/";
            String categoryPath = imagePath + category + "/";
            String diseasePath = categoryPath + diseaseName + "/";
            // Get the list of image files in the category directory
            String[] imageFiles = assetManager.list(diseasePath);
            Log.e("DiseaseDetailActivity", "diseasePath: " + diseasePath);
            if (imageFiles != null) {
                for (String imageFile : imageFiles) {
                    // Create a new ImageView for each image and add it to the ViewFlipper
                    ImageView imageView = new ImageView(this);
                    // Open the image from the assets folder
                    imageView.setImageDrawable(Drawable.createFromStream(assetManager.open(diseasePath + imageFile), null));
                    viewFlipper.addView(imageView);
                    Log.e("DiseaseDetailActivity", "Images found for category: " + imageFile);
                }
            } else {
                Log.e("DiseaseDetailActivity", "No images found for category: " + category);
            }
        } catch (IOException e) {
            Log.e("DiseaseDetailActivity", "Error loading images from assets", e);
        }
    }
}

