package com.example.licenta_stroescumarius;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta_stroescumarius.customAdapters.IstoricAdapter;
import com.example.licenta_stroescumarius.helpers.ItemRepository;
import com.example.licenta_stroescumarius.models.ItemIstoric;

import java.util.ArrayList;

public class Istoric extends BaseActivity {
    private boolean taskSuccessful;
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

    private void initViews() {
        taskSuccessful=false;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);


        listaItemsIstoric.add(new ItemIstoric("Ana are COVID", ""));
        listaItemsIstoric.add(new ItemIstoric("Ion Paduraroo", ""));
        listaItemsIstoric.add(new ItemIstoric("Markel", ""));
        listaItemsIstoric.add(new ItemIstoric("Katena", ""));
//        insertIntoDb();
        ItemRepository repo = ItemRepository.getInstance();
        repo.insertIntoDb(this, listaItemsIstoric.get(2));
        if (taskSuccessful)
            Toast.makeText(this, "Inserted!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Failed on inserting! Check passed context!", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, resultOperation, Toast.LENGTH_SHORT).show();

        recyclerAdapter = new IstoricAdapter(listaItemsIstoric);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void setBoolSuccessfulTask(){
        taskSuccessful=true;
    }


}
