package com.example.myappstore.Activities.Configurators;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Https.OnImageLoadListener;
import com.example.myappstore.R;
import com.example.myappstore.Service.CategoriaService;
import com.example.myappstore.Service.ImagenService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

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
                Toast.makeText(dialogView.getContext(), "CRUDPRODUCTO: ", Toast.LENGTH_SHORT).show();
                if (dialogFragment != null) {
                    dialogFragment.dismiss(); // Cierra solo el diálogo
                }
            });
        }
    }

    public void configureDialogWithArguments(View dialogView, Bundle arguments) {
        if (arguments != null) {
            setText(dialogView, R.id.editIdP, arguments.getString("idProducto"));
            setText(dialogView, R.id.editNombreP, arguments.getString("nombre"));
            setText(dialogView, R.id.editDescripcionP, arguments.getString("descripcion"));
            setText(dialogView, R.id.editPrecioP, String.format("$%.2f", arguments.getDouble("precio")));

            int idProductoInt = Integer.parseInt(arguments.getString("idProducto"));
            obtenerImagenes(dialogView, idProductoInt);

            int idCategoriaSeleccionada = arguments.getInt("idCategoria", -1); // Default to -1 if not provided

            // Cargar y configurar el Spinner de categorías
            Spinner spinnerCategoria = dialogView.findViewById(R.id.editIdCategoriaP);
            cargarCategorias(spinnerCategoria, idCategoriaSeleccionada);
        } else {
            // If arguments are null, still load all categories but with no pre-selection
            Spinner spinnerCategoria = dialogView.findViewById(R.id.editIdCategoriaP);
            cargarCategorias(spinnerCategoria, -1); // -1 indicates no category selected
        }
    }

    private void cargarCategorias(Spinner spinnerCategoria, int idCategoriaSeleccionada) {
        CategoriaService cs = new CategoriaService();
        cs.obtenerTodoCategoria(new CallBackApi<Categoria>() {
            @Override
            public void onResponse(Categoria response) {}

            @Override
            public void onResponseBool(Response<Boolean> response) {}

            @Override
            public void onResponseList(List<Categoria> response) {
                List<String> categoriasNombres = new ArrayList<>();
                for (Categoria categoria : response) {
                    categoriasNombres.add(categoria.getCategoria()); // Asumiendo que Categoria tiene getCategoria()
                }

                // Configurar el ArrayAdapter
                ArrayAdapter<String> categoriaAdapter = new ArrayAdapter<>(spinnerCategoria.getContext(), android.R.layout.simple_spinner_item, categoriasNombres);
                categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategoria.setAdapter(categoriaAdapter);

                // Seleccionar la categoría correcta si idCategoriaSeleccionada es válido
                if (idCategoriaSeleccionada != -1) {
                    for (int i = 0; i < response.size(); i++) {
                        if (response.get(i).getIdCategoria() == idCategoriaSeleccionada) {
                            spinnerCategoria.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Manejar el error aquí
                Toast.makeText(spinnerCategoria.getContext(), "Error al cargar categorías: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setText(View dialogView, int viewId, String text) {
        TextView textView = dialogView.findViewById(viewId);
        if (textView != null) {
            textView.setText(text);
        }
    }

    private void obtenerImagenes(View dialogView, int idProducto) {
        ImagenService is = new ImagenService();
        is.obtenerImagenes(idProducto, new OnImageLoadListener() {
            @Override
            public void onImagesLoad(List<Bitmap> bitmaps) {
                updateImageViews(dialogView, bitmaps);
            }

            @Override
            public void onImagesLoadError(String errorMessage) {
                showToast(dialogView, "No hay imágenes disponibles: " + errorMessage);
            }
        });
    }

    private void updateImageViews(View dialogView, List<Bitmap> bitmaps) {
        int[] imageViewIds = {R.id.Img1, R.id.Img2, R.id.Img3};
        for (int i = 0; i < imageViewIds.length; i++) {
            ImageView imageView = dialogView.findViewById(imageViewIds[i]);
            if (i < bitmaps.size()) {
                imageView.setImageBitmap(bitmaps.get(i));
                setLongClickListener(dialogView, imageView, bitmaps, i);
            } else {
                imageView.setImageDrawable(null);
                // Eliminar el listener si no hay imagen
                imageView.setOnLongClickListener(null);
            }
        }
    }

    private void setLongClickListener(View dialogView, ImageView imageView, List<Bitmap> bitmaps, int index) {
        imageView.setOnLongClickListener(v -> {
            if (index >= 0 && index < bitmaps.size()) {
                bitmaps.remove(index);
                // Actualizar los ImageView después de eliminar
                updateImageViews(dialogView, bitmaps);
                showToast(dialogView, "Imagen eliminada");
                return true;
            }
            return false; // Return false to indicate the long click event is not handled
        });
    }


    private void showToast(View dialogView, String message) {
        Toast.makeText(dialogView.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

