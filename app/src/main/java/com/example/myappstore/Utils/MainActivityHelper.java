package com.example.myappstore.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myappstore.Activities.Fragment.FrHistorialProducto;
import com.example.myappstore.Activities.Fragment.FrMiscomprasPoducto;
import com.example.myappstore.R;
import com.example.myappstore.Activities.Fragment.AdminFragment;
import com.example.myappstore.Activities.Fragment.ClienteFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivityHelper {

    private Context context;
    private TextView textViewName, textViewEmail, textViewId, textViewAut;
    private ImageView imageViewMenu, imageUser;
    private SharedPreferences sharedPreferences;
    private FragmentTransactionHelper fragmentTransactionHelper;

    public MainActivityHelper(Context context, TextView textViewName, TextView textViewEmail, TextView textViewId, TextView textViewAut, ImageView imageViewMenu, ImageView imageUser, FragmentManager fragmentManager) {
        this.context = context;
        this.textViewName = textViewName;
        this.textViewEmail = textViewEmail;
        this.textViewId = textViewId;
        this.textViewAut = textViewAut;
        this.imageViewMenu = imageViewMenu;
        this.imageUser = imageUser;
        this.sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        this.fragmentTransactionHelper = new FragmentTransactionHelper(fragmentManager);
    }

    public void displayUserData() {
        String name = sharedPreferences.getString("name", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String id = sharedPreferences.getString("id", "N/A");
        String aut = sharedPreferences.getString("aut", "N/A");
        String photoUrl = sharedPreferences.getString("photoUrl", null);

        textViewName.setText((name != null ? name : "N/A"));
        textViewEmail.setText("Email: " + (email != null ? email : "N/A"));
        textViewId.setText("Id: " + (id != null ? id : "N/A"));
        textViewAut.setText("Aut: " + (aut != null ? aut : "N/A"));
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
        } else {
            fragment = new ClienteFragment();
            imageViewMenu.setVisibility(ImageView.VISIBLE);
        }
        fragmentTransactionHelper.replaceFragment(fragment, false);
    }

    private boolean shouldLoadAdminLayout() {
        String name = sharedPreferences.getString("name", "N/A");
        return "Edgar Fuentes".equals(name);
    }
}
