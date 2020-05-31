package com.example.licenta_stroescumarius.helpers;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.licenta_stroescumarius.MainActivity;

public class GetPathFromUri {

    public String getPathFromUri(Uri uriPath, MainActivity activity) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uriPath, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        }
        return res;
    }
}


