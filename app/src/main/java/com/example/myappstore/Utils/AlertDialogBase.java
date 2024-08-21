package com.example.myappstore.Utils;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;
import java.util.Map;
import com.example.myappstore.Activities.Configurators.BaseConfigurator;
import com.example.myappstore.Activities.Configurators.FrProgramCategoriaCrud;
import com.example.myappstore.Activities.Configurators.FrProgramDialogProducto;
import com.example.myappstore.Activities.Configurators.FrProgramProductoCrud;
import com.example.myappstore.Activities.Configurators.FrProgramUsuarioCrud;
import com.example.myappstore.R;

public class AlertDialogBase extends DialogFragment {
    private int layoutResId;
    private boolean cancelableOnTouchOutside;

    // Mapa para asociar IDs de layout con configuradores
    private static final Map<Integer, BaseConfigurator> configuratorMap = new HashMap<>();

    static {
        configuratorMap.put(R.layout.dialog_producto, new FrProgramDialogProducto());
        configuratorMap.put(R.layout.activity_crud_categoria, new FrProgramCategoriaCrud());
        configuratorMap.put(R.layout.activity_crud_productos, new FrProgramProductoCrud());
        configuratorMap.put(R.layout.activity_crud_usuarios, new FrProgramUsuarioCrud());
        // Añadir más configuradores aquí
    }

    public AlertDialogBase(int layoutResId, boolean cancelableOnTouchOutside) {
        this.layoutResId = layoutResId;
        this.cancelableOnTouchOutside = cancelableOnTouchOutside;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(layoutResId, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        alertDialog.setCanceledOnTouchOutside(cancelableOnTouchOutside);

        // Delegar la configuración al configurador basado en el ID del layout
        BaseConfigurator configurator = configuratorMap.get(layoutResId);
        if (configurator != null) {
            configurator.configureDialog(dialogView, this); // Pasa el DialogFragment a configureDialog
        }

        return alertDialog;
    }
}