package com.example.myappstore.Activities.Fragment;

import com.example.myappstore.CLS.Usuario;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Interface.Utils.DialogDismissListener;
import com.example.myappstore.MainActivity;
import com.example.myappstore.Service.UsuarioService;
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

public class FrListaUsuarios extends Fragment implements DialogDismissListener {

    private EditText editBuscarUsuarios;
    private ImageView delete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_usuarios, container, false);

        ImageView imageView = view.findViewById(R.id.idElementoUsuario);
        imageView.setOnClickListener(v -> showCustomDialog(null));
        TableLayout tableLayout = view.findViewById(R.id.tableLayoutUsuarios);
        delete = view.findViewById(R.id.delete);
        editBuscarUsuarios = view.findViewById(R.id.buscarUsuarios); // Asegúrate de tener un EditText con este ID
        ajustarTexto();
        obtenerUsuarios(tableLayout, ""); // Inicialmente muestra todos los usuarios

        // Añadir el TextWatcher para el campo de búsqueda
        editBuscarUsuarios.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtra los usuarios en función del texto de búsqueda
                obtenerUsuarios(tableLayout, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Configurar el botón de eliminar para borrar el texto del EditText
        delete.setOnClickListener(v -> {
            editBuscarUsuarios.setText(""); // Limpia el texto del EditText
            obtenerUsuarios(tableLayout, ""); // Recargar productos sin filtro
        });

        return view;
    }

    private void showCustomDialog(@Nullable Usuario usuario) {
        AlertDialogBase dialogFragment = new AlertDialogBase(R.layout.activity_crud_usuarios, false);

        if (usuario != null) {
            Bundle args = new Bundle();
            args.putString("idUsuario", String.valueOf(usuario.getIdUsuario()));
            args.putString("nombre", usuario.getNombre());
            args.putString("email", usuario.getCorreo());
            args.putInt("idRol", usuario.getIdRol());
            args.putString("rol", usuario.getRol());
            dialogFragment.setArguments(args);
        }
        dialogFragment.setDialogDismissListener(this);
        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }

    private void obtenerUsuarios(TableLayout tableLayout, String searchQuery) {
        UsuarioService us = new UsuarioService();

        us.obtenerUsuarios(new CallBackApi<Usuario>() {
            @Override
            public void onResponse(Usuario response) {}

            @Override
            public void onResponseBool(Response<Boolean> response) {}

            @Override
            public void onResponseList(List<Usuario> usuarios) {
                // Limpia las filas existentes si es necesario
                tableLayout.removeAllViews();

                // Inflar la fila desde el archivo XML
                LayoutInflater inflater = LayoutInflater.from(getContext());

                for (Usuario usuario : usuarios) {
                    // Filtrar los usuarios basándose en la búsqueda
                    if (usuario.getNombre().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            usuario.getCorreo().toLowerCase().contains(searchQuery.toLowerCase())) {

                        // Inflar la fila
                        TableRow newRow = (TableRow) inflater.inflate(R.layout.table_row_usuarios, tableLayout, false);

                        // Configurar los datos de la fila
                        TextView textViewId = newRow.findViewById(R.id.textViewId);
                        TextView textViewUsuario = newRow.findViewById(R.id.textViewUsuario);
                        TextView textViewEmail = newRow.findViewById(R.id.textViewEmail);
                        TextView textViewRol = newRow.findViewById(R.id.textViewRol);

                        // Asignar datos a la fila
                        textViewId.setText(String.valueOf(usuario.getIdUsuario()));
                        textViewUsuario.setText(usuario.getNombre());
                        textViewEmail.setText(usuario.getCorreo());
                        textViewRol.setText(usuario.getRol());

                        // Configura el touch listener para la fila
                        final int originalColor = ContextCompat.getColor(getContext(), R.color.white);
                        final int pressedColor = ContextCompat.getColor(getContext(), R.color.select_color);
                        newRow.setOnLongClickListener(v -> {
                            newRow.setBackgroundColor(pressedColor);
                            showCustomDialog(usuario);
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
                Toast.makeText(getContext(), "Error al cargar usuarios: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDialogDismissed() {
        TableLayout tableLayout = getView().findViewById(R.id.tableLayoutUsuarios);
        if (tableLayout != null) {
            obtenerUsuarios(tableLayout, editBuscarUsuarios.getText().toString()); // Recargar usuarios al cerrar el diálogo
        }
    }

    private void ajustarTexto() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Lista de Usuarios", false);
        }
    }
}
