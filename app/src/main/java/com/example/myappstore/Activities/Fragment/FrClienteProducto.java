package com.example.myappstore.Activities.Fragment;

import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.MainActivity;
import com.example.myappstore.Service.ProductoService;
import com.example.myappstore.R;
import com.example.myappstore.Utils.AlertDialogBase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Response;

public class FrClienteProducto extends Fragment {

    private GridLayout gridLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frame_cliente_productos, container, false);
        gridLayout = view.findViewById(R.id.grid_layout);
        ajustarTexto();
        obtenerProductos();
        return view;
    }

    private void obtenerProductos() {
        ProductoService pd = new ProductoService();

        pd.obtenerTodoProducto(new CallBackApi<Producto>() {
            @Override
            public void onResponse(Producto response) {
                // No se usa en este contexto
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
                // No se usa en este contexto
            }

            @Override
            public void onResponseList(List<Producto> response) {
                // Limpiar el GridLayout antes de agregar nuevos elementos
                gridLayout.removeAllViews();

                // Inflar y agregar cada producto al GridLayout
                for (Producto producto : response) {
                    // Crear un nuevo View para el producto
                    View itemView = crearItemProducto(producto);

                    // Crear LayoutParams y agregar al GridLayout
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 0; // Ancho de la columna
                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.setMargins(5, 5, 5, 5); // Margen de 5dp alrededor de cada elemento

                    // Configurar las posiciones de la columna y fila
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Cada item ocupa 1 columna con peso
                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED); // Fila indefinida

                    itemView.setLayoutParams(params);

                    // Agregar el elemento al GridLayout
                    gridLayout.addView(itemView);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Manejo del error si es necesario
            }
        });
    }

    private View crearItemProducto(Producto producto) {
        // Inflar el layout del producto
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View itemView = inflater.inflate(R.layout.item_producto, null, false);

        // Configurar los datos del producto
        ImageView imagen = itemView.findViewById(R.id.producto_imagen);
        TextView nombre = itemView.findViewById(R.id.producto_nombre);
        TextView descripcion = itemView.findViewById(R.id.producto_descripcion);
        TextView precio = itemView.findViewById(R.id.producto_precio);

        nombre.setText(producto.getNombre());
        descripcion.setText(producto.getDescripcion());
        precio.setText(String.format("$%.2f", producto.getPrecio()));

        // Aquí puedes cargar la imagen con una biblioteca como Glide o Picasso si es necesario
        // Ejemplo usando Glide:
        // Glide.with(this).load(producto.getImagenUrl()).into(imagen);

        itemView.setOnClickListener(v -> showCustomDialog(producto));

        return itemView;
    }
    private void showCustomDialog(Producto producto) {
        // Crear una instancia de AlertDialogBase
        AlertDialogBase dialogFragment = new AlertDialogBase(R.layout.dialog_producto, false);

        // Pasar los datos del producto en un Bundle
        Bundle args = new Bundle();
        args.putString("nombre", producto.getNombre());
        args.putString("descripcion", producto.getDescripcion());
        args.putDouble("precio", producto.getPrecio());
        // Añadir más datos si es necesario

        dialogFragment.setArguments(args);

        // Mostrar el diálogo
        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }
    private void ajustarTexto(){
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Productos", false);
        }
    }
}

