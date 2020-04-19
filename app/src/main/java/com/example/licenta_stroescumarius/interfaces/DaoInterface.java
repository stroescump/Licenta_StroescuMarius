package com.example.licenta_stroescumarius.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.licenta_stroescumarius.models.ItemIstoric;

import java.util.List;

@Dao
public interface DaoInterface {

    @Insert
    void insertItemInInstoric(ItemIstoric item);

    @Query("SELECT * FROM ItemIstoric")
    List<ItemIstoric> getItemsIstoricList();

    @Delete
    void deleteItemFromIstoric(ItemIstoric item);
}
