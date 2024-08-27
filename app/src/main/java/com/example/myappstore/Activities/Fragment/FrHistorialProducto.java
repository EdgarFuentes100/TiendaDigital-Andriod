package com.example.myappstore.Activities.Fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myappstore.CLS.Pedido;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.R;
import com.example.myappstore.Service.PedidoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

public class FrHistorialProducto extends Fragment {

    private GridLayout gridLayout;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private ImageView imageViewClearFilter;
    private List<Pedido> allPedidos = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_historial_productos, container, false);

        gridLayout = view.findViewById(R.id.grid_layout);
        textViewStartDate = view.findViewById(R.id.textViewStartDate);
        textViewEndDate = view.findViewById(R.id.textViewEndDate);
        imageViewClearFilter = view.findViewById(R.id.imageViewClearFilter);

        textViewStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        textViewEndDate.setOnClickListener(v -> showDatePickerDialog(false));
        imageViewClearFilter.setOnClickListener(v -> clearFilter());

        obtenerHistorial(); // Cargar todos los pedidos inicialmente

        return view;
    }

    private void showDatePickerDialog(boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDay);
            String date = dateFormat.format(selectedDate.getTime());

            if (isStartDate) {
                textViewStartDate.setText(date);
            } else {
                textViewEndDate.setText(date);
            }

            // Filtrar resultados cuando la fecha es seleccionada
            filtrarPorFechas();
        }, year, month, day);

        datePickerDialog.show();
    }

    private void filtrarPorFechas() {
        String startDate = textViewStartDate.getText().toString();
        String endDate = textViewEndDate.getText().toString();

        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            // No realizar filtrado si alguna fecha está vacía
            return;
        }

        List<Pedido> filteredPedidos = new ArrayList<>();
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            for (Pedido pedido : allPedidos) {
                Date pedidoDate = dateFormat.parse(pedido.getFechaPedido());
                if (pedidoDate != null && !pedidoDate.before(start) && !pedidoDate.after(end)) {
                    filteredPedidos.add(pedido);
                }
            }

            actualizarGridLayout(filteredPedidos);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void clearFilter() {
        textViewStartDate.setText("");
        textViewEndDate.setText("");
        actualizarGridLayout(allPedidos); // Mostrar todos los pedidos nuevamente
    }

    private void obtenerHistorial() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "N/A");

        PedidoService ps = new PedidoService();
        ps.obtenerHistorialsuario(email, new CallBackApi<Pedido>() {
            @Override
            public void onResponse(Pedido response) {
                // Implementar si necesitas manejar la respuesta individual
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
                // Implementar si necesitas manejar una respuesta booleana
            }

            @Override
            public void onResponseList(List<Pedido> response) {
                allPedidos = response; // Guardar todos los pedidos
                actualizarGridLayout(allPedidos); // Mostrar todos los pedidos inicialmente
            }

            @Override
            public void onFailure(String errorMessage) {
                // Manejar el error de la llamada a la API
            }
        });
    }

    private void actualizarGridLayout(List<Pedido> pedidos) {
        gridLayout.removeAllViews();

        // Inflar y agregar cada pedido al GridLayout
        for (Pedido pedido : pedidos) {
            View itemView = crearItemPedido(pedido);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // Ancho de la columna
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(5, 5, 5, 5); // Margen alrededor de cada elemento
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Cada item ocupa 1 columna con peso
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED); // Fila indefinida
            itemView.setLayoutParams(params);
            gridLayout.addView(itemView);
        }
    }

    private View crearItemPedido(Pedido pedido) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View itemView = inflater.inflate(R.layout.item_historial_pedido, null, false);

        TextView idPedido = itemView.findViewById(R.id.textViewIdPedido);
        TextView correo = itemView.findViewById(R.id.textViewCorreo);
        TextView fechaPedido = itemView.findViewById(R.id.textViewFechaPedido);
        TextView estado = itemView.findViewById(R.id.textViewEstado);
        TextView total = itemView.findViewById(R.id.textViewTotal);

        idPedido.setText("ID Pedido: " + pedido.getIdPedido());
        fechaPedido.setText("Fecha: " + pedido.getFechaPedido());
        estado.setText("Estado: " + pedido.getEstado());
        total.setText("$" + pedido.getTotal());

        return itemView;
    }
}
