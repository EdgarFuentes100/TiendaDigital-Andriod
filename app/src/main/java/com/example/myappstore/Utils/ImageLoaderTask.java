package com.example.myappstore.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

    public interface ImageLoaderCallback {
        void onImageLoaded(Bitmap bitmap);
    }

    private final ImageLoaderCallback callback;

    public ImageLoaderTask(ImageLoaderCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String base64String = params[0];
        if (base64String != null && !base64String.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64String.split(",")[1], Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (callback != null) {
            callback.onImageLoaded(bitmap);
        }
    }
}
