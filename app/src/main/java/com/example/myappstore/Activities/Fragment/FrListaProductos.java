package com.example.myappstore.Activities.Fragment;

import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Interface.Utils.DialogDismissListener;
import com.example.myappstore.MainActivity;
import com.example.myappstore.Service.ProductoService;
import com.example.myappstore.Utils.AlertDialogBase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.myappstore.R;
import java.util.List;
import retrofit2.Response;

public class FrListaProductos extends Fragment implements DialogDismissListener {

    private EditText editbuscar;
    private ImageView delete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_productos, container, false);

        ImageView imageView = view.findViewById(R.id.idElementoProducto);
        delete = view.findViewById(R.id.delete);
        editbuscar = view.findViewById(R.id.buscarProducto);

        imageView.setOnClickListener(v -> showCustomDialog(null));

        TableLayout tableLayout = view.findViewById(R.id.tableLayoutProductos);
        ajustarTexto();
        obtenerProductos(tableLayout, ""); // Inicialmente muestra todos los productos

        // Configurar el TextWatcher para el campo de búsqueda
        editbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtrar los productos en función del texto de búsqueda
                obtenerProductos(tableLayout, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Configurar el botón de eliminar para borrar el texto del EditText
        delete.setOnClickListener(v -> {
            editbuscar.setText(""); // Limpia el texto del EditText
            obtenerProductos(tableLayout, ""); // Recargar productos sin filtro
        });

        return view;
    }

    private void obtenerProductos(TableLayout tableLayout, String searchQuery) {
        ProductoService pd = new ProductoService();
        pd.obtenerTodoProductoSinImagen(new CallBackApi<Producto>() {
            @Override
            public void onResponse(Producto response) {
                // No se usa en este contexto
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
                // No se usa en este contexto
            }

            @Override
            public void onResponseList(List<Producto> productos) {
                tableLayout.removeAllViews();

                // Inflar la fila desde el archivo XML
                LayoutInflater inflater = LayoutInflater.from(getContext());

                for (Producto producto : productos) {
                    // Filtrar los productos basándose en la búsqueda
                    if (producto.getNombre().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            producto.getCategoria().toLowerCase().contains(searchQuery.toLowerCase())) {

                        // Inflar la fila
                        TableRow newRow = (TableRow) inflater.inflate(R.layout.table_row_productos, tableLayout, false);

                        // Configurar los datos de la fila
                        TextView textViewId = newRow.findViewById(R.id.textViewId);
                        TextView textViewProducto = newRow.findViewById(R.id.textViewProducto);
                        TextView textViewCategoria = newRow.findViewById(R.id.textViewCategoria);
                        TextView textViewPrecio = newRow.findViewById(R.id.textViewPrecio);

                        // Asignar datos a la fila
                        textViewId.setText(String.valueOf(producto.getIdProducto()));
                        textViewProducto.setText(producto.getNombre());
                        textViewCategoria.setText(producto.getCategoria());
                        textViewPrecio.setText(String.valueOf(producto.getPrecio()));

                        final int originalColor = ContextCompat.getColor(getContext(), R.color.white);
                        final int pressedColor = ContextCompat.getColor(getContext(), R.color.select_color);
                        newRow.setOnLongClickListener(v -> {
                            newRow.setBackgroundColor(pressedColor);
                            showCustomDialog(producto);
                            new Handler().postDelayed(() -> newRow.setBackgroundColor(originalColor), 500);
                            return true;
                        });

                        // Añadir la fila al TableLayout
                        tableLayout.addView(newRow);

                        // Crear una línea divisoria
                        View divider = new View(getContext());
                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT, 1);
                        divider.setLayoutParams(params);
                        divider.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));

                        // Añadir la línea divisoria al TableLayout
                        tableLayout.addView(divider);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Manejo del error si es necesario
            }
        });
    }

    private void showCustomDialog(@Nullable Producto producto) {
        AlertDialogBase dialogFragment = new AlertDialogBase(R.layout.activity_crud_productos, false);

        if (producto != null) {
            Bundle args = new Bundle();
            args.putString("idProducto", String.valueOf(producto.getIdProducto()));
            args.putString("nombre", producto.getNombre());
            args.putString("descripcion", producto.getDescripcion());
            args.putDouble("precio", producto.getPrecio());
            args.putInt("idCategoria", producto.getIdCategoria());
            dialogFragment.setArguments(args);
        }
        dialogFragment.setDialogDismissListener(this);
        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }

    @Override
    public void onDialogDismissed() {
        TableLayout tableLayout = getView().findViewById(R.id.tableLayoutProductos);
        if (tableLayout != null) {
            obtenerProductos(tableLayout, editbuscar.getText().toString()); // Recargar productos con el texto actual del EditText
        }
    }

    private void ajustarTexto() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Lista de Productos", false);
        }
    }
}
