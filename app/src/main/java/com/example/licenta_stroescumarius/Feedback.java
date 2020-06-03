package com.example.licenta_stroescumarius;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.math.MathUtils;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Feedback extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        FloatingActionButton fab = findViewById(R.id.fabFeedback);
        EditText tv_feedback = findViewById(R.id.tv_feedback);
        EditText tv_nume = findViewById(R.id.tv_nume);
        tv_nume.requestFocus();
        AnimatedVectorDrawableCompat avd = AnimatedVectorDrawableCompat.create(this,
                R.drawable.avd_send_feedback_fab);
        fab.setImageDrawable(avd);
        fab.setOnClickListener(v -> {
            try {
                avd.start();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                int idFeedback = new Random().nextInt();
                reference.child("feedback_users").
                        child(idFeedback +"_"+ tv_nume.getText().toString()).
                        setValue(tv_feedback.getText().toString());
                Toast.makeText(this, "Inserted!", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
