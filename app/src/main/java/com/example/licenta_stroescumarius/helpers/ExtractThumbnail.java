package com.example.licenta_stroescumarius.helpers;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

public class ExtractThumbnail {
    private ExtractThumbnail instance = new ExtractThumbnail();

    public ExtractThumbnail getInstance() {
        return instance;
    }

    private ExtractThumbnail() {
    }

    private Bitmap getThumbnail(Bitmap source) {
        return ThumbnailUtils.extractThumbnail(source, 80, 80);
    }
}
