package edu.upvictoria.spolancom.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;

public class LeerTest {
    private Scanner scannerMock;
    private Leer leer;

    @Before
    public void setUp() {
        scannerMock = mock(Scanner.class);
        leer = new Leer(scannerMock);
    }

    // ---------------------------------------------------------
    // Constructores
    // ---------------------------------------------------------

    @Test
    public void constructorConScannerNullLanzaExcepcion() {
        try {
            new Leer(null);
            fail("Se esperaba IllegalArgumentException al pasar un Scanner null");
        } catch (IllegalArgumentException e) {
            assertEquals("El scanner no puede ser null", e.getMessage());
        }
    }

    @Test
    public void constructorPorDefectoNoLanzaExcepcion() {
        // Solo se valida que la construcción no falle.
        // No se ejecuta ninguna lectura real de System.in en este test.
        Leer leerConsola = new Leer();
        assertTrue(leerConsola != null);
    }

    // ---------------------------------------------------------
    // leer() -> detecta Integer
    // ---------------------------------------------------------

    @Test
    public void leerDetectaEntero() {
        when(scannerMock.nextLine()).thenReturn("42");
        Object resultado = leer.leer();
        assertTrue(resultado instanceof Integer);
        assertEquals(42, resultado);
    }

    @Test
    public void leerDetectaEnteroNegativo() {
        when(scannerMock.nextLine()).thenReturn("-15");
        Object resultado = leer.leer();
        assertTrue(resultado instanceof Integer);
        assertEquals(-15, resultado);
    }

    // ---------------------------------------------------------
    // leer() -> detecta Double
    // ---------------------------------------------------------

    @Test
    public void leerDetectaDecimal() {
        when(scannerMock.nextLine()).thenReturn("3.14");
        Object resultado = leer.leer();
        assertTrue(resultado instanceof Double);
        assertEquals(3.14, (Double) resultado, 0.0001);
    }

    // ---------------------------------------------------------
    // leer() -> detecta Boolean
    // ---------------------------------------------------------

    @Test
    public void leerDetectaBooleanTrue() {
        when(scannerMock.nextLine()).thenReturn("true");
        Object resultado = leer.leer();
        assertTrue(resultado instanceof Boolean);
        assertEquals(Boolean.TRUE, resultado);
    }

    @Test
    public void leerDetectaBooleanFalseIgnorandoMayusculas() {
        when(scannerMock.nextLine()).thenReturn("FALSE");
        Object resultado = leer.leer();
        assertTrue(resultado instanceof Boolean);
        assertEquals(Boolean.FALSE, resultado);
    }

    // ---------------------------------------------------------
    // leer() -> detecta Character
    // ---------------------------------------------------------

    @Test
    public void leerDetectaCaracterUnico() {
        when(scannerMock.nextLine()).thenReturn("x");
        Object resultado = leer.leer();
        assertTrue(resultado instanceof Character);
        assertEquals('x', resultado);
    }

    // ---------------------------------------------------------
    // leer() -> cae en String
    // ---------------------------------------------------------

    @Test
    public void leerDetectaStringCuandoNoEsNadaMasEspecifico() {
        when(scannerMock.nextLine()).thenReturn("hola mundo");
        Object resultado = leer.leer();
        assertTrue(resultado instanceof String);
        assertEquals("hola mundo", resultado);
    }

    @Test
    public void leerAplicaTrimAntesDeEvaluar() {
        when(scannerMock.nextLine()).thenReturn("  100  ");
        Object resultado = leer.leer();
        assertTrue(resultado instanceof Integer);
        assertEquals(100, resultado);
    }

    // ---------------------------------------------------------
    // leerInt()
    // ---------------------------------------------------------

    @Test
    public void leerIntConEntradaValidaDirecta() {
        when(scannerMock.nextLine()).thenReturn("25");
        int resultado = leer.leerInt();
        assertEquals(25, resultado);
        verify(scannerMock, times(1)).nextLine();
    }

