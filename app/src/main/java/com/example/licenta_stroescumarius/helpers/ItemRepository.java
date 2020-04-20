package com.example.licenta_stroescumarius.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.licenta_stroescumarius.Istoric;
import com.example.licenta_stroescumarius.exceptions.AsyncTaskDbExceptions;
import com.example.licenta_stroescumarius.models.ItemIstoric;

import java.util.ArrayList;

public class ItemRepository {
    private static ItemRepository instance;

    public static synchronized ItemRepository getInstance() {
        if (instance == null) {
            instance = new ItemRepository();
        }
        return instance;
    }

    private ItemRepository() {
    }

    @SuppressLint("StaticFieldLeak")
    public void getAllFromDb(Istoric activity, ArrayList<ItemIstoric> istoricArrayList) {
        new AsyncTask<Context, Void, ArrayList<ItemIstoric>>() {
            @Override
            protected void onPostExecute(ArrayList<ItemIstoric> item) {
                super.onPostExecute(item);
                activity.getRecyclerAdapter().notifyDataSetChanged();
            }

            @Override
            protected ArrayList<ItemIstoric> doInBackground(Context... ctx) {
                try {
                    AppDatabase db = AppDatabase.getInstance(ctx[0]);
                    istoricArrayList.addAll(db.daoInterface().getItemsIstoricList());
                    return istoricArrayList;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        }.execute(activity.getApplicationContext());
    }

    @SuppressLint("StaticFieldLeak")
    public void insertIntoDb(Context context, ItemIstoric itemIstoric) {

        new AsyncTask<Context, Void, Void>() {
            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
                try {
                    throw new AsyncTaskDbExceptions("Check context against null!");
                } catch (AsyncTaskDbExceptions asyncTaskDbExceptions) {
                    asyncTaskDbExceptions.printStackTrace();
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context, "Inserted!", Toast.LENGTH_SHORT).show();

            }

            @Override
            protected Void doInBackground(Context... ctx) {
                try {
                    AppDatabase db = AppDatabase.getInstance(ctx[0]);
                    db.daoInterface().insertItemInInstoric(itemIstoric);

                } catch (Exception ex) {
                    cancel(true);
                }
                return null;
            }

        }.execute(context);
    }

    @SuppressLint("StaticFieldLeak")
    public void removeItemFromDb(Istoric activity, ItemIstoric item) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                activity.getRecyclerAdapter().notifyDataSetChanged();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase db = AppDatabase.getInstance(activity.getApplicationContext());
                db.daoInterface().deleteItemFromIstoric(item);
                return null;
            }
        }.execute();
    }
}
