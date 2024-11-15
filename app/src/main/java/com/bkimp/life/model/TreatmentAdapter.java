package com.bkimp.life.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bkimp.life.R;

import java.util.List;
import java.util.Map;

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.ViewHolder> {
    private static final String TAG = "TreatmentAdapter";
    private List<Map<String, Disease>> diseasesList;

    public TreatmentAdapter(List<Map<String, Disease>> diseasesList) {
        this.diseasesList = diseasesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout and return the ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_treatment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the disease map at the current position
        Map<String, Disease> diseaseMap = diseasesList.get(position);
        Log.d(TAG, "onBindViewHolder: " + diseasesList.get(position));
        // Iterate over the map entries, since each item is a map of disease name -> Disease
        for (Map.Entry<String, Disease> entry : diseaseMap.entrySet()) {
            String diseaseName = entry.getKey();
            Disease disease = entry.getValue();

            // Set data to the view elements, for example:
            holder.diseaseNameTextView.setText(diseaseName);
            holder.diseaseDescriptionTextView.setText(disease.getDescription());
            // Add other fields as necessary
        }
    }

    @Override
    public int getItemCount() {
        return diseasesList.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView diseaseNameTextView, diseaseDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            // Ensure the IDs match those defined in item_treatment.xml
            diseaseNameTextView = itemView.findViewById(R.id.diseaseNameTextView);
            diseaseDescriptionTextView = itemView.findViewById(R.id.diseaseDescriptionTextView);

            // Log to check if the TextViews are correctly initialized
            if (diseaseNameTextView == null || diseaseDescriptionTextView == null) {
                Log.e("ViewHolder", "TextViews are not initialized properly.");
            }
        }
    }



}

