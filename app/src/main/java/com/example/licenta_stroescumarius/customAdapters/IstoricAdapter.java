package com.example.licenta_stroescumarius.customAdapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta_stroescumarius.R;
import com.example.licenta_stroescumarius.models.ItemIstoric;

import java.io.File;
import java.util.ArrayList;

public class IstoricAdapter extends RecyclerView.Adapter<IstoricAdapter.IstoricViewHolder> {
    private ArrayList<ItemIstoric> itemIstoricArrayList;

    public IstoricAdapter(ArrayList<ItemIstoric> itemList) {
        itemIstoricArrayList = itemList;
    }

    @NonNull
    @Override
    public IstoricViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_istoric_adapter, parent, false);
        IstoricViewHolder viewHolder = new IstoricViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IstoricViewHolder holder, int position) {
        ItemIstoric currentItem = itemIstoricArrayList.get(position);
        File image = new File(currentItem.getImaginePath());
        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
        Bitmap bmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        holder.imagine.setImageBitmap(bmp);
        holder.traducere.setText(currentItem.getTraducere());
    }

    @Override
    public int getItemCount() {
        return itemIstoricArrayList.size();
    }

    public static class IstoricViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagine;
        public TextView traducere;

        public IstoricViewHolder(@NonNull View itemView) {
            super(itemView);
            imagine = itemView.findViewById(R.id.imagineAdaptor);
            traducere = itemView.findViewById(R.id.traducereAdaptor);
        }
    }
}
