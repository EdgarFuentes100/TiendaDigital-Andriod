package com.example.myappstore.Activities.Configurators;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myappstore.CLS.DetallePedido;
import com.example.myappstore.CLS.Pedido;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.MainActivity;
import com.example.myappstore.R;
import com.example.myappstore.Service.DetallePedidoService;
import com.example.myappstore.Service.ImagenService;
import com.example.myappstore.Https.OnImageLoadListener;
import com.example.myappstore.Service.PedidoService;
import com.example.myappstore.Utils.ImagePagerAdapter;

import java.util.List;

import retrofit2.Response;

public class FrProgramDialogProducto extends BaseConfigurator {
    private ImageView imageView;
    private ViewPager2 viewPager;
    private String idPedido;
    private TextView nombre, descripcion, precio, cantidad;
    private Integer idProducto;
    private  SharedPreferences sharedPreferences;
    DialogFragment dialogFragment;

    @Override
    public void configureDialog(View dialogView, DialogFragment dialogFragment) {
        Button acceptButton = dialogView.findViewById(R.id.acceptButton);
        viewPager = dialogView.findViewById(R.id.viewPager);
        imageView = dialogView.findViewById(R.id.closeIcon);
        this.dialogFragment = dialogFragment;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogFragment != null) {
                    dialogFragment.dismiss(); // Cierra solo el diálogo
                }
            }
        });

        if (acceptButton != null) {
            acceptButton.setOnClickListener(v -> {
                String cantidadText = cantidad.getText().toString().trim(); // Usa trim() para eliminar espacios en blanco
                if (cantidadText.isEmpty()) { // Verifica si el campo está vacío
                    // Mostrar un mensaje de error o realizar una acción adecuada
                    Toast.makeText(viewPager.getContext(), "Por favor, ingrese una cantidad.", Toast.LENGTH_SHORT).show();
                } else {
                    // Convertir cantidad a entero y continuar con la lógica
                    sharedPreferences = viewPager.getContext().getSharedPreferences("UserPrefs", viewPager.getContext().MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "N/A");
                    obtenerPedidos(email);
                }
            });
        }
    }

    @Override
    public void configureDialogWithArguments(View dialogView, Bundle arguments) {
        if (arguments != null) {
             nombre = dialogView.findViewById(R.id.productName);
             descripcion = dialogView.findViewById(R.id.productDescription);
             precio = dialogView.findViewById(R.id.productPrice);
             cantidad = dialogView.findViewById(R.id.productQuantity);

            if (nombre != null) {
                nombre.setText(arguments.getString("nombre"));
            }
            if (descripcion != null) {
                descripcion.setText(arguments.getString("descripcion"));
            }
            if (precio != null) {
                precio.setText(String.format("$%.2f", arguments.getDouble("precio")));
            }
            if (cantidad != null) {
                cantidad.setText("1");
            }

            String idProductoString = arguments.getString("idProducto");
            idProducto = Integer.parseInt(idProductoString); // Convertir de String a int
            // Usar idProducto como int
            obtenerImagenes(idProducto);
        }
    }

    private void obtenerImagenes(int idProducto) {
        ImagenService is = new ImagenService();
        is.obtenerImagenes(idProducto, new OnImageLoadListener() {
            @Override
            public void onImagesLoad(List<Bitmap> bitmaps) {
                if (bitmaps != null && !bitmaps.isEmpty()) {
                    ImagePagerAdapter adapter = new ImagePagerAdapter(bitmaps);
                    viewPager.setAdapter(adapter);
                } else {
                    Toast.makeText(imageView.getContext(), "No hay imágenes disponibles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onImagesLoadError(String errorMessage) {
                Toast.makeText(imageView.getContext(), "No hay imágenes disponibles", Toast.LENGTH_SHORT).show();
            }
        });
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
                if (response.size() > 0 ) {
                    for (Pedido pedido : response){
                        idPedido = String.valueOf( pedido.getIdPedido());
                        //obtener detalles, aca buscariamos el id del producto
                        break;
                    }
                    obtenerDetalles(idPedido);

                }else {
                    String idUsuario = sharedPreferences.getString("idUsuario", "N/A");

                    Pedido pedido = new Pedido();
                    pedido.setIdUsuario(Integer.parseInt(idUsuario));
                    pedido.setFechaPedido("2024-08-23 10:00:00");
                    pedido.setEstado("pendiente");
                    int cantidadValue = Integer.parseInt(cantidad.getText().toString());
                    Double precio = Double.parseDouble(cantidad.getText().toString());
                    double total = (cantidadValue * precio);
                    pedido.setTotal(total);
                    insertarPedido(pedido);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void obtenerDetalles(String idPedido){
        DetallePedidoService ds = new DetallePedidoService();
        ds.obtenerDetalleSinFoto(idPedido, new CallBackApi<DetallePedido>() {
            @Override
            public void onResponse(DetallePedido response) {

            }

            @Override
            public void onResponseBool(Response<Boolean> response) {

            }

            @Override
            public void onResponseList(List<DetallePedido> response) {
                int cantidadEncontrada = response.size();
                Boolean existe = false;
                int encontrado = 0;
                int cantidadActual = 0;
                if (response.size() > 0 ) {
                    for (DetallePedido detallePedido : response){
                        if (idProducto == detallePedido.getIdProducto()){
                            encontrado = detallePedido.getIdDetalle();
                            cantidadActual = detallePedido.getCantidad();
                            existe = true;
                            break;
                        }
                    }
                    if (existe){
                        int cantidadValue = Integer.parseInt(cantidad.getText().toString());
                        DetallePedido detallePedido = new DetallePedido();
                        detallePedido.setIdDetalle(encontrado);
                        detallePedido.setCantidad(cantidadActual + cantidadValue);
                        actualizarCantidad(detallePedido);
                    }else{
                        DetallePedido detalle = new DetallePedido();
                        detalle.setIdPedido(Integer.parseInt(idPedido));
                        detalle.setIdProducto(idProducto);
                        int cantidadValue = Integer.parseInt(cantidad.getText().toString());
                        Double precio = Double.parseDouble(cantidad.getText().toString());
                        detalle.setCantidad(cantidadValue);
                        detalle.setPrecio(precio);
                        insertarDetalles(detalle, cantidadEncontrada);
                    }
                }else{
                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdPedido(Integer.parseInt(idPedido));
                    detalle.setIdProducto(idProducto);
                    int cantidadValue = Integer.parseInt(cantidad.getText().toString());
                    Double precio = Double.parseDouble(cantidad.getText().toString());
                    detalle.setCantidad(cantidadValue);
                    detalle.setPrecio(precio);
                    insertarDetalles(detalle, cantidadEncontrada);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(viewPager.getContext(), "aca", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarCantidad(DetallePedido detallePedido){
        DetallePedidoService ds = new DetallePedidoService();
        ds.actualizarCantidad(detallePedido, new CallBackApi<DetallePedido>() {
            @Override
            public void onResponse(DetallePedido response) {
                int idDetalle = response.getIdDetalle();
                if (idDetalle != 0){
                    String mensaje = "¡Se agregaron mas productos a su pedido! 🎉";
                    Toast.makeText(viewPager.getContext(), mensaje, Toast.LENGTH_LONG).show();
                    if (dialogFragment != null) {
                        dialogFragment.dismiss(); // Cierra solo el diálogo
                    }
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

    private void insertarDetalles(DetallePedido detalle, int cantidad){
        DetallePedidoService ds = new DetallePedidoService();
        ds.insertarDetalle(detalle, new CallBackApi<DetallePedido>() {
            @Override
            public void onResponse(DetallePedido response) {
                int idDetalle = response.getIdDetalle();
                if (idDetalle != 0){
                    if (dialogFragment.getActivity() instanceof MainActivity) {
                        MainActivity mainActivity = (MainActivity)  dialogFragment.getActivity();
                        mainActivity.updateTextCantidad(String.valueOf(cantidad+1));
                    }
                    String mensaje = "¡Gracias por su compra! 🎉";
                    Toast.makeText(viewPager.getContext(), mensaje, Toast.LENGTH_LONG).show();
                    if (dialogFragment != null) {
                        dialogFragment.dismiss(); // Cierra solo el diálogo
                    }
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
    }
     private void insertarPedido(Pedido pedido){
        PedidoService ps = new PedidoService();
        ps.insertarPedido(pedido, new CallBackApi<Pedido>() {
            @Override
            public void onResponse(Pedido response) {
                if (response != null) {
                    String email = sharedPreferences.getString("email", "N/A");
                    obtenerPedidos(email);
                } else {
                    // Manejar el caso cuando la respuesta es null
                    Toast.makeText(imageView.getContext(), "Respuesta vacía del servidor.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
                Toast.makeText(imageView.getContext(), "haol", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseList(List<Pedido> response) {

                for (Pedido ped: response){
                    ped.getIdPedido();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(imageView.getContext(), "haol", Toast.LENGTH_SHORT).show();
            }
        });
     }
}
