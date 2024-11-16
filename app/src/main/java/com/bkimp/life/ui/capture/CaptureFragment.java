package com.bkimp.life.ui.capture;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Camera;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.bkimp.life.databinding.FragmentCaptureBinding;
import com.bkimp.life.model.Category;
import com.bkimp.life.model.Disease;
import com.bkimp.life.model.DiseaseDetailActivity;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CaptureFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private  final String[] REQUIRED_PERMISSION = new String[] {"android.permission.CAMERA"};
    private FragmentCaptureBinding binding;

    private PreviewView previewView;
    private TextView textView;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    List<String> image_classes;
    private List<Map<String, Disease>> diseasesList;

    List<Category> categories;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CaptureViewModel captureViewModel =
                new ViewModelProvider(this).get(CaptureViewModel.class);

        binding = FragmentCaptureBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadTreatmentsFromFile();

        previewView = binding.cameraView;
        textView = binding.textCapture;

        if (!checkPermission()){
            ActivityCompat.requestPermissions((Activity) requireContext(), REQUIRED_PERMISSION, REQUEST_CAMERA_PERMISSION);

        }

        loadTorchModule("model.ptl");
        image_classes = LoadClasses("tomato_label.txt");

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCamera(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {

            }
        }, ContextCompat.getMainExecutor(requireContext()));
        return root;
    }

    Executor executor = Executors.newSingleThreadExecutor();

    private void startCamera(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                    .setTargetResolution(new Size(128, 128))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build();

            imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
                @Override
                public void analyze(@NonNull ImageProxy image) {
                    int rotation = image.getImageInfo().getRotationDegrees();
                    analyzeImage(image, rotation);
                    image.close();
                }
            });
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) requireContext(), cameraSelector, preview, imageAnalysis);
    }

    private boolean checkPermission(){
        for (String permission : REQUIRED_PERMISSION){
            if (ActivityCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    Module module;
    void loadTorchModule(String fileName){
        File modelFile = new File(requireContext().getFilesDir(), fileName);
        try {
            if (!modelFile.exists()) {
                InputStream inputStream = requireContext().getAssets().open(fileName);
                FileOutputStream outputStream = new FileOutputStream((modelFile));
                byte[] buffer = new byte[2048];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
            }
            module = LiteModuleLoader.load(modelFile.getAbsolutePath());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isProcessing = false;
    void analyzeImage(ImageProxy image, int rotation) {
        // Show loading message or indicator
        if (isProcessing) {
            // If a detection is already in progress, skip processing this image
            image.close();
            return;
        }
        isProcessing = true;
        mHandler.post(() -> textView.setText("Analyzing..."));

        @SuppressWarnings("UnsafeOptInusageError")
        Tensor inputTensor = TensorImageUtils.imageYUV420CenterCropToFloat32Tensor(image.getImage(), rotation, 224, 224,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);

        Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
        float[] scores = outputTensor.getDataAsFloatArray();
        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;

        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxScoreIdx = i;
            }
        }

        // TODO
        String classResult = image_classes.get(maxScoreIdx % 10);
        Log.v("Torch", "Detected - " + classResult);

        // Update UI on the main thread
        mHandler.post(() -> {
            textView.setText(classResult);

            // Extract category and disease name from classResult string (split by '___')
            String[] parts = classResult.split("___");
            if (parts.length == 2) {
                String category = parts[0]; // First part is the category
                String diseaseName = parts[1]; // Second part is the disease name

                // Set an OnClickListener to navigate to DiseaseDetailActivity
                textView.setOnClickListener(v -> {
                    // Create an intent to go to DiseaseDetailActivity
                    Intent intent = new Intent(requireContext(), DiseaseDetailActivity.class);
                    intent.putExtra("diseaseName", diseaseName);  // Pass the disease name
                    intent.putExtra("category", category);  // Pass the category

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Map<String, Disease>>>() {}.getType();
                    diseasesList = gson.fromJson(new Gson().toJson(getEn(category)), listType);


                    intent.putExtra("disease", getDiseaseByKey(diseaseName));
                    startActivity(intent);
                });
            } else {
                Log.e("CaptureFragment", "Invalid class result format");
            }
        });

        mHandler.postDelayed(() -> {
            isProcessing = false;  // Reset the flag after the delay, allowing next detection
        }, 2000);  // 2000 milliseconds (2 seconds)
    }


    List<String> LoadClasses(String fileName){
        List<String> classes = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(requireContext().getAssets().open(fileName)));
            String line;
            while ((line = br.readLine()) != null) {
                classes.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    void loadTreatmentsFromFile() {
        loadTreatments("treatments/treatment.json"); // assuming your JSON file is named treatments.json
    }

    private void loadTreatments(String fileName) {
        try {
            // Read the JSON from the assets
            InputStream inputStream = requireContext().getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line;

            // Read the file into a string
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // Parse the JSON string into a JsonObject
            String jsonString = sb.toString();
            JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);

            // Assuming the structure where the "Treatments" key is a list of objects
            JsonArray treatments = jsonObject.getAsJsonArray("Treatments");

            categories = new ArrayList<>();
            for (JsonElement treatmentElement : treatments) {
                JsonObject treatmentObject = treatmentElement.getAsJsonObject();

                // Assuming there's a "category", "en" and "iconName" field in each treatment
                String category = treatmentObject.get("category").getAsString();
                String iconName = treatmentObject.get("iconName").getAsString();
                JsonElement en = treatmentObject.getAsJsonArray("en"); // assuming en is an array

                Category categoryObj = new Category(en, iconName, category);
                categories.add(categoryObj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Retrieve "en" treatments for Potato
    private JsonElement getEn(String name) {
        for (Category category : categories) {
            if (name.equals(category.getCategory())) {
                return category.getEn();
            }
        }
        return null; // Return null if Potato is not found
    }

    public Disease getDiseaseByKey(String key) {
        // Loop through the list of maps
        for (Map<String, Disease> diseaseMap : diseasesList) {
            // Check if the current map contains the key
            if (diseaseMap.containsKey(key)) {
                // If the key is found, return the corresponding Disease
                return diseaseMap.get(key);
            }
        }

        // Return null if no matching key is found
        return null;
    }
}

