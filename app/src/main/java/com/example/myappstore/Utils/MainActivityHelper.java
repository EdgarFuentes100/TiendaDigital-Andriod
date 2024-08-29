package com.example.myappstore.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myappstore.Activities.Fragment.FrDraweCliente;
import com.example.myappstore.Activities.Fragment.FrHistorialProducto;
import com.example.myappstore.Activities.Fragment.FrMiscomprasPoducto;
import com.example.myappstore.CLS.Pedido;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.R;
import com.example.myappstore.Activities.Fragment.AdminFragment;
import com.example.myappstore.Activities.Fragment.ClienteFragment;
import com.example.myappstore.Service.DetallePedidoService;
import com.example.myappstore.Service.PedidoService;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Response;

public class MainActivityHelper {

    private Context context;
    private TextView textViewName, textViewEmail, textViewCantidad;
    private ImageView imageViewMenu, imageUser;
    private SharedPreferences sharedPreferences;
    private FragmentTransactionHelper fragmentTransactionHelper;
    FragmentManager fragmentManager;

    public MainActivityHelper(Context context, TextView textViewName, TextView textViewEmail,TextView textViewCantidad , ImageView imageViewMenu, ImageView imageUser, FragmentManager fragmentManager) {
        this.context = context;
        this.textViewName = textViewName;
        this.textViewEmail = textViewEmail;
        this.imageViewMenu = imageViewMenu;
        this.textViewCantidad = textViewCantidad;
        this.imageUser = imageUser;
        this.sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        this.fragmentTransactionHelper = new FragmentTransactionHelper(fragmentManager);
        this.fragmentManager = fragmentManager;
    }

    public void displayUserData() {
        String name = sharedPreferences.getString("name", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String id = sharedPreferences.getString("id", "N/A");
        String aut = sharedPreferences.getString("aut", "N/A");
        String photoUrl = sharedPreferences.getString("photoUrl", null);
        obtenerPedidos(email);

        textViewName.setText((name != null ? name : "N/A"));
        textViewEmail.setText((email != null ? email : "N/A"));
        if (photoUrl != null && !photoUrl.isEmpty()) {
            Glide.with(context)
                    .load(photoUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(imageUser);
        }
    }

    public void setupPopupMenu() {
        imageViewMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item == popupMenu.getMenu().findItem(R.id.option1)) {
                    // Acción para "Mis Compras"
                    fragmentTransactionHelper.replaceFragment(new FrMiscomprasPoducto(), true);
                    return true;
                } else if (item == popupMenu.getMenu().findItem(R.id.option2)) {
                    // Acción para "Historial"
                    fragmentTransactionHelper.replaceFragment(new FrHistorialProducto(), true);
                    return true;
                } else if (item == popupMenu.getMenu().findItem(R.id.option3)) {
                    Toast.makeText(context, "AJUSTES", Toast.LENGTH_SHORT).show();
                    // Acción para "Ajustes"
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    public void loadAppropriateFragment() {
        Fragment fragment;
        if (shouldLoadAdminLayout()) {
            fragment = new AdminFragment();
            imageViewMenu.setVisibility(ImageView.GONE);
            textViewCantidad.setVisibility(TextView.GONE);

        } else {
            fragment = new ClienteFragment();
            imageViewMenu.setVisibility(ImageView.VISIBLE);
            replaceFragment(new FrDraweCliente(), false);
        }
        fragmentTransactionHelper.replaceFragment(fragment, false);
    }

    private boolean shouldLoadAdminLayout() {
        String rol = sharedPreferences.getString("rol", "N/A"); // Suponiendo que la clave es "role"
        return "admin".equals(rol);
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.draweContainer, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void obtenerPedidos(String email){
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
                String idPedido = "";
                if (response.size() > 0){
                    for (Pedido pedido : response){
                        idPedido = String.valueOf(pedido.getIdPedido());
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

            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
                // Puedes agregar alguna lógica si es necesario
            }

            @Override
            public void onResponseList(List<Map<String, Object>> response) {
                int cantidad = 0;

                for (Map<String, Object> item : response) {
                    cantidad++;
                }

                textViewCantidad.setText(String.valueOf(cantidad));
            }


            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }
}
