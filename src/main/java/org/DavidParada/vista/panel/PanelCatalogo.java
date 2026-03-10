package org.DavidParada.vista.panel;

import org.DavidParada.controlador.JuegoControlador;
import org.DavidParada.modelo.dto.JuegoDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PanelCatalogo extends JPanel {

    private JuegoControlador juegoControlador;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelCatalogo(JuegoControlador juegoControlador) {
        this.juegoControlador = juegoControlador;

        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(
                new String[]{"Título", "Precio", "Estado"}, 0);

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() {
        List<JuegoDto> juegos =
                juegoControlador.consultarCatalogo(null);

        for (JuegoDto j : juegos) {
            modelo.addRow(new Object[]{
                    j.titulo(),
                    j.precioBase(),
                    j.estado()
            });
        }
    }
}
