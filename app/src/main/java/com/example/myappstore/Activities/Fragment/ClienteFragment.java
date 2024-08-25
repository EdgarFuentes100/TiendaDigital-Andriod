package com.example.myappstore.Activities.Fragment;
import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.MainActivity;
import com.example.myappstore.R;
import com.example.myappstore.Service.CategoriaService;
import com.example.myappstore.Service.ProductoService;
import com.example.myappstore.Utils.FragmentTransactionHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import retrofit2.Response;

public class ClienteFragment extends Fragment {
    private GridLayout gridLayout;
    private FragmentTransactionHelper fragmentTransactionHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frame_cliente, container, false);
        gridLayout = view.findViewById(R.id.grid_layout);

        // Inicializa el FragmentTransactionHelper
        FragmentManager fragmentManager = getParentFragmentManager(); // O usa requireActivity().getSupportFragmentManager()
        fragmentTransactionHelper = new FragmentTransactionHelper(fragmentManager);

        // Configura el botón para abrir el fragmento
        LinearLayout linearLayout = view.findViewById(R.id.open_fragment_button);
        linearLayout.setOnClickListener(v -> openDetailFragment());
        ajustarTexto();

        obtenerCategorias();
        return view;
    }

    private void openDetailFragment() {
        Fragment fragment = new FrClienteProducto(); // Usa el fragmento simple para pruebas
        fragmentTransactionHelper.replaceFragment(fragment, true);
    }
    private void ajustarTexto(){
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Categorias", false);
        }
    }
    private void obtenerCategorias() {
        CategoriaService cg = new CategoriaService();

        cg.obtenerTodoCategoria(new CallBackApi<Categoria>() {
            @Override
            public void onResponse(Categoria response) {
                // No se usa en este contexto
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
                // No se usa en este contexto
            }

            @Override
            public void onResponseList(List<Categoria> response) {
                // Limpiar el GridLayout antes de agregar nuevos elementos
                gridLayout.removeAllViews();

                // Inflar y agregar cada producto al GridLayout
                for (Categoria categoria : response) {
                    // Crear un nuevo View para el producto
                    View itemView = crearItemProducto(categoria);

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

    private View crearItemProducto(Categoria categoria) {
        // Inflar el layout del producto
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View itemView = inflater.inflate(R.layout.item_categoria, null, false);

        // Configurar los datos del producto
        ImageView imagen = itemView.findViewById(R.id.CategoriaImg);
        TextView nombre = itemView.findViewById(R.id.CategoriaNombre);
        TextView descripcion = itemView.findViewById(R.id.CategoriaDescripcion);

        nombre.setText(categoria.getCategoria());
        descripcion.setText(categoria.getDescripcion());
        String ID = String.valueOf(categoria.getIdCategoria());

        // Aquí puedes cargar la imagen con una biblioteca como Glide o Picasso si es necesario
        // Ejemplo usando Glide:
        // Glide.with(this).load(producto.getImagenUrl()).into(imagen);
        itemView.setOnClickListener(v -> showCustomDialog(ID));
        return itemView;
    }
    private void showCustomDialog(String id){
        Toast.makeText(getContext(), "CLICK: " + id, Toast.LENGTH_SHORT).show();

        FrClienteProducto fragment = new FrClienteProducto();
        Bundle args = new Bundle();
        args.putString("id_key", id); // Usa una clave adecuada para el argumento
        fragment.setArguments(args);
        fragmentTransactionHelper.replaceFragment(fragment, true);
    }

}
