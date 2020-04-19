package com.example.licenta_stroescumarius;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Feedback extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_feedback;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.TabFeedback;
    }

}
