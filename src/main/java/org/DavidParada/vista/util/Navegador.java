package org.DavidParada.vista.util;

import org.DavidParada.vista.principal.VentanaPrincipal;

public class Navegador {

    private VentanaPrincipal ventana;

    public Navegador(VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    public void irA(String panel) {
        ventana.mostrar(panel);
    }
}
