package edu.upvictoria.spolancom.utils;

import java.util.Scanner;

public class Leer {
    private final Scanner scanner;

    /** Constructor por defecto: usa la entrada estándar (consola real). */
    public Leer() {
        this(new Scanner(System.in));
    }

    /** Constructor para inyectar cualquier Scanner (real o mock en tests). */
    public Leer(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("El scanner no puede ser null");
        }
        this.scanner = scanner;
    }

    public Object leer() {
        String input = scanner.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // no es entero
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            // no es decimal
        }
        if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(input);
        }
        if (input.length() == 1) {
            return input.charAt(0);
        }
        return input;
    }

    public int leerInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Error. Ingresa un número entero válido: ");
            }
        }
    }

    public double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Error. Ingresa un número decimal válido: ");
            }
        }
    }

    public boolean leerBoolean() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                return Boolean.parseBoolean(input);
            }
            System.out.print("Error. Ingresa 'true' o 'false': ");
        }
    }

    public char leerChar() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.length() == 1) {
                return input.charAt(0);
            }
            System.out.print("Error. Ingresa un único carácter: ");
        }
    }

    public String leerString() {
        return scanner.nextLine().trim();
    }
}
