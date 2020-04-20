package com.example.licenta_stroescumarius;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private int getSelectedItemMenu(BottomNavigationView navigationView) {
        int valueReturned = 0;
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            if (navigationView.getMenu().getItem(i).isChecked()) {
                valueReturned = navigationView.getMenu().getItem(i).getItemId();
            }
        }
        return valueReturned;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int currentItemSelected = getSelectedItemMenu(navigationView);
        int itemId = item.getItemId();

        if (itemId == R.id.TabTraducere && itemId != currentItemSelected) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (itemId == R.id.TabIstoric && itemId != currentItemSelected) {
            startActivity(new Intent(this, Istoric.class));
        } else if (itemId == R.id.TabFeedback && itemId != currentItemSelected) {
            startActivity(new Intent(this, Feedback.class));
        }
        return true;
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

}



