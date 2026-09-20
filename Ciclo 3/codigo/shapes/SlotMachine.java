import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Representa una máquina tragamonedas.
 * La máquina está compuesta por ruedas que contienen símbolos.
 *
 * @author
 * @version 1.1
 */
public class SlotMachine
{
    private ArrayList<Wheel> wheels;
    private boolean visible;
    private boolean operationOK;

    /**
     * Construye una máquina tragamonedas vacía.
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        visible = false;
        operationOK = true;
    }

    /**
     * Construye una máquina de n ruedas y n símbolos, inicializada
     * aleatoriamente. Todas las ruedas comparten el mismo conjunto de
     * símbolos (mismos nombres y colores, en el mismo orden), pero cada
     * una inicia en una posición aleatoria e independiente.
     *
     * @param n número de ruedas y símbolos que tendrá la máquina.
     */
    public SlotMachine(int n)
    {
        this();
        operationOK = n >= 1;

        if (operationOK) {
            for (int w = 1; w <= n; w++) {
                addWheel(w);
            }

            for (int s = 1; s <= n; s++) {
                int color = 1 + ((s - 1) % 6);
                String symbolName = "S" + s;

                for (int w = 1; w <= n; w++) {
                    addSymbol(w, color, symbolName);
                }
            }

            Random random = new Random();

            for (int w = 1; w <= n; w++) {
                int steps = random.nextInt(n);

                if (steps > 0) {
                    spin(w, steps);
                }
            }

            operationOK = true;
        } else {
            showError("El número de ruedas y símbolos debe ser mayor a cero.");
        }
    }

    /**
     * Agrega una rueda en la posición indicada.
     *
     * @param pos posición de la rueda.
     */
    public void addWheel(int pos)
    {
        operationOK = true;

        if (pos < 1) {
            pos = 1;
        }

        if (pos > wheels.size() + 1) {
            pos = wheels.size() + 1;
        }

        wheels.add(pos - 1, new Wheel());
    }

    /**
     * Elimina una rueda.
     *
     * @param pos posición de la rueda.
     */
    public void delWheel(int pos)
    {
        operationOK = validWheel(pos);

        if (operationOK) {
            wheels.remove(pos - 1);
        } else {
            showError("La rueda indicada no existe.");
        }
    }

    /**
     * Intercambia la posición de dos ruedas dentro de la máquina.
     *
     * @param wheel1 posición de la primera rueda.
     * @param wheel2 posición de la segunda rueda.
     */
    public void swap(int wheel1, int wheel2)
    {
        operationOK = validWheel(wheel1) && validWheel(wheel2);

        if (operationOK) {
            Wheel temp = wheels.get(wheel1 - 1);
            wheels.set(wheel1 - 1, wheels.get(wheel2 - 1));
            wheels.set(wheel2 - 1, temp);
        } else {
            showError("Una o ambas ruedas indicadas no existen.");
        }
    }

    /**
     * Bloquea una rueda para que no sea afectada por spin() (el que gira
     * todas las ruedas) ni por spin(wheel, steps) sobre esa misma rueda.
     *
     * @param wheel posición de la rueda.
     * @return la posición actual de la rueda en el momento de bloquearla,
     *         o -1 si la rueda indicada no existe.
     */
    public int lock(int wheel)
    {
        operationOK = validWheel(wheel);
        int result = -1;

        if (operationOK) {
            Wheel selected = wheels.get(wheel - 1);
            result = selected.currentPosition();
            selected.lock();
        } else {
            showError("La rueda indicada no existe.");
        }

        return result;
    }

    /**
     * Desbloquea una rueda previamente bloqueada.
     *
     * @param wheel posición de la rueda.
     */
    public void unlock(int wheel)
    {
        operationOK = validWheel(wheel);

        if (operationOK) {
            wheels.get(wheel - 1).unlock();
        } else {
            showError("La rueda indicada no existe.");
        }
    }

    /**
     * Agrega un símbolo a una rueda.
     *
     * @param pos posición de la rueda.
     * @param color código del color.
     * @param symbol nombre del símbolo.
     */
    public void addSymbol(int pos, int color, String symbol)
    {
        operationOK = validWheel(pos);

        if (operationOK && symbol != null && !symbol.isEmpty()) {
            String colorName = colorName(color);
            Symbol newSymbol = new Symbol(symbol, colorName);
            wheels.get(pos - 1).addSymbol(newSymbol,
                                          wheels.get(pos - 1).size() + 1);
        } else {
            operationOK = false;
            showError("No se pudo agregar el símbolo.");
        }
    }

    /**
     * Elimina un símbolo de todas las ruedas.
     *
     * @param symbol nombre del símbolo.
     */
    public void delSymbol(String symbol)
    {
        operationOK = false;

        for (Wheel wheel : wheels) {
            int pos = wheel.findSymbol(symbol);

            if (pos != -1) {
                wheel.delSymbol(pos);
                operationOK = true;
            }
        }

        if (!operationOK) {
            showError("El símbolo no existe.");
        }
    }

    /**
     * Coloca un símbolo existente en una posición de una rueda.
     *
     * @param wheel posición de la rueda.
     * @param symbol posición del símbolo.
     * @param symbolName nombre del símbolo.
     */
    public void placeSymbol(int wheel, int symbol, String symbolName)
    {
        operationOK = validWheel(wheel);

        if (operationOK) {
            Wheel selected = wheels.get(wheel - 1);
            int found = selected.findSymbol(symbolName);

            operationOK = found != -1 && symbol >= 1
                          && symbol <= selected.size();

            if (operationOK) {
                selected.placeSymbol(symbol);
            } else {
                showError("No se pudo colocar el símbolo.");
            }
        } else {
            showError("La rueda indicada no existe.");
        }
    }

