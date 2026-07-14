package upvictoria.spolancom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CuentaAhorroTest {

    private Usuario usuario;
    private CuentaAhorro cuentaAhorroValida;
    private CuentaAhorro cuentaAhorroConSaldoNegativo;

    @Before
    public void setUp() {
        usuario = new Usuario("Juan", "12345678");
        cuentaAhorroValida = new CuentaAhorro(usuario, "001", 0.05f);
        cuentaAhorroConSaldoNegativo = new CuentaAhorro(usuario, "002", -0.05f);
    }

    @Test
    public void testConstructorCasoValido() {
        Assert.assertNotNull(cuentaAhorroValida);
        Assert.assertEquals("001", cuentaAhorroValida.getNumeroCuenta());
        Assert.assertEquals(0.05f, cuentaAhorroValida.getTasaInteres(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorCasoInvalidoTasaInteresNegativa() {
        new CuentaAhorro(usuario, "003", -0.05f);
    }

    @Test
    public void testAplicarInteresMensual() {
        cuentaAhorroValida.setSaldo(1000.0f);
        cuentaAhorroValida.aplicarInteresMensual();
        Assert.assertEquals(1050.0f, cuentaAhorroValida.getSaldo(), 0.001);
    }

    @Test
    public void testRetirarConSuficienteSaldo() {
        cuentaAhorroValida.setSaldo(2000.0f);
        boolean resultado = cuentaAhorroValida.retirar(500.0f);
        Assert.assertTrue(resultado);
        Assert.assertEquals(1500.0f, cuentaAhorroValida.getSaldo(), 0.001);
    }

    @Test
    public void testRetirarConFondosInsuficientes() {
        cuentaAhorroValida.setSaldo(1000.0f);
        boolean resultado = cuentaAhorroValida.retirar(2000.0f);
        Assert.assertFalse(resultado);
        Assert.assertEquals(1000.0f, cuentaAhorroValida.getSaldo(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetirarConMontoNegativo() {
        cuentaAhorroValida.retirar(-100.0f);
    }

    @Test
    public void testAplicarInteresMensualConSaldoNegativo() {
        cuentaAhorroConSaldoNegativo.setSaldo(-1000.0f);
        try {
            cuentaAhorroConSaldoNegativo.aplicarInteresMensual();
            Assert.fail("Se esperaba una excepción para tasa de interés negativa.");
        } catch (IllegalArgumentException e) {
            // Excepción capturada como esperado
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTasaInteresNegativa() {
        cuentaAhorroValida.setTasaInteres(-0.05f);
    }

    // Método auxiliar para liberar recursos después de cada prueba
    @After
    public void tearDown() {
        usuario = null;
        cuentaAhorroValida = null;
        cuentaAhorroConSaldoNegativo = null;
    }
}