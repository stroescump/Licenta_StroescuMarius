package com.example.licenta_stroescumarius;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.licenta_stroescumarius.helpers.GetPathFromUri;
import com.example.licenta_stroescumarius.models.Translate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class MainActivity extends BaseActivity {

    private MaterialButton uploadBtn;
    private ImageView imgView;
    private ImageView braille_animation;
    private MaterialTextView tv_translate;
    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 101;
    private Uri imageUri;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        initViews();
        loadAnimations();
    }

    private void loadAnimations() {
        new Runnable() {
            @Override
            public void run() {
                AnimatedVectorDrawableCompat avd1 = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.avd_braille);
                braille_animation.setImageDrawable(avd1);
                avd1.start();
                avd1.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        super.onAnimationEnd(drawable);
                        avd1.start();
                    }
                });
            }
        }.run();

        new Runnable() {
            @Override
            public void run() {
                Drawable drawable = uploadBtn.getIcon();
                if (drawable instanceof AnimatedVectorDrawable) {
                    AnimatedVectorDrawable avd = (AnimatedVectorDrawable) drawable;
                    avd.start();
                }
            }
        }.run();
    }

    private void changeStrokeWidth(MaterialButton btn) {
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();

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
        braille_animation = findViewById(R.id.braille_animation);
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialogOptions = new AlertDialog.Builder(MainActivity.this);
            View view = getLayoutInflater().inflate(R.layout.material_alert_dialog, null);
            dialogOptions.setView(view);
            dialogOptions.setCancelable(true);
            MaterialButton galerieBtn = view.findViewById(R.id.galerieBtn);
            MaterialButton cameraBtn = view.findViewById(R.id.cameraBtn);
            AlertDialog referenceToDialog = dialogOptions.create();
            galerieBtn.setOnClickListener(v1 -> {
                referenceToDialog.dismiss();
                openGallery();
            });
            cameraBtn.setOnClickListener(v1 -> {
                referenceToDialog.dismiss();
                openCamera();
            });
            referenceToDialog.show();
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
            File directory = new File(Environment.getExternalStorageDirectory() + "/Downloads/Braille_To_Text/");
            directory.mkdirs();
            File file = new File(Environment.getExternalStorageDirectory() + "/Downloads/Braille_To_Text/", "cameraTaken_" + Calendar.getInstance().getTime().toString() + ".jpg");
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