    /**
     * Gira una rueda determinada una posición hacia adelante.
     *
     * @param wheel posición de la rueda.
     */
    public void spin(int wheel)
    {
        spin(wheel, 1);
    }

    /**
     * Gira una rueda determinada un número arbitrario de pasos (puede ser
     * negativo, para girar en sentido contrario). No tiene efecto si la
     * rueda está bloqueada.
     *
     * @param wheel posición de la rueda.
     * @param steps número de pasos a girar.
     */
    public void spin(int wheel, int steps)
    {
        operationOK = validWheel(wheel);

        if (operationOK) {
            Wheel selected = wheels.get(wheel - 1);
            operationOK = !selected.isLocked();

            if (operationOK) {
                selected.spin(steps);
            } else {
                showError("La rueda está bloqueada.");
            }
        } else {
            showError("La rueda indicada no existe.");
        }
    }

    /**
     * Fija directamente la configuración visible de todas las ruedas,
     * indicando el nombre del símbolo que cada una debe mostrar.
     *
     * @param setSymbols arreglo con el nombre del símbolo deseado para
     *                    cada rueda, en orden. Debe tener exactamente
     *                    tantos elementos como ruedas tenga la máquina.
     */
    public void spin(String[] setSymbols)
    {
        operationOK = setSymbols != null && setSymbols.length == wheels.size();

        if (operationOK) {
            for (int i = 0; i < setSymbols.length && operationOK; i++) {
                Wheel selected = wheels.get(i);
                int found = selected.findSymbol(setSymbols[i]);
                operationOK = found != -1 && !selected.isLocked();

                if (operationOK) {
                    selected.placeSymbol(found);
                }
            }

            if (!operationOK) {
                showError("No fue posible fijar la configuración indicada.");
            }
        } else {
            showError("La cantidad de símbolos no coincide con el número de ruedas.");
        }
    }

    /**
     * Gira todas las ruedas de la máquina que no estén bloqueadas.
     */
    public void spin()
    {
        operationOK = !wheels.isEmpty();

        if (operationOK) {
            for (Wheel wheel : wheels) {
                if (!wheel.isLocked()) {
                    wheel.spin();
                }
            }
        } else {
            showError("La máquina no tiene ruedas.");
        }
    }

    /**
     * Obtiene los nombres de todos los símbolos que existen en la
     * máquina (su inventario), sin importar si están visibles o no.
     *
     * @return arreglo con los nombres de los símbolos.
     */
    public String[] symbols()
    {
        ArrayList<String> result = new ArrayList<String>();

        for (Wheel wheel : wheels) {
            for (Symbol symbol : wheel.getSymbols()) {
                if (!result.contains(symbol.getName())) {
                    result.add(symbol.getName());
                }
            }
        }

        return result.toArray(new String[0]);
    }

    /**
     * Obtiene la cantidad de símbolos distintos que se ven actualmente en
     * la máquina, es decir, entre los símbolos que cada rueda muestra en
     * este momento (no el total de símbolos existentes en la máquina).
     * Este es el valor que se usa como retroalimentación al resolver el
     * problema de la maratón.
     *
     * @return cantidad de símbolos distintos actualmente visibles.
     */
    public int distinctSymbols()
    {
        ArrayList<String> visibleNames = new ArrayList<String>();

        for (Wheel wheel : wheels) {
            String current = wheel.configuration();

            if (!current.isEmpty() && !visibleNames.contains(current)) {
                visibleNames.add(current);
            }
        }

        return visibleNames.size();
    }

    /**
     * Obtiene la configuración actual de la máquina.
     *
     * @return símbolos visibles en cada rueda.
     */
    public String[] configuration()
    {
        String[] result = new String[wheels.size()];

        for (int i = 0; i < wheels.size(); i++) {
            result[i] = wheels.get(i).configuration();
        }

        return result;
    }

    /**
     * Determina si la configuración actual es ganadora.
     *
     * @return true si todas las ruedas muestran el mismo símbolo.
     */
    public boolean isJackpot()
    {
        if (wheels.isEmpty()) {
            return false;
        }

        String first = wheels.get(0).configuration();

        if (first.isEmpty()) {
            return false;
        }

        for (Wheel wheel : wheels) {
            if (!first.equals(wheel.configuration())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Hace visible el simulador.
     */
    public void makeVisible()
    {
        visible = true;

        for (Wheel wheel : wheels) {
            wheel.makeVisible();
        }
    }

    /**
     * Hace invisible el simulador.
     */
    public void makeInvisible()
    {
        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }

        visible = false;
    }

    /**
     * Termina la ejecución del simulador.
     */
    public void exit()
    {
        makeInvisible();
        System.exit(0);
    }

    /**
     * Indica si la última operación se realizó correctamente.
     *
     * @return true si la operación fue correcta.
     */
    public boolean ok()
    {
        return operationOK;
    }

    /**
     * Verifica si una posición de rueda es válida.
     *
     * @param pos posición que se desea verificar.
     * @return true si la posición es válida.
     */
    private boolean validWheel(int pos)
    {
        return pos >= 1 && pos <= wheels.size();
    }

    /**
     * Convierte un código numérico en un color soportado por shapes.
     *
     * @param color código numérico.
     * @return nombre del color.
     */
    private String colorName(int color)
    {
        switch (color) {
            case 1:
                return "red";
            case 2:
                return "yellow";
            case 3:
                return "blue";
            case 4:
                return "green";
            case 5:
                return "magenta";
            case 6:
                return "black";
            default:
                return "white";
        }
    }

    /**
     * Muestra un mensaje de error solamente cuando el simulador es visible.
     *
     * @param message mensaje que se mostrará.
     */
    private void showError(String message)
    {
        if (visible) {
            JOptionPane.showMessageDialog(null, message);
        }
    }
}
