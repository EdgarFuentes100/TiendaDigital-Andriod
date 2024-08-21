package com.example.myappstore.Activities.Fragment;
import  com.example.myappstore.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AdminFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frame_admin, container, false);

        LinearLayout linearLayout = view.findViewById(R.id.IdCrudCategoria);
        LinearLayout linearLayout1 = view.findViewById(R.id.IdCrudProducto);
        LinearLayout linearLayout2 = view.findViewById(R.id.IdCrudUsuario);

        linearLayout.setOnClickListener(v -> openDetailFragment());
        linearLayout1.setOnClickListener(v -> openDetailFragment1());
        linearLayout2.setOnClickListener(v -> openDetailFragment2());

        return view;
    }
    private void openDetailFragment() {
        Fragment fragment = new FrListaCategoria(); // Usa el fragmento simple para pruebas
        FragmentManager fragmentManager = getParentFragmentManager(); // Usa getParentFragmentManager() o requireActivity().getSupportFragmentManager()
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
        transaction.commit();
    }
    private void openDetailFragment1() {
        Fragment fragment = new FrListaProductos(); // Usa el fragmento simple para pruebas
        FragmentManager fragmentManager = getParentFragmentManager(); // Usa getParentFragmentManager() o requireActivity().getSupportFragmentManager()
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
        transaction.commit();
    }
    private void openDetailFragment2() {
        Fragment fragment = new FrListaUsuarios(); // Usa el fragmento simple para pruebas
        FragmentManager fragmentManager = getParentFragmentManager(); // Usa getParentFragmentManager() o requireActivity().getSupportFragmentManager()
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
        transaction.commit();
    }
}
