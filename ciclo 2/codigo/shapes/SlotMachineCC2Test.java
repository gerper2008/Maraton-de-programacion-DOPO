import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas COMPARTIDA del curso para el Ciclo 2 (construcción
 * colectiva vía wiki). Cada estudiante/pareja agrega aquí mínimo dos
 * casos de prueba sobre los requisitos nuevos de SlotMachine.
 *
 * IMPORTANTE - convención de nombres exigida por el enunciado:
 *   according<Iniciales>Should<Comportamiento>
 * donde <Iniciales> son las iniciales de los primeros apellidos de
 * los autores, con la primera letra del segundo apellido, en orden
 * alfabético. Ejemplo: accordingDcAvShould....
 *
 * TODO: reemplazar "XxYy" por las iniciales reales del equipo antes
 * de subir el caso al wiki, y fusionar aquí los casos de los demás
 * equipos sin borrar los existentes.
 *
 * @author (agregar aquí cada autor que sume un caso)
 * @version 1.0
 */
public class SlotMachineCC2Test
{
    private SlotMachine machine;

    @Before
    public void setUp()
    {
        machine = new SlotMachine();

        machine.addWheel(1);
        machine.addWheel(2);

        machine.addSymbol(1, 1, "cherry");
        machine.addSymbol(1, 3, "bell");

        machine.addSymbol(2, 1, "cherry");
        machine.addSymbol(2, 3, "bell");
    }

    /**
     * Debería: intercambiar la configuración visible entre dos ruedas
     * válidas. (Ejemplo #1 - reemplazar XxYy por las iniciales del equipo).
     */
    @Test
    public void accordingXxYyShouldSwapConfigurationBetweenTwoWheels()
    {
        machine.spin(new String[] {"cherry", "bell"});

        machine.swap(1, 2);

        assertTrue(machine.ok());
        assertEquals("bell", machine.configuration()[0]);
        assertEquals("cherry", machine.configuration()[1]);
    }

    /**
     * NO debería: cambiar el símbolo visible de una rueda bloqueada,
     * aunque se le pida girar varios pasos. (Ejemplo #2 - reemplazar XxYy).
     */
    @Test
    public void accordingXxYyShouldNotSpinALockedWheelEvenWithMultipleSteps()
    {
        machine.lock(1);

        String before = machine.configuration()[0];
        machine.spin(1, 4);

        assertEquals(before, machine.configuration()[0]);
    }

    // Agregar aquí los casos de los demás equipos, sin eliminar los anteriores.
}
