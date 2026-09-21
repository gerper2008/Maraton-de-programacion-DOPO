import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Pruebas de unidad compartidas para SlotMachineContest.
 *
 * Estas pruebas utilizan únicamente la interfaz pública de
 * SlotMachineContest y verifican propiedades que debe cumplir
 * cualquier solución válida del problema.
 *
 * @author
 * @version 1.0
 */
public class SlotMachineContestCTest
{
    /**
     * Verifica que solve(n) produzca una secuencia de acciones
     * correctamente formada para una máquina válida.
     *
     * Cada acción debe tener la forma {rueda, pasos}, donde la
     * rueda está entre 1 y n y la cantidad de pasos está dentro
     * del rango permitido por el problema.
     */
    @Test
    public void testSolveDevuelveAccionesValidas()
    {
        int n = 5;

        int[][] acciones = SlotMachineContest.solve(n);

        assertNotNull("solve(n) no debe retornar null", acciones);

        for (int[] accion : acciones) {
            assertNotNull("Una acción no puede ser null", accion);

            assertEquals(
                "Cada acción debe tener exactamente dos valores",
                2,
                accion.length
            );

            int rueda = accion[0];
            int pasos = accion[1];

            assertTrue(
                "La rueda debe estar entre 1 y n",
                rueda >= 1 && rueda <= n
            );

            assertTrue(
                "Los pasos deben estar dentro del límite permitido",
                pasos >= -1000000000 && pasos <= 1000000000
            );
        }
    }

    /**
     * Verifica que la solución respete el máximo de 10.000 acciones
     * para diferentes tamaños válidos de máquina.
     */
    @Test
    public void testSolveRespetaLimiteDeAcciones()
    {
        int[] tamanos = {3, 10, 50};

        for (int n : tamanos) {
            int[][] acciones = SlotMachineContest.solve(n);

            assertNotNull(
                "solve(n) no debe retornar null para n=" + n,
                acciones
            );

            assertTrue(
                "La solución supera las 10000 acciones para n=" + n,
                acciones.length <= 10000
            );
        }
    }
}