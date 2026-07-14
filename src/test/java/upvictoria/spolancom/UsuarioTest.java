package upvictoria.spolancom;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UsuarioTest {
    @Test
    public void testConstructor_CasoValido() {
        Usuario usuario = new Usuario("Juan", "12345678");

        Assert.assertNotNull(usuario);
        Assert.assertEquals("Juan", usuario.get_nombre());
        Assert.assertEquals("12345678", usuario.get_identification());
    }

    @Test
    public void testConstructor_CasoInvalido_NombreVacio() { // Utilizando assertThrows()
        // El test pasa si se lanza IllegalArgumentExcpetion
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new Usuario("", "12345678");
        });
    }

    @Test(expected = IllegalArgumentException.class) // utilizando expected
    public void testConstructor_CasoInvalido_NombreNull(){
        new Usuario(null, "12345678");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_CasoInvalido_IdentificacionNull() {
        // Junit esperará de forma automática que el contructor falle
        new Usuario("", "12345678");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorCasoInvalidoIdentificacionVacio() {
            new Usuario("Juan", "");
    }

    @Test
    public void testGetSetNombreCasoValido() {
        Usuario usuario = new Usuario("Juan", "12345678");

        usuario.set_nombre("Pedro");
        Assert.assertEquals("Pedro", usuario.get_nombre());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNombreCasoInvalidoNull() {
        Usuario usuario = new Usuario("Juan", "12345678");
        usuario.set_nombre(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNombreCasoInvalidoVacio() {
        Usuario usuario = new Usuario("Juan", "12345678");
        usuario.set_nombre("");
    }
}