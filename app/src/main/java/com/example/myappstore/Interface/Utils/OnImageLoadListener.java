package com.example.myappstore.Interface.Utils;

import android.graphics.Bitmap;

import java.util.List;

public interface OnImageLoadListener {
    void onImagesLoad(List<Bitmap> bitmaps);
    void onImagesLoadError(String errorMessage);
}
