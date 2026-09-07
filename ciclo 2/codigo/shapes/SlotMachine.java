import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Representa una máquina tragamonedas.
 * La máquina está compuesta por ruedas que contienen símbolos.
 *
 * @author
 * @version 2.0
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
     * Intercambia la posición de dos ruedas de la máquina.
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
            showError("Alguna de las ruedas indicadas no existe.");
        }
    }

    /**
     * Fija (bloquea) una rueda para que no gire.
     *
     * @param wheel posición de la rueda.
     */
    public void lock(int wheel)
    {
        operationOK = validWheel(wheel);

        if (operationOK) {
            wheels.get(wheel - 1).lock();
        } else {
            showError("La rueda indicada no existe.");
        }
    }

    /**
     * Suelta (desbloquea) una rueda para que pueda volver a girar.
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
     * Gira una rueda determinada.
     *
     * @param wheel posición de la rueda.
     */
    public void spin(int wheel)
    {
        operationOK = validWheel(wheel);

        if (operationOK) {
            wheels.get(wheel - 1).spin();
        } else {
            showError("La rueda indicada no existe.");
        }
    }

    /**
     * Gira una rueda determinada un número de pasos. Si la máquina es
     * visible, el movimiento de la rueda se visualiza paso a paso.
     *
     * @param wheel posición de la rueda.
     * @param steps cantidad de pasos que se desea girar.
     */
    public void spin(int wheel, int steps)
    {
        operationOK = validWheel(wheel) && steps >= 0;

        if (operationOK) {
            wheels.get(wheel - 1).spin(steps);
        } else {
            showError("No se pudo rotar la rueda indicada.");
        }
    }

    /**
     * Gira todas las ruedas de la máquina.
     */
    public void spin()
    {
        operationOK = !wheels.isEmpty();

        if (operationOK) {
            for (Wheel wheel : wheels) {
                wheel.spin();
            }
        } else {
            showError("La máquina no tiene ruedas.");
        }
    }

    /**
     * Deja la máquina en una configuración dada: coloca en cada rueda
     * el símbolo indicado en la posición correspondiente del arreglo.
     *
     * @param setSymbols nombres de los símbolos, uno por cada rueda,
     * en el mismo orden en que están las ruedas en la máquina.
     */
    public void spin(String[] setSymbols)
    {
        operationOK = setSymbols != null && setSymbols.length == wheels.size();

        int[] positions = new int[wheels.size()];

        if (operationOK) {
            for (int i = 0; i < wheels.size() && operationOK; i++) {
                positions[i] = wheels.get(i).findSymbol(setSymbols[i]);
                operationOK = positions[i] != -1;
            }
        }

        if (operationOK) {
            for (int i = 0; i < wheels.size(); i++) {
                wheels.get(i).placeSymbol(positions[i]);
            }
        } else {
            showError("No se pudo establecer la configuración indicada.");
        }
    }

    /**
     * Obtiene los nombres de los símbolos de la máquina.
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
     * Obtiene la cantidad de símbolos diferentes.
     *
     * @return cantidad de símbolos diferentes.
     */
    public int distinctSymbols()
    {
        return symbols().length;
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
