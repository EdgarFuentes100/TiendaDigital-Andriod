package com.example.myappstore.Activities.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myappstore.MainActivity;
import com.example.myappstore.R;
import com.example.myappstore.Utils.FragmentTransactionHelper;

public class FrDraweCliente extends Fragment {

    private LinearLayout linearLayout, linearLayout1;
    private FragmentTransactionHelper fragmentTransactionHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_drawe_cliente, container, false);
        linearLayout = view.findViewById(R.id.idVerProducto);
        linearLayout1 = view.findViewById(R.id.idVerCategroia);
        // Inicializa el FragmentTransactionHelper
        fragmentTransactionHelper = new FragmentTransactionHelper(getParentFragmentManager());

        linearLayout.setOnClickListener(v -> openDetailFragment(new FrClienteProducto()));
        linearLayout1.setOnClickListener(v -> openDetailFragment(new ClienteFragment()));

        return view;
    }

    private void openDetailFragment(Fragment fragment) {
        if (fragmentTransactionHelper != null) {
            fragmentTransactionHelper.replaceFragment(fragment, true);
        } else {
            Toast.makeText(getContext(), "FragmentTransactionHelper no está inicializado", Toast.LENGTH_SHORT).show();
        }
    }
}
