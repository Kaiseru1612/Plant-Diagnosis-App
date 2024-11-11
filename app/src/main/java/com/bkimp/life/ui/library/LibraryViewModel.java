package com.bkimp.life.ui.library;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bkimp.life.model.Category;
import com.bkimp.life.model.DataWrapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LibraryViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Category>> categories;

    public LibraryViewModel(Application application) {
        super(application);
        categories = new MutableLiveData<>();
        loadDataFromJson();
    }

    private void loadDataFromJson() {
        Context context = getApplication();
        AssetManager assetManager = context.getAssets();

        try (InputStream is = assetManager.open("treatments/treatment.json")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            String json = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            DataWrapper dataWrapper = gson.fromJson(json, DataWrapper.class);
            categories.setValue(dataWrapper.getTreatments());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }
}
