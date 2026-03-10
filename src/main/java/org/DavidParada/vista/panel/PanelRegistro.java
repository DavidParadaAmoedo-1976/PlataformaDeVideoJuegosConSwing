package org.DavidParada.vista.panel;

import org.DavidParada.controlador.UsuarioControlador;
import org.DavidParada.excepcion.ValidationException;
import org.DavidParada.modelo.enums.EstadoCuentaEnum;
import org.DavidParada.modelo.enums.PaisEnum;
import org.DavidParada.modelo.formulario.UsuarioForm;
import org.DavidParada.vista.componente.BotonPrincipal;
import org.DavidParada.vista.componente.BotonSecundario;
import org.DavidParada.vista.util.Navegador;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;

public class PanelRegistro extends JPanel {

    public PanelRegistro(Navegador nav,
                         UsuarioControlador usuarioControlador) {

        setLayout(new BorderLayout());

        // ======================
        // TÍTULO
        // ======================

        JLabel titulo = new JLabel("Crear nueva cuenta");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(titulo, BorderLayout.NORTH);

        // ======================
        // FORMULARIO
        // ======================

        JPanel formulario = new JPanel(new GridLayout(7, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JTextField txtUsuario = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JTextField txtNombreReal = new JTextField();
        JComboBox<PaisEnum> comboPais = new JComboBox<>(PaisEnum.values());
        JTextField txtFechaNacimiento = new JTextField("1995-01-01");

        formulario.add(new JLabel("Nombre usuario:"));
        formulario.add(txtUsuario);

        formulario.add(new JLabel("Email:"));
        formulario.add(txtEmail);

        formulario.add(new JLabel("Contraseña:"));
        formulario.add(txtPassword);

        formulario.add(new JLabel("Nombre real:"));
        formulario.add(txtNombreReal);

        formulario.add(new JLabel("País:"));
        formulario.add(comboPais);

        formulario.add(new JLabel("Fecha nacimiento (YYYY-MM-DD):"));
        formulario.add(txtFechaNacimiento);

        // ======================
        // BOTONES
        // ======================

        BotonPrincipal btnRegistrar = new BotonPrincipal(
                "Registrar",
                200,
                40,
                e -> {
                    try {

                        UsuarioForm form = new UsuarioForm(
                                txtUsuario.getText(),
                                txtEmail.getText(),
                                new String(txtPassword.getPassword()),
                                txtNombreReal.getText(),
                                (PaisEnum) comboPais.getSelectedItem(),
                                LocalDate.parse(txtFechaNacimiento.getText()),
                                Instant.now(),
                                null,
                                0.0,
                                EstadoCuentaEnum.ACTIVA
                        );

                        usuarioControlador.registrarUsuario(form);

                        JOptionPane.showMessageDialog(this,
                                "Usuario registrado correctamente");

                        nav.irA("login");

                    } catch (ValidationException ex) {

                        StringBuilder mensaje = new StringBuilder();

                        ex.getErrores().forEach(err ->
                                mensaje.append(err.campo())
                                        .append(" - ")
                                        .append(err.error())
                                        .append("\n"));

                        JOptionPane.showMessageDialog(this,
                                mensaje.toString(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "Formato de fecha inválido. Use YYYY-MM-DD",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });

        BotonSecundario btnVolver = new BotonSecundario(
                "Volver",
                200,
                40,
                e -> nav.irA("inicio"));

        formulario.add(btnRegistrar);
        formulario.add(btnVolver);

        add(formulario, BorderLayout.CENTER);
    }
}