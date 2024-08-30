package com.example.myappstore.Activities.Fragment;
import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Interface.Utils.DialogDismissListener;
import com.example.myappstore.MainActivity;
import com.example.myappstore.Service.CategoriaService;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.myappstore.R;

import java.util.List;

import retrofit2.Response;

public class FrListaCategoria extends Fragment implements DialogDismissListener {
    private EditText editBuscar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_categoria, container, false);

        ImageView imageView = view.findViewById(R.id.idElementCategoria);
        ImageView delete = view.findViewById(R.id.delete);
        editBuscar = view.findViewById(R.id.buscar);
        imageView.setOnClickListener(v -> showCustomDialog(null));
        TableLayout tableLayout = view.findViewById(R.id.tableLayoutCategorias);
        ajustarTexto();
        obtenerCategorias(tableLayout, ""); // Inicialmente muestra todas las categorías

        // Añadir el TextWatcher para el campo de búsqueda
        editBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtra las categorías en función del texto de búsqueda
                obtenerCategorias(tableLayout, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        delete.setOnClickListener(v -> {
            editBuscar.setText(""); // Limpia el texto del EditText
            obtenerCategorias((TableLayout) getView().findViewById(R.id.tableLayoutCategorias), ""); // Recargar categorías sin filtro
        });

        return view;
    }

    private void obtenerCategorias(TableLayout tableLayout, String searchQuery) {
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
                    // Filtrar las categorías basándose en la búsqueda
                    if (categoria.getCategoria().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            categoria.getDescripcion().toLowerCase().contains(searchQuery.toLowerCase())) {

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
                        final int originalColor = ContextCompat.getColor(getContext(), R.color.white);
                        final int pressedColor = ContextCompat.getColor(getContext(), R.color.select_color);
                        newRow.setOnLongClickListener(v -> {
                            newRow.setBackgroundColor(pressedColor);
                            showCustomDialog(categoria);
                            new Handler().postDelayed(() -> newRow.setBackgroundColor(originalColor), 500);
                            return true;
                        });

                        // Añadir la fila al TableLayout
                        tableLayout.addView(newRow);

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

    private void showCustomDialog(@Nullable Categoria categoria) {
        AlertDialogBase dialogFragment = new AlertDialogBase(R.layout.activity_crud_categoria, false);

        if (categoria != null) {
            Bundle args = new Bundle();
            args.putString("idCategoria", String.valueOf(categoria.getIdCategoria()));
            args.putString("categoria", categoria.getCategoria());
            args.putString("descripcion", categoria.getDescripcion());
            dialogFragment.setArguments(args);
        }
        dialogFragment.setDialogDismissListener(this); // Establece el listener
        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }
    public void onDialogDismissed() {
        TableLayout tableLayout = getView().findViewById(R.id.tableLayoutCategorias);
        if (tableLayout != null) {
            obtenerCategorias(tableLayout, ""); // Recargar productos al cerrar el diálogo
        }
    }
    private void ajustarTexto(){
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Lista de Categorias", true);
        }
    }
}

