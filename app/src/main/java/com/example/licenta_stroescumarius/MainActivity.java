package com.example.licenta_stroescumarius;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.licenta_stroescumarius.helpers.GetPathFromUri;
import com.example.licenta_stroescumarius.models.Translate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.api.client.util.DateTime;

import org.threeten.bp.DateTimeUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;

public class MainActivity extends BaseActivity {

    private MaterialButton uploadBtn;
    private ImageView imgView;
    private MaterialTextView tv_translate;
    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 101;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
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
        uploadBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialogOptions = new AlertDialog.Builder(MainActivity.this);
            View view = getLayoutInflater().inflate(R.layout.material_alert_dialog, null);
            dialogOptions.setView(view);
            dialogOptions.setCancelable(true);
            MaterialButton galerieBtn = view.findViewById(R.id.galerieBtn);
            MaterialButton cameraBtn = view.findViewById(R.id.cameraBtn);
            galerieBtn.setOnClickListener(v1 -> openGallery());
            cameraBtn.setOnClickListener(v2 -> openCamera());
            dialogOptions.create().show();
        });
        imgView = findViewById(R.id.imgView);
        tv_translate = findViewById(R.id.tv_translate);
    }

    private void openCamera() {
        Intent intentOpenCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentOpenCamera.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intentOpenCamera, TAKE_PHOTO);
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PICK_IMAGE);
        }
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    TAKE_PHOTO);
        }
    }

    private void openGallery() {
        Intent intentOpenGallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentOpenGallery.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intentOpenGallery, PICK_IMAGE);

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Translate translate = Translate.getInstance();
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                imageUri = data.getData();
                imgView.setImageURI(imageUri);
                final String path = new GetPathFromUri().getPathFromUri(imageUri, this);
                if (path != null) {
                    translate.findPrediction(path, this);
                }
            }
        }
        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(imageBitmap);
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/", "cameraTaken_"+ Calendar.getInstance().getTime().toString()+".jpg");
            try {
                file.createNewFile();
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();
                translate.findPrediction(file.getPath(), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public TextView getTextView() {
        return this.tv_translate;
    }

}

