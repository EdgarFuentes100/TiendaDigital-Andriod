package com.example.myappstore.Utils;

import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 30; // Reduce el umbral para mayor sensibilidad
    private static final int SWIPE_VELOCITY_THRESHOLD = 50;
    private static final int MAX_SWIPE_DISTANCE = 300; // Límite máximo de desplazamiento
    private View targetView;
    private Drawable originalBackground; // Para restaurar el fondo original

    // Constructor público
    public SwipeGestureListener(View targetView) {
        this.targetView = targetView;
        // Guardar el fondo original
        this.originalBackground = targetView.getBackground();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // Verifica si el desplazamiento horizontal supera el umbral
        if (Math.abs(distanceX) > SWIPE_THRESHOLD && Math.abs(distanceY) < SWIPE_THRESHOLD) {
            float newTranslationX = targetView.getTranslationX() - distanceX;
            // Limita el desplazamiento horizontal
            newTranslationX = Math.max(0, Math.min(newTranslationX, MAX_SWIPE_DISTANCE));
            targetView.setTranslationX(newTranslationX);
            // Cambia el color de fondo mientras se desliza
            targetView.setBackgroundColor(0x80CCCCCC); // Color gris claro con opacidad
            return true;
        }
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Restaurar el color de fondo original
        targetView.setBackground(originalBackground);

        // Crear una animación para mover el elemento de vuelta a su posición original
        animateReset(targetView);

        // Si el elemento se mueve más de la mitad de su ancho, realiza una acción
        if (targetView.getTranslationX() > targetView.getWidth() / 2) {
            // Realiza una acción si se deslizó lo suficiente
            showSwipeMessage();
        }

        return true;
    }

    private void showSwipeMessage() {
        Toast.makeText(targetView.getContext(), "Elemento deslizado a la izquierda", Toast.LENGTH_SHORT).show();
    }

    private void animateReset(View view) {
        // Crear una animación para mover el elemento de vuelta a su posición original
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), 0);
        animator.setDuration(300); // Duración de la animación
        animator.start();
    }
}
