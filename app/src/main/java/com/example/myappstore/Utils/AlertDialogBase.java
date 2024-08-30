package com.example.myappstore.Utils;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
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
import com.example.myappstore.Interface.Utils.DialogDismissListener;
import com.example.myappstore.R;

public class AlertDialogBase extends DialogFragment {
    private int layoutResId;
    private boolean cancelableOnTouchOutside;
    private DialogDismissListener dismissListener;

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

    public void setDialogDismissListener(DialogDismissListener listener) {
        this.dismissListener = listener;
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

        BaseConfigurator configurator = configuratorMap.get(layoutResId);
        if (configurator != null) {
            configurator.configureDialog(dialogView, this);

            Bundle arguments = getArguments();
            configurator.configureDialogWithArguments(dialogView, arguments);
        }

        return alertDialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDialogDismissed();
        }
    }
}
