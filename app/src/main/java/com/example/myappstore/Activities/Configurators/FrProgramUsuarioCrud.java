package com.example.myappstore.Activities.Configurators;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

import com.example.myappstore.CLS.Rol;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.R;
import com.example.myappstore.Service.RolService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
public class FrProgramUsuarioCrud extends BaseConfigurator {

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
                Toast.makeText(dialogView.getContext(), "CRUDUSUSARIO: ", Toast.LENGTH_SHORT).show();
                if (dialogFragment != null) {
                    dialogFragment.dismiss(); // Cierra solo el diálogo
                }
            });
        }
    }

    public void configureDialogWithArguments(View dialogView, Bundle arguments) {
        if (arguments != null) {
            setText(dialogView, R.id.textViewId, arguments.getString("idUsuario"));
            setText(dialogView, R.id.nombreU, arguments.getString("nombre"));
            setText(dialogView, R.id.correoU, arguments.getString("email"));

            Integer idRolSeleccionado = arguments.getInt("idRol", -1); // Usar -1 como valor predeterminado si no está presente
            Spinner spinnerRol = dialogView.findViewById(R.id.rolU); // Cambiar el id según tu diseño
            cargarRoles(spinnerRol, idRolSeleccionado);
        }else{
            Spinner spinnerRol = dialogView.findViewById(R.id.rolU); // Cambiar el id según tu diseño
            cargarRoles(spinnerRol, -1); // -1 indica que no hay rol seleccionado
        }
    }

    private void setText(View dialogView, int viewId, String text) {
        TextView textView = dialogView.findViewById(viewId);
        if (textView != null) {
            textView.setText(text);
        }
    }

    private void cargarRoles(Spinner spinner, Integer idRolSeleccionado) {
        RolService rs = new RolService();
        rs.obtenerRol(new CallBackApi<Rol>() {
            @Override
            public void onResponse(Rol response) {
                // Este método no se usa aquí
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
                // Este método no se usa aquí
            }

            @Override
            public void onResponseList(List<Rol> response) {
                List<String> valores = new ArrayList<>();
                for (Rol rol : response) {
                    valores.add(rol.getRol()); // Asumiendo que Rol tiene un método getRol()
                }

                // Configurar el ArrayAdapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, valores);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Seleccionar el rol correcto si idRolSeleccionado es válido
                if (idRolSeleccionado != -1) {
                    for (int i = 0; i < response.size(); i++) {
                        if (response.get(i).getIdRol() == idRolSeleccionado) { // Asegúrate de que el método getIdRol() esté en Rol
                            spinner.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Manejar el error aquí
                Toast.makeText(spinner.getContext(), "Error al cargar roles: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
