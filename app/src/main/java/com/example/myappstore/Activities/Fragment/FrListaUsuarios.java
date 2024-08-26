package com.example.myappstore.Activities.Fragment;
import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Usuario;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.MainActivity;
import com.example.myappstore.Service.UsuarioService;
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

public class FrListaUsuarios extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_usuarios, container, false);

        ImageView imageView = view.findViewById(R.id.idElementoUsuario);
        imageView.setOnClickListener(v -> showCustomDialog(null));
        TableLayout tableLayout = view.findViewById(R.id.tableLayoutUsuarios);
        ajustarTexto();
        obtenerUsuarios(tableLayout);
        return view;
    }

    private void showCustomDialog(@Nullable Usuario usuario) {
        AlertDialogBase dialogFragment = new AlertDialogBase(R.layout.activity_crud_usuarios, false);

        if (usuario != null) {
            Bundle args = new Bundle();
            args.putString("idUsuario", (String.valueOf(usuario.getIdUsuario())));
            args.putString("nombre", usuario.getNombre());
            args.putString("email", usuario.getCorreo());
            args.putInt("idRol", usuario.getIdRol());
            args.putString("rol", usuario.getRol());
            dialogFragment.setArguments(args);
        }
        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }

    private  void obtenerUsuarios(TableLayout tableLayout){
        UsuarioService us = new UsuarioService();
        us.obtenerUsuarios(new CallBackApi<Usuario>() {
            @Override
            public void onResponse(Usuario response) {

            }

            @Override
            public void onResponseBool(Response<Boolean> response) {

            }

            @Override
            public void onResponseList(List<Usuario> usuarios) {
                // Limpia las filas existentes si es necesario
                tableLayout.removeAllViews();

                // Inflar la fila desde el archivo XML
                LayoutInflater inflater = LayoutInflater.from(getContext());

                for (Usuario usuario : usuarios) {
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
                    final int originalColor = ContextCompat.getColor(getContext(), R.color.white); // Cambia el color original según tu diseño
                    final int pressedColor = ContextCompat.getColor(getContext(), R.color.select_color); // Color al mantener presionado
                    newRow.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // Cambia el color de fondo al mantener presionado
                            newRow.setBackgroundColor(pressedColor);
                            showCustomDialog(usuario);
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

            }
        });
    }
    private void ajustarTexto(){
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Lista de Usuarios", false);
        }
    }
}
