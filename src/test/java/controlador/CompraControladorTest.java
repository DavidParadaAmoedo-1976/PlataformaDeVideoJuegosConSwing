package controlador;

import org.DavidParada.controlador.BibliotecaControlador;
import org.DavidParada.controlador.CompraControlador;
import org.DavidParada.excepcion.ValidationException;
import org.DavidParada.modelo.dto.CompraDto;
import org.DavidParada.modelo.dto.DetallesCompraDto;
import org.DavidParada.modelo.dto.FacturaDto;
import org.DavidParada.modelo.enums.*;
import org.DavidParada.modelo.formulario.CompraForm;
import org.DavidParada.modelo.formulario.JuegoForm;
import org.DavidParada.modelo.formulario.UsuarioForm;
import org.DavidParada.repositorio.implementacionMemoria.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompraControladorTest {

    private CompraControlador compraControlador;
    private UsuarioRepoMemoria usuarioRepoMemoria;
    private JuegoRepoMemoria juegoRepoMemoria;
    private CompraRepoMemoria compraRepoMemoria;
    private BibliotecaRepoMemoria bibliotecaRepo;
    private BibliotecaControlador bibliotecaControlador;

    @BeforeEach
    void setup() {
        usuarioRepoMemoria = new UsuarioRepoMemoria();
        juegoRepoMemoria = new JuegoRepoMemoria();
        compraRepoMemoria = new CompraRepoMemoria();
        bibliotecaRepo = new BibliotecaRepoMemoria();
        bibliotecaControlador = new BibliotecaControlador(bibliotecaRepo, usuarioRepoMemoria, juegoRepoMemoria);

        compraControlador = new CompraControlador(
                compraRepoMemoria,
                usuarioRepoMemoria,
                juegoRepoMemoria,
                bibliotecaRepo,
                bibliotecaControlador
        );
    }

    // =========================
    // REALIZAR COMPRA
    // =========================

    @Test
    void realizarCompra_ok() throws Exception {

        var usuario = usuarioRepoMemoria.crear(new UsuarioForm(
                "user1","email@test.com","123","Nombre",
                PaisEnum.ESPANA, LocalDate.now().minusYears(20),
                Instant.now(),"avatar",100.0, EstadoCuentaEnum.ACTIVA));

        var juego = juegoRepoMemoria.crear(new JuegoForm(
                "Juego1","desc","dev",
                LocalDate.now(),50.0,0,"accion",
                ClasificacionJuegoEnum.PEGI_18,new String[]{"ES"},
                EstadoJuegoEnum.DISPONIBLE));

        Double precioFinal = juego.getPrecioBase() * (1- juego.getDescuento() / 100.0);
        CompraForm form = new CompraForm(
                usuario.getIdUsuario(),
                juego.getIdJuego(),
                Instant.now(),
                MetodoPagoEnum.TARJETA,
                juego.getPrecioBase(),
                precioFinal,
                EstadoCompraEnum.PENDIENTE
        );

        CompraDto dto = compraControlador.realizarCompra(form);

        assertNotNull(dto);
        assertEquals(usuario.getIdUsuario(), dto.idUsuario());
    }

    @Test
    void realizarCompra_usuarioNoExiste() {
        assertThrows(ValidationException.class, () -> {
            compraControlador.realizarCompra(
                    new CompraForm(1L,1L,Instant.now(),
                            MetodoPagoEnum.PAYPAL,10.0,50.0,
                            EstadoCompraEnum.PENDIENTE));
        });
    }

    @Test
    void realizarCompra_juegoNoDisponible() throws Exception {

        var usuario = usuarioRepoMemoria.crear(new UsuarioForm(
                "u","e","p","n",PaisEnum.ESPANA,
                LocalDate.now().minusYears(20),
                Instant.now(),"a",100.0,EstadoCuentaEnum.ACTIVA));

        var juego = juegoRepoMemoria.crear(new JuegoForm(
                "j","d","dev",
                LocalDate.now(),50.0,0,"accion",
                ClasificacionJuegoEnum.PEGI_18,new String[]{"ES"},
                EstadoJuegoEnum.DISPONIBLE));

        // ahora lo actualizamos a NO_DISPONIBLE
        juegoRepoMemoria.actualizar(juego.getIdJuego(),
                new JuegoForm(
                        juego.getTitulo(),
                        juego.getDescripcion(),
                        juego.getDesarrollador(),
                        juego.getFechaLanzamiento(),
                        juego.getPrecioBase(),
                        juego.getDescuento(),
                        juego.getCategoria(),
                        juego.getClasificacionPorEdad(),
                        juego.getIdiomas(),
                        EstadoJuegoEnum.NO_DISPONIBLE
                ));

        assertThrows(ValidationException.class, () ->
                compraControlador.realizarCompra(
                        new CompraForm(usuario.getIdUsuario(),
                                juego.getIdJuego(),
                                Instant.now(),
                                MetodoPagoEnum.PAYPAL,
                                50.0,50.0,
                                EstadoCompraEnum.PENDIENTE)
                )
        );
    }

    // =========================
    // PROCESAR PAGO
    // =========================

    @Test
    void procesarPago_ok_tarjeta() throws Exception {

        var usuario = usuarioRepoMemoria.crear(new UsuarioForm(
                "u","e","p","n",PaisEnum.ESPANA,
                LocalDate.now().minusYears(20),
                Instant.now(),"a",100.0,EstadoCuentaEnum.ACTIVA));

        var juego = juegoRepoMemoria.crear(new JuegoForm(
                "j","d","dev",
                LocalDate.now(),50.0,0,"accion",
                ClasificacionJuegoEnum.PEGI_18,new String[]{"ES"},
                EstadoJuegoEnum.DISPONIBLE));

        CompraForm form = new CompraForm(
                usuario.getIdUsuario(),
                juego.getIdJuego(),
                Instant.now(),
                MetodoPagoEnum.TARJETA,
                50.0,50.0,
                EstadoCompraEnum.PENDIENTE);

        var compra = compraRepoMemoria.crear(form);

        boolean resultado = compraControlador.procesarPago(
                compra.getIdCompra(),
                MetodoPagoEnum.TARJETA);

        assertTrue(resultado);
    }

    // =========================
    // LISTAR COMPRAS
    // =========================

    @Test
    void listarCompras_ok() throws Exception {

        var usuario = usuarioRepoMemoria.crear(new UsuarioForm(
                "u","e","p","n",PaisEnum.ESPANA,
                LocalDate.now().minusYears(20),
                Instant.now(),"a",100.0,EstadoCuentaEnum.ACTIVA));

        List<CompraDto> lista = compraControlador.listarCompras(usuario.getIdUsuario());

        assertNotNull(lista);
    }

    // =========================
    // DETALLES COMPRA
    // =========================

    @Test
    void detallesCompra_ok() throws Exception {

        var usuario = usuarioRepoMemoria.crear(new UsuarioForm(
                "u","e","p","n",PaisEnum.ESPANA,
                LocalDate.now().minusYears(20),
                Instant.now(),"a",100.0,EstadoCuentaEnum.ACTIVA));

        var juego = juegoRepoMemoria.crear(new JuegoForm(
                "j","d","dev",
                LocalDate.now(),50.0,0,"accion",
                ClasificacionJuegoEnum.PEGI_18,new String[]{"ES"},
                EstadoJuegoEnum.DISPONIBLE));

        var compra = compraRepoMemoria.crear(new CompraForm(
                usuario.getIdUsuario(),
                juego.getIdJuego(),
                Instant.now(),
                MetodoPagoEnum.PAYPAL,
                50.0,50.0,
                EstadoCompraEnum.COMPLETADA));

        DetallesCompraDto detalles = compraControlador.detallesDeUnaCompra(
                compra.getIdCompra(),
                usuario.getIdUsuario());

        assertNotNull(detalles);
        assertNotNull(detalles.facturaDto());
    }

    // =========================
    // REEMBOLSO
    // =========================

    @Test
    void solicitarReembolso_ok() throws Exception {

        var usuario = usuarioRepoMemoria.crear(new UsuarioForm(
                "u","e","p","n",PaisEnum.ESPANA,
                LocalDate.now().minusYears(20),
                Instant.now(),"a",100.0,EstadoCuentaEnum.ACTIVA));

        var juego = juegoRepoMemoria.crear(new JuegoForm(
                "j","d","dev",
                LocalDate.now(),50.0,0,"accion",
                ClasificacionJuegoEnum.PEGI_18,new String[]{"ES"},
                EstadoJuegoEnum.DISPONIBLE));

        var compra = compraRepoMemoria.crear(new CompraForm(
                usuario.getIdUsuario(),
                juego.getIdJuego(),
                Instant.now(),
                MetodoPagoEnum.PAYPAL,
                50.0,50.0,
                EstadoCompraEnum.PENDIENTE));

        compraControlador.procesarPago(
                compra.getIdCompra(),
                MetodoPagoEnum.PAYPAL);

        compraControlador.solicitarReembolso(compra.getIdCompra());

        assertEquals(
                EstadoCompraEnum.REEMBOLSADA,
                compraRepoMemoria.buscarPorId(compra.getIdCompra()).getEstadoCompra());
    }

    // =========================
    // FACTURA
    // =========================

    @Test
    void generarFactura_ok() throws Exception {

        var usuario = usuarioRepoMemoria.crear(new UsuarioForm(
                "u","e","p","n",PaisEnum.ESPANA,
                LocalDate.now().minusYears(20),
                Instant.now(),"a",100.0,EstadoCuentaEnum.ACTIVA));

        var juego = juegoRepoMemoria.crear(new JuegoForm(
                "j","d","dev",
                LocalDate.now(),50.0,0,"accion",
                ClasificacionJuegoEnum.PEGI_18,new String[]{"ES"},
                EstadoJuegoEnum.DISPONIBLE));

        var compra = compraRepoMemoria.crear(new CompraForm(
                usuario.getIdUsuario(),
                juego.getIdJuego(),
                Instant.now(),
                MetodoPagoEnum.PAYPAL,
                50.0,50.0,
                EstadoCompraEnum.COMPLETADA));

        FacturaDto factura = compraControlador.generarFactura(compra.getIdCompra());

        assertNotNull(factura);
        assertEquals(compra.getIdCompra(), factura.idCompra());
    }
}
