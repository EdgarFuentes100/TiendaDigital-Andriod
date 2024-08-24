package com.example.myappstore.Activities.Configurators;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.DialogFragment;

public abstract class BaseConfigurator {
    public abstract void configureDialog(View dialogView, DialogFragment dialogFragment);
    public void configureDialogWithArguments(View dialogView, Bundle arguments) {
        // Implementación opcional en los configuradores concretos
    }
}
