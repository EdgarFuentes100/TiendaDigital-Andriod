package com.example.myappstore.Activities.Configurators;
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
        TextView idTextView = dialogView.findViewById(R.id.textViewId);
        Button cancelButton = dialogView.findViewById(R.id.buttonCancel);
        Button acceptButton = dialogView.findViewById(R.id.buttonAccept);
        EditText categoryEditText = dialogView.findViewById(R.id.editTextCategory);

        if (idTextView != null) {
            idTextView.setText("ID: 123");
        }

        if (categoryEditText != null) {
            categoryEditText.setHint("Ingrese categoría");
        }

        if (cancelButton != null) {
            cancelButton.setOnClickListener(v -> {
                if (dialogFragment != null) {
                    dialogFragment.dismiss(); // Cierra solo el diálogo
                }
            });
        }

        if (acceptButton != null) {
            acceptButton.setOnClickListener(v -> {
                String category = categoryEditText.getText().toString();
                Toast.makeText(dialogView.getContext(), "CRUDCATEGORIA: " + category, Toast.LENGTH_SHORT).show();
                if (dialogFragment != null) {
                    dialogFragment.dismiss(); // Cierra solo el diálogo
                }
            });
        }
    }
}
