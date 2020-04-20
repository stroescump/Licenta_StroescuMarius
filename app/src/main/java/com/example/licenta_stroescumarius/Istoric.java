package com.example.licenta_stroescumarius;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta_stroescumarius.customAdapters.IstoricAdapter;
import com.example.licenta_stroescumarius.helpers.ItemRepository;
import com.example.licenta_stroescumarius.models.ItemIstoric;

import java.util.ArrayList;

public class Istoric extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    ArrayList<ItemIstoric> listaItemsIstoric = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_istoric;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.TabIstoric;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);

        ItemRepository repo = ItemRepository.getInstance();
        repo.getAllFromDb(this, listaItemsIstoric);

        recyclerAdapter = new IstoricAdapter(listaItemsIstoric);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder target, int direction) {
                int position = target.getAdapterPosition();
                repo.removeItemFromDb(Istoric.this, listaItemsIstoric.get(position));
                listaItemsIstoric.remove(position);
            }
        });

        helper.attachToRecyclerView(recyclerView);
    }

    public RecyclerView.Adapter getRecyclerAdapter() {
        return recyclerAdapter;
    }

}
