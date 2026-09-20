import java.util.ArrayList;
import java.util.List;

/**
 * Resuelve y simula el problema de la maratón "Slot Machine" (2025 ICPC
 * World Finals, Problem I) usando una {@link SlotMachine}.
 *
 * Solo se permite usar de SlotMachine los métodos SlotMachine(n),
 * spin(wheel, steps) y distinctSymbols(): SlotMachine es la herramienta de
 * prueba de la maratón y no puede usarse para "ver" directamente los
 * símbolos, tal como en el problema original solo se conoce la cantidad de
 * símbolos distintos visibles después de cada acción.
 *
 * @author
 * @version 1.0
 */
public class SlotMachineContest
{
    private SlotMachineContest()
    {
        // Clase de utilidad: no se instancia.
    }

    /**
     * Calcula la secuencia de acciones {i, j} (girar la rueda i por j
     * posiciones) necesarias para ganar el jackpot en una máquina de n
     * ruedas y n símbolos, inicializada aleatoriamente. La máquina usada
     * para resolver permanece invisible durante todo el proceso.
     *
     * @param n número de ruedas y símbolos de la máquina.
     * @return secuencia de acciones {i, j} necesarias para ganar.
     */
    public static int[][] solve(int n)
    {
        SlotMachine sm = new SlotMachine(n);
        List<int[]> actions = resolverEn(sm, n);

        return actions.toArray(new int[0][]);
    }

    /**
     * Simula visualmente las acciones necesarias para ganar el jackpot en
     * una máquina de n ruedas y n símbolos, inicializada aleatoriamente.
     * La máquina se muestra visible durante toda la simulación.
     *
     * @param n número de ruedas y símbolos de la máquina.
     */
    public static void simulate(int n)
    {
        SlotMachine sm = new SlotMachine(n);
        sm.makeVisible();
        resolverEn(sm, n);
        sm.makeInvisible();
    }

    /**
     * Aplica sobre la máquina dada el algoritmo de tres fases que resuelve
     * el problema de la maratón, usando únicamente spin(wheel, steps) y
     * distinctSymbols() como retroalimentación, y retorna la lista de
     * acciones realizadas.
     *
     * Fase 1: deja todas las ruedas mostrando símbolos distintos entre sí.
     * Fase 2: descubre, para cada rueda, cuál es su "sucesora" (la rueda
     *         cuyo símbolo visible está exactamente una posición adelante
     *         en el orden cíclico de símbolos), sin alterar el estado
     *         final de la máquina.
     * Fase 3: usando la cadena de sucesores, alinea todas las ruedas al
     *         símbolo de la primera.
     *
     * @param sm máquina sobre la cual resolver.
     * @param n número de ruedas y símbolos.
     * @return lista de acciones {i, j} realizadas, en orden.
     */
    private static List<int[]> resolverEn(SlotMachine sm, int n)
    {
        List<int[]> actions = new ArrayList<int[]>();

        if (n <= 1) {
            // con 0 o 1 ruedas no hay nada que resolver
            return actions;
        }

        separarSimbolos(sm, n, actions);
        int[] sucesor = hallarSucesores(sm, n, actions);
        alinearRuedas(sm, n, sucesor, actions);

        return actions;
    }

    /**
     * Fase 1: para cada rueda (excepto la primera), prueba sus n
     * posiciones relativas y la deja en la que maximiza la cantidad de
     * símbolos distintos visibles en toda la máquina. Al terminar, las n
     * ruedas muestran n símbolos distintos entre sí.
     */
    private static void separarSimbolos(SlotMachine sm, int n, List<int[]> actions)
    {
        for (int i = 2; i <= n; i++) {
            int bestRelative = 0;
            int bestDistinct = sm.distinctSymbols();

            for (int step = 1; step < n; step++) {
                girar(sm, i, 1, actions);

                int distinct = sm.distinctSymbols();

                if (distinct > bestDistinct) {
                    bestDistinct = distinct;
                    bestRelative = step;
                }
            }

            // la rueda i ya dio (n - 1) pasos completos desde su posición
            // original; regresamos a la mejor posición relativa hallada
            int back = ((bestRelative - (n - 1)) % n + n) % n;

            if (back != 0) {
                girar(sm, i, back, actions);
            }
        }
    }

    /**
     * Fase 2: para cada rueda i, halla cuál otra rueda j es su sucesora
     * (es decir, que su símbolo visible está una posición adelante del de
     * i en el orden cíclico) sin alterar el estado final de la máquina:
     * cada prueba, exitosa o no, se deshace antes de continuar.
     *
     * @return arreglo donde sucesor[i] es la rueda sucesora de la rueda i
     *         (índices base 1; la posición 0 no se usa).
     */
    private static int[] hallarSucesores(SlotMachine sm, int n, List<int[]> actions)
    {
        int[] sucesor = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            girar(sm, i, 1, actions);

            for (int j = 1; j <= n; j++) {
                if (j == i) {
                    continue;
                }

                girar(sm, j, -1, actions);

                if (sm.distinctSymbols() == n) {
                    sucesor[i] = j;
                    girar(sm, j, 1, actions);
                    break;
                } else {
                    girar(sm, j, 1, actions);
                }
            }

            girar(sm, i, -1, actions);
        }

        return sucesor;
    }

    /**
     * Fase 3: recorre la cadena de sucesores a partir de la rueda 1 para
     * calcular a cuántas posiciones de distancia está cada rueda respecto
     * a ella, y gira cada una lo necesario para igualar su símbolo.
     */
    private static void alinearRuedas(SlotMachine sm, int n, int[] sucesor, List<int[]> actions)
    {
        int[] distancia = new int[n + 1];
        int actual = sucesor[1];
        int d = 1;

        while (actual != 1) {
            distancia[actual] = d;
            actual = sucesor[actual];
            d++;
        }

        for (int w = 2; w <= n; w++) {
            if (distancia[w] != 0) {
                int steps = ((-distancia[w]) % n + n) % n;
                girar(sm, w, steps, actions);
            }
        }
    }

    /**
     * Ejecuta un giro sobre la máquina y lo registra en la lista de
     * acciones.
     */
    private static void girar(SlotMachine sm, int wheel, int steps, List<int[]> actions)
    {
        sm.spin(wheel, steps);
        actions.add(new int[]{wheel, steps});
    }
}