    @Test
    public void leerIntReintentaTrasEntradaInvalida() {
        when(scannerMock.nextLine()).thenReturn("abc", "10");
        int resultado = leer.leerInt();
        assertEquals(10, resultado);
        verify(scannerMock, times(2)).nextLine();
    }

    @Test
    public void leerIntReintentaVariasVeces() {
        when(scannerMock.nextLine()).thenReturn("uno", "3.5", "99");
        int resultado = leer.leerInt();
        assertEquals(99, resultado);
        verify(scannerMock, times(3)).nextLine();
    }

    // ---------------------------------------------------------
    // leerDouble()
    // ---------------------------------------------------------

    @Test
    public void leerDoubleConEntradaValidaDirecta() {
        when(scannerMock.nextLine()).thenReturn("3.14");
        double resultado = leer.leerDouble();
        assertEquals(3.14, resultado, 0.0001);
        verify(scannerMock, times(1)).nextLine();
    }

    @Test
    public void leerDoubleAceptaEnteroComoDecimal() {
        when(scannerMock.nextLine()).thenReturn("7");
        double resultado = leer.leerDouble();
        assertEquals(7.0, resultado, 0.0001);
    }

    @Test
    public void leerDoubleReintentaTrasEntradaInvalida() {
        when(scannerMock.nextLine()).thenReturn("no-numero", "2.71");
        double resultado = leer.leerDouble();
        assertEquals(2.71, resultado, 0.0001);
        verify(scannerMock, times(2)).nextLine();
    }

    // ---------------------------------------------------------
    // leerBoolean()
    // ---------------------------------------------------------

    @Test
    public void leerBooleanConTrueDirecto() {
        when(scannerMock.nextLine()).thenReturn("true");
        boolean resultado = leer.leerBoolean();
        assertTrue(resultado);
        verify(scannerMock, times(1)).nextLine();
    }

    @Test
    public void leerBooleanConFalseIgnorandoMayusculas() {
        when(scannerMock.nextLine()).thenReturn("False");
        boolean resultado = leer.leerBoolean();
        assertFalse(resultado);
    }

    @Test
    public void leerBooleanReintentaTrasEntradaInvalida() {
        when(scannerMock.nextLine()).thenReturn("quizas", "si", "true");
        boolean resultado = leer.leerBoolean();
        assertTrue(resultado);
        verify(scannerMock, times(3)).nextLine();
    }

    // ---------------------------------------------------------
    // leerChar()
    // ---------------------------------------------------------

    @Test
    public void leerCharConEntradaValidaDirecta() {
        when(scannerMock.nextLine()).thenReturn("z");
        char resultado = leer.leerChar();
        assertEquals('z', resultado);
        verify(scannerMock, times(1)).nextLine();
    }

    @Test
    public void leerCharReintentaSiHayMasDeUnCaracter() {
        when(scannerMock.nextLine()).thenReturn("ab", "c");
        char resultado = leer.leerChar();
        assertEquals('c', resultado);
        verify(scannerMock, times(2)).nextLine();
    }

    @Test
    public void leerCharReintentaSiCadenaEstaVacia() {
        when(scannerMock.nextLine()).thenReturn("", "k");
        char resultado = leer.leerChar();
        assertEquals('k', resultado);
        verify(scannerMock, times(2)).nextLine();
    }

    // ---------------------------------------------------------
    // leerString()
    // ---------------------------------------------------------

    @Test
    public void leerStringDevuelveTextoConTrim() {
        when(scannerMock.nextLine()).thenReturn("  texto de prueba  ");
        String resultado = leer.leerString();
        assertEquals("texto de prueba", resultado);
        verify(scannerMock, times(1)).nextLine();
    }

    @Test
    public void leerStringDevuelveCadenaVaciaSiSoloHayEspacios() {
        when(scannerMock.nextLine()).thenReturn("    ");
        String resultado = leer.leerString();
        assertEquals("", resultado);
    }

}