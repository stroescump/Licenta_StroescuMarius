package com.example.licenta_stroescumarius.models;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta_stroescumarius.MainActivity;
import com.example.licenta_stroescumarius.helpers.NetworkClient;
import com.example.licenta_stroescumarius.helpers.UploadApis;

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

public class Translate {
    private static final String TAG = "TranslateClass";
    private static final Translate instance = new Translate();

    private Translate() {

    }

    public static Translate getInstance(){
        return instance;
    }

    public void findPrediction(String fileLocation, final MainActivity ctx) {
        File file = new File(fileLocation);
         String text ="";
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
                            ctx.getTextView().setText(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                        Log.v(TAG, "findPrediction: "+"Empty Body");
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.v(TAG, "findPrediction: "+t.getMessage());
                }
            });
        } catch (Exception ex) {
            Log.v(TAG, "findPrediction: "+ex.getMessage());
        }
    }
}
