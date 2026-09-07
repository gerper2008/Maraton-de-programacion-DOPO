import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de unidad para los requisitos funcionales agregados en el
 * Ciclo 2 de SlotMachine: swap, lock/unlock, spin(wheel, steps) y
 * spin(String[] setSymbols).
 *
 * Las pruebas se ejecutan con la máquina en modo INVISIBLE (nunca se
 * llama a makeVisible()), tal como lo exige el enunciado.
 *
 * Para cada operación se probaron dos tipos de caso:
 *   - qué SÍ debería hacer (caso válido / camino feliz)
 *   - qué NO debería hacer (caso inválido / borde)
 *
 * @author
 * @version 1.0
 */
public class SlotMachineC2Test
{
    private SlotMachine machine;

    /**
     * Crea, antes de cada prueba, una máquina con 3 ruedas y los
     * mismos 3 símbolos ("cherry", "bell", "bar") en cada una, en ese
     * orden, dejándolas en la configuración "cherry-cherry-cherry".
     */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);

        for (int wheel = 1; wheel <= 3; wheel++) {
            machine.addSymbol(wheel, 1, "cherry"); // posición 1
            machine.addSymbol(wheel, 3, "bell");   // posición 2
            machine.addSymbol(wheel, 4, "bar");    // posición 3
        }

        machine.spin(new String[] {"cherry", "cherry", "cherry"});
    }

    // ---------------------------------------------------------------
    // swap(wheel1, wheel2)  -  Requisito 9
    // ---------------------------------------------------------------

    /**
     * Debería: intercambiar la configuración visible de dos ruedas
     * válidas, dejando el resto de ruedas intactas.
     */
    @Test
    public void swapShouldExchangeTheConfigurationOfTwoValidWheels()
    {
        machine.spin(new String[] {"cherry", "bell", "bar"});

        machine.swap(1, 3);

        assertTrue(machine.ok());
        String[] config = machine.configuration();
        assertEquals("bar", config[0]);
        assertEquals("bell", config[1]);
        assertEquals("cherry", config[2]);
    }

    /**
     * Debería: permitir "intercambiar" una rueda consigo misma sin
     * error y sin cambiar su configuración.
     */
    @Test
    public void swapShouldAllowSwappingAWheelWithItself()
    {
        machine.spin(new String[] {"cherry", "bell", "bar"});

        machine.swap(2, 2);

        assertTrue(machine.ok());
        assertEquals("bell", machine.configuration()[1]);
    }

    /**
     * NO debería: intercambiar nada, ni modificar la máquina, si una
     * de las posiciones de rueda indicadas no existe.
     */
    @Test
    public void swapShouldFailWhenAWheelPositionDoesNotExist()
    {
        String[] before = machine.configuration();

        machine.swap(1, 10);

        assertFalse(machine.ok());
        assertArrayEquals(before, machine.configuration());
    }

    /**
     * NO debería: aceptar una posición de rueda cero o negativa.
     */
    @Test
    public void swapShouldFailWhenAWheelPositionIsZeroOrNegative()
    {
        machine.swap(0, 2);

        assertFalse(machine.ok());
    }

    // ---------------------------------------------------------------
    // lock(wheel) / unlock(wheel)  -  Requisito 10
    // ---------------------------------------------------------------

    /**
     * NO debería: dejar que una rueda bloqueada cambie de símbolo al
     * girarla individualmente con spin(wheel).
     */
    @Test
    public void lockShouldPreventAWheelFromSpinningIndividually()
    {
        machine.lock(1);

        String before = machine.configuration()[0];
        machine.spin(1);

        assertEquals(before, machine.configuration()[0]);
    }

    /**
     * NO debería: dejar que una rueda bloqueada cambie de símbolo al
     * girar toda la máquina con spin().
     */
    @Test
    public void lockShouldPreventAWheelFromSpinningWithGeneralSpin()
    {
        machine.lock(2);

        String before = machine.configuration()[1];
        machine.spin();

        assertEquals(before, machine.configuration()[1]);
    }

    /**
     * Debería: bloquear sólo la rueda indicada; las demás deben
     * seguir girando normalmente con spin().
     */
    @Test
    public void lockShouldNotAffectTheOtherWheelsOnGeneralSpin()
    {
        machine.lock(2);

        String beforeWheel1 = machine.configuration()[0];
        machine.spin();

        assertNotEquals(beforeWheel1, machine.configuration()[0]);
    }

    /**
     * Debería: permitir que una rueda vuelva a girar normalmente
     * después de desbloquearla.
     */
    @Test
    public void unlockShouldAllowAPreviouslyLockedWheelToSpinAgain()
    {
        machine.lock(1);
        machine.unlock(1);

        String before = machine.configuration()[0];
        machine.spin(1);

        assertTrue(machine.ok());
        assertNotEquals(before, machine.configuration()[0]);
    }

    /**
     * NO debería: bloquear una rueda que no existe.
     */
    @Test
    public void lockShouldFailForAWheelThatDoesNotExist()
    {
        machine.lock(99);

        assertFalse(machine.ok());
    }

    /**
     * NO debería: desbloquear una rueda que no existe.
     */
    @Test
    public void unlockShouldFailForAWheelThatDoesNotExist()
    {
        machine.unlock(99);

        assertFalse(machine.ok());
    }

    // ---------------------------------------------------------------
    // spin(wheel, steps)  -  Requisito 11
    // ---------------------------------------------------------------

    /**
     * Debería: avanzar la rueda exactamente el número de pasos
     * indicado (2 pasos desde "cherry" con 3 símbolos termina en "bar").
     */
    @Test
    public void spinWithStepsShouldAdvanceTheWheelTheGivenNumberOfPositions()
    {
        machine.spin(1, 2);

        assertTrue(machine.ok());
        assertEquals("bar", machine.configuration()[0]);
    }

    /**
     * Debería: no cambiar la configuración si se piden 0 pasos, y
     * aun así considerar la operación válida.
     */
    @Test
    public void spinWithZeroStepsShouldNotChangeTheConfiguration()
    {
        String before = machine.configuration()[0];

        machine.spin(1, 0);

        assertTrue(machine.ok());
        assertEquals(before, machine.configuration()[0]);
    }

    /**
     * NO debería: girar una rueda que no existe.
     */
    @Test
    public void spinWithStepsShouldFailForAWheelThatDoesNotExist()
    {
        machine.spin(99, 3);

        assertFalse(machine.ok());
    }

    /**
     * NO debería: aceptar un número de pasos negativo, ni modificar
     * la rueda al rechazar la operación.
     */
    @Test
    public void spinWithStepsShouldFailForANegativeNumberOfSteps()
    {
        String before = machine.configuration()[0];

        machine.spin(1, -1);

        assertFalse(machine.ok());
        assertEquals(before, machine.configuration()[0]);
    }

    /**
     * NO debería: mover una rueda bloqueada aunque se le pidan
     * varios pasos.
     */
    @Test
    public void spinWithStepsShouldNotMoveALockedWheel()
    {
        machine.lock(1);

        String before = machine.configuration()[0];
        machine.spin(1, 5);

        assertEquals(before, machine.configuration()[0]);
    }

    // ---------------------------------------------------------------
    // spin(String[] setSymbols)  -  Requisito 12
    // ---------------------------------------------------------------

    /**
     * Debería: dejar cada rueda mostrando exactamente el símbolo
     * indicado en la posición correspondiente del arreglo.
     */
    @Test
    public void spinWithConfigurationShouldSetEachWheelToTheGivenSymbol()
    {
        machine.spin(new String[] {"bar", "cherry", "bell"});

        assertTrue(machine.ok());
        String[] config = machine.configuration();
        assertEquals("bar", config[0]);
        assertEquals("cherry", config[1]);
        assertEquals("bell", config[2]);
    }

    /**
     * NO debería: aceptar un arreglo cuyo tamaño no coincida con el
     * número de ruedas, ni modificar la configuración actual.
     */
    @Test
    public void spinWithConfigurationShouldFailWhenArraySizeDoesNotMatchWheelCount()
    {
        String[] before = machine.configuration();

        machine.spin(new String[] {"cherry", "bell"});

        assertFalse(machine.ok());
        assertArrayEquals(before, machine.configuration());
    }

    /**
     * NO debería: aplicar la configuración si alguno de los símbolos
     * no existe en su rueda correspondiente.
     */
    @Test
    public void spinWithConfigurationShouldFailWhenASymbolDoesNotExistInItsWheel()
    {
        String[] before = machine.configuration();

        machine.spin(new String[] {"cherry", "diamond", "bar"});

        assertFalse(machine.ok());
        assertArrayEquals(before, machine.configuration());
    }

    /**
     * NO debería: dejar la máquina a medio configurar. Si el último
     * símbolo del arreglo no existe, ninguna rueda debe cambiar,
     * ni siquiera las que sí tenían un símbolo válido.
     */
    @Test
    public void spinWithConfigurationShouldNotChangeAnyWheelWhenOneSymbolIsInvalid()
    {
        String[] before = machine.configuration();

        machine.spin(new String[] {"bar", "bell", "unknown"});

        assertFalse(machine.ok());
        assertArrayEquals(before, machine.configuration());
    }

    /**
     * NO debería: fallar con una excepción si se invoca con un
     * arreglo nulo; debe rechazarse de forma controlada (ok() = false).
     */
    @Test
    public void spinWithConfigurationShouldFailWhenArgumentIsNull()
    {
        machine.spin((String[]) null);

        assertFalse(machine.ok());
    }
}
