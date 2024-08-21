package com.example.myappstore.Activities.Configurators;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.example.myappstore.R;

public class FrProgramDialogProducto extends BaseConfigurator{
    @Override
    public void configureDialog(View dialogView, DialogFragment dialogFragment) {
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
}
