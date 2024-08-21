package com.example.myappstore.Activities.Configurators;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.example.myappstore.R;

public class FrProgramProductoCrud extends BaseConfigurator {

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
                Toast.makeText(dialogView.getContext(), "CRUDPRODUCTO: " , Toast.LENGTH_SHORT).show();
                if (dialogFragment != null) {
                    dialogFragment.dismiss(); // Cierra solo el diálogo
                }
            });
        }
    }
}
