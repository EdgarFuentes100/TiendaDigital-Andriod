package com.example.myappstore.Activities.Fragment;
import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.MainActivity;
import com.example.myappstore.Service.ProductoService;
import com.example.myappstore.Utils.AlertDialogBase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FrListaProductos extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_productos, container, false);

        ImageView imageView = view.findViewById(R.id.idElementoProducto);
        imageView.setOnClickListener(v -> showCustomDialog(null));
        TableLayout tableLayout = view.findViewById(R.id.tableLayoutProductos);
        ajustarTexto();
        obtenerProductos(tableLayout);
        return view;
    }

    private void obtenerProductos(TableLayout tableLayout){
        ProductoService pd = new ProductoService();
        pd.obtenerTodoProducto(new CallBackApi<Producto>() {
            @Override
            public void onResponse(Producto response) {

            }

            @Override
            public void onResponseBool(Response<Boolean> response) {

            }

            @Override
            public void onResponseList(List<Producto> productos) {
                productos.size();
                tableLayout.removeAllViews();

                // Inflar la fila desde el archivo XML
                LayoutInflater inflater = LayoutInflater.from(getContext());

                for (Producto producto : productos) {
                    // Inflar la fila
                    TableRow newRow = (TableRow) inflater.inflate(R.layout.table_row_productos, tableLayout, false);

                    // Configurar los datos de la fila
                    TextView textViewId = newRow.findViewById(R.id.textViewId);
                    TextView textViewProducto = newRow.findViewById(R.id.textViewProducto);
                    TextView textViewCategoria = newRow.findViewById(R.id.textViewCategoria);
                    TextView textViewPrecio= newRow.findViewById(R.id.textViewPrecio);

                    // Asignar datos a la fila
                    textViewId.setText(String.valueOf(producto.getIdProducto()));
                    textViewProducto.setText(producto.getNombre());
                    textViewCategoria.setText(producto.getCategoria()); // Aquí puedes añadir más datos
                    textViewPrecio.setText(String.valueOf(producto.getPrecio())); // Aquí puedes añadir más datos

                    final int originalColor = ContextCompat.getColor(getContext(), R.color.white); // Cambia el color original según tu diseño
                    final int pressedColor = ContextCompat.getColor(getContext(), R.color.select_color); // Color al mantener presionado

                    // Configurar los eventos de clic si es necesario
                    newRow.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // Cambia el color de fondo al mantener presionado
                            newRow.setBackgroundColor(pressedColor);
                            showCustomDialog(producto);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    newRow.setBackgroundColor(originalColor);
                                }
                            }, 500); // 2000 milisegundos = 2 segundos, ajusta según sea necesario

                            return true;
                        }
                    });

                    /*SwipeGestureListener swipeListener = new SwipeGestureListener(newRow);
                    GestureDetector gestureDetector = new GestureDetector(getContext(), swipeListener);

                    newRow.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));*/


                    // Añadir la fila al TableLayout
                    tableLayout.addView(newRow);

                    // Crear una línea divisoria
                    View divider = new View(getContext());
                    TableRow.LayoutParams params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT, 1); // Ancho y alto del divisor
                    divider.setLayoutParams(params);
                    divider.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black)); // Define el color en recursos

                    // Añadir la línea divisoria al TableLayout
                    tableLayout.addView(divider);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

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

        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }


    private void ajustarTexto(){
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Lista de Productos", false);
        }
    }
}

