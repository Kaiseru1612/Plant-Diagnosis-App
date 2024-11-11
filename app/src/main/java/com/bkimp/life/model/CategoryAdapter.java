package com.bkimp.life.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bkimp.life.R;
import com.google.gson.Gson;

import java.util.List;

//public class CategoryAdapter extends ArrayAdapter<Category> {
//
//    public CategoryAdapter(Context context, List<Category> categories) {
//        super(context, 0, categories);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
//        }
//
//        Category category = getItem(position);
//
//        TextView categoryName = convertView.findViewById(R.id.categoryName);
//        TextView categoryIcon = convertView.findViewById(R.id.categoryIcon);
//
//        categoryName.setText(category.getCategory());
//        categoryIcon.setText(category.getIconName());
//
//        return convertView;
//    }
//}

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        }

        Category category = getItem(position);

        TextView categoryName = convertView.findViewById(R.id.categoryName);
        TextView categoryIcon = convertView.findViewById(R.id.categoryIcon);

        categoryName.setText(category.getCategory());
        categoryIcon.setText(category.getIconName());
        Log.d("TreatmentListAdapter", "EN Treatments Object: " + category.getEn());


        // Set click listener to open detail screen
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TreatmentDetailActivity.class);
            intent.putExtra("category", category.getCategory());
            Log.d("TreatmentListAdapter", "Category: " + category.getCategory());
            intent.putExtra("iconName", category.getIconName());
            Log.d("TreatmentListAdapter", "Icon Name: " + category.getIconName());
            Log.d("TreatmentListAdapter", "EN Treatments: " + new Gson().toJson(category.getEn()));
            intent.putExtra("enTreatments", new Gson().toJson(category.getEn())); // Passing English treatments as JSON string
//            intent.putExtra("vnTreatments", new Gson().toJson(category.getVn())); // Passing Vietnamese treatments as JSON string
            getContext().startActivity(intent);
        });

        return convertView;
    }
}
