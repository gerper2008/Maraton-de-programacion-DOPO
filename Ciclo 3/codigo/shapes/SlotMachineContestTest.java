import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Assume;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Pruebas de unidad para SlotMachineContest.
 *
 * Nota de diseño: solve(n) y simulate(n) crean su propia SlotMachine
 * interna y no la exponen, tal como lo pide el enunciado (SlotMachine
 * es solo la "testing tool" de la maratón, no algo que el llamador
 * manipule directamente). Por eso, para verificar que el algoritmo
 * realmente LOGRA el jackpot (y no solo que corre sin errores), estas
 * pruebas usan reflexión para invocar el método privado resolverEn(...)
 * sobre una SlotMachine que sí controlamos, y ahí revisamos isJackpot().
 *
 * @author
 * @version 1.0
 */
public class SlotMachineContestTest
{
    /**
     * Invoca por reflexión el método privado
     * SlotMachineContest.resolverEn(SlotMachine, int) sobre la máquina
     * dada, y retorna la lista de acciones que aplicó.
     */
    @SuppressWarnings("unchecked")
    private List<int[]> resolverEn(SlotMachine sm, int n) throws Exception
    {
        Method m = SlotMachineContest.class.getDeclaredMethod(
                "resolverEn", SlotMachine.class, int.class);
        m.setAccessible(true);
        return (List<int[]>) m.invoke(null, sm, n);
    }

    /**
     * El algoritmo debe lograr el jackpot para varios tamaños de
     * máquina, repitiendo cada tamaño varias veces para cubrir distintas
     * configuraciones aleatorias iniciales.
     */
    @Test
    public void testAlgoritmoLograJackpot() throws Exception
    {
        int[] tamanos = {2, 3, 5, 10, 20, 50};
        int repeticionesPorTamano = 5;

        for (int n : tamanos) {
            for (int r = 0; r < repeticionesPorTamano; r++) {
                SlotMachine sm = new SlotMachine(n);
                resolverEn(sm, n);

                assertTrue("La maquina de n=" + n + " no llego al jackpot "
                           + "(intento " + r + ")", sm.isJackpot());
            }
        }
    }

    /**
     * Con 0 o 1 ruedas no hay nada que resolver: no se necesita ninguna
     * acción y, si hay 1 rueda, ya es jackpot por definición.
     */
    @Test
    public void testCasosBordeSinAcciones() throws Exception
    {
        assertEquals(0, SlotMachineContest.solve(0).length);
        assertEquals(0, SlotMachineContest.solve(1).length);

        SlotMachine unaSolaRueda = new SlotMachine(1);
        assertTrue(unaSolaRueda.isJackpot());
    }

    /**
     * Cada acción retornada por solve(n) debe tener la forma {i, j}: la
     * posición de rueda i debe existir en la máquina (entre 1 y n).
     */
    @Test
    public void testEstructuraDeLasAccionesEsValida()
    {
        int n = 8;
        int[][] acciones = SlotMachineContest.solve(n);

        assertNotNull(acciones);

        for (int[] accion : acciones) {
            assertEquals("Cada accion debe tener exactamente 2 valores {i, j}",
                         2, accion.length);

            int wheel = accion[0];
            assertTrue("La rueda referenciada esta fuera de rango: " + wheel,
                       wheel >= 1 && wheel <= n);
        }
    }

    /**
     * El número de acciones no debe dispararse sin control: para el
     * algoritmo de 3 fases, el orden esperado es aproximadamente
     * proporcional a n al cuadrado.
     */
    @Test
    public void testCantidadDeAccionesEsRazonable()
    {
        int[] tamanos = {5, 10, 25, 50};

        for (int n : tamanos) {
            int[][] acciones = SlotMachineContest.solve(n);
            int limite = 10 * n * n + 100;

            assertTrue("Demasiadas acciones para n=" + n
                       + ": " + acciones.length,
                       acciones.length <= limite);
        }
    }

    /**
     * simulate(n) debe ejecutarse sin lanzar excepciones en un entorno
     * gráfico. Esta prueba se omite automáticamente si se corre en un
     * entorno sin pantalla (headless), ya que simulate() necesita un
     * Canvas visible.
     */
    @Test
    public void testSimulateNoLanzaExcepciones()
    {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        SlotMachineContest.simulate(6);
        // si llega aquí sin excepción, la prueba pasa
        assertTrue(true);
    }
}
