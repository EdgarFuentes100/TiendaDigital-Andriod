package com.example.myappstore.Activities.Configurators;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myappstore.R;
import com.example.myappstore.Service.ImagenService;
import com.example.myappstore.Https.OnImageLoadListener;
import com.example.myappstore.Utils.ImagePagerAdapter;

import java.util.List;

public class FrProgramDialogProducto extends BaseConfigurator {
    private ImageView imageView;
    private ViewPager2 viewPager;

    @Override
    public void configureDialog(View dialogView, DialogFragment dialogFragment) {
        Button acceptButton = dialogView.findViewById(R.id.acceptButton);
        viewPager = dialogView.findViewById(R.id.viewPager);


        if (acceptButton != null) {
            acceptButton.setOnClickListener(v -> {
                Toast.makeText(dialogView.getContext(), "PRODUCTO: ", Toast.LENGTH_SHORT).show();
                if (dialogFragment != null) {
                    dialogFragment.dismiss(); // Cierra solo el diálogo
                }
            });
        }
    }

    @Override
    public void configureDialogWithArguments(View dialogView, Bundle arguments) {
        if (arguments != null) {
            TextView nombre = dialogView.findViewById(R.id.productName);
            TextView descripcion = dialogView.findViewById(R.id.productDescription);
            TextView precio = dialogView.findViewById(R.id.productPrice);
            TextView cantidad = dialogView.findViewById(R.id.productQuantity);

            if (nombre != null) {
                nombre.setText(arguments.getString("nombre"));
            }
            if (descripcion != null) {
                descripcion.setText(arguments.getString("descripcion"));
            }
            if (precio != null) {
                precio.setText(String.format("$%.2f", arguments.getDouble("precio")));
            }
            if (cantidad != null) {
                cantidad.setText("1");
            }

            int idProducto = arguments.getInt("idProducto");
            obtenerImagenes(1);
        }
    }

    private void obtenerImagenes(int idProducto) {
        ImagenService is = new ImagenService();
        is.obtenerImagenes(idProducto, new OnImageLoadListener() {
            @Override
            public void onImagesLoad(List<Bitmap> bitmaps) {
                if (bitmaps != null && !bitmaps.isEmpty()) {
                    ImagePagerAdapter adapter = new ImagePagerAdapter(bitmaps);
                    viewPager.setAdapter(adapter);
                } else {
                    Toast.makeText(imageView.getContext(), "No hay imágenes disponibles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onImagesLoadError(String errorMessage) {
                Toast.makeText(imageView.getContext(), "No hay imágenes disponibles", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
