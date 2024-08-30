package com.example.myappstore.Activities.Configurators;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.R;
import com.example.myappstore.Service.CategoriaService;

import java.util.List;

import retrofit2.Response;

public class FrProgramCategoriaCrud extends BaseConfigurator {
    private View dialogView;

    private TextView textId, textCategoria, textDescripcion;
    private ImageView  closeIcon;
    private CategoriaService cs = new CategoriaService();
    @Override
    public void configureDialog(View dialogView, DialogFragment dialogFragment) {
        this.dialogView = dialogView; // Guardar referencia a la vista del diálogo
        Button acceptButton = dialogView.findViewById(R.id.buttonAccept);
        closeIcon = dialogView.findViewById(R.id.closeIcon);
        textId = dialogView.findViewById(R.id.textViewId);
        textCategoria = dialogView.findViewById(R.id.editTextCategory);
        textDescripcion = dialogView.findViewById(R.id.editDescripcion);

        if (acceptButton != null) {
            acceptButton.setOnClickListener(v -> {
                Boolean validacion = validarCampos();
                if (validacion){
                    Categoria categoria = new Categoria();
                    categoria.setCategoria(textCategoria.getText().toString());
                    categoria.setDescripcion(textDescripcion.getText().toString());

                    if(textId.getText().toString().equals("") ){
                        insertarCategoria(categoria);
                    }else{
                        int idProducto = Integer.parseInt(textId.getText().toString());
                        actualizarCategoria(idProducto, categoria);
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
    public void configureDialogWithArguments(View dialogView, Bundle arguments) {
        if (arguments != null) {
            setText(dialogView, R.id.textViewId, arguments.getString("idCategoria"));
            setText(dialogView, R.id.editTextCategory, arguments.getString("categoria"));
            setText(dialogView, R.id.editDescripcion, arguments.getString("descripcion"));
        }
    }

    private void insertarCategoria(Categoria categoria){
        cs.insertarCategoria(categoria, new CallBackApi<Categoria>() {
            @Override
            public void onResponse(Categoria response) {
                Toast.makeText(dialogView.getContext(), "Categoria insertada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {

            }

            @Override
            public void onResponseList(List<Categoria> response) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
    private void actualizarCategoria(int id, Categoria categoria){
        cs.actualizarCategoria(id, categoria, new CallBackApi<Categoria>() {
            @Override
            public void onResponse(Categoria response) {
                Toast.makeText(dialogView.getContext(), "Categoria actualizada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {

            }

            @Override
            public void onResponseList(List<Categoria> response) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
    private Boolean validarCampos() {
        Boolean resultado = true;

        // Verificar si alguno de los campos de texto está vacío
        if (textCategoria.getText().toString().trim().isEmpty() ||
                textDescripcion.getText().toString().trim().isEmpty()){
            resultado = false;
        }

        return resultado;
    }
    private void setText(View dialogView, int viewId, String text) {
        TextView textView = dialogView.findViewById(viewId);
        if (textView != null) {
            textView.setText(text);
        }
    }
}
