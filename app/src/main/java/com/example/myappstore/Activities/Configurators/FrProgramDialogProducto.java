package com.example.myappstore.Activities.Configurators;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.example.myappstore.R;

public class FrProgramDialogProducto extends BaseConfigurator {
    @Override
    public void configureDialog(View dialogView, DialogFragment dialogFragment) {
        // Configuración básica del diálogo, sin datos específicos
        Button acceptButton = dialogView.findViewById(R.id.acceptButton);

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
        // Configurar el diálogo con los datos del producto
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
            if (cantidad != null){
                cantidad.setText("1");
            }
        }
    }
}
