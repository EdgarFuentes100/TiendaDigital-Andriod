package com.example.myappstore.Activities.Fragment;
import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.MainActivity;
import com.example.myappstore.Service.CategoriaService;
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

public class FrListaCategoria extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_categoria, container, false);

        ImageView imageView = view.findViewById(R.id.idElementCategoria);
        imageView.setOnClickListener(v -> showCustomDialog(null));
        TableLayout tableLayout = view.findViewById(R.id.tableLayoutCategorias);
        ajustarTexto();
        obtenerCategorias(tableLayout);
        return view;
    }

    private void obtenerCategorias(TableLayout tableLayout) {
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
            public void onResponseList(List<Categoria> categorias) {
                // Limpia las filas existentes si es necesario
                tableLayout.removeAllViews();

                // Inflar la fila desde el archivo XML
                LayoutInflater inflater = LayoutInflater.from(getContext());

                for (Categoria categoria : categorias) {
                    // Inflar la fila
                    TableRow newRow = (TableRow) inflater.inflate(R.layout.table_row_categoria, tableLayout, false);

                    // Configurar los datos de la fila
                    TextView textViewId = newRow.findViewById(R.id.textViewId);
                    TextView textViewCategoria = newRow.findViewById(R.id.textViewCategoria);
                    TextView textViewOtro = newRow.findViewById(R.id.textViewOtro);

                    // Asignar datos a la fila
                    textViewId.setText(String.valueOf(categoria.getIdCategoria()));
                    textViewCategoria.setText(categoria.getCategoria());
                    textViewOtro.setText(categoria.getDescripcion());

                    // Configura el touch listener para la fila
                    final int originalColor = ContextCompat.getColor(getContext(), R.color.white); // Cambia el color original según tu diseño
                    final int pressedColor = ContextCompat.getColor(getContext(), R.color.select_color); // Color al mantener presionado
                    newRow.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // Cambia el color de fondo al mantener presionado
                            newRow.setBackgroundColor(pressedColor);
                            showCustomDialog(categoria);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    newRow.setBackgroundColor(originalColor);
                                }
                            }, 500); // 2000 milisegundos = 2 segundos, ajusta según sea necesario

                            return true;
                        }
                    });
                    // Añadir la fila al TableLayout
                    tableLayout.addView(newRow);

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
                // Manejo del error si es necesario
            }
        });
    }
    private void showCustomDialog(@Nullable Categoria categoria) {
        AlertDialogBase dialogFragment = new AlertDialogBase(R.layout.activity_crud_categoria, false);

        if (categoria != null) {
            Bundle args = new Bundle();
            args.putString("idCategoria", String.valueOf(categoria.getIdCategoria()));
            args.putString("categoria", categoria.getCategoria());
            args.putString("descripcion", categoria.getDescripcion());
            dialogFragment.setArguments(args);
        }

        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }

    private void ajustarTexto(){
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Lista de Categorias", true);
        }
    }
}

