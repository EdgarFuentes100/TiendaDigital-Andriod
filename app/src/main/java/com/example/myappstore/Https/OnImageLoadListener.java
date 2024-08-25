package com.example.myappstore.Https;

import android.graphics.Bitmap;

import java.util.List;

public interface OnImageLoadListener {
    void onImagesLoad(List<Bitmap> bitmaps);
    void onImagesLoadError(String errorMessage);
}
