package com.example.licenta_stroescumarius.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta_stroescumarius.Istoric;
import com.example.licenta_stroescumarius.exceptions.AsyncTaskDbExceptions;
import com.example.licenta_stroescumarius.models.ItemIstoric;

import java.util.ArrayList;

public class ItemRepository {
    private static ItemRepository instance;
    private ArrayList<ItemIstoric> itemsToBeReturned = new ArrayList<>();
    private String resultFromInsertOperation = "";

    public static synchronized ItemRepository getInstance() {
        if (instance == null) {
            instance = new ItemRepository();
        }
        return instance;
    }

    private ItemRepository() {
    }

    @SuppressLint("StaticFieldLeak")
    public ArrayList<ItemIstoric> getAllFromDb() {
        new AsyncTask<Context, Void, ItemIstoric>() {
            @Override
            protected void onPostExecute(ItemIstoric item) {
                super.onPostExecute(item);
                itemsToBeReturned.add(item);
            }

            @Override
            protected ItemIstoric doInBackground(Context... ctx) {
                try {
                    AppDatabase db = AppDatabase.getInstance(ctx[0]);
                    return db.daoInterface().getItemsIstoricList().get(0);
                } catch (Exception ex) {
                    return null;
                }
            }
        }.execute();
        return itemsToBeReturned;
    }

    @SuppressLint("StaticFieldLeak")
    public void insertIntoDb(Istoric activity, ItemIstoric itemIstoric) {

        new AsyncTask<Context, Void, Void>() {
            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);

            }

            @Override
            protected Void doInBackground(Context... ctx) {
                try {
                    AppDatabase db = AppDatabase.getInstance(ctx[0]);
                    db.daoInterface().insertItemInInstoric(itemIstoric);
                    activity.setBoolSuccessfulTask();
//                    activity.setBoolSuccessfulTask();
                }catch (Exception ex){
                    new AsyncTaskDbExceptions("Check context!").printStackTrace();
                    cancel(true);
                }
                return null;
            }

        }.execute(activity.getApplicationContext());
    }
}
