package com.example.myappstore.Activities.Configurators;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;

import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Interface.Utils.OnImageLoadListener;
import com.example.myappstore.R;
import com.example.myappstore.Service.CategoriaService;
import com.example.myappstore.Service.ImagenService;
import com.example.myappstore.Service.ProductoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class FrProgramProductoCrud extends BaseConfigurator {
    private static final int[] IMAGE_VIEW_IDS = {R.id.Img1, R.id.Img2, R.id.Img3}; // IDs de los ImageView
    private ImageView imageViewToUpdate, closeIcon; // Variable para mantener la referencia del ImageView que debe actualizarse
    private View dialogView;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private List<Bitmap> bitmaps = new ArrayList<>(); // Lista de bitmaps cargados
    private TextView textId, textProducto, textDescripcion, textPrecio;
    private Spinner spinerCategoria;
    private ProductoService ps = new ProductoService();

    @Override
    public void configureDialog(View dialogView, DialogFragment dialogFragment) {
        bitmaps.clear();
        this.dialogView = dialogView; // Guardar referencia a la vista del diálogo
        Button acceptButton = dialogView.findViewById(R.id.buttonAccept);
        Button agregarButton = dialogView.findViewById(R.id.bttAgregar);
        closeIcon = dialogView.findViewById(R.id.closeIcon);
        textId = dialogView.findViewById(R.id.editIdP);
        textProducto = dialogView.findViewById(R.id.editNombreP);
        textDescripcion = dialogView.findViewById(R.id.editDescripcionP);
        spinerCategoria = dialogView.findViewById(R.id.editIdCategoriaP);
        textPrecio = dialogView.findViewById(R.id.editPrecioP);

        // Inicializar el launcher de resultados para seleccionar imágenes
        imagePickerLauncher = dialogFragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageViewToUpdate != null) {
                            imageViewToUpdate.setImageURI(imageUri);
                            Bitmap bitmap = getBitmapFromUri(imageUri);
                            if (bitmap != null) {
                                bitmaps.add(bitmap);
                                updateImageViews(dialogView, bitmaps);
                            }
                            imageViewToUpdate = null; // Reset imageViewToUpdate after use
                            checkImageViewsFull(); // Check if all ImageViews are full
                        }
                    }
                }
        );

        agregarButton.setOnClickListener(v -> {
            ImageView imageView = getFirstAvailableImageView(dialogView);
            if (imageView != null) {
                imageViewToUpdate = imageView; // Guarda la referencia del ImageView
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(intent); // Usa el launcher para abrir la galería
            } else {
                Toast.makeText(dialogView.getContext(), "Todos los espacios están llenos", Toast.LENGTH_SHORT).show();
            }
        });

        closeIcon.setOnClickListener(v -> {
            if (dialogFragment != null) {
                dialogFragment.dismiss(); // Cierra solo el diálogo
            }
        });

        if (acceptButton != null) {
            acceptButton.setOnClickListener(v -> {
                Boolean validacion = validarCampos();
                if (validacion){
                    Producto producto = new Producto();
                    producto.setNombre(textProducto.getText().toString());
                    producto.setDescripcion(textDescripcion.getText().toString());
                    Categoria selectedCategoria = (Categoria) spinerCategoria.getSelectedItem();
                    int categoriaId = selectedCategoria.getIdCategoria();
                    producto.setIdCategoria(categoriaId);
                    producto.setPrecio(Double.parseDouble(textPrecio.getText().toString()));
                    producto.setStock(20);

                    if(textId.getText().toString().equals("")){
                        insertarProducto(producto);
                    }else{
                        int idProducto = Integer.parseInt(textId.getText().toString());
                        actualizarProducto(idProducto, producto);
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
    }

    private void insertarProducto(Producto producto){
        ps.insertarProducto(producto, new CallBackApi<Producto>() {
            @Override
            public void onResponse(Producto response) {
                Toast.makeText(dialogView.getContext(), "Producto insertado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {}

            @Override
            public void onResponseList(List<Producto> response) {}

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(dialogView.getContext(), "Error al insertar producto: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarProducto(int idProducto, Producto producto){
        ps.actualizarProducto(idProducto, producto, new CallBackApi<Producto>() {
            @Override
            public void onResponse(Producto response) {
                Toast.makeText(dialogView.getContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {}

            @Override
            public void onResponseList(List<Producto> response) {}

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(dialogView.getContext(), "Error al actualizar producto: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validarCampos() {
        Boolean resultado = true;

        // Verificar si alguno de los campos de texto está vacío
        if (textProducto.getText().toString().trim().isEmpty() ||
                textDescripcion.getText().toString().trim().isEmpty() ||
                textPrecio.getText().toString().trim().isEmpty()) {
            resultado = false;
        }

        // Verificar si se ha seleccionado una categoría
        int selectedCategoryPosition = spinerCategoria.getSelectedItemPosition();
        if (selectedCategoryPosition == -1) {
            resultado = false;
        }

        return resultado;
    }

    private Bitmap getBitmapFromUri(Uri imageUri) {
        try {
            return MediaStore.Images.Media.getBitmap(dialogView.getContext().getContentResolver(), imageUri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkImageViewsFull() {
        boolean allFull = true;
        for (int id : IMAGE_VIEW_IDS) {
            ImageView imageView = dialogView.findViewById(id);
            if (imageView.getDrawable() == null) {
                allFull = false;
                break;
            }
        }
        if (allFull) {
            Toast.makeText(dialogView.getContext(), "Todos los espacios están llenos", Toast.LENGTH_SHORT).show();
        }
    }

    private ImageView getFirstAvailableImageView(View dialogView) {
        for (int id : IMAGE_VIEW_IDS) {
            ImageView imageView = dialogView.findViewById(id);
            if (imageView != null && imageView.getDrawable() == null) {
                return imageView;
            }
        }
        return null; // Todos los ImageView están ocupados
    }

    public void configureDialogWithArguments(View dialogView, Bundle arguments) {
        if (arguments != null) {
            setText(dialogView, R.id.editIdP, arguments.getString("idProducto"));
            setText(dialogView, R.id.editNombreP, arguments.getString("nombre"));
            setText(dialogView, R.id.editDescripcionP, arguments.getString("descripcion"));
            setText(dialogView, R.id.editPrecioP, String.format("%.2f", arguments.getDouble("precio")));

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
                // Crear ArrayAdapter con tipo específico
                ArrayAdapter<Categoria> categoriaAdapter = new ArrayAdapter<Categoria>(
                        spinnerCategoria.getContext(),
                        android.R.layout.simple_spinner_item,
                        response
                ) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        textView.setText(response.get(position).getCategoria()); // Usa el nombre de la categoría
                        return textView;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                        textView.setText(response.get(position).getCategoria()); // Usa el nombre de la categoría
                        return textView;
                    }
                };
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
                FrProgramProductoCrud.this.bitmaps = bitmaps; // Actualizar la lista de bitmaps
                updateImageViews(dialogView, bitmaps);
            }

            @Override
            public void onImagesLoadError(String errorMessage) {
                showToast(dialogView, "No hay imágenes disponibles: " + errorMessage);
            }
        });
    }

    private void updateImageViews(View dialogView, List<Bitmap> bitmaps) {
        for (int i = 0; i < IMAGE_VIEW_IDS.length; i++) {
            ImageView imageView = dialogView.findViewById(IMAGE_VIEW_IDS[i]);
            if (i < bitmaps.size()) {
                imageView.setImageBitmap(bitmaps.get(i));
                setLongClickListener(dialogView, imageView, bitmaps, i);
            } else {
                imageView.setImageDrawable(null);
                imageView.setOnLongClickListener(null); // Eliminar el listener si no hay imagen
            }
        }
    }

    private void setLongClickListener(View dialogView, ImageView imageView, List<Bitmap> bitmaps, int index) {
        imageView.setOnLongClickListener(v -> {
            if (index >= 0 && index < bitmaps.size()) {
                bitmaps.remove(index);
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
