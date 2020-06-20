package com.example.licenta_stroescumarius;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.licenta_stroescumarius.helpers.NetworkClient;
import com.example.licenta_stroescumarius.interfaces.UploadApis;
import com.google.android.material.button.MaterialButton;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LiveProcessing extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "LiveProcessing";
    private Mat matGray;
    private String resultTranslated=null;
    private MaterialButton liveBtn;
    private ImageView touchBackgroundRef;
    private JavaCamera2View javaCameraView;
    private double xCoord;
    private double yCoord;
    private List<Point> touchCoordinates = new ArrayList<>(2);
    private double virtualSceneHeight;
    private double virtualSceneWidth;
    private double ratio = 0;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "WORKING!");
        } else {
            Log.d(TAG, "NOT WORKING!");
        }
    }

    public LiveProcessing() {
    }

    private double[] transformCoordinates(double x, double y, float openCVWidth, float actualWidth, float openCVHeight, float actualHeight) {
        x -= (openCVWidth / 2) - (actualWidth / 2);
        x = x / ratio;
        y -= (openCVHeight / 2) - (actualHeight / 2);
        y = y / ratio;
        double[] coordinates = new double[2];
        coordinates[0] = x;
        coordinates[1] = y;
        return coordinates;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_processing);
        View currentView = findViewById(R.id.parent_layout);
        javaCameraView = findViewById(R.id.cvCamera);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        javaCameraView.enableView();
        currentView.post(() -> {
//            android.graphics.Rect touchArea = new android.graphics.Rect();
//            javaCameraView.getHitRect(touchArea);
//            touchArea.left-=10;
//            touchArea.right-=10;
//
//            currentView.setTouchDelegate(new TouchDelegate(touchArea,javaCameraView));
        });
        checkingPermissions();

        javaCameraView.setOnTouchListener((v, event) -> {
            float actualHeightVS = (float) (virtualSceneHeight * ratio);
            float actualWidthVS = (float) (virtualSceneWidth * ratio);
            float openCVPanelWidth = javaCameraView.getWidth();
            float openCVPanelHeight = javaCameraView.getHeight();
            xCoord = event.getX();
            yCoord = event.getY();
            if (touchCoordinates.size() == 2) {
                touchCoordinates.removeAll(touchCoordinates);
            }
            if (xCoord >= (openCVPanelWidth / 2) - (actualWidthVS / 2) && xCoord <= (openCVPanelWidth / 2) + (actualWidthVS / 2)) {
                if (yCoord >= (openCVPanelHeight / 2) - (actualHeightVS / 2) && yCoord <= (openCVPanelHeight / 2) + (actualHeightVS / 2)) {
                    double[] coordinates = transformCoordinates(xCoord, yCoord, openCVPanelWidth, actualWidthVS, openCVPanelHeight, actualHeightVS);
                    xCoord = coordinates[0];
                    yCoord = coordinates[1];
                    Toast.makeText(this, "X:" + xCoord + "\tY:" + yCoord, Toast.LENGTH_SHORT).show();
                    touchCoordinates.add(new Point(xCoord, yCoord));
                } else {
                    Toast.makeText(this, "Must select a point from the image-bound context area!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Must select a point from the image-bound context area!", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        touchBackgroundRef = findViewById(R.id.touchBackgroundRef);
        Toast.makeText(this, String.valueOf(touchBackgroundRef.getWidth()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void checkingPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        matGray = new Mat(height, width, CvType.CV_8UC2);
    }

    @Override
    public void onCameraViewStopped() {
        matGray.release();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        if (javaCameraView != null) {
            matGray = inputFrame.gray();
            if (ratio == 0) {
                virtualSceneHeight = matGray.size().height;
                virtualSceneWidth = matGray.size().width;
                ratio = javaCameraView.getHeight() / virtualSceneHeight;
            }
            if (touchCoordinates.size() == 2) {
                Point firstPoint = touchCoordinates.get(0);
                Point secondPoint = touchCoordinates.get(1);
                Scalar lineColor = new Scalar(255, 100, 100);
                Rect r = new Rect(firstPoint, secondPoint);
                if (resultTranslated != null) {
                    Scalar txtColor = new Scalar(255, 100, 40);
                    Imgproc.putText(matGray, resultTranslated,
                            new Point(matGray.size().width / 2 - 50, matGray.size().height / 2 + 80),
                            50,
                            1,
                            txtColor);
                }
                try {
                    new AsyncTask<Void, Void, Mat>() {
                        @Override
                        protected void onPostExecute(Mat mat) {

                        }

                        @Override
                        protected Mat doInBackground(Void... voids) {
                            try {
                                Mat cropped = matGray.submat(r);
                                File img = new File(Environment.getExternalStorageDirectory() + "/Download/", "test.png");
                                img.createNewFile();
                                Imgcodecs.imwrite(img.getAbsolutePath(), cropped);
                                cropped.release();
                                findTranslate(img);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return matGray;
                        }
                    }.execute();
                    Thread.sleep(500);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Imgproc.rectangle(matGray, firstPoint, secondPoint, lineColor, 1);
            }
        }

        return matGray;
    }



    private void findTranslate(File img) {
        TranslatorAPI translatorAPI = new TranslatorAPI(img.getAbsolutePath(), this);
        translatorAPI.findPrediction();
    }

    private class TranslatorAPI {
        private String fileLocation;
        private Context ctx;

        public TranslatorAPI(String fileLocation, Context ctx) {
            this.fileLocation = fileLocation;
            this.ctx = ctx;
        }

        public void findPrediction() {
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
                                resultTranslated = responseBody.string();
                                Log.d(TAG, "onResponse: MODIFIED->"+resultTranslated);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else
                            Log.v(TAG, "findPrediction: " + "Empty Body");
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.v(TAG, "findPrediction: " + t.getMessage());
                    }
                });
            } catch (Exception ex) {
                Log.v(TAG, "findPrediction: " + ex.getMessage());
            }
        }
    }
}
