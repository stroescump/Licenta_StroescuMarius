package com.example.licenta_stroescumarius.helpers;

import android.os.AsyncTask;

import com.google.gson.Gson;

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

public class UploadImage extends AsyncTask<String,Void,String> {
    private String prediction;

    @Override
    protected String doInBackground(String... strings) {
        File file = new File(strings[0]);
        RequestBody imgPart = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part parts = MultipartBody.Part
                .createFormData("img_predict", "img_predict.png", imgPart);
        Retrofit retrofit = NetworkClient.getRetrofit();
        UploadApis uploadApis = retrofit.create(UploadApis.class);
        Call<ResponseBody> call = uploadApis.uploadImage(parts);
        try {
            final Gson gson = new Gson();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call call, Response response) {

                    ResponseBody responseBody = (ResponseBody) response.body();
                    if (responseBody != null) {
                        try {
                            prediction=responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                        prediction = "Empty Response BODY!";
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    prediction = t.getMessage();
                }
            });
        } catch (Exception ex) {
            prediction = ex.getMessage();
        }
        return prediction;
    }

}
