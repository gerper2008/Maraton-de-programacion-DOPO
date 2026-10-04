import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Representa una máquina tragamonedas.
 * La máquina está compuesta por ruedas que contienen símbolos.
 * Soporta diferentes tipos de ruedas (normal, lefty, rebel, mirror)
 * y diferentes tipos de símbolos (normal, ephemeral, shy, wild).
 *
 * @author DOPO
 * @version 4.0
 */
public class SlotMachine
{
    private ArrayList<Wheel> wheels;
    private boolean visible;
    private boolean operationOK;

    // Constantes para el layout visual
    private static final int FRAME_WIDTH = 50;
    private static final int FRAME_HEIGHT = 50;
    private static final int FRAME_GAP = 10;
    private static final int START_X = 20;
    private static final int START_Y = 40;

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
     * aleatoriamente. Todas las ruedas son normales y todos los
     * símbolos son normales.
     *
     * @param n número de ruedas y símbolos.
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

    // ==================== Gestión de ruedas ====================

    /**
     * Agrega una rueda normal en la posición indicada.
     *
     * @param pos posición de la rueda.
     */
    public void addWheel(int pos)
    {
        addWheel(pos, "normal");
    }

    /**
     * Agrega una rueda del tipo indicado en la posición indicada.
     *
     * @param pos posición de la rueda.
     * @param type tipo de rueda: "normal", "lefty", "rebel", "mirror".
     */
    public void addWheel(int pos, String type)
    {
        operationOK = true;

        if (pos < 1) {
            pos = 1;
        }
        if (pos > wheels.size() + 1) {
            pos = wheels.size() + 1;
        }

        Wheel newWheel;
        switch (type.toLowerCase()) {
            case "lefty":
                newWheel = new LeftyWheel();
                break;
            case "rebel":
                newWheel = new RebelWheel();
                break;
            case "mirror":
                newWheel = new MirrorWheel();
                break;
            default:
                newWheel = new Wheel();
                break;
        }

        wheels.add(pos - 1, newWheel);
        updateLeftyNeighbors();
    }

    /**
     * Elimina una rueda. No se elimina si canBeDeleted() es false.
     *
     * @param pos posición de la rueda.
     */
    public void delWheel(int pos)
    {
        operationOK = validWheel(pos);

        if (operationOK) {
            Wheel wheel = wheels.get(pos - 1);
            if (wheel.canBeDeleted()) {
                wheels.remove(pos - 1);
                updateLeftyNeighbors();
            } else {
                operationOK = false;
                showError("Esta rueda no se puede eliminar.");
            }
        } else {
            showError("La rueda indicada no existe.");
        }
    }

    /**
     * Intercambia la posición de dos ruedas. No se intercambia
     * si alguna de las ruedas tiene canBeSwapped() en false.
     *
     * @param wheel1 posición de la primera rueda.
     * @param wheel2 posición de la segunda rueda.
     */
    public void swap(int wheel1, int wheel2)
    {
        operationOK = validWheel(wheel1) && validWheel(wheel2);

        if (operationOK) {
            Wheel w1 = wheels.get(wheel1 - 1);
            Wheel w2 = wheels.get(wheel2 - 1);

            if (w1.canBeSwapped() && w2.canBeSwapped()) {
                wheels.set(wheel1 - 1, w2);
                wheels.set(wheel2 - 1, w1);
                updateLeftyNeighbors();
            } else {
                operationOK = false;
                showError("Una o ambas ruedas no se pueden intercambiar.");
            }
        } else {
            showError("Una o ambas ruedas indicadas no existen.");
        }
    }

    /**
     * Bloquea una rueda.
     *
     * @param wheel posición de la rueda.
     * @return posición actual de la rueda al bloquearla, o -1 si no existe.
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
     * Desbloquea una rueda.
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

    // ==================== Gestión de símbolos ====================

    /**
     * Agrega un símbolo normal a una rueda.
     *
     * @param pos posición de la rueda.
     * @param color código del color.
     * @param symbol nombre del símbolo.
     */
    public void addSymbol(int pos, int color, String symbol)
    {
        addSymbol(pos, color, symbol, "normal");
    }

