package org.DavidParada.vista.panel;

import org.DavidParada.vista.componente.BotonPrincipal;
import org.DavidParada.vista.util.Navegador;
import org.DavidParada.vista.util.Sesion;

import javax.swing.*;
import java.awt.*;

public class PanelInicio extends JPanel {

    public PanelInicio(Navegador nav, PanelMenu panelMenu){

        setLayout(new GridLayout(3, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        add(new BotonPrincipal("Iniciar sesión", 200, 50, e ->
                nav.irA("login")));

        add(new BotonPrincipal("Registrarse", 200, 50, e ->
                nav.irA("registro")));

        add(new BotonPrincipal("Continuar como invitado", 200, 50, e -> {
            Sesion.iniciarComoInvitado();
            panelMenu.refrescar();
            nav.irA("menu");
        }));
    }
}
