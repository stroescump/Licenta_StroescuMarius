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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.licenta_stroescumarius.com.example.licenta_stroescumarius.helpers.GetPathFromUri;
import com.example.licenta_stroescumarius.com.example.licenta_stroescumarius.helpers.NetworkClient;
import com.example.licenta_stroescumarius.com.example.licenta_stroescumarius.helpers.UploadApis;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_main);
        initViews();

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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

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
                    findPrediction(path);
                }
            }
        }
    }

    private void findPrediction(String fileLocation) {
        File file = new File(fileLocation);
        RequestBody imgPart = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part parts = MultipartBody.Part
                .createFormData("img_predict", "img_predict.png", imgPart);
        Retrofit retrofit = NetworkClient.getRetrofit();
        UploadApis uploadApis = retrofit.create(UploadApis.class);
        Call<ResponseBody> call = uploadApis.uploadImage(parts);
        try {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call call, Response response) {

                    ResponseBody responseBody = (ResponseBody) response.body();
                    if (responseBody != null) {
                        try {
                            tv_translate.setText(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                        Toast.makeText(MainActivity.this, "Empty Body!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}

