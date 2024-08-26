package com.example.myappstore.Activities.Configurators;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.example.myappstore.R;

public class FrProgramCategoriaCrud extends BaseConfigurator {

    @Override
    public void configureDialog(View dialogView, DialogFragment dialogFragment) {
        Button cancelButton = dialogView.findViewById(R.id.buttonCancel);
        Button acceptButton = dialogView.findViewById(R.id.buttonAccept);

        if (cancelButton != null) {
            cancelButton.setOnClickListener(v -> {
                if (dialogFragment != null) {
                    dialogFragment.dismiss(); // Cierra solo el diálogo
                }
            });
        }

        if (acceptButton != null) {
            acceptButton.setOnClickListener(v -> {
                if (dialogFragment != null) {
                    dialogFragment.dismiss(); // Cierra solo el diálogo
                }
            });
        }
    }
    public void configureDialogWithArguments(View dialogView, Bundle arguments) {
        if (arguments != null) {
            TextView idCategoria = dialogView.findViewById(R.id.textViewId);
            TextView categoria = dialogView.findViewById(R.id.editTextCategory);
            TextView descripcion = dialogView.findViewById(R.id.editDescripcion);

            if (idCategoria != null) {
                idCategoria.setText(arguments.getString("idCategoria"));
            }
            if (categoria != null) {
                categoria.setText(arguments.getString("categoria"));
            }
            if (descripcion != null) {
                descripcion.setText(arguments.getString("descripcion"));
            }
        }
    }
}
