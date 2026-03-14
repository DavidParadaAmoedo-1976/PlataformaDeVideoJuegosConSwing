package org.davidparada.util;

import java.security.MessageDigest;

public class EncriptarPassword {

    public static String generarHash(String texto) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");

            byte[] hashBytes = md.digest(texto.getBytes());

            StringBuilder hashHex = new StringBuilder();

            for (byte b : hashBytes) {
                hashHex.append(String.format("%02x", b));
            }

            return hashHex.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

