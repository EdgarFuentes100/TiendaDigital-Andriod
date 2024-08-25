package com.example.myappstore.Utils;

import android.os.Bundle;
import com.example.myappstore.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentTransactionHelper {

    private FragmentManager fragmentManager;
    private static final int CONTAINER_ID = R.id.frameContainer; // Cambia esto al ID de tu contenedor

    public FragmentTransactionHelper(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    // Método con Bundle opcional
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        replaceFragment(fragment, addToBackStack, null);
    }

    // Método con Bundle
    public void replaceFragment(Fragment fragment, boolean addToBackStack, Bundle args) {
        // Verificar si el fragment ya está agregado
        Fragment existingFragment = fragmentManager.findFragmentById(CONTAINER_ID);

        if (existingFragment != null && existingFragment.getClass().equals(fragment.getClass())) {
            // El fragment ya está agregado y es del mismo tipo, solo mostrarlo
            return;
        }

        if (args != null) {
            fragment.setArguments(args);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(CONTAINER_ID, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
