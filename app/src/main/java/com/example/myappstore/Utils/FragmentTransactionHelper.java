package com.example.myappstore.Utils;
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

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(CONTAINER_ID, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
