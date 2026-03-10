package org.DavidParada.app;

import org.DavidParada.controlador.*;

public class AppContext {

    private final UsuarioControlador usuarioControlador;
    private final JuegoControlador juegoControlador;
    private final BibliotecaControlador bibliotecaControlador;
    private final CompraControlador compraControlador;
    private final ResenaControlador resenaControlador;

    public AppContext(
            UsuarioControlador usuarioControlador,
            JuegoControlador juegoControlador,
            BibliotecaControlador bibliotecaControlador,
            CompraControlador compraControlador,
            ResenaControlador resenaControlador
    ) {
        this.usuarioControlador = usuarioControlador;
        this.juegoControlador = juegoControlador;
        this.bibliotecaControlador = bibliotecaControlador;
        this.compraControlador = compraControlador;
        this.resenaControlador = resenaControlador;
    }

    public UsuarioControlador usuario() {
        return usuarioControlador;
    }

    public JuegoControlador juegos() {
        return juegoControlador;
    }

    public BibliotecaControlador biblioteca() {
        return bibliotecaControlador;
    }

    public CompraControlador compras() {
        return compraControlador;
    }

    public ResenaControlador resenas() {
        return resenaControlador;
    }
}
