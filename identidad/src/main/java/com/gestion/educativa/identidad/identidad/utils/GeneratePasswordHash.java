package com.gestion.educativa.identidad.identidad.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswordHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123";
        String hash = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("\nSQL UPDATE statement:");
        System.out.println("UPDATE usuario SET contrasena = '" + hash + "' WHERE run_usuario = '12345678';");
    }
}
