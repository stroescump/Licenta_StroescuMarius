package com.example.licenta_stroescumarius.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ItemIstoric implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "traducere")
    private String traducere;
    @ColumnInfo(name = "imagine_path")
    private String imaginePath;

    protected ItemIstoric(Parcel in) {
        uid = in.readInt();
        traducere = in.readString();
        imaginePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(traducere);
        dest.writeString(imaginePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemIstoric> CREATOR = new Creator<ItemIstoric>() {
        @Override
        public ItemIstoric createFromParcel(Parcel in) {
            return new ItemIstoric(in);
        }

        @Override
        public ItemIstoric[] newArray(int size) {
            return new ItemIstoric[size];
        }
    };

    public ItemIstoric(String traducere, String imaginePath) {
        this.traducere = traducere;
        this.imaginePath = imaginePath;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTraducere() {
        return traducere;
    }

    public void setTraducere(String traducere) {
        this.traducere = traducere;
    }

    public String getImaginePath() {
        return imaginePath;
    }

    public void setImaginePath(String imaginePath) {
        this.imaginePath = imaginePath;
    }
}
