## Intento 1: el truco clásico de "quitar el `final` por reflexión"

Este era el enfoque típico en Java 8 y anteriores:

```java
@Before
public void setUp() throws Exception {
    scannerMock = mock(Scanner.class);

    Field scannerField = Leer.class.getDeclaredField("scanner");
    scannerField.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(scannerField, scannerField.getModifiers() & ~Modifier.FINAL);

    scannerField.set(null, scannerMock);
}
```

**Este código ya no funciona de forma confiable en Java 21.** Desde el JDK 12 (JDK-8210522), la JVM impide que la modificación del campo `modifiers` de un `Field` tenga efecto real sobre su condición de `final`; y en versiones más recientes, el propio `getDeclaredField("modifiers")` sobre la clase `Field` puede lanzar `NoSuchFieldException` porque ese campo interno fue reestructurado. Es decir: el truco que "todo el mundo copiaba de Stack Overflow" quedó obsoleto justo por este tipo de casos.

## Intento 2: `VarHandle` — tampoco sirve aquí

Uno podría pensar en `MethodHandles.Lookup` con `findStaticVarHandle`, la API moderna sucesora de reflexión pura:

```java
VarHandle vh = MethodHandles.privateLookupIn(Leer.class, MethodHandles.lookup())
        .findStaticVarHandle(Leer.class, "scanner", Scanner.class);
vh.set(mockScanner); // <-- lanza UnsupportedOperationException
```

El problema es que `VarHandle` **respeta la semántica de `final` en tiempo de creación**: si el campo es `final`, el `VarHandle` que obtienes no soporta el modo de acceso de escritura (`set`), y lanzará `UnsupportedOperationException` en cuanto lo intentes. No hay forma de "forzarlo" desde esta API; fue diseñada precisamente para evitar el abuso que permitía la reflexión clásica.

## El único camino que realmente funciona en Java 21: `sun.misc.Unsafe`

Para forzar la escritura de un campo `static final` en Java 21, hay que saltarse por completo el modelo de acceso de Java y escribir directamente en la posición de memoria del campo, usando `Unsafe`:

```java
package edu.spolancom.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

public class LeerTest {

    private Scanner scannerMock;
    private static Unsafe unsafe;
    private static Field scannerField;
    private static Scanner scannerOriginal;

    @Before
    public void setUp() throws Exception {
        // Obtener la instancia interna de Unsafe (normalmente inaccesible)
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        unsafe = (Unsafe) unsafeField.get(null);

        // Localizar el campo estático "scanner" de la clase Leer
        scannerField = Leer.class.getDeclaredField("scanner");

        // Guardar referencia original para poder restaurarla después
        Object base = unsafe.staticFieldBase(scannerField);
        long offset = unsafe.staticFieldOffset(scannerField);
        scannerOriginal = (Scanner) unsafe.getObject(base, offset);

        // Sustituir el valor del campo static final directamente en memoria
        scannerMock = mock(Scanner.class);
        unsafe.putObject(base, offset, scannerMock);
    }

    @After
    public void tearDown() {
        // Restaurar el estado original para no contaminar otros tests
        Object base = unsafe.staticFieldBase(scannerField);
        long offset = unsafe.staticFieldOffset(scannerField);
        unsafe.putObject(base, offset, scannerOriginal);
    }

    @Test
    public void leerIntConEntradaValidaDirecta() {
        when(scannerMock.nextLine()).thenReturn("25");
        assertEquals(25, Leer.leerInt());
    }

    @Test
    public void leerIntReintentaTrasEntradaInvalida() {
        when(scannerMock.nextLine()).thenReturn("abc", "10");
        assertEquals(10, Leer.leerInt());
    }

    // ... el resto de los tests seguiría el mismo patrón,
    // llamando a Leer.leerX() como métodos estáticos.
}
```

### Por qué esto funciona cuando lo anterior no

`Unsafe.putObject(base, offset, valor)` escribe directamente en la dirección de memoria donde vive el campo, **sin pasar por el verificador de acceso del lenguaje Java**. No le importa si el campo es `private`, `static` o `final` — a ese nivel, `final` es una restricción del compilador y del modelo de objetos de Java, no una propiedad física de la memoria. Por eso `Unsafe` puede "mentirle" a la JVM.

### El precio que se paga por esto

- **Configuración extra en Maven.** Necesitarías abrir el módulo `java.base` para que la reflexión sobre `Unsafe` funcione, agregando en el plugin `maven-surefire-plugin`:
```xml
<configuration>
    <argLine>--add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/jdk.internal.ref=ALL-UNNAMED</argLine>
</configuration>
```
- **Fragilidad entre versiones de JDK.** `sun.misc.Unsafe` es una API interna no soportada oficialmente; Oracle ha anunciado en varias ocasiones planes de restringirla o eliminarla en futuras versiones del JDK. Un test así puede dejar de compilar o de ejecutarse con un simple *upgrade* de Java.
- **Efectos colaterales entre tests.** Como `scanner` es un campo compartido por *toda* la clase `Leer`, si olvidas restaurarlo en el `@After` (o si los tests corren en paralelo), un test puede "contaminar" a otro con un mock que ya no aplica — un problema que simplemente no existe cuando cada test crea su propia instancia de `Leer`.
- **Legibilidad.** Cualquier persona que abra este archivo de test necesita entender `Unsafe`, offsets de memoria y el modelo de módulos de Java solo para entender *cómo* se inyecta una dependencia — comparado con `new Leer(mockScanner)`, que se entiende en un segundo.

Este ejercicio confirma, en la práctica, la razón de fondo detrás del rediseño: pasar de estático a instancias no fue solo "para facilitar el testing" en abstracto, sino que evitó por completo tener que depender de una API interna e inestable de la JVM.
