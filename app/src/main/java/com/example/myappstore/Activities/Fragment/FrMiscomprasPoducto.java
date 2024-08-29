package com.example.myappstore.Activities.Fragment;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.myappstore.CLS.DetallePedido;
import com.example.myappstore.CLS.Pedido;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.MainActivity;
import com.example.myappstore.R;
import com.example.myappstore.Service.DetallePedidoService;
import com.example.myappstore.Service.PedidoService;
import com.example.myappstore.Utils.ImageLoaderTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Response;

public class FrMiscomprasPoducto extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ImageView imageViewLocation;
    private EditText editTextLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    private String idPedido;
    private GridLayout gridLayout;
    private Double subtotal = 0.0;
    private Map<Integer, Bitmap> imageCache = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_miscompras_productos, container, false);

        imageViewLocation = view.findViewById(R.id.imageViewLocation);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        gridLayout = view.findViewById(R.id.grid_layout);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        imageViewLocation.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Requesting location...", Toast.LENGTH_SHORT).show();
            getLocation();
        });
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "N/A");
        obtenerPedidos(email);
        ajustarTexto();
        return view;
    }

    private void ajustarTexto() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Mis Compras", false);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                convertLocationToAddress(location);
            } else {
                Toast.makeText(getContext(), "Location not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void convertLocationToAddress(Location location) {
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address address = addresses.get(0);

                // Get address components
                StringBuilder addressText = new StringBuilder();
                String street = address.getThoroughfare(); // Street address
                String city = address.getLocality(); // City
                String state = address.getAdminArea(); // State
                String country = address.getCountryName(); // Country
                String postalCode = address.getPostalCode(); // Postal code

                if (street != null) addressText.append(street).append(", ");
                if (city != null) addressText.append(city).append(", ");
                if (state != null) addressText.append(state).append(", ");
                if (country != null) addressText.append(country).append(", ");
                if (postalCode != null) addressText.append(postalCode).append(", ");

                // Remove the trailing comma and space
                if (addressText.length() > 2) {
                    addressText.setLength(addressText.length() - 2);
                }

                // Set the address in the EditText
                editTextLocation.setText(addressText.toString());
                Toast.makeText(getContext(), "Address: " + addressText.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "No address found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Geocoder failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                getLocation();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void obtenerPedidos(String email){
        PedidoService ps = new PedidoService();
        ps.obtenerPedidosUsuario(email, new CallBackApi<Pedido>() {
            @Override
            public void onResponse(Pedido response) {

            }

            @Override
            public void onResponseBool(Response<Boolean> response) {

            }

            @Override
            public void onResponseList(List<Pedido> response) {
                if (response.size() > 0){
                    for (Pedido pedido : response){
                        idPedido = String.valueOf( pedido.getIdPedido());
                        break;
                    }
                    obtenerDetalles(idPedido);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
    private void obtenerDetalles(String idPedido) {
        DetallePedidoService ds = new DetallePedidoService();
        ds.obtenerDetallePedido(idPedido, new CallBackApi<Map<String, Object>>() {
            @Override
            public void onResponse(Map<String, Object> response) {
                // Puedes agregar alguna lógica si es necesario
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
                // Puedes agregar alguna lógica si es necesario
            }

            @Override
            public void onResponseList(List<Map<String, Object>> response) {
                int cantidad = 0;
                gridLayout.removeAllViews();
                double total = 0.0;

                for (Map<String, Object> item : response) {
                    cantidad++;
                    // Infla el diseño de cada ítem
                    LinearLayout itemView = (LinearLayout) getLayoutInflater().inflate(R.layout.item_miscompras, gridLayout, false);

                    // Configura los datos del ítem
                    configureItemView(itemView, item);

                    // Agrega la vista al GridLayout
                    gridLayout.addView(itemView);
                }
                TextView textViewTotal = getView().findViewById(R.id.textViewTotal);
                if (textViewTotal != null) {
                    textViewTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", subtotal));
                }
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.updateTextCantidad(String.valueOf(cantidad));
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Error al obtener detalles: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void configureItemView(LinearLayout itemView, Map<String, Object> item) {
        TextView nombreTextView = itemView.findViewById(R.id.item_name);
        TextView descripcionTextView = itemView.findViewById(R.id.item_description);
        TextView precioTextView = itemView.findViewById(R.id.item_price);
        TextView cantidadTextView = itemView.findViewById(R.id.item_quantity);
        TextView totalTextView = itemView.findViewById(R.id.item_total_price);
        ImageView imagenView = itemView.findViewById(R.id.item_image);
        ImageView item_increment = itemView.findViewById(R.id.item_increment);
        ImageView item_decrement = itemView.findViewById(R.id.item_decrement);

        // Extrae los datos del mapa
        Integer idDetalle = ((Double) item.get("idDetalle")).intValue();
        String nombre = (String) item.get("nombre");
        String descripcion = (String) item.get("descripcion");
        String precio = (String) item.get("precio");
        Double cantidad = (Double) item.get("cantidad");
        String image = (String) item.get("image");

        // Configura los TextViews
        nombreTextView.setText(nombre);
        descripcionTextView.setText(descripcion);
        precioTextView.setText(precio);
        cantidadTextView.setText("Cantidad: " + (cantidad != null ? cantidad.intValue() : 0));
        Double precioDouble = Double.parseDouble(precioTextView.getText().toString());
        Double total = precioDouble * cantidad;
        totalTextView.setText("Total: " + (total != null ? total.intValue() : 0));
        subtotal = total + subtotal;
        // Configura la imagen
        if (imageCache.containsKey(idDetalle)) {
            // Imagen ya cargada, usa la imagen almacenada en el cache
            imagenView.setImageBitmap(imageCache.get(idDetalle));
        } else {
            // Carga la imagen en segundo plano
            new ImageLoaderTask(new ImageLoaderTask.ImageLoaderCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    // Guarda la imagen en el cache
                    imageCache.put(idDetalle, bitmap);
                    imagenView.setImageBitmap(bitmap);
                }
            }).execute(image);
        }

        DetallePedido detallePedido = new DetallePedido();
        int idDet = idDetalle;
        int Can = cantidad != null ? cantidad.intValue() : 0;
        detallePedido.setIdDetalle(idDet);

        item_increment.setOnClickListener(v -> {
            if (Can > 0) {
                detallePedido.setCantidad(Can + 1);
                actualizarCantidad(detallePedido);
            }
        });

        item_decrement.setOnClickListener(v -> {
            if (Can > 1) {
                detallePedido.setCantidad(Can - 1);
                actualizarCantidad(detallePedido);
            } else {
                eliminarDetalle(idDet);
            }
        });
    }

    private void actualizarCantidad(DetallePedido detallePedido){
        subtotal = 0.0;
        DetallePedidoService ds = new DetallePedidoService();
        ds.actualizarCantidad(detallePedido, new CallBackApi<DetallePedido>() {
            @Override
            public void onResponse(DetallePedido response) {
                int idDetalle = response.getIdDetalle();
                if (idDetalle != 0){
                    obtenerDetalles(idPedido);
                }
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {

            }

            @Override
            public void onResponseList(List<DetallePedido> response) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

    };

    private void eliminarDetalle(Integer idDetalle){
        subtotal = 0.0;
        DetallePedidoService dp = new DetallePedidoService();
        dp.eliminarDetalle(idDetalle, new CallBackApi<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                if (response == true){
                    //Toast.makeText(getContext(), "Elemento borrado", Toast.LENGTH_SHORT).show();
                    obtenerDetalles(idPedido);

                }
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {

            }

            @Override
            public void onResponseList(List<Boolean> response) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
