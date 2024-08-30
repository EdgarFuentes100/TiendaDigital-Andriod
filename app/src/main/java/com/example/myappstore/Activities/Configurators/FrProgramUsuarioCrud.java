package com.example.myappstore.Activities.Configurators;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Rol;
import com.example.myappstore.CLS.Usuario; // Asegúrate de tener una clase Usuario
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.R;
import com.example.myappstore.Service.RolService;
import com.example.myappstore.Service.UsuarioService; // Asegúrate de tener un UsuarioService

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class FrProgramUsuarioCrud extends BaseConfigurator {
    private View dialogView;
    private TextView textId, textUsuario, textcorreo;
    private ImageView closeIcon; // Variable para mantener la referencia del ImageView que debe actualizarse
    private Spinner spinerRol;

    private UsuarioService us = new UsuarioService(); // Asume que tienes un UsuarioService

    @Override
    public void configureDialog(View dialogView, DialogFragment dialogFragment) {
        this.dialogView = dialogView;
        Button acceptButton = dialogView.findViewById(R.id.buttonAccept);
        closeIcon = dialogView.findViewById(R.id.closeIcon);
        textId = dialogView.findViewById(R.id.textViewId);
        textUsuario = dialogView.findViewById(R.id.nombreU);
        textcorreo = dialogView.findViewById(R.id.correoU);
        spinerRol = dialogView.findViewById(R.id.rolU);

        if (acceptButton != null) {
            acceptButton.setOnClickListener(v -> {
                if (validarCampos(dialogView)) {
                    Usuario usuario = new Usuario();
                    usuario.setNombre(textUsuario.getText().toString());
                    usuario.setCorreo(textcorreo.getText().toString());

                    Rol selectedRol = (Rol) spinerRol.getSelectedItem();
                    if (selectedRol != null) {
                        usuario.setIdRol(selectedRol.getIdRol());
                    } else {
                        Toast.makeText(dialogView.getContext(), "No se ha seleccionado un rol válido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (textId.getText().toString().equals("")) {
                        //insertarUsuario(usuario);
                    } else {
                        int idUsuario = Integer.parseInt(textId.getText().toString());
                        actualizarUsuario(idUsuario, usuario);
                        Toast.makeText(dialogView.getContext(), "ACTUALIZAR", Toast.LENGTH_SHORT).show();
                    }

                    if (dialogFragment != null) {
                        dialogFragment.dismiss(); // Cierra solo el diálogo
                    }
                } else {
                    Toast.makeText(dialogView.getContext(), "Por favor asegúrese de llenar todos los campos necesarios", Toast.LENGTH_SHORT).show();
                }
            });
        }
        closeIcon.setOnClickListener(v -> {
            if (dialogFragment != null) {
                dialogFragment.dismiss(); // Cierra solo el diálogo
            }
        });
    }


    private Boolean validarCampos(View dialogView) {
        Boolean resultado = true;

        // Verificar si alguno de los campos de texto está vacío
        if (((TextView) dialogView.findViewById(R.id.nombreU)).getText().toString().trim().isEmpty() ||
                ((TextView) dialogView.findViewById(R.id.correoU)).getText().toString().trim().isEmpty()) {
            resultado = false;
        }

        // Verificar si se ha seleccionado un rol
        Spinner spinnerRol = dialogView.findViewById(R.id.rolU);
        if (spinnerRol.getSelectedItemPosition() == -1) {
            resultado = false;
        }

        return resultado;
    }

    private void actualizarUsuario(int idUsuario, Usuario usuario) {
        us.actualizarUsuario(idUsuario, usuario, new CallBackApi<Usuario>() {
            @Override
            public void onResponse(Usuario response) {
                Toast.makeText(dialogView.getContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {}

            @Override
            public void onResponseList(List<Usuario> response) {}

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(dialogView.getContext(), "Error al actualizar usuario: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void configureDialogWithArguments(View dialogView, Bundle arguments) {
        if (arguments != null) {
            setText(dialogView, R.id.textViewId, arguments.getString("idUsuario"));
            setText(dialogView, R.id.nombreU, arguments.getString("nombre"));
            setText(dialogView, R.id.correoU, arguments.getString("email"));

            Integer idRolSeleccionado = arguments.getInt("idRol", -1);
            Spinner spinnerRol = dialogView.findViewById(R.id.rolU);
            cargarRoles(spinnerRol, idRolSeleccionado);
        } else {
            Spinner spinnerRol = dialogView.findViewById(R.id.rolU);
            cargarRoles(spinnerRol, -1);
        }
    }

    private void cargarRoles(Spinner spinnerRol, Integer idRolSeleccionado) {
        RolService rolService = new RolService();
        rolService.obtenerRol(new CallBackApi<Rol>() {
            @Override
            public void onResponse(Rol response) {}

            @Override
            public void onResponseBool(Response<Boolean> response) {}

            @Override
            public void onResponseList(List<Rol> roles) {
                // Crear ArrayAdapter con tipo específico
                ArrayAdapter<Rol> rolAdapter = new ArrayAdapter<Rol>(
                        spinnerRol.getContext(),
                        android.R.layout.simple_spinner_item,
                        roles
                ) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        textView.setText(roles.get(position).getRol()); // Usa el nombre del rol
                        return textView;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                        textView.setText(roles.get(position).getRol()); // Usa el nombre del rol
                        return textView;
                    }
                };
                rolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRol.setAdapter(rolAdapter);

                // Seleccionar el rol correcto si idRolSeleccionado es válido
                if (idRolSeleccionado != -1) {
                    for (int i = 0; i < roles.size(); i++) {
                        if (roles.get(i).getIdRol() == idRolSeleccionado) {
                            spinnerRol.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(spinnerRol.getContext(), "Error al cargar roles: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setText(View dialogView, int viewId, String text) {
        TextView textView = dialogView.findViewById(viewId);
        if (textView != null) {
            textView.setText(text);
        }
    }
}
