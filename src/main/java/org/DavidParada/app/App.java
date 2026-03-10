package org.DavidParada.app;

import org.DavidParada.config.DatosPrueba;
import org.DavidParada.controlador.*;
import org.DavidParada.excepcion.ValidationException;
import org.DavidParada.repositorio.implementacionMemoria.*;
import org.DavidParada.repositorio.interfaces.*;

import javax.swing.*;

public class App {

    public static void iniciar() throws ValidationException {

        // repositorios
        IUsuarioRepo usuarioRepo = new UsuarioRepoMemoria();
        IJuegoRepo juegoRepo = new JuegoRepoMemoria();
        IBibliotecaRepo bibliotecaRepo = new BibliotecaRepoMemoria();
        ICompraRepo compraRepo = new CompraRepoMemoria();
        IResenaRepo resenaRepo = new ResenaRepoMemoria();

        // controladores
        UsuarioControlador usuarioControlador = new UsuarioControlador(usuarioRepo);
        JuegoControlador juegoControlador = new JuegoControlador(juegoRepo);

        BibliotecaControlador bibliotecaControlador =
                new BibliotecaControlador(bibliotecaRepo, usuarioRepo, juegoRepo);

        CompraControlador compraControlador =
                new CompraControlador(compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, bibliotecaControlador);

        ResenaControlador resenaControlador =
                new ResenaControlador(resenaRepo, usuarioRepo, juegoRepo);

        // contexto
        AppContext ctx = new AppContext(
                usuarioControlador,
                juegoControlador,
                bibliotecaControlador,
                compraControlador,
                resenaControlador
        );

        // datos prueba
        DatosPrueba.cargarDatos(
                ctx.usuario(),
                ctx.juegos(),
                ctx.biblioteca(),
                ctx.compras(),
                ctx.resenas()
        );

        SwingUtilities.invokeLater(() -> UI.iniciar(ctx));
    }
}