    /**
     * Agrega un símbolo del tipo indicado a una rueda.
     *
     * @param pos posición de la rueda.
     * @param color código del color.
     * @param symbol nombre del símbolo.
     * @param type tipo: "normal", "ephemeral", "shy", "wild".
     */
    public void addSymbol(int pos, int color, String symbol, String type)
    {
        operationOK = validWheel(pos);

        if (operationOK && symbol != null && !symbol.isEmpty()) {
            String colorName = colorName(color);
            Symbol newSymbol;

            switch (type.toLowerCase()) {
                case "ephemeral":
                    newSymbol = new EphemeralSymbol(symbol, colorName);
                    break;
                case "shy":
                    newSymbol = new ShySymbol(symbol, colorName);
                    break;
                case "wild":
                    newSymbol = new WildSymbol(symbol, colorName);
                    break;
                default:
                    newSymbol = new Symbol(symbol, colorName);
                    break;
            }

            Wheel wheel = wheels.get(pos - 1);
            wheel.addSymbol(newSymbol, wheel.size() + 1);
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

    // ==================== Giros ====================

    /**
     * Gira una rueda determinada una posición.
     *
     * @param wheel posición de la rueda.
     */
    public void spin(int wheel)
    {
        spin(wheel, 1);
    }

    /**
     * Gira una rueda determinada un número de pasos.
     * No tiene efecto si la rueda está bloqueada.
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
                if (visible) {
                    redraw();
                }
            } else {
                showError("La rueda está bloqueada.");
            }
        } else {
            showError("La rueda indicada no existe.");
        }
    }

    /**
     * Fija la configuración visible de todas las ruedas.
     *
     * @param setSymbols nombre del símbolo deseado para cada rueda.
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
            } else if (visible) {
                redraw();
            }
        } else {
            showError("La cantidad de símbolos no coincide con el número de ruedas.");
        }
    }

    /**
     * Gira todas las ruedas que no estén bloqueadas.
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

            if (visible) {
                redraw();
            }
        } else {
            showError("La máquina no tiene ruedas.");
        }
    }

    // ==================== Consultas ====================

    /**
     * Obtiene los nombres de todos los símbolos que existen
     * en la máquina (inventario).
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
     * Obtiene la cantidad de símbolos distintos visibles.
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
     * Un símbolo wild cuenta como cualquier otro símbolo.
     * Para ser jackpot, todos los no-wild deben ser iguales.
     *
     * @return true si es jackpot.
     */
    public boolean isJackpot()
    {
        if (wheels.isEmpty()) {
            return false;
        }

        // Buscar el nombre de referencia (primer no-wild)
        String reference = null;

        for (Wheel wheel : wheels) {
            Symbol sym = wheel.currentSymbol();
            if (sym == null) {
                return false;
            }
            if (!sym.isWild()) {
                if (reference == null) {
                    reference = sym.getName();
                } else if (!reference.equals(sym.getName())) {
                    return false;
                }
            }
        }

        // Si llegamos aquí, todos los no-wild son iguales
        // (o todos son wild, lo cual también es jackpot)
        return true;
    }

    // ==================== Visual ====================

    /**
     * Hace visible el simulador.
     */
    public void makeVisible()
    {
        visible = true;
        redraw();
    }

    /**
     * Hace invisible el simulador.
     */
    public void makeInvisible()
    {
        for (Wheel wheel : wheels) {
            for (Symbol symbol : wheel.getSymbols()) {
                symbol.erase();
            }
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

    // ==================== Métodos privados ====================

    /**
     * Verifica si una posición de rueda es válida.
     */
    private boolean validWheel(int pos)
    {
        return pos >= 1 && pos <= wheels.size();
    }

    /**
     * Convierte un código numérico en un nombre de color.
     */
    private String colorName(int color)
    {
        switch (color) {
            case 1: return "red";
            case 2: return "yellow";
            case 3: return "blue";
            case 4: return "green";
            case 5: return "magenta";
            case 6: return "black";
            default: return "white";
        }
    }

    /**
     * Muestra un mensaje de error solo cuando el simulador es visible.
     */
    private void showError(String message)
    {
        if (visible) {
            JOptionPane.showMessageDialog(null, message);
        }
    }

    /**
     * Actualiza las referencias de vecino izquierdo para ruedas lefty.
     */
    private void updateLeftyNeighbors()
    {
        for (int i = 0; i < wheels.size(); i++) {
            Wheel wheel = wheels.get(i);
            if (wheel instanceof LeftyWheel) {
                LeftyWheel lefty = (LeftyWheel) wheel;
                if (i > 0) {
                    lefty.setLeftNeighbor(wheels.get(i - 1));
                } else {
                    lefty.setLeftNeighbor(null);
                }
            }
        }
    }

    /**
     * Redibuja toda la máquina.
     * Cada rueda se muestra como un rectángulo marco con un color
     * que indica su tipo, y el símbolo actual centrado dentro.
     */
    private void redraw()
    {
        // Primero borrar todos los símbolos
        for (Wheel wheel : wheels) {
            for (Symbol symbol : wheel.getSymbols()) {
                symbol.erase();
            }
        }

        // Dibujar cada rueda
        for (int i = 0; i < wheels.size(); i++) {
            Wheel wheel = wheels.get(i);

            // Posición del marco
            int frameX = START_X + i * (FRAME_WIDTH + FRAME_GAP);
            int frameY = START_Y;

            // Dibujar el marco (rectángulo del color del tipo)
            Rectangle frame = new Rectangle();
            frame.changeSize(FRAME_HEIGHT, FRAME_WIDTH);

            String frameColor;
            switch (wheel.getType()) {
                case "lefty":  frameColor = "blue"; break;
                case "rebel":  frameColor = "red"; break;
                case "mirror": frameColor = "green"; break;
                default:       frameColor = "black"; break;
            }
            frame.changeColor(frameColor);
            frame.moveHorizontal(frameX - 70);
            frame.moveVertical(frameY - 15);
            frame.makeVisible();

            // Centro del marco para posicionar el símbolo
            int centerX = frameX + FRAME_WIDTH / 2;
            int centerY = frameY + FRAME_HEIGHT / 2;

            // Dibujar el símbolo actual
            Symbol current = wheel.currentSymbol();
            if (current != null && current.shouldDisplay()) {
                current.isCurrentlyVisible = true;
                current.drawAt(centerX, centerY);
            }
        }
    }
}
