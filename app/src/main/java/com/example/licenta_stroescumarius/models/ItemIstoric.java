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
    @ColumnInfo(name = "imagine_uri")
    private String imagineUri;

    public ItemIstoric(String traducere, String imagineUri) {
        this.traducere = traducere;
        this.imagineUri = imagineUri;
    }

    protected ItemIstoric(Parcel in) {
        traducere = in.readString();
        imagineUri = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(traducere);
        dest.writeString(imagineUri);
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

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public String getTraducere() {
        return traducere;
    }

    public void setTraducere(String traducere) {
        this.traducere = traducere;
    }

    public String getImagineUri() {
        return imagineUri;
    }

    public void setImagineUri(String imagineUri) {
        this.imagineUri = imagineUri;
    }
}
