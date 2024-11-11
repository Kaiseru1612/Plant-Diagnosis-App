package com.bkimp.life.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bkimp.life.R;

public class DiseaseAdapter extends ArrayAdapter<String> {

    private Context context;
    private Disease disease;

    public DiseaseAdapter(Context context, Disease disease) {
        super(context, R.layout.list_item_treatment); // Custom layout for the list item
        this.context = context;
        this.disease = disease;
    }

    @Override
    public int getCount() {
        return 4;  // We have 4 treatment categories: Description, Organic, Conventional, Biological
    }

    @Override
    public String getItem(int position) {
        switch (position) {
            case 0:
                return disease.getDescription();
            case 1:
                return disease.getOrganicTreatment().getTitle() + "\n" + disease.getOrganicTreatment().getShortContent();
            case 2:
                return disease.getConventionalTreatment().getTitle() + "\n" + disease.getConventionalTreatment().getShortContent();
            case 3:
                return disease.getBiologicalTreatment().getTitle() + "\n" + disease.getBiologicalTreatment().getShortContent();
            default:
                return "";
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_treatment, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textViewTreatment);
        textView.setText(getItem(position));

        return convertView;
    }
}

