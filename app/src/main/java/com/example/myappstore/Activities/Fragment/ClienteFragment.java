package com.example.myappstore.Activities.Fragment;
import com.example.myappstore.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ClienteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frame_cliente, container, false);

        // Configura el botón para abrir el fragmento
        Button openFragmentButton = view.findViewById(R.id.open_fragment_button);
        openFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailFragment();
            }
        });

        return view;
    }

    private void openDetailFragment() {
        Fragment fragment = new FrClienteProducto(); // Usa el fragmento simple para pruebas
        FragmentManager fragmentManager = getParentFragmentManager(); // Usa getParentFragmentManager() o requireActivity().getSupportFragmentManager()
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
        transaction.commit();
    }
}
