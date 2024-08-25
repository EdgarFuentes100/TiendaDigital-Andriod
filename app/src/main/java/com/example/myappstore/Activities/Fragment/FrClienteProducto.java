package com.example.myappstore.Activities.Fragment;
import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.MainActivity;
import com.example.myappstore.Service.ProductoService;
import com.example.myappstore.R;
import com.example.myappstore.Utils.AlertDialogBase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;

public class FrClienteProducto extends Fragment {

    private GridLayout gridLayout;
    private ImageView clearImageView;
    private EditText searchEditText;
    private List<Producto> productos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frame_cliente_productos, container, false);
        gridLayout = view.findViewById(R.id.grid_layout);
        searchEditText = view.findViewById(R.id.idBuscar);
        clearImageView = view.findViewById(R.id.delete);
        clearImageView.setOnClickListener(v -> clearSearchText());

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarProductos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            String idCategoria = args.getString("id_key");
            if (idCategoria != null && !idCategoria.trim().isEmpty()) {
                obtenerProductoPorId(idCategoria);
            } else {
                obtenerProductos();
            }
        } else {
            obtenerProductos();
        }

        ajustarTexto();
        return view;
    }

    private void clearSearchText() {
        // Limpiar el texto del EditText
        if (searchEditText != null) {
            searchEditText.setText("");
            // Actualizar la lista de productos si es necesario
            // Por ejemplo, puedes volver a obtener todos los productos
            obtenerProductos();
        }
    }

    private void obtenerProductoPorId(String idCategoria) {
        ProductoService pd = new ProductoService();
        pd.obtenerProductoPorId(idCategoria, new CallBackApi<Producto>() {
            @Override
            public void onResponse(Producto response) {
                // Suponiendo que obtienes un solo producto; no se usa aquí
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
            }

            @Override
            public void onResponseList(List<Producto> response) {
                productos = response;
                mostrarProductos(productos);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("FrClienteProducto", "Error: " + errorMessage);
            }
        });
    }

    private void obtenerProductos() {
        ProductoService pd = new ProductoService();
        pd.obtenerTodoProducto(new CallBackApi<Producto>() {
            @Override
            public void onResponse(Producto response) {
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
            }

            @Override
            public void onResponseList(List<Producto> response) {
                productos = response;
                mostrarProductos(productos);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("FrClienteProducto", "Error: " + errorMessage);
            }
        });
    }

    private void mostrarProductos(List<Producto> productos) {
        gridLayout.removeAllViews();
        for (Producto producto : productos) {
            View itemView = crearItemProducto(producto);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(5, 5, 5, 5);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
            itemView.setLayoutParams(params);
            gridLayout.addView(itemView);
        }
    }

    private void filtrarProductos(String query) {
        List<Producto> filtrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                    producto.getDescripcion().toLowerCase().contains(query.toLowerCase())) {
                filtrados.add(producto);
            }
        }
        mostrarProductos(filtrados);
    }

    private View crearItemProducto(Producto producto) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View itemView = inflater.inflate(R.layout.item_producto, null, false);
        ImageView imagen = itemView.findViewById(R.id.producto_imagen);
        TextView nombre = itemView.findViewById(R.id.producto_nombre);
        TextView descripcion = itemView.findViewById(R.id.producto_descripcion);
        TextView precio = itemView.findViewById(R.id.producto_precio);

        nombre.setText(producto.getNombre());
        descripcion.setText(producto.getDescripcion());
        precio.setText(String.format("$%.2f", producto.getPrecio()));

        // Carga de imagen (opcional)
        // Glide.with(this).load(producto.getImagenUrl()).into(imagen);

        itemView.setOnClickListener(v -> showCustomDialog(producto));
        return itemView;
    }

    private void showCustomDialog(Producto producto) {
        AlertDialogBase dialogFragment = new AlertDialogBase(R.layout.dialog_producto, false);
        Bundle args = new Bundle();
        args.putString("nombre", producto.getNombre());
        args.putString("descripcion", producto.getDescripcion());
        args.putDouble("precio", producto.getPrecio());
        dialogFragment.setArguments(args);
        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }

    private void ajustarTexto() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Productos", false);
        }
    }
}
