package com.example.licenta_stroescumarius;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.licenta_stroescumarius.helpers.GetPathFromUri;
import com.example.licenta_stroescumarius.models.Translate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CAMERA = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private MaterialButton uploadBtn;
    private ImageView imgView;
    private MaterialTextView tv_translate;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private static final String TAG = "MAMA";
    private String prediction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.TabTraducere;
    }

    private void initViews() {
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialogOptions = new MaterialAlertDialogBuilder(MainActivity.this);
                dialogOptions.setCancelable(true);
                final CharSequence[] optiuni = {"Galerie", "Camera Foto"};
                dialogOptions.setItems(optiuni, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch ((optiuni[which]).toString()) {
                            case "Galerie":
                                openGallery();
                                break;
                            case "Camera foto":
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA);
                                openCamera();
                                break;
                        }
                    }
                }).create().show();
            }
        });
        imgView = findViewById(R.id.imgView);
        tv_translate = findViewById(R.id.tv_translate);

    }

    private void openCamera() {
        Intent intentOpenCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intentOpenCamera);
    }

    private void openGallery() {
        Intent intentOpenGallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentOpenGallery, PICK_IMAGE);

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                imageUri = data.getData();
                imgView.setImageURI(imageUri);
                final String path = new GetPathFromUri().getPathFromUri(imageUri, this);
                if (path != null) {
                    Translate translate = Translate.getInstance();
                    translate.findPrediction(path, this);
                }
            }
        }
    }

    public TextView getTextView() {
        return this.tv_translate;
    }

}

