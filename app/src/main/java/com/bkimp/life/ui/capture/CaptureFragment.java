package com.bkimp.life.ui.capture;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import com.google.common.util.concurrent.ListenableFuture;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CaptureViewModel captureViewModel =
                new ViewModelProvider(this).get(CaptureViewModel.class);

        binding = FragmentCaptureBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        previewView = binding.cameraView;
        textView = binding.textCapture;

        if (!checkPermission()){
            ActivityCompat.requestPermissions((Activity) requireContext(), REQUIRED_PERMISSION, REQUEST_CAMERA_PERMISSION);

        }

        loadTorchModule("model.ptl");
        image_classes = LoadClasses("imagenet-simple-labels.txt");

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
                    .setTargetResolution(new Size(224, 224))
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

    void analyzeImage(ImageProxy image, int rotation) {
        @SuppressWarnings("UnsafeOptInusageError") Tensor inputTensor = TensorImageUtils.imageYUV420CenterCropToFloat32Tensor(image.getImage(), rotation, 224, 224,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);

        Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
        float[] scores = outputTensor.getDataAsFloatArray();
        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;

        for (int i = 0; i < scores.length; i++){
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxScoreIdx = i;
            }
        }

        String classResult = image_classes.get(maxScoreIdx);
        Log.v("Torch", "Detected - " + classResult);
        // anywhere else in your code
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                textView.setText(classResult);

            }
        });

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

}